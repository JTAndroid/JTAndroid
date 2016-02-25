package com.utils.note.json;

/**
 * Created by zhangchangwei on 2015/8/19.
 */
public class Attachment {
    public static final String TYPE_IMAGE = "attachment-image";
    public static final String TYPE_AUDIO = "attachment-audio";
    public static final String BASE_URL = "http://file.online.gintong.com/meeting/pic/0014420619";

    private String suffix;

    private String type;

    private String url;

    public Attachment(){
        suffix = "";
        type = "";
        url = "";
    }

    public String getSuffix(){
        return suffix;
    }

    public String getType(){
        return type;
    }

    public String getUrl(){
        return url;
    }

    public void setSuffix(String suffix){
        this.suffix = suffix;
    }

    public void setType(String type){
        this.type = type;
    }

    public void setUrl(String url){
        this.url = url;
    }
}
