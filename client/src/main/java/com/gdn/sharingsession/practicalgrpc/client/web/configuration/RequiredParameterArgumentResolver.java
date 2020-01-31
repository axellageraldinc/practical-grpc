package com.gdn.sharingsession.practicalgrpc.client.web.configuration;

import com.gdn.sharingsession.practicalgrpc.client.helper.RequiredParameterHelper;
import com.gdn.sharingsession.practicalgrpc.client.model.master.RequiredParameter;
import com.gdn.sharingsession.practicalgrpc.client.model.master.RequiredParameterConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.BindingContext;
import org.springframework.web.reactive.result.method.HandlerMethodArgumentResolver;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Created by axellageraldinc.a on 11/29/2019.
 */
@Configuration
@Slf4j
public class RequiredParameterArgumentResolver implements HandlerMethodArgumentResolver {

  private RequiredParameterHelper requiredParameterHelper;

  @Autowired
  public RequiredParameterArgumentResolver(RequiredParameterHelper requiredParameterHelper) {
    this.requiredParameterHelper = requiredParameterHelper;
  }

  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    return parameter.getParameterType() == RequiredParameter.class;
  }

  @Override
  public Mono<Object> resolveArgument(
      MethodParameter parameter,
      BindingContext bindingContext,
      ServerWebExchange exchange) {
    return Mono.fromSupplier(() -> {
      RequiredParameter requiredParameter =
          constructRequiredParameter(exchange.getRequest().getHeaders());
      requiredParameterHelper.set(requiredParameter);
      return requiredParameter;
    });
  }

  private RequiredParameter constructRequiredParameter(HttpHeaders httpHeaders) {
    return RequiredParameter.builder()
        .requestId(httpHeaders.getFirst(RequiredParameterConstant.REQUEST_ID.getVariable()))
        .build();
  }
}