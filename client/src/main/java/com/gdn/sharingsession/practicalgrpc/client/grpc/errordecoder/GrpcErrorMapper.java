package com.gdn.sharingsession.practicalgrpc.client.grpc.errordecoder;

import com.gdn.sharingsession.practicalgrpc.client.model.enums.ErrorCode;
import io.grpc.Status;

import java.util.stream.Stream;

/**
 * Created by axellageraldinc.a on 1/30/2020.
 */
public class GrpcErrorMapper {
  public static ErrorCode map(Status status) {
    return Stream.of(ErrorCode.values())
        .filter(errorCode -> errorCode.getGrpcStatus()
            .getCode()
            .name()
            .equals(status.getCode().name()))
        .findFirst()
        .orElse(ErrorCode.UNKNOWN_ERROR);
  }
}
