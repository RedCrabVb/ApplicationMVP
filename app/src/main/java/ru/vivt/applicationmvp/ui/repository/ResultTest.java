package ru.vivt.applicationmvp.ui.repository;

public class ResultTest {
    private int idTest;
    private String time;
    private String countRightAnswer;
    private String answer;

    public ResultTest(int idTest, String time, String countRightAnswer, String answer) {
        this.idTest = idTest;
        this.time = time;
        this.countRightAnswer = countRightAnswer;
        this.answer = answer;
    }

    public String getCountRightAnswer() {
        return countRightAnswer;
    }

    public String getAnswer() {
        return answer;
    }

    public String getTime() {
        return time;
    }

    public int getIdTest() {
        return idTest;
    }
}