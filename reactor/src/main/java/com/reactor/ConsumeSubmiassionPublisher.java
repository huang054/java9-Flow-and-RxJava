package com.reactor;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.SubmissionPublisher;
import java.util.stream.LongStream;

public class ConsumeSubmiassionPublisher {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        publish();
    }

    private static void publish() throws ExecutionException, InterruptedException {
        CompletableFuture future = null;
        try(SubmissionPublisher publisher = new SubmissionPublisher<Long>()){
            System.out.println("buffer size :"+publisher.getMaxBufferCapacity());
            future=publisher.consume(System.out::println);
            LongStream.range(1,10).forEach(publisher::submit);
        }finally {
          future.get();
        }

    }
}
