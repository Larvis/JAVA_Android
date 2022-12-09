package kz.altenic.task8;

import java.util.ArrayList;

public class Post {
    private String title;
    private String text;
    private String author;
    private String date;
    private String viewsCount;
    private ArrayList<String> related;

    public Post() {
        this.related = new ArrayList<>();
    }

    public Post(String title, String text, String author, String date, String viewsCount) {
        this.title = title;
        this.text = text;
        this.author = author;
        this.date = date;
        this.viewsCount = viewsCount;
        this.related = new ArrayList<>();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getViewsCount() {
        return viewsCount;
    }

    public void setViewsCount(String viewsCount) {
        this.viewsCount = viewsCount;
    }

    public ArrayList<String> getRelated() {
        return related;
    }

    public void setRelated(ArrayList<String> related) {
        this.related = related;
    }
}
