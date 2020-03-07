package com.xxw.spider.parsers.youku;

import com.xxw.spider.parsers.ContentParse;
import com.xxw.spider.parsers.ContentParseEnum;
import org.springframework.stereotype.Service;

/**
 * Created by xiexianwu on 20/2/29.
 */
@Service
public class YoukuContentParser implements ContentParse {

    @Override
    public void processor(String content) {

    }

    @Override
    public ContentParseEnum getParseType() {
        return ContentParseEnum.YOUKU;
    }
}
