package com.xxw.spider.parsers;

import com.xxw.spider.httpclient.HttpRequestTemplate;
import com.xxw.spider.httpclient.exception.HttpProcessException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by xiexianwu on 20/3/5.
 */
@Service
public class SpiderParserExecutorService implements InitializingBean{

    private LinkedBlockingQueue<SpiderParseCommand> commandQueue = new LinkedBlockingQueue<>();

    private ExecutorService executorService = Executors.newCachedThreadPool();

    @Autowired
    private HttpRequestTemplate httpRequestTemplate;

    public void addCommand(SpiderParseCommand command){
        commandQueue.add(command);
    }

    private Thread thread = new Thread(new Runnable() {
        @Override
        public void run() {
          while (true){
              try {
                  SpiderParseCommand command = commandQueue.take();
                  command.execute(httpRequestTemplate, SpiderParserExecutorService.this);
              } catch (Exception e) {
                  e.printStackTrace();
              }
          }
        }
    });


    @Override
    public void afterPropertiesSet() throws Exception {
        thread.start();
    }
}
