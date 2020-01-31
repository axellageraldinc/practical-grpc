package com.gdn.sharingsession.practicalgrpc.server.configuration.scheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.cloud.sleuth.instrument.async.TraceableExecutorService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Configuration
@RequiredArgsConstructor
public class SchedulerConfiguration {

  private final BeanFactory beanFactory;

  @Bean
  public Scheduler commonScheduler() {
    int minIdle = 50;
    int maxThread = 100;
    int ttl = 1000;
    int maxQueue = 20000;

    ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
        minIdle,
        maxThread,
        ttl,
        TimeUnit.SECONDS,
        new LinkedBlockingQueue(maxQueue),
        new ThreadWithPrefixFactory("common")
    );
    return Schedulers.fromExecutorService(new TraceableExecutorService(beanFactory,
        threadPoolExecutor));
  }

}
