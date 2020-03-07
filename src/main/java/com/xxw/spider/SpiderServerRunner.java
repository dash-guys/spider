package com.xxw.spider;

import com.xxw.spider.httpclient.HttpRequestConfig;
import com.xxw.spider.httpclient.HttpRequestTemplate;
import com.xxw.spider.httpclient.common.HttpHeader;
import com.xxw.spider.parsers.ContentParse;
import com.xxw.spider.parsers.ContentParseEnum;
import org.apache.http.Header;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Created by xiexianwu on 19/12/23.
 */
@Component
@Order(1)
@ConfigurationProperties(prefix = "spider")
public class SpiderServerRunner implements ApplicationRunner{

    @Autowired
    private HttpRequestTemplate httpRequestTemplate;

    private Map<String,String> startPages;

    @Autowired
    private List<ContentParse> contentProcessors;

    @Override
    public void run(ApplicationArguments applicationArguments) throws Exception {
        // 循环配置的站点抓数据
        for(Map.Entry<String,String> startPageMap : startPages.entrySet()){
            String name = startPageMap.getKey();
            String startPage = startPageMap.getValue();
            ContentParseEnum contentProcessorEnum = ContentParseEnum.getByValue(name);

            // 开始爬虫获取内容
            //插件式配置请求参数（网址、请求参数、编码、client）
            HttpRequestConfig config = HttpRequestConfig.custom()
                    .headers(getHeaders())	  //设置headers，不需要时则无需设置
                    .url(startPage)	          //设置请求的url
                    .encoding("utf-8");       //设置请求和返回编码，默认就是Charset.defaultCharset()
            String content = httpRequestTemplate.get(config);

            // 获取解析类
            ContentParse contentProcessor = getContentProcessor(contentProcessorEnum);
            if(contentProcessor != null){
               contentProcessor.processor(content);
            }

        }
    }

    private ContentParse getContentProcessor(ContentParseEnum contentProcessorEnum){
        for(ContentParse contentProcessor: contentProcessors){
            if(contentProcessorEnum == contentProcessor.getParseType()){
                return contentProcessor;
            }
        }
        return null;
    }

    private Header[] getHeaders() {
        return HttpHeader.custom()
                .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.86 Safari/537.36")
                .build();
    }

    public void setStartPages(Map<String, String> startPages) {
        this.startPages = startPages;
    }
}
