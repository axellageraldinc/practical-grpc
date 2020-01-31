package com.gdn.sharingsession.practicalgrpc.client.model.web.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by axellageraldinc.a on 1/30/2020.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BaseResponse<T> {
  private int httpCode;
  private String grpcCode;
  private T data;
  private String message;
}
