package com.gdn.sharingsession.practicalgrpc.server.grpc.interceptor;

import com.gdn.sharingsession.practicalgrpc.server.helper.RequiredParameterHelper;
import com.gdn.sharingsession.practicalgrpc.server.model.master.RequiredParameter;
import io.grpc.ForwardingServerCall;
import io.grpc.ForwardingServerCallListener;
import io.grpc.Metadata;
import io.grpc.MethodDescriptor;
import io.grpc.ServerCall;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;
import io.grpc.Status;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

/**
 * Created by axellageraldinc.a on 1/29/2020.
 */
@Slf4j
@RequiredArgsConstructor
public class GrpcServerInterceptor implements ServerInterceptor {

  private static final String REQUIRED_PARAMETER = "required-parameter";

  private final RequiredParameterHelper requiredParameterHelper;
  private final Metadata.AsciiMarshaller<RequiredParameter> requiredParameterAsciiMarshaller;

  /**
   * Intercept every call coming to this gRPC server.
   */
  @Override
  public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> call,
      Metadata headers,
      ServerCallHandler<ReqT, RespT> next) {
    setRequiredParameter(headers);

    log.debug("[{} - {}] Incoming request",
        call.getMethodDescriptor().getFullMethodName(),
        call.getMethodDescriptor().getType());

    log.debug("[{} - {}] Incoming request headers --> {}",
        call.getMethodDescriptor().getFullMethodName(),
        call.getMethodDescriptor().getType(), headers);

    return new GrpcServerCallListener<>(next.startCall(new GrpcServerCallForwarder<>(call,
            requiredParameterAsciiMarshaller, requiredParameterHelper),
        headers), call.getMethodDescriptor());
  }

  /**
   * Set required parameter from request to tracer
   *
   * @param headers the headers (metadata) coming from gRPC request
   */
  private void setRequiredParameter(Metadata headers) {
    requiredParameterHelper.set(RequiredParameter.builder()
        .requestId(Optional.ofNullable(headers.get(Metadata.Key.of(REQUIRED_PARAMETER,
            requiredParameterAsciiMarshaller)))
            .orElse(RequiredParameter.builder().build())
            .getRequestId())
        .build());
  }


  private static class GrpcServerCallListener<ReqT, ResT>
      extends ForwardingServerCallListener.SimpleForwardingServerCallListener<ReqT> {

    private final MethodDescriptor<ReqT, ResT> methodDescriptor;

    protected GrpcServerCallListener(ServerCall.Listener<ReqT> delegate,
        MethodDescriptor<ReqT, ResT> methodDescriptor) {
      super(delegate);
      this.methodDescriptor = methodDescriptor;
    }

    @Override
    public void onMessage(ReqT message) {
      log.debug("[{} - {}] Incoming request body --> {}",
          methodDescriptor.getFullMethodName(),
          methodDescriptor.getType(), message);
      super.onMessage(message);
    }
  }


  private static class GrpcServerCallForwarder<ReqT, ResT>
      extends ForwardingServerCall.SimpleForwardingServerCall<ReqT, ResT> {

    private final Metadata.AsciiMarshaller<RequiredParameter> requiredParameterAsciiMarshaller;
    private final RequiredParameterHelper requiredParameterHelper;

    public GrpcServerCallForwarder(ServerCall<ReqT, ResT> delegate,
        Metadata.AsciiMarshaller<RequiredParameter> requiredParameterAsciiMarshaller,
        RequiredParameterHelper requiredParameterHelper) {
      super(delegate);
      this.requiredParameterAsciiMarshaller = requiredParameterAsciiMarshaller;
      this.requiredParameterHelper = requiredParameterHelper;
    }

    @Override
    public void sendHeaders(Metadata headers) {
      putRequiredParameterToHeaders(headers);

      log.debug("[{} - {}] Outcoming headers --> {}",
          getMethodDescriptor().getFullMethodName(),
          getMethodDescriptor().getType(), headers);
      super.sendHeaders(headers);
    }

    private void putRequiredParameterToHeaders(Metadata headers) {
      headers.put(Metadata.Key.of(REQUIRED_PARAMETER, requiredParameterAsciiMarshaller),
          RequiredParameter.builder()
              .requestId(requiredParameterHelper.getRequestId())
              .build());
    }

    @Override
    public void sendMessage(ResT message) {
      log.debug("[{} - {}] Outcoming response body --> {}",
          getMethodDescriptor().getFullMethodName(),
          getMethodDescriptor().getType(), message);
      super.sendMessage(message);
    }

    @Override
    public void close(Status status, Metadata trailers) {
      trailers.put(Metadata.Key.of("test", Metadata.ASCII_STRING_MARSHALLER), "test");
      log.debug("[{} - {}] Outcoming trailers --> {}",
          getMethodDescriptor().getFullMethodName(),
          getMethodDescriptor().getType(), trailers);
      super.close(status, trailers);
    }
  }
}
