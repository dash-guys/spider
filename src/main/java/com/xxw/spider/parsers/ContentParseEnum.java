package com.xxw.spider.parsers;

/**
 * Created by xiexianwu on 20/3/1.
 */
public enum ContentParseEnum {
    YOUKU("优酷","youku"),
    BILIBIBLI("B站","bilibili"),
    DOUBAN("豆瓣","douban");

    ContentParseEnum(String name, String value) {
        this.name = name;
        this.value = value;
    }

    String name;
    String value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public static ContentParseEnum getByValue(String value){
        for(ContentParseEnum contentParseEnum : values()){
            if(contentParseEnum.getValue().equals(value)){
                return contentParseEnum;
            }
        }
        throw new IllegalArgumentException("ContentParseEnum " + value + "not exist!");
    }
}
