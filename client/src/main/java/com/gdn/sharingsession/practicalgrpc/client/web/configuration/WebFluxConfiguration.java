package com.gdn.sharingsession.practicalgrpc.client.web.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.result.method.annotation.ArgumentResolverConfigurer;

/**
 * Created by axellageraldinc.a on 11/28/2019.
 */
@Configuration
@Slf4j
public class WebFluxConfiguration implements WebFluxConfigurer {

  private RequiredParameterArgumentResolver requiredParameterArgumentResolver;

  @Autowired
  public WebFluxConfiguration(RequiredParameterArgumentResolver requiredParameterArgumentResolver) {
    this.requiredParameterArgumentResolver = requiredParameterArgumentResolver;
  }

  @Override
  public void configureArgumentResolvers(ArgumentResolverConfigurer configurer) {
    configurer.addCustomResolver(requiredParameterArgumentResolver);
  }
}
