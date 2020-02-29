package com.xxw.spider.config;

import com.xxw.spider.httpclient.DefaultHttpClientBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * Created by xiexianwu on 20/2/29.
 */
@Configuration
public class HttpClientBuilderConfig {

    @Bean
    @Primary
    @ConfigurationProperties(prefix = "http-client")
    public HttClientBuilderConfigProperties httpClientBuilderConfigProperties(){
       return new HttClientBuilderConfigProperties();
    }

    @Bean
    @Primary
    public HttpClientBuilder httpClientBuilder(@Qualifier("httpClientBuilderConfigProperties") HttClientBuilderConfigProperties properties ){
        DefaultHttpClientBuilder httpClientBuilder = DefaultHttpClientBuilder.custom()
                .pool(properties.getMaxTotal(), properties.getMaxPerRoute())
                .timeout(properties.getConnectionRequestTimeout(), properties.getConnectTimeout(), properties.getSocketTimeout(), true)
                .retry(properties.getRetryTimes());
        return httpClientBuilder;
    }

    /**
     * 注入连接池，用于获取httpClient
     * @param httpClientBuilder
     * @return
     */
    @Bean
    public CloseableHttpClient getCloseableHttpClient(@Qualifier("httpClientBuilder") HttpClientBuilder httpClientBuilder){
        return httpClientBuilder.build();
    }

}
