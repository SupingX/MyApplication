package com.laputa.zeejp.lib_common.http.response;

/**
 * 消息类型
 */
public class MessageTypeResponse {
    private String id;
    private String typeName;
    private String typeValue;
    private String description;
    /**  1 有未读的  0 没有未读*/
    private String haveMessage;
    private String date;
    /** 图标icon */
    private String url;
    private int noReadCount;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeValue() {
        return typeValue;
    }

    public void setTypeValue(String typeValue) {
        this.typeValue = typeValue;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHaveMessage() {
        return haveMessage;
    }

    public void setHaveMessage(String haveMessage) {
        this.haveMessage = haveMessage;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getNoReadCount() {
        return noReadCount;
    }

    public void setNoReadCount(int noReadCount) {
        this.noReadCount = noReadCount;
    }
}
