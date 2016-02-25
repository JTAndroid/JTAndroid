package com.utils.note.json;

/**
 * Created by zhangchangwei on 2015/8/18.
 */

import java.util.List;

public class JsonText {
    private List<Runs> runs;

    private String string;

    public void setRuns(List<Runs> runs) {
        this.runs = runs;
    }

    public List<Runs> getRuns() {
        return this.runs;
    }

    public void setString(String string) {
        this.string = string;
    }

    public String getString() {
        return this.string;
    }

}
