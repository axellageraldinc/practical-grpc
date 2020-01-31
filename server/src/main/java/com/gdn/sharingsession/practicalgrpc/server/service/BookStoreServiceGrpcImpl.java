package com.gdn.sharingsession.practicalgrpc.server.service;

import com.gdn.sharingsession.practicalgrpc.server.generatedproto.BookStoreProto;
import com.gdn.sharingsession.practicalgrpc.server.generatedproto.BookStoreServiceGrpc;
import com.gdn.sharingsession.practicalgrpc.server.model.entity.Book;
import com.gdn.sharingsession.practicalgrpc.server.repository.BookRepository;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by axellageraldinc.a on 1/29/2020.
 */
//@GRpcService // comment to deactivate
@RequiredArgsConstructor
@Slf4j
public class BookStoreServiceGrpcImpl extends BookStoreServiceGrpc.BookStoreServiceImplBase {

  private final BookRepository bookRepository;

  @Override
  public void getAllBook(BookStoreProto.GetAllBookRequest request,
      StreamObserver<BookStoreProto.GetAllBookResponse> responseObserver) {
    List<BookStoreProto.GetBookResponse> getBookResponses = bookRepository.findAll()
        .stream()
        .map(this::toGetBookResponse)
        .collect(Collectors.toList());
    responseObserver.onNext(toAllBookResponse(getBookResponses));
    responseObserver.onCompleted();
  }

  private BookStoreProto.GetBookResponse toGetBookResponse(Book book) {
    return BookStoreProto.GetBookResponse.newBuilder()
        .setId(book.getId())
        .setTitle(book.getTitle())
        .build();
  }

  private BookStoreProto.GetAllBookResponse toAllBookResponse(List<BookStoreProto.GetBookResponse> getBookResponses) {
    return BookStoreProto.GetAllBookResponse.newBuilder()
        .addAllGetBookResponses(getBookResponses)
        .build();
  }

  @Override
  public void streamAllBook(BookStoreProto.GetAllBookRequest request,
      StreamObserver<BookStoreProto.GetBookResponse> responseObserver) {
    List<BookStoreProto.GetBookResponse> responses = new ArrayList<>();
    responses.add(BookStoreProto.GetBookResponse.newBuilder()
        .setId("1")
        .setTitle("1")
        .build());
    responses.add(BookStoreProto.GetBookResponse.newBuilder()
        .setId("2")
        .setTitle("2")
        .build());
    responses.add(BookStoreProto.GetBookResponse.newBuilder()
        .setId("3")
        .setTitle("3")
        .build());
    responses
        .stream()
        .peek(responseObserver::onNext)
        .collect(Collectors.toList());
    responseObserver.onCompleted();
  }

//  @Override
//  public StreamObserver<BookStoreProto.GetOneBookRequest> getBookByIdClientStream(StreamObserver<BookStoreProto.GetAllBookResponse> responseObserver) {
//    return new StreamObserver<BookStoreProto.GetOneBookRequest>() {
//      List<String> bookIds = new ArrayList<>();
//
//      @Override
//      public void onNext(BookStoreProto.GetOneBookRequest value) {
//        log.info("#Incoming stream request : {}", value);
//        bookIds.add(value.getId());
//      }
//
//      @Override
//      public void onError(Throwable t) {
//        responseObserver.onError(t);
//      }
//
//      @Override
//      public void onCompleted() {
//        List<BookStoreProto.GetBookResponse> getBookResponses =
//            bookRepository.findAllByIdIn(bookIds)
//                .stream()
//                .map(book -> BookStoreProto.GetBookResponse.newBuilder()
//                    .setId(book.getId())
//                    .setTitle(book.getTitle())
//                    .build())
//                .collect(Collectors.toList());
//        responseObserver.onNext(toAllBookResponse(getBookResponses));
//        responseObserver.onCompleted();
//      }
//    };
//  }

//  @Override
//  public StreamObserver<BookStoreProto.GetOneBookRequest> getAllBookBidirectional(StreamObserver<BookStoreProto.GetBookResponse> responseObserver) {
//    return new StreamObserver<BookStoreProto.GetOneBookRequest>() {
//
//      @Override
//      public void onNext(BookStoreProto.GetOneBookRequest value) {
//        log.info("#Incoming request bidirectional-streaming : {}", value);
//        Book book = bookRepository.findById(value.getId()).orElse(Book.builder().build());
//        responseObserver.onNext(BookStoreProto.GetBookResponse.newBuilder()
//            .setId(book.getId())
//            .setTitle(book.getTitle())
//            .build());
//      }
//
//      @Override
//      public void onError(Throwable t) {
//        responseObserver.onError(t);
//      }
//
//      @Override
//      public void onCompleted() {
//        responseObserver.onCompleted();
//      }
//    };
//  }
}
