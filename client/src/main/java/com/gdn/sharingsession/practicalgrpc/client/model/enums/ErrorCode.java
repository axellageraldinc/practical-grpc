package com.gdn.sharingsession.practicalgrpc.client.model.enums;

import io.grpc.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Created by axellageraldinc.a on 1/30/2020.
 */
@AllArgsConstructor
@Getter
public enum ErrorCode {
  TIMEOUT(HttpStatus.REQUEST_TIMEOUT, Status.DEADLINE_EXCEEDED),
  UNKNOWN_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, Status.INTERNAL);

  private HttpStatus httpStatus;
  private Status grpcStatus;
}
