package com.xxw.spider;

import com.xxw.spider.httpclient.HttpRequestConfig;
import com.xxw.spider.httpclient.HttpRequestTemplate;
import com.xxw.spider.httpclient.common.HttpHeader;
import com.xxw.spider.httpclient.exception.HttpProcessException;
import org.apache.http.Header;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by xiexianwu on 20/2/29.
 */
@RunWith(SpringRunner.class)
//启动Spring
@SpringBootTest
public class TestHttpRequest {
    @Autowired
    private HttpRequestTemplate httpRequestTemplate;

    @Test
    public void testProcessGitHub() throws HttpProcessException {
        String url = "https://github.com/Arronlong/httpclientutil";
        Header[] headers = getHeaders();

        CookieStore cookieStore = new BasicCookieStore();
        // Populate cookies if needed
        BasicClientCookie cookie = new BasicClientCookie("_ga", "GA1.2.1905312831.1517999736");
        cookie.setDomain(".github.com");
        cookie.setPath("/");

        BasicClientCookie loggedInCookie = new BasicClientCookie("logged_in", "no");
        cookie.setDomain(".github.com");
        cookie.setPath("/");
        cookieStore.addCookie(cookie);
        cookieStore.addCookie(loggedInCookie);

        HttpClientContext context = HttpClientContext.create();
        //context.setCookieSpecRegistry(createCookieSpecRegistry());
        context.setCookieStore(cookieStore);

        RequestConfig globalConfig = RequestConfig.custom()
                .setCookieSpec(CookieSpecs.DEFAULT)
                .build();

        //插件式配置请求参数（网址、请求参数、编码、client）
        HttpRequestConfig config = HttpRequestConfig.custom()
                .headers(headers)	//设置headers，不需要时则无需设置
                //.requestConfig(globalConfig)
                //.context(context)
                .url(url)	          //设置请求的url
                .encoding("utf-8"); //设置请求和返回编码，默认就是Charset.defaultCharset()

        String result = httpRequestTemplate.get(config);
        System.out.println(result);
    }

    private Header[] getHeaders() {
        return HttpHeader.custom()
                .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.86 Safari/537.36")
                .build();
    }
}
