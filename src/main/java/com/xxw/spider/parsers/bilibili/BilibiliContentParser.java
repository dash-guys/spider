package com.xxw.spider.parsers.bilibili;

import com.xxw.spider.parsers.ContentParse;
import com.xxw.spider.parsers.ContentParseEnum;
import org.springframework.stereotype.Service;

/**
 * Created by xiexianwu on 20/3/1.
 */
@Service
public class BilibiliContentParser implements ContentParse {
    @Override
    public void processor(String content) {

    }

    @Override
    public ContentParseEnum getParseType() {
        return ContentParseEnum.BILIBIBLI;
    }
}
