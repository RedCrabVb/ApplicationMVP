package ru.vivt.applicationmvp.ui.repository;

public class Question {
    private String idQuestion;
    private String question;
    private String response;
    private String comment;
    private int idTest;

    public String getIdQuestion() {
        return idQuestion;
    }

    public String getText() {
        return question;
    }

    public String getAnswer() {
        return response;
    }

    public String getComment() {
        return comment;
    }


}
