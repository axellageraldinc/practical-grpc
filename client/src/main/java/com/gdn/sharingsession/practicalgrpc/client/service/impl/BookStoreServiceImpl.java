package com.gdn.sharingsession.practicalgrpc.client.service.impl;

import com.gdn.sharingsession.practicalgrpc.client.generatedproto.BookStoreProto;
import com.gdn.sharingsession.practicalgrpc.client.generatedproto.BookStoreServiceGrpc;
import com.gdn.sharingsession.practicalgrpc.client.service.BookStoreService;
import com.gdn.sharingsession.practicalgrpc.client.model.web.response.BookResponse;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lognet.springboot.grpc.GRpcService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by axellageraldinc.a on 1/28/2020.
 */
@GRpcService
@RequiredArgsConstructor
@Slf4j
public class BookStoreServiceImpl implements BookStoreService {

  private final BookStoreServiceGrpc.BookStoreServiceBlockingStub bookStoreServiceGrpcBlockingStub;
  private final BookStoreServiceGrpc.BookStoreServiceStub bookStoreServiceGrpcAsyncStub;

  @Override
  public List<BookResponse> getAllBooks() {
    return bookStoreServiceGrpcBlockingStub.getAllBook(constructGetAllBookRequest())
        .getGetBookResponsesList()
        .stream()
        .map(this::toBookResponse)
        .collect(Collectors.toList());
  }

  private BookStoreProto.GetAllBookRequest constructGetAllBookRequest() {
    return BookStoreProto.GetAllBookRequest.newBuilder().build();
  }

  private BookResponse toBookResponse(BookStoreProto.GetBookResponse getBookResponse) {
    return BookResponse.builder()
        .id(getBookResponse.getId())
        .title(getBookResponse.getTitle())
        .build();
  }

  @Override
  public void streamAllBooks() {
    bookStoreServiceGrpcBlockingStub.streamAllBook(constructGetAllBookRequest())
        .forEachRemaining(getBookResponse -> log.info(
            "#Incoming response from server-side streaming : {}",
            getBookResponse));
  }

//  @Override
//  public void getBookByIdClientStream() {
//    StreamObserver<BookStoreProto.GetAllBookResponse> responseObserver =
//        new StreamObserver<BookStoreProto.GetAllBookResponse>() {
//          @Override
//          public void onNext(BookStoreProto.GetAllBookResponse value) {
//            log.info("#Incoming response from client-side streaming : {}", value);
//          }
//
//          @Override
//          public void onError(Throwable t) {
//
//          }
//
//          @Override
//          public void onCompleted() {
//
//          }
//        };
//
//    StreamObserver<BookStoreProto.GetOneBookRequest> requestObserver =
//        bookStoreServiceGrpcAsyncStub.getBookByIdClientStream(responseObserver);
//
//    requestObserver.onNext(BookStoreProto.GetOneBookRequest.newBuilder()
//        .setId("5e3125822ddfca51a4d44ac7")
//        .build());
//    requestObserver.onNext(BookStoreProto.GetOneBookRequest.newBuilder()
//        .setId("5e31258c2ddfca51a4d44ac8")
//        .build());
//    requestObserver.onNext(BookStoreProto.GetOneBookRequest.newBuilder()
//        .setId("5e3125982ddfca51a4d44ac9")
//        .build());
//    requestObserver.onCompleted();
//  }

//  @Override
//  public void getAllBookBidirectional() {
//    StreamObserver<BookStoreProto.GetBookResponse> responseObserver =
//        new StreamObserver<BookStoreProto.GetBookResponse>() {
//          @Override
//          public void onNext(BookStoreProto.GetBookResponse value) {
//            log.info("#Incoming response from bidirectional-streaming : {}", value);
//          }
//
//          @Override
//          public void onError(Throwable t) {
//
//          }
//
//          @Override
//          public void onCompleted() {
//
//          }
//        };
//
//    StreamObserver<BookStoreProto.GetOneBookRequest> requestObserver =
//        bookStoreServiceGrpcAsyncStub.getAllBookBidirectional(responseObserver);
//
//    requestObserver.onNext(BookStoreProto.GetOneBookRequest.newBuilder()
//        .setId("5e3125822ddfca51a4d44ac7")
//        .build());
//    requestObserver.onNext(BookStoreProto.GetOneBookRequest.newBuilder()
//        .setId("5e31258c2ddfca51a4d44ac8")
//        .build());
//    requestObserver.onNext(BookStoreProto.GetOneBookRequest.newBuilder()
//        .setId("5e3125982ddfca51a4d44ac9")
//        .build());
//    requestObserver.onCompleted();
//  }
}
