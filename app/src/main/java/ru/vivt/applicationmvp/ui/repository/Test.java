package ru.vivt.applicationmvp.ui.repository;

public class Test {
    private int idTest;
    private String header;
    private String description;

    public Test(int idTest, String header, String description) {
        this.header = header;
        this.description = description;
        this.idTest = idTest;
    }

    public String getDescription() {
        return description;
    }

    public String getHeader() {
        return header;
    }

    public int getIdTest() {
        return idTest;
    }
}
