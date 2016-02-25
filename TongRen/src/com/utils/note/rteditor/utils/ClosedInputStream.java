package com.utils.note.rteditor.utils;


import java.io.InputStream;

public class ClosedInputStream extends InputStream {
 public static final ClosedInputStream CLOSED_INPUT_STREAM = new ClosedInputStream();

 public ClosedInputStream() {
 }

 public int read() {
     return -1;
 }
}

