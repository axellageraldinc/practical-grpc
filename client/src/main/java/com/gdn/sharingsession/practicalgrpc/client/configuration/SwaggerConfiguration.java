package com.gdn.sharingsession.practicalgrpc.client.configuration;

import com.gdn.sharingsession.practicalgrpc.client.model.master.RequiredParameterConstant;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Collections;

/**
 * Created by axellageraldinc.a on 11/28/2019.
 */
@Configuration
public class SwaggerConfiguration {

  private static final String HEADER = "header";
  private static final String STRING_TYPE = "string";

  @Bean
  public Docket docket() {
    return new Docket(DocumentationType.SWAGGER_2).select()
        .apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
        .paths(PathSelectors.any())
        .build()
        .globalOperationParameters(Collections.singletonList(
            new ParameterBuilder()
                .name(RequiredParameterConstant.REQUEST_ID.getVariable())
                .parameterType(HEADER)
                .modelRef(new ModelRef(STRING_TYPE))
                .required(true)
                .defaultValue(RequiredParameterConstant.REQUEST_ID.getDefaultValue())
                .description(RequiredParameterConstant.REQUEST_ID.getDescription())
                .build()
        ));
  }
}
