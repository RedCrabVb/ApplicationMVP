package ru.vivt.applicationmvp.ui.repository;

public class Test {
    private String header;
    private String description;

    public Test(String header, String description) {
        this.header = header;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }
}
