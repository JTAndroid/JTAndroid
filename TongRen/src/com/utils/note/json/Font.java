package com.utils.note.json;

public class Font {
    private String fontName;

    private int fontSize;

    private String fontStyle;

    private String fontWeight;


    public Font(){
        fontName = ".Helvetica Neue Interface";
        fontSize = 14;
        fontStyle = "normal";
        fontWeight = "normal";
    }

    public void setFontName(String fontName) {
        this.fontName = fontName;
    }

    public String getFontName() {
        return this.fontName;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    public int getFontSize() {
        return this.fontSize;
    }

    public void setFontStyle(String fontStyle) {
        this.fontStyle = fontStyle;
    }

    public String getFontStyle() {
        return this.fontStyle;
    }

    public void setFontWeight(String fontWeight) {
        this.fontWeight = fontWeight;
    }

    public String getFontWeight() {
        return this.fontWeight;
    }

}
