package com.gdn.sharingsession.practicalgrpc.client.grpc.configuration;

import com.gdn.sharingsession.practicalgrpc.client.generatedproto.BookStoreServiceGrpc;
import io.grpc.Channel;
import io.grpc.ClientInterceptor;
import io.grpc.ClientInterceptors;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by axellageraldinc.a on 1/28/2020.
 */
@Configuration
public class GrpcNonReactiveClientConfiguration {
  @Bean
  public BookStoreServiceGrpc.BookStoreServiceBlockingStub bookStoreServiceGrpcBlockingStub(Channel channel,
      ClientInterceptor grpcInterceptor) {
    Channel channelWithInterceptor =
        ClientInterceptors.intercept(channel, grpcInterceptor);
    return BookStoreServiceGrpc.newBlockingStub(channelWithInterceptor);
  }

  @Bean
  public BookStoreServiceGrpc.BookStoreServiceStub bookStoreServiceGrpcAsyncStub(Channel channel,
      ClientInterceptor grpcInterceptor) {
    Channel channelWithInterceptor =
        ClientInterceptors.intercept(channel, grpcInterceptor);
    return BookStoreServiceGrpc.newStub(channelWithInterceptor);
  }
}
