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
  @Bean
  public Metadata.AsciiMarshaller<RequiredParameter> requiredParameterAsciiMarshaller(ObjectMapper objectMapper) {
    return new RequiredParameterAsciiMarshaller(objectMapper);
  }

  @Bean
  @GRpcGlobalInterceptor
  public ServerInterceptor grpcServerInterceptor(RequiredParameterHelper requiredParameterHelper,
      Metadata.AsciiMarshaller<RequiredParameter> requiredParameterAsciiMarshaller) {
    return new GrpcServerInterceptor(requiredParameterHelper, requiredParameterAsciiMarshaller);
  }
}
