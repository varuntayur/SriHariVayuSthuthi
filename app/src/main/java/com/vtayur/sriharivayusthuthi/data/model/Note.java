package com.vtayur.sriharivayusthuthi.data.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.io.Serializable;

/**
 * Created by varuntayur on 5/19/2014.
 */
@Root
public class Note implements Serializable {

    @Element(required = false)
    private String title;

    @Element(required = false)
    private String text;

    public Note() {
    }

    public String getTitle() {
        return title == null ? "" : title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text == null ? "" : text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getFormattedNote() {
        return "<b>".concat(getTitle()).concat("</b>").concat("<p align='justify'>").concat(getText()).concat("</p>");
    }

    @Override
    public String toString() {
        return "Note{" +
                "title='" + title + '\'' +
                ", text='" + text + '\'' +
                '}';
    }
}
