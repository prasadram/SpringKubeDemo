package org.letuslearn.springkubedemo.controller;

import lombok.extern.slf4j.Slf4j;
import org.letuslearn.springkubedemo.config.GreetingsConfiguration;
import org.letuslearn.springkubedemo.controller.dto.StatusDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.concurrent.*;

@Controller
@Slf4j
public class HelloController {

    @Autowired
    private GreetingsConfiguration greetingsConfiguration;

    @GetMapping(value = "/kube/greetings", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StatusDto> greetings() throws InterruptedException {
        log.info("op=greetings, status=OK, desc=get greetings message:{}", greetingsConfiguration.getMessage());
        int start = 1;
        int end = 500;
        /*ExecutorService eService = Executors.newFixedThreadPool(5);
        CompletableFuture<Void> completableFuture = CompletableFuture.runAsync(new Runnable() {
            @Override
            public void run() {
                try {
                    TimeUnit.SECONDS.sleep(2);
                   // int myend = start + 100;
                    //int mystart = start;
                    for(int i=0;i<15;i++) {
                        log.info("op=async_greetings, status=OK, desc=processing {}", i);
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                log.info("op=async_greetings, status=OK, desc=processing completed");
            }
        }, eService);*/
       /* int j = 100;
        for (int i=0;i<1000000;) {
            processNumbers(i, j);
            i = j;
            j = j + 1000;
        }*/
        return ResponseEntity.ok().body(StatusDto.builder()
        .message(greetingsConfiguration.getMessage())
        .status("OK").build());
    }

    @Async//("threadPoolTaskExecutor")
    public CompletableFuture<Integer> processNumbers(int start, int end) throws InterruptedException{
        for(;start<end;start++) {
            log.info("op=process, desc=item {}", start);
        }
        return CompletableFuture.completedFuture(start);
    }
}
