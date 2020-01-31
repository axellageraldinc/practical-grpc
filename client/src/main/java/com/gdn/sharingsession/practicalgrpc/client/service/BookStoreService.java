package com.gdn.sharingsession.practicalgrpc.client.service;

import com.gdn.sharingsession.practicalgrpc.client.model.web.response.BookResponse;

import java.util.List;

/**
 * Created by axellageraldinc.a on 1/28/2020.
 */
public interface BookStoreService {
  List<BookResponse> getAllBooks();

  void streamAllBooks();
}
