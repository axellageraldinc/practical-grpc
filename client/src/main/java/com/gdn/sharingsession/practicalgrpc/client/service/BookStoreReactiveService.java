package com.gdn.sharingsession.practicalgrpc.client.service;

import com.gdn.sharingsession.practicalgrpc.client.model.web.request.CreateBookRequest;
import com.gdn.sharingsession.practicalgrpc.client.model.web.request.DeleteBookRequest;
import com.gdn.sharingsession.practicalgrpc.client.model.web.response.BookResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Created by axellageraldinc.a on 1/28/2020.
 */
public interface BookStoreReactiveService {
  Mono<List<BookResponse>> getAllBooks();

  Flux<BookResponse> streamAllBooks();

  Mono<List<BookResponse>> streamDeleteBook(DeleteBookRequest deleteBookRequest);

  Flux<BookResponse> createBookOneByOne(CreateBookRequest createBookRequest);
}
