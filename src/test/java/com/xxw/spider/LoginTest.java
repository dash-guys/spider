package com.xxw.spider;

import com.xxw.spider.httpclient.HttpRequestConfig;
import com.xxw.spider.httpclient.HttpRequestTemplate;
import com.xxw.spider.httpclient.common.HttpHeader;
import org.apache.http.Header;
import org.apache.http.client.CookieStore;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

/**
 * Unit test for simple App.
 */
@RunWith(SpringRunner.class)
//启动Spring
@SpringBootTest
public class LoginTest {

    @Autowired
    private HttpRequestTemplate httpRequestTemplate;

    @Test
    public void loginDouban() throws Exception {
        String loginUrl = "https://accounts.douban.com/j/mobile/login/basic";
        Map<String,Object> params = new HashMap<>();
        params.put("ck","");
        params.put("name","18823750056");
        params.put("password","xxxxxxxx");// 填入自己的豆瓣账号
        params.put("remember","true");
        params.put("ticket","");

        CookieStore cookieStore = new BasicCookieStore();
        HttpClientContext context = HttpClientContext.create();
        context.setCookieStore(cookieStore);

        //插件式配置请求参数（网址、请求参数、编码、client）
        HttpRequestConfig config = HttpRequestConfig.custom()
                .headers(getHeaders())	//设置headers，不需要时则无需设置
                .map(params)
                .context(context)
                .url(loginUrl)	          //设置请求的url
                .encoding("utf-8"); //设置请求和返回编码，默认就是Charset.defaultCharset()
        String result = httpRequestTemplate.post(config);
        System.out.println();
        System.out.println(result);

        String hostUrl = "https://www.douban.com/people/135257391/";
        config = HttpRequestConfig.custom()
                .headers(getHeaders())	//设置headers，不需要时则无需设置
                .context(context)
                .url(hostUrl);
        result = httpRequestTemplate.get(config);
        System.out.println(result);
    }

    private Header[] getHeaders() {
        return HttpHeader.custom()
                .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.86 Safari/537.36")
                .build();
    }
}