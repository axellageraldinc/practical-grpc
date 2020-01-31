package com.gdn.sharingsession.practicalgrpc.client.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by axellageraldinc.a on 1/29/2020.
 */
@Configuration
public class GeneralBeanConfiguration {
  @Bean
  public ObjectMapper objectMapper() {
    return new ObjectMapper();
  }
}
