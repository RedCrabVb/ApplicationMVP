package ru.vivt.applicationmvp.ui.repository;

public class ResultTest {
    private int idTest;
    private String time;
    private Integer countWrongAnswer;
    private Integer countAnswer;
    private String answer;

    public ResultTest(int idTest, String time, Integer countWrongAnswer, Integer countAnswer, String answer) {
        this.idTest = idTest;
        this.time = time;
        this.countAnswer = countAnswer;
        this.countWrongAnswer = countWrongAnswer;
        this.answer = answer;
    }

    public Integer getCountAnswer() {
        return countAnswer;
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

    public Integer getCountWrongAnswer() {
        return countWrongAnswer;
    }
}