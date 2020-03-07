package com.xxw.spider.parsers;

import com.xxw.spider.httpclient.HttpRequestTemplate;
import com.xxw.spider.httpclient.exception.HttpProcessException;

/**
 * Created by xiexianwu on 20/3/5.
 */
public interface SpiderParseCommand {

    void setActionParams(SpiderParserParams parserParams);

    SpiderParserParams getActionParams();

    void execute(HttpRequestTemplate httpRequestTemplate, SpiderParserExecutorService spiderService) throws HttpProcessException;
}
