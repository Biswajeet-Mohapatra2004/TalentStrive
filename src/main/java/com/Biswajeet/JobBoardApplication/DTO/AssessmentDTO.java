package com.Biswajeet.JobBoardApplication.DTO;

public class AssessmentDTO {
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "AssessmentDTO{" +
                "url='" + url + '\'' +
                '}';
    }
}
