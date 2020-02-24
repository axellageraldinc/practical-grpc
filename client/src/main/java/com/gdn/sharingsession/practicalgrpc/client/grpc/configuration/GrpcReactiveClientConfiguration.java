package com.gdn.sharingsession.practicalgrpc.client.grpc.configuration;

import com.gdn.sharingsession.practicalgrpc.client.generatedproto.ReactorBookStoreServiceGrpc;
import io.grpc.Channel;
import io.grpc.ClientInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by axellageraldinc.a on 1/28/2020.
 */
@Configuration
public class GrpcReactiveClientConfiguration {
  /**
   * Declaring the STUB as a bean.
   *
   * @param channel the channel to connect to gRPC server
   * @param grpcInterceptor the client interceptor desired for this particular stub
   * @return the stub
   */
  @Bean
  public ReactorBookStoreServiceGrpc.ReactorBookStoreServiceStub bookStoreServiceGrpcReactorStub(
      Channel channel,
      ClientInterceptor grpcInterceptor) {
    return ReactorBookStoreServiceGrpc.newReactorStub(channel)
        .withInterceptors(grpcInterceptor);
  }
}
