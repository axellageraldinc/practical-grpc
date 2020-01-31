package com.gdn.sharingsession.practicalgrpc.server.repository;

import com.gdn.sharingsession.practicalgrpc.server.model.entity.Book;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Created by axellageraldinc.a on 1/28/2020.
 */
public interface BookRepository extends MongoRepository<Book, String> {
  List<Book> findAllByIdIn(List<String> ids);
}
