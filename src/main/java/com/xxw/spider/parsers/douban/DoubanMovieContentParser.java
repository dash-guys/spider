package com.xxw.spider.parsers.douban;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xxw.spider.httpclient.exception.HttpProcessException;
import com.xxw.spider.parsers.ContentParse;
import com.xxw.spider.parsers.ContentParseEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by xiexianwu on 20/3/1.
 */
@Service
public class DoubanMovieContentParser implements ContentParse {
    @Autowired
    private DoubanSubjectPageParser doubanSubjectPageParse;

    @Override
    public void processor(String content) {
        // 获取所有标签
        JSONObject contentJson = JSONObject.parseObject(content);
        JSONArray tags = contentJson.getJSONArray("tags");
        for(int i = 0;i< tags.size();i++){
            String tag = tags.getString(i);
            try {
                String encodeTag = URLEncoder.encode(tag, "UTF-8");

                String childContent = doubanSubjectPageParse.download(Constants.subjectUrl, encodeTag, Constants.pageLimit, 0);
                doubanSubjectPageParse.parseSubject(childContent,encodeTag,0);

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (HttpProcessException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public ContentParseEnum getParseType() {
        return ContentParseEnum.DOUBAN;
    }
}
