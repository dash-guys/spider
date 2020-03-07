package com.xxw.spider.parsers.douban;

import com.xxw.spider.parsers.SpiderParserParams;

/**
 * Created by xiexianwu on 20/3/7.
 */
public class DoubanSubjectPageParams extends SpiderParserParams {

    private String tag;

    private int pageSize;

    private int pageNo;

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }
}
