package com.Biswajeet.JobBoardApplication.DTO;

import com.Biswajeet.JobBoardApplication.Model.Application;
import com.Biswajeet.JobBoardApplication.Model.JobPostSchema;
import com.Biswajeet.JobBoardApplication.Model.Users;

public class ApplicationDTO {
    private Long id;
    private Long userId;
    private String ApplicantName;
    private Long jobPostId;
    private String title;
    private String status;
    private String Company;
    private Long employerId;

    public Long getEmployerId() {
        return employerId;
    }

    public void setEmployerId(Long employerId) {
        this.employerId = employerId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getApplicantName() {
        return ApplicantName;
    }

    public void setApplicantName(String name) {
        this.ApplicantName = name;
    }

    public Long getJobPostId() {
        return jobPostId;
    }

    public void setJobPostId(Long jobPostId) {
        this.jobPostId = jobPostId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCompany() {
        return Company;
    }

    public void setCompany(String company) {
        Company = company;
    }

    @Override
    public String toString() {
        return "ApplicationDTO{" +
                "id=" + id +
                ", userId=" + userId +
                ", ApplicantName='" + ApplicantName + '\'' +
                ", jobPostId=" + jobPostId +
                ", title='" + title + '\'' +
                ", status='" + status + '\'' +
                ", Company='" + Company + '\'' +
                ", employerId=" + employerId +
                '}';
    }
}
