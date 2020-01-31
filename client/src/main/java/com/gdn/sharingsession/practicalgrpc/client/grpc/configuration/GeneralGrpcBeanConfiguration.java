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

  @Bean
  public Channel channel() {
    return ManagedChannelBuilder
        .forTarget(serverHost)
        .usePlaintext()
        .build();
  }

  @Bean
  public Metadata.AsciiMarshaller<RequiredParameter> requiredParameterAsciiMarshaller(
      ObjectMapper objectMapper) {
    return new RequiredParameterAsciiMarshaller(objectMapper);
  }

  @Bean
  public ClientInterceptor grpcInterceptor(
      Metadata.AsciiMarshaller<RequiredParameter> requiredParameterAsciiMarshaller,
      RequiredParameterHelper requiredParameterHelper) {
    return new GrpcClientInterceptor(requiredParameterAsciiMarshaller, requiredParameterHelper);
  }
}
