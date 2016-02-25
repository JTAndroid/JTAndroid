package com.utils.note.rteditor.utils;

//
//Source code recreated from a .class file by IntelliJ IDEA
//(powered by Fernflower decompiler)
//


import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class LineIterator implements Iterator<String> {
 private final BufferedReader bufferedReader;
 private String cachedLine;
 private boolean finished = false;

 public LineIterator(Reader reader) throws IllegalArgumentException {
     if(reader == null) {
         throw new IllegalArgumentException("Reader must not be null");
     } else {
         if(reader instanceof BufferedReader) {
             this.bufferedReader = (BufferedReader)reader;
         } else {
             this.bufferedReader = new BufferedReader(reader);
         }

     }
 }

 public boolean hasNext() {
     if(this.cachedLine != null) {
         return true;
     } else if(this.finished) {
         return false;
     } else {
         try {
             String ioe;
             do {
                 ioe = this.bufferedReader.readLine();
                 if(ioe == null) {
                     this.finished = true;
                     return false;
                 }
             } while(!this.isValidLine(ioe));

             this.cachedLine = ioe;
             return true;
         } catch (IOException var2) {
             this.close();
             throw new IllegalStateException(var2);
         }
     }
 }

 protected boolean isValidLine(String line) {
     return true;
 }

 public String next() {
     return this.nextLine();
 }

 public String nextLine() {
     if(!this.hasNext()) {
         throw new NoSuchElementException("No more lines");
     } else {
         String currentLine = this.cachedLine;
         this.cachedLine = null;
         return currentLine;
     }
 }

 public void close() {
     this.finished = true;
     RTIOUtils.closeQuietly(this.bufferedReader);
     this.cachedLine = null;
 }

 public void remove() {
     throw new UnsupportedOperationException("Remove unsupported on LineIterator");
 }

 public static void closeQuietly(LineIterator iterator) {
     if(iterator != null) {
         iterator.close();
     }

 }
}

