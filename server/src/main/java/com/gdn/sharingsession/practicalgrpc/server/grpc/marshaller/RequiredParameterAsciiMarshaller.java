package com.gdn.sharingsession.practicalgrpc.server.grpc.marshaller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gdn.sharingsession.practicalgrpc.server.model.master.RequiredParameter;
import io.grpc.Metadata;
import lombok.RequiredArgsConstructor;

/**
 * Created by axellageraldinc.a on 1/29/2020.
 */
@RequiredArgsConstructor
public class RequiredParameterAsciiMarshaller
    implements Metadata.AsciiMarshaller<RequiredParameter> {

  private final ObjectMapper objectMapper;

  @Override
  public String toAsciiString(RequiredParameter value) {
    try {
      return objectMapper.writeValueAsString(value);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
    return "";
  }

  @Override
  public RequiredParameter parseAsciiString(String serialized) {
    try {
      return objectMapper.readValue(serialized, RequiredParameter.class);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
    return RequiredParameter.builder().build();
  }
}
