package com.Biswajeet.JobBoardApplication.DTO;
import java.time.LocalDateTime;

public class InterviewDTO {
    private String url;
    private LocalDateTime date;

    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }

    public LocalDateTime getDate() {
        return date;
    }
    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "InterviewDTO{" +
                "url='" + url + '\'' +
                ", date=" + date +
                '}';
    }
}
