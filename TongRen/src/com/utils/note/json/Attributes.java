package com.utils.note.json;

/**
 * Created by zhangchangwei on 2015/8/18.
 */
public class Attributes {
    private Font font;

    private ParagraphStyle paragraphStyle;

    private Attachment attachment;

    private String foregroundColor;

    private int strikethrough;

    private int underline;

    public Attributes(){
        font = new Font();
        paragraphStyle = new ParagraphStyle();
        foregroundColor = "rgb(0,0,0)";
        strikethrough = 0;
        underline = 0;
    }

    public void setFont(Font font) {
        this.font = font;
    }

    public Font getFont() {
        return this.font;
    }

    public void setParagraphStyle(ParagraphStyle paragraphStyle) {
        this.paragraphStyle = paragraphStyle;
    }

    public ParagraphStyle getParagraphStyle() {
        return this.paragraphStyle;
    }

    public void setAttachment(Attachment attachment){
        this.attachment = attachment;
    }

    public Attachment getAttachment(){
        return attachment;
    }

    public int getStrikethrough(){
        return strikethrough;
    }

    public void setStrikethrough(int strikthrough){
        this.strikethrough = strikthrough;
    }

    public int getUnderline(){
        return underline;
    }

    public void setUnderline(int underline){
        this.underline = underline;
    }

    public String getForegroundColor(){
        return foregroundColor;
    }

    public void setForegroundColor(String foregroundColor){
        this.foregroundColor = foregroundColor;
    }

}
