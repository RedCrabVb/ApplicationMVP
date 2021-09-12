package ru.vivt.applicationmvp.ui.repository;

public class News {
    private String title;
    private String body;
    private String imgPath;


    public News(String title, String body, String imgPath) {
        this.title = title;
        this.body = body;
        this.imgPath = imgPath;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public String getImgPath() {
        return imgPath;
    }
}
