package com.gdn.sharingsession.practicalgrpc.server.grpc.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gdn.sharingsession.practicalgrpc.server.grpc.interceptor.GrpcServerInterceptor;
import com.gdn.sharingsession.practicalgrpc.server.grpc.marshaller.RequiredParameterAsciiMarshaller;
import com.gdn.sharingsession.practicalgrpc.server.helper.RequiredParameterHelper;
import com.gdn.sharingsession.practicalgrpc.server.model.master.RequiredParameter;
import io.grpc.Metadata;
import io.grpc.ServerInterceptor;
import org.lognet.springboot.grpc.GRpcGlobalInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by axellageraldinc.a on 1/30/2020.
 */
@Configuration
public class GeneralGrpcBeanConfiguration {
  /**
   * Declare an ascii marshaller for required parameter as a bean. This marshaller will be used to marshall a required parameter as ascii.
   *
   * @param objectMapper Jackson's object mapper
   * @return the ascii marshaller for required parameter
   */
  @Bean
  public Metadata.AsciiMarshaller<RequiredParameter> requiredParameterAsciiMarshaller(ObjectMapper objectMapper) {
    return new RequiredParameterAsciiMarshaller(objectMapper);
  }

  /**
   * Declaring server interceptor as a bean. This will act as an interceptor on any incoming request AND outcoming response.
   *
   * @param requiredParameterHelper          helper for required parameter (parameter which is passed around on each request for tracing)
   * @param requiredParameterAsciiMarshaller ascii marshaller to marshal an object as ascii
   * @return the server interceptor
   */
  @Bean
  @GRpcGlobalInterceptor
  public ServerInterceptor grpcServerInterceptor(RequiredParameterHelper requiredParameterHelper,
      Metadata.AsciiMarshaller<RequiredParameter> requiredParameterAsciiMarshaller) {
    return new GrpcServerInterceptor(requiredParameterHelper, requiredParameterAsciiMarshaller);
  }
}
