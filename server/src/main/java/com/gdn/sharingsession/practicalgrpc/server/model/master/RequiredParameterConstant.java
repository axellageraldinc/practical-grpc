package com.gdn.sharingsession.practicalgrpc.server.model.master;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Created by axellageraldinc.a on 11/28/2019.
 */
@AllArgsConstructor
@Getter
@NoArgsConstructor
public enum RequiredParameterConstant {
  REQUEST_ID("requestId", UUID.randomUUID().toString(), "client's request id");

  String variable;
  String defaultValue;
  String description;
}
