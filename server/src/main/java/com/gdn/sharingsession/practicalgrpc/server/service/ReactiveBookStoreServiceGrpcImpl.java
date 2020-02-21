package com.gdn.sharingsession.practicalgrpc.server.service;

import com.gdn.sharingsession.practicalgrpc.server.generatedproto.BookStoreProto;
import com.gdn.sharingsession.practicalgrpc.server.generatedproto.ReactorBookStoreServiceGrpc;
import com.gdn.sharingsession.practicalgrpc.server.grpc.interceptor.GrpcServerInterceptor;
import com.gdn.sharingsession.practicalgrpc.server.model.entity.Book;
import com.gdn.sharingsession.practicalgrpc.server.repository.ReactiveBookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lognet.springboot.grpc.GRpcService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

import java.time.Duration;
import java.util.List;

/**
 * Created by axellageraldinc.a on 1/29/2020.
 */
@GRpcService(interceptors = GrpcServerInterceptor.class)
@RequiredArgsConstructor
@Slf4j
public class ReactiveBookStoreServiceGrpcImpl
    extends ReactorBookStoreServiceGrpc.BookStoreServiceImplBase {

  private final ReactiveBookRepository reactiveBookRepository;
  private final Scheduler commonScheduler;

  @Override
  public Mono<BookStoreProto.GetAllBookResponse> getAllBook(Mono<BookStoreProto.GetAllBookRequest> request) {
    return request
        .flatMapMany(getAllBookRequest -> reactiveBookRepository.findAll())
        .publishOn(commonScheduler)
        .map(this::toGetBookResponse)
        .collectList()
        .map(this::toGetAllBookResponse);
  }

  private BookStoreProto.GetBookResponse toGetBookResponse(Book book) {
    return BookStoreProto.GetBookResponse.newBuilder()
        .setId(book.getId())
        .setTitle(book.getTitle())
        .build();
  }

  private BookStoreProto.GetAllBookResponse toGetAllBookResponse(
      List<BookStoreProto.GetBookResponse> getBookResponses) {
    return BookStoreProto.GetAllBookResponse.newBuilder()
        .addAllGetBookResponses(getBookResponses)
        .build();
  }

  @Override
  public Flux<BookStoreProto.GetBookResponse> streamAllBook(Mono<BookStoreProto.GetAllBookRequest> request) {
    return request
        .flatMapMany(getAllBookRequest -> reactiveBookRepository.findAll())
        .publishOn(commonScheduler)
        .delayElements(Duration.ofMillis(500)) // to simulate the streaming
        .concatMapDelayError(book -> {
          if (book.getTitle().equalsIgnoreCase("Error Book")) {
            return Flux.error(() -> new RuntimeException("Intended error for " + book));
          }
          return Flux.just(toGetBookResponse(book));
        })
        .doOnError(throwable -> log.error(
            "#ReactiveBookStoreServiceGrpcImpl streamAllBook() error caused by ",
            throwable));
  }

  @Override
  public Flux<BookStoreProto.CreateBookResponse> createBookOneByOne(Flux<BookStoreProto.CreateBookRequest> request) {
    return request
        .map(this::toBook)
        .flatMap(reactiveBookRepository::save)
        .map(this::toCreateBookResponse);
  }

  private Book toBook(BookStoreProto.CreateBookRequest createBookRequest) {
    return Book.builder()
        .title(createBookRequest.getTitle())
        .build();
  }

  private BookStoreProto.CreateBookResponse toCreateBookResponse(Book book) {
    return BookStoreProto.CreateBookResponse.newBuilder()
        .setId(book.getId())
        .setTitle(book.getTitle())
        .build();
  }
}
