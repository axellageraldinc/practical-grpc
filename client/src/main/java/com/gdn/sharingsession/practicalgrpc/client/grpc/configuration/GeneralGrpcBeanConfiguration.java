package com.gdn.sharingsession.practicalgrpc.client.grpc.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gdn.sharingsession.practicalgrpc.client.grpc.interceptor.GrpcClientInterceptor;
import com.gdn.sharingsession.practicalgrpc.client.grpc.marshaller.RequiredParameterAsciiMarshaller;
import com.gdn.sharingsession.practicalgrpc.client.helper.RequiredParameterHelper;
import com.gdn.sharingsession.practicalgrpc.client.model.master.RequiredParameter;
import io.grpc.Channel;
import io.grpc.ClientInterceptor;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Metadata;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by axellageraldinc.a on 1/30/2020.
 */
@Configuration
public class GeneralGrpcBeanConfiguration {
  @Value("${grpc.server.host}")
  private String serverHost;

  /**
   * @return Defining the gRPC channel (server address)
   */
  @Bean
  public Channel channel() {
    return ManagedChannelBuilder
        .forTarget(serverHost)
        .usePlaintext()
        .build();
  }

  /**
   * Declare an ascii marshaller for required parameter as a bean. This marshaller will be used to marshall a required parameter as ascii.
   *
   * @param objectMapper Jackson's object mapper
   * @return the ascii marshaller for required parameter
   */
  @Bean
  public Metadata.AsciiMarshaller<RequiredParameter> requiredParameterAsciiMarshaller(
      ObjectMapper objectMapper) {
    return new RequiredParameterAsciiMarshaller(objectMapper);
  }

  /**
   * Declaring client interceptor as a bean. This interceptor will intercept every outcoming request and incoming response.
   *
   * @param requiredParameterHelper          helper for required parameter (parameter which is passed around on each request for tracing)
   * @param requiredParameterAsciiMarshaller ascii marshaller to marshal an object as ascii
   * @return the client interceptor
   */
  @Bean
  public ClientInterceptor grpcInterceptor(
      Metadata.AsciiMarshaller<RequiredParameter> requiredParameterAsciiMarshaller,
      RequiredParameterHelper requiredParameterHelper) {
    return new GrpcClientInterceptor(requiredParameterAsciiMarshaller, requiredParameterHelper);
  }
}
