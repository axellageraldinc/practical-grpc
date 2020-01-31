package com.gdn.sharingsession.practicalgrpc.server.repository;

import com.gdn.sharingsession.practicalgrpc.server.model.entity.Book;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

/**
 * Created by axellageraldinc.a on 1/29/2020.
 */
public interface ReactiveBookRepository extends ReactiveMongoRepository<Book, String> {
}
