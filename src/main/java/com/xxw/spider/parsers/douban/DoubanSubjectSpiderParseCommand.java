package com.xxw.spider.parsers.douban;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xxw.spider.httpclient.HttpRequestConfig;
import com.xxw.spider.httpclient.HttpRequestTemplate;
import com.xxw.spider.httpclient.common.HttpHeader;
import com.xxw.spider.httpclient.exception.HttpProcessException;
import com.xxw.spider.parsers.SpiderParseCommand;
import com.xxw.spider.parsers.SpiderParserExecutorService;
import com.xxw.spider.parsers.SpiderParserParams;
import org.apache.http.Header;

/**
 * Created by xiexianwu on 20/3/7.
 */
public class DoubanSubjectSpiderParseCommand implements SpiderParseCommand {

    private SpiderParserParams spiderParserParams;

    @Override
    public void setActionParams(SpiderParserParams params) {
       this.spiderParserParams = params;
    }

    @Override
    public SpiderParserParams getActionParams() {
        return spiderParserParams;
    }

    @Override
    public void execute(HttpRequestTemplate httpRequestTemplate, SpiderParserExecutorService spiderService) throws HttpProcessException {
        DoubanSubjectPageParams params = (DoubanSubjectPageParams) spiderParserParams;
        String formatUrl = String.format(params.getUrl(), params.getTag(), params.getPageSize(), params.getPageNo());

        // 开始爬虫获取内容
        //插件式配置请求参数（网址、请求参数、编码、client）
        HttpRequestConfig config = HttpRequestConfig.custom()
                .headers(getHeaders())	  //设置headers，不需要时则无需设置
                .url(formatUrl)	          //设置请求的url
                .encoding("utf-8");       //设置请求和返回编码，默认就是Charset.defaultCharset()
        String content = httpRequestTemplate.get(config);

        JSONObject contentJson = JSONObject.parseObject(content);
        JSONArray subjects = contentJson.getJSONArray("subjects");
        for(int i = 0;i< subjects.size();i++){
            MovieSubject movie = subjects.getObject(i, MovieSubject.class);
            System.out.println(movie.getTitle());
        }

        if(subjects.size() >= Constants.pageLimit){
            DoubanSubjectSpiderParseCommand command = new DoubanSubjectSpiderParseCommand();
            DoubanSubjectPageParams pageParams = new DoubanSubjectPageParams();
            pageParams.setPageNo(params.getPageNo() + 1);
            pageParams.setPageSize(Constants.pageLimit);
            pageParams.setTag(params.getTag());
            pageParams.setUrl(Constants.subjectUrl);

            command.setActionParams(pageParams);
            spiderService.addCommand(command);

        }
    }

    private Header[] getHeaders() {
        return HttpHeader.custom()
                .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.86 Safari/537.36")
                .build();
    }
}
