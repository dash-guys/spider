package com.xxw.spider.parsers.douban;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xxw.spider.httpclient.HttpRequestConfig;
import com.xxw.spider.httpclient.HttpRequestTemplate;
import com.xxw.spider.httpclient.common.HttpHeader;
import com.xxw.spider.httpclient.exception.HttpProcessException;
import com.xxw.spider.parsers.SpiderParserExecutorService;
import org.apache.http.Header;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by xiexianwu on 20/3/1.
 */
@Service
public class DoubanSubjectPageParser {

    @Autowired
    private SpiderParserExecutorService spiderParserExecutorService;

    @Autowired
    private HttpRequestTemplate httpRequestTemplate;

    public String download(String url,String tag, int pageSize,int pageNo) throws HttpProcessException {
        String formatUrl = String.format(url, tag, pageSize, pageNo);
        // 开始爬虫获取内容
        //插件式配置请求参数（网址、请求参数、编码、client）
        HttpRequestConfig config = HttpRequestConfig.custom()
                .headers(getHeaders())	  //设置headers，不需要时则无需设置
                .url(formatUrl)	              //设置请求的url
                .encoding("utf-8");       //设置请求和返回编码，默认就是Charset.defaultCharset()
        String content = httpRequestTemplate.get(config);
        return content;
    }

    public void parseSubject(String content,String encodeTag, int pageNo){
        JSONObject contentJson = JSONObject.parseObject(content);
        JSONArray subjects = contentJson.getJSONArray("subjects");
        for(int i = 0;i< subjects.size();i++){
            MovieSubject movie = subjects.getObject(i, MovieSubject.class);
            System.out.println(movie.getTitle());
        }

        if(subjects.size() >= Constants.pageLimit){
            DoubanSubjectSpiderParseCommand command = new DoubanSubjectSpiderParseCommand();
            DoubanSubjectPageParams pageParams = new DoubanSubjectPageParams();
            pageParams.setPageNo(pageNo + 1);
            pageParams.setPageSize(Constants.pageLimit);
            pageParams.setTag(encodeTag);
            pageParams.setUrl(Constants.subjectUrl);

            command.setActionParams(pageParams);
            spiderParserExecutorService.addCommand(command);

        }
        //String formatUrl = String.format(Constants.subjectUrl, encodeTag, Constants.pageLimit, pageNo + 1);
    }

    private Header[] getHeaders() {
        return HttpHeader.custom()
                .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.86 Safari/537.36")
                .build();
    }
}
