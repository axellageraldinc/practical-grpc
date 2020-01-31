package com.gdn.sharingsession.practicalgrpc.client.model.web.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by axellageraldinc.a on 1/28/2020.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookResponse {
  private String id;
  private String title;
}
