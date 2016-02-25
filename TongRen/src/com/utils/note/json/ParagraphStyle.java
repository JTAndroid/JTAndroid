package com.utils.note.json;

/**
 * Created by zhangchangwei on 2015/8/18.
 */
public class ParagraphStyle {
    public static final int ALIGNMENT_NORMAL = 4;
    public static final int ALIGNMENT_FULL = 3;
    public static final int ALIGNMENT_RIGHT = 2;
    public static final int ALIGNMENT_CENTER = 1;
    public static final int ALIGNMENT_LEFT = 0;

    private int alignment;

    private int firstLineHeadIndent;

    private int headIndent;

    private int lineBreakMode;

    private int lineHeightMultiple;

    private int lineSpacing;

    private int paragraphSpacing;

    private int paragraphSpacingBefore;

    private int tailIndent;

    public ParagraphStyle(){
        alignment = ALIGNMENT_NORMAL;
        firstLineHeadIndent = 0;
        headIndent = 0;
        lineBreakMode = 0;
        lineHeightMultiple = 0;
        lineSpacing = 9;
        paragraphSpacing = 0;
        paragraphSpacingBefore = 0;
        tailIndent = 0;
    }

    public void setAlignment(int alignment) {
        this.alignment = alignment;
    }

    public int getAlignment() {
        return this.alignment;
    }

    public void setFirstLineHeadIndent(int firstLineHeadIndent) {
        this.firstLineHeadIndent = firstLineHeadIndent;
    }

    public int getFirstLineHeadIndent() {
        return this.firstLineHeadIndent;
    }

    public void setHeadIndent(int headIndent) {
        this.headIndent = headIndent;
    }

    public int getHeadIndent() {
        return this.headIndent;
    }

    public void setLineBreakMode(int lineBreakMode) {
        this.lineBreakMode = lineBreakMode;
    }

    public int getLineBreakMode() {
        return this.lineBreakMode;
    }

    public void setLineHeightMultiple(int lineHeightMultiple) {
        this.lineHeightMultiple = lineHeightMultiple;
    }

    public int getLineHeightMultiple() {
        return this.lineHeightMultiple;
    }

    public void setLineSpacing(int lineSpacing) {
        this.lineSpacing = lineSpacing;
    }

    public int getLineSpacing() {
        return this.lineSpacing;
    }

    public void setParagraphSpacing(int paragraphSpacing) {
        this.paragraphSpacing = paragraphSpacing;
    }

    public int getParagraphSpacing() {
        return this.paragraphSpacing;
    }

    public void setParagraphSpacingBefore(int paragraphSpacingBefore) {
        this.paragraphSpacingBefore = paragraphSpacingBefore;
    }

    public int getParagraphSpacingBefore() {
        return this.paragraphSpacingBefore;
    }

    public void setTailIndent(int tailIndent) {
        this.tailIndent = tailIndent;
    }

    public int getTailIndent() {
        return this.tailIndent;
    }

}
