package com.gdn.sharingsession.practicalgrpc.client.model.exception;

import io.grpc.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * Created by axellageraldinc.a on 1/30/2020.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BusinessException extends RuntimeException {
  private HttpStatus httpStatus;
  private Status grpcStatus;
  private String message;
}
