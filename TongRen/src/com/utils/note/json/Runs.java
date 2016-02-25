package com.utils.note.json;

/**
 * Created by zhangchangwei on 2015/8/18.
 */

import java.util.List;

public class Runs {
    private Attributes attributes;

    private int[] range;

    public void setAttributes(Attributes attributes) {
        this.attributes = attributes;
    }

    public Attributes getAttributes() {
        return this.attributes;
    }

    public void setRange(int[] range) {
        this.range = range;
    }

    public int[] getRange() {
        return this.range;
    }

}
