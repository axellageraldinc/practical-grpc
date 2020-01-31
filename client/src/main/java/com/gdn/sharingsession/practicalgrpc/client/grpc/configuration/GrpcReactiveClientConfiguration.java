package com.gdn.sharingsession.practicalgrpc.client.grpc.configuration;

import com.gdn.sharingsession.practicalgrpc.client.generatedproto.ReactorBookStoreServiceGrpc;
import io.grpc.Channel;
import io.grpc.ClientInterceptor;
import io.grpc.ClientInterceptors;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by axellageraldinc.a on 1/28/2020.
 */
@Configuration
public class GrpcReactiveClientConfiguration {
  @Bean
  public ReactorBookStoreServiceGrpc.ReactorBookStoreServiceStub bookStoreServiceGrpcReactorStub(
      Channel channel,
      ClientInterceptor grpcInterceptor) {
    Channel channelWithInterceptor =
        ClientInterceptors.intercept(channel, grpcInterceptor);
    return ReactorBookStoreServiceGrpc.newReactorStub(channelWithInterceptor);
  }
}
