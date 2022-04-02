package ru.vivt.applicationmvp.ui.repository;

public class Test {
    private int idTest;
    private String header;
    private String description;
    private boolean active;

    public Test(int idTest, String header, String description, boolean active) {
        this.header = header;
        this.description = description;
        this.idTest = idTest;
        this.active = active;
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

    public boolean isActive() {
        return active;
    }
}
