package com.gdn.sharingsession.practicalgrpc.server.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by axellageraldinc.a on 1/28/2020.
 */
@Document(collection = "book")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Book {
  @Id
  private String id;

  private String title;

  private String author;
}
