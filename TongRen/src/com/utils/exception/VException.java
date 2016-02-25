package com.utils.exception;

/**
 * 
 * @Filename VException.java
 * @Author xuxinjian
 * @Date 2014-3-13
 */
public class VException extends Exception {

    private int statusCode=-1;
    /**
     * 无参构造，
     * @param   
     * 为创建缺省参数对象提供方便
     * */
    public VException(){}
    /**
     * 有参构造，
     * @param msg 在创建这个异常对象是提供描述这个异常信息的字符串，
     * 通过调用超类构造器向上传递给超类，对超类中的toString()方法中返回的原有信息进行覆盖
     * */
    public VException(String msg) {
        super(msg);
    }
    /**
     * 
     * @param cause
     */
    public VException(Exception cause) {
        super(cause);
    }

    public VException(String msg, int statusCode) {
        super(msg);
        this.statusCode=statusCode;
    }

    public VException(String msg, Exception cause) {
        super(msg, cause);
    }

    public VException(String msg, Exception cause, int statusCode) {
        super(msg, cause);
        this.statusCode=statusCode;
    }

    public int getStatusCode() {
        return this.statusCode;
    }
}
