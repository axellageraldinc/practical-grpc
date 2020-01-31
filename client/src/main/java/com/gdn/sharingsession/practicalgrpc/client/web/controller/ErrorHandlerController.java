package com.gdn.sharingsession.practicalgrpc.client.web.controller;

import com.gdn.sharingsession.practicalgrpc.client.grpc.errordecoder.GrpcErrorMapper;
import com.gdn.sharingsession.practicalgrpc.client.model.enums.ErrorCode;
import com.gdn.sharingsession.practicalgrpc.client.model.web.response.BaseResponse;
import io.grpc.StatusRuntimeException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Created by axellageraldinc.a on 1/30/2020.
 */
@RestControllerAdvice
public class ErrorHandlerController {
  @ExceptionHandler(StatusRuntimeException.class)
  public ResponseEntity<BaseResponse> handleGrpcStatusRuntimeException(StatusRuntimeException ex) {
    ErrorCode errorCode = GrpcErrorMapper.map(ex.getStatus());
    return ResponseEntity
        .status(errorCode.getHttpStatus().value())
        .body(BaseResponse.builder()
            .httpCode(errorCode.getHttpStatus().value())
            .grpcCode(errorCode.getGrpcStatus().getCode().name())
            .message(ex.getMessage())
            .build());
  }
}
