package com.gdn.sharingsession.practicalgrpc.client.configuration;

import brave.propagation.B3Propagation;
import brave.propagation.ExtraFieldPropagation;
import com.gdn.sharingsession.practicalgrpc.client.model.master.RequiredParameterConstant;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class TracerConfiguration {
  @Bean
  public ExtraFieldPropagation.Factory extraFieldPropagation() {
    return ExtraFieldPropagation.newFactoryBuilder(B3Propagation.FACTORY)
        .addField(RequiredParameterConstant.REQUEST_ID.getVariable())
        .build();
  }
}
