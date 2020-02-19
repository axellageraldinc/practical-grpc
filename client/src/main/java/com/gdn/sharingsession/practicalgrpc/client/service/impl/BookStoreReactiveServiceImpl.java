package com.gdn.sharingsession.practicalgrpc.client.service.impl;

import com.gdn.sharingsession.practicalgrpc.client.generatedproto.BookStoreProto;
import com.gdn.sharingsession.practicalgrpc.client.generatedproto.ReactorBookStoreServiceGrpc;
import com.gdn.sharingsession.practicalgrpc.client.model.web.request.CreateBookRequest;
import com.gdn.sharingsession.practicalgrpc.client.model.web.response.BookResponse;
import com.gdn.sharingsession.practicalgrpc.client.service.BookStoreReactiveService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by axellageraldinc.a on 1/29/2020.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class BookStoreReactiveServiceImpl implements BookStoreReactiveService {

  private final ReactorBookStoreServiceGrpc.ReactorBookStoreServiceStub
      bookStoreServiceGrpcReactorStub;
  private final Scheduler commonScheduler;

  @Override
  public Mono<List<BookResponse>> getAllBooks() {
    return bookStoreServiceGrpcReactorStub.getAllBook(BookStoreProto.GetAllBookRequest.newBuilder()
        .build())
        .publishOn(commonScheduler)
        .map(BookStoreProto.GetAllBookResponse::getGetBookResponsesList)
        .map(this::toBookResponses);
  }

  private List<BookResponse> toBookResponses(List<BookStoreProto.GetBookResponse> getBookResponses) {
    return getBookResponses
        .stream()
        .map(this::toBookResponse)
        .collect(Collectors.toList());
  }

  private BookResponse toBookResponse(BookStoreProto.GetBookResponse getBookResponse) {
    return BookResponse.builder()
        .id(getBookResponse.getId())
        .title(getBookResponse.getTitle())
        .build();
  }

  @Override
  public Flux<BookResponse> streamAllBooks() {
    return bookStoreServiceGrpcReactorStub.streamAllBook(BookStoreProto.GetAllBookRequest.newBuilder()
        .build())
        .publishOn(commonScheduler)
        .filter(getBookResponse -> !StringUtils.isEmpty(getBookResponse.getId()))
        .map(this::toBookResponse);
  }

  @Override
  public Flux<BookResponse> createBookOneByOne(CreateBookRequest createBookRequest) {
    Flux<BookStoreProto.CreateBookRequest> createBookRequestFlux =
        Flux.fromIterable(createBookRequest.getTitles())
            .map(this::toCreateBookRequest)
            .delayElements(Duration.ofMillis(500));
    return bookStoreServiceGrpcReactorStub.createBookOneByOne(createBookRequestFlux)
        .publishOn(commonScheduler)
        .map(this::toBookResponse)
        .map(this::passCreatedBookToOtherService);
  }

  private BookStoreProto.CreateBookRequest toCreateBookRequest(String title) {
    return BookStoreProto.CreateBookRequest.newBuilder()
        .setTitle(title)
        .build();
  }

  private BookResponse toBookResponse(BookStoreProto.CreateBookResponse createBookResponse) {
    return BookResponse.builder()
        .id(createBookResponse.getId())
        .title(createBookResponse.getTitle())
        .build();
  }

  private BookResponse passCreatedBookToOtherService(BookResponse bookResponse) {
    // We could pass the created book to other service to be processed further
    return bookResponse;
  }
}
