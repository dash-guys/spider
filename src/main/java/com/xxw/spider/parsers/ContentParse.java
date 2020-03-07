package com.xxw.spider.parsers;

/**
 * Created by xiexianwu on 20/2/29.
 */
public interface ContentParse {
    void processor(String content);

    ContentParseEnum getParseType();
}
