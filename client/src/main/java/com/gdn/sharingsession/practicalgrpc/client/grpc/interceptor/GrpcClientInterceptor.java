package com.gdn.sharingsession.practicalgrpc.client.grpc.interceptor;

import com.gdn.sharingsession.practicalgrpc.client.helper.RequiredParameterHelper;
import com.gdn.sharingsession.practicalgrpc.client.model.master.RequiredParameter;
import io.grpc.CallOptions;
import io.grpc.Channel;
import io.grpc.ClientCall;
import io.grpc.ClientInterceptor;
import io.grpc.Deadline;
import io.grpc.ForwardingClientCall;
import io.grpc.ForwardingClientCallListener;
import io.grpc.Metadata;
import io.grpc.MethodDescriptor;
import io.grpc.Status;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.Clock;
import java.util.concurrent.TimeUnit;

/**
 * Created by axellageraldinc.a on 1/29/2020.
 */
@Slf4j
@RequiredArgsConstructor
public class GrpcClientInterceptor implements ClientInterceptor {

  private static final String REQUIRED_PARAMETER = "required-parameter";

  private final Metadata.AsciiMarshaller<RequiredParameter> requiredParameterAsciiMarshaller;
  private final RequiredParameterHelper requiredParameterHelper;

  @Override
  public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(MethodDescriptor<ReqT, RespT> method,
      CallOptions callOptions,
      Channel next) {
    CallOptions callOptionsWithDeadline = callOptions
        .withDeadline(Deadline.after(30, TimeUnit.SECONDS));

    return new GrpcClientCallForwarder<>(next.newCall(method, callOptionsWithDeadline),
        method,
        requiredParameterAsciiMarshaller,
        requiredParameterHelper);
  }

  private static class GrpcClientCallForwarder<ReqT, ResT>
      extends ForwardingClientCall.SimpleForwardingClientCall<ReqT, ResT> {

    private final MethodDescriptor<ReqT, ResT> methodDescriptor;
    private final Metadata.AsciiMarshaller<RequiredParameter> requiredParameterAsciiMarshaller;
    private final RequiredParameterHelper requiredParameterHelper;

    public GrpcClientCallForwarder(ClientCall<ReqT, ResT> delegate,
        MethodDescriptor<ReqT, ResT> methodDescriptor,
        Metadata.AsciiMarshaller<RequiredParameter> requiredParameterAsciiMarshaller,
        RequiredParameterHelper requiredParameterHelper) {
      super(delegate);
      this.methodDescriptor = methodDescriptor;
      this.requiredParameterAsciiMarshaller = requiredParameterAsciiMarshaller;
      this.requiredParameterHelper = requiredParameterHelper;
    }

    @Override
    public void start(Listener<ResT> responseListener, Metadata headers) {
      Clock clock = Clock.systemUTC();

      log.debug("[{} - {}] Request started",
          methodDescriptor.getFullMethodName(),
          methodDescriptor.getType());

      putRequiredParameterToHeaders(headers);

      log.debug("[{} - {}] Request headers --> {}",
          methodDescriptor.getFullMethodName(),
          methodDescriptor.getType(), headers);

      GrpcResponseListener<ReqT, ResT> grpcResponseListener =
          new GrpcResponseListener<>(responseListener, methodDescriptor, new LogContext(clock));

      super.start(grpcResponseListener, headers);
    }

    private void putRequiredParameterToHeaders(Metadata headers) {
      headers.put(Metadata.Key.of(REQUIRED_PARAMETER, requiredParameterAsciiMarshaller),
          RequiredParameter.builder()
              .requestId(requiredParameterHelper.getRequestId())
              .build());
    }

    @Override
    public void sendMessage(ReqT message) {
      log.debug("[{} - {}] Request body --> {}",
          methodDescriptor.getFullMethodName(),
          methodDescriptor.getType(), message);
      super.sendMessage(message);
    }
  }


  private static class GrpcResponseListener<ReqT, ResT>
      extends ForwardingClientCallListener.SimpleForwardingClientCallListener<ResT> {

    private final MethodDescriptor<ReqT, ResT> methodDescriptor;
    private final LogContext logContext;

    public GrpcResponseListener(ClientCall.Listener<ResT> delegate,
        MethodDescriptor<ReqT, ResT> methodDescriptor,
        LogContext logContext) {
      super(delegate);
      this.methodDescriptor = methodDescriptor;
      this.logContext = logContext;
    }

    @Override
    public void onHeaders(Metadata headers) {
      log.debug("[{} - {}] Response headers --> {}",
          methodDescriptor.getFullMethodName(),
          methodDescriptor.getType(), headers);
      super.onHeaders(headers);
    }

    @Override
    public void onMessage(ResT message) {
      log.debug("[{} - {}] Response body --> {}",
          methodDescriptor.getFullMethodName(),
          methodDescriptor.getType(), message);
      super.onMessage(message);
    }

    @Override
    public void onClose(Status status, Metadata trailers) {
      log.debug("[{} - {}] Response status --> {}",
          methodDescriptor.getFullMethodName(),
          methodDescriptor.getType(), status);
      log.debug("[{} - {}] Response trailers --> {}",
          methodDescriptor.getFullMethodName(),
          methodDescriptor.getType(), trailers);
      log.debug("[{} - {}] Operation takes {} milliseconds",
          methodDescriptor.getFullMethodName(),
          methodDescriptor.getType(), logContext.timeSpent());

      super.onClose(status, trailers);
    }
  }


  private static class LogContext {
    private final Clock clock;
    private final long startTime;

    public LogContext(Clock clock) {
      this.clock = clock;
      this.startTime = clock.millis();
    }

    public long timeSpent() {
      return clock.millis() - startTime;
    }
  }
}
