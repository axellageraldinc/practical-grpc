package com.gdn.sharingsession.practicalgrpc.client.web.controller;

import com.gdn.sharingsession.practicalgrpc.client.model.web.request.CreateBookRequest;
import com.gdn.sharingsession.practicalgrpc.client.model.web.response.BaseResponse;
import com.gdn.sharingsession.practicalgrpc.client.model.web.response.BookResponse;
import com.gdn.sharingsession.practicalgrpc.client.service.BookStoreReactiveService;
import io.grpc.Status;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by axellageraldinc.a on 1/29/2020.
 */
@RestController
@RequestMapping(value = "/api/reactive/books")
@RequiredArgsConstructor
@Slf4j
public class ReactiveBookController {

  private final BookStoreReactiveService bookStoreReactiveService;
  private final Scheduler commonScheduler;

  @GetMapping(
      produces = MediaType.APPLICATION_JSON_VALUE
  )
  public Mono<ResponseEntity<BaseResponse<List<BookResponse>>>> reactiveGetAllBooks() {
    return bookStoreReactiveService.getAllBooks()
        .map(this::ok)
        .map(ResponseEntity::ok)
        .subscribeOn(commonScheduler);
  }

  @GetMapping(
      value = "/server-stream",
      produces = MediaType.APPLICATION_JSON_VALUE
  )
  public Mono<ResponseEntity<BaseResponse<List<BookResponse>>>> reactiveStreamAllBooks() {
    return bookStoreReactiveService.streamAllBooks()
        .collectList()
        .map(this::ok)
        .map(ResponseEntity::ok)
        .subscribeOn(commonScheduler);
  }

  @PostMapping(
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE
  )
  public Mono<ResponseEntity<BaseResponse<List<BookResponse>>>> reactiveCreateBookOneByOne(
      @RequestBody @Valid CreateBookRequest createBookRequest) {
    return bookStoreReactiveService.createBookOneByOne(createBookRequest)
        .collectList()
        .map(this::ok)
        .map(ResponseEntity::ok)
        .subscribeOn(commonScheduler);
  }

  private <T> BaseResponse<T> ok(T response) {
    return BaseResponse.<T>builder()
        .httpCode(HttpStatus.OK.value())
        .grpcCode(Status.OK.getCode().name())
        .data(response)
        .build();
  }

}
