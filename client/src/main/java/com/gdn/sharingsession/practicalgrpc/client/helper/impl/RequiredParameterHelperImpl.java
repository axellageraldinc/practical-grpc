package com.gdn.sharingsession.practicalgrpc.client.helper.impl;

import brave.Span;
import brave.Tracer;
import brave.propagation.ExtraFieldPropagation;
import brave.propagation.TraceContext;
import com.gdn.sharingsession.practicalgrpc.client.helper.RequiredParameterHelper;
import com.gdn.sharingsession.practicalgrpc.client.model.master.RequiredParameter;
import com.gdn.sharingsession.practicalgrpc.client.model.master.RequiredParameterConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

/**
 * Created by axellageraldinc.a on 11/28/2019.
 */
@Service
@Slf4j
public class RequiredParameterHelperImpl implements RequiredParameterHelper {

  private Tracer tracer;

  @Autowired
  public RequiredParameterHelperImpl(Tracer tracer) {
    this.tracer = tracer;
  }

  @Override
  public RequiredParameter get() {
    return RequiredParameter.builder()
        .requestId(getRequestId())
        .build();
  }

  @Override
  public String getRequestId() {
    TraceContext currentContext = tracer.currentSpan().context();
    return ExtraFieldPropagation.get(currentContext,
        RequiredParameterConstant.REQUEST_ID.getVariable());
  }

  @Override
  public void set(RequiredParameter requiredParameter) {
    if (ObjectUtils.isEmpty(requiredParameter)) {
      return;
    }
    Span initialSpan = tracer.currentSpan();
    if (ObjectUtils.isEmpty(initialSpan)) {
      initialSpan = tracer.nextSpan();
    }
    ExtraFieldPropagation.set(initialSpan.context(),
        RequiredParameterConstant.REQUEST_ID.getVariable(),
        requiredParameter.getRequestId());
  }
}
