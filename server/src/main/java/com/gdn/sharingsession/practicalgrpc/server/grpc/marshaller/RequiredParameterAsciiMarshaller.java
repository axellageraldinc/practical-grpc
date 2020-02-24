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

  /**
   * Marshall a required parameter object as string
   *
   * @param value the required parameter to be marshalled
   * @return the marshalled required parameter
   */
  @Override
  public String toAsciiString(RequiredParameter value) {
    try {
      return objectMapper.writeValueAsString(value);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
    return "";
  }

  /**
   * Unmarshal a marshalled required parameter
   *
   * @param serialized marshalled required parameter (ascii string)
   * @return unmarshaled required parameter
   */
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
