package com.Biswajeet.JobBoardApplication.Model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import org.springframework.stereotype.Component;


@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class JobPostSchema {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String location;
    private String jobType;
    private String category;
    private String status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="employerId")
    private Employer employer;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="companyId")
    private Company company;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Employer getEmployer() {
        return employer;
    }

    public void setEmployer(Employer employer) {
        this.employer = employer;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    @Override
    public String toString() {
        return "JobPostSchema{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", location='" + location + '\'' +
                ", jobType='" + jobType + '\'' +
                ", category='" + category + '\'' +
                ", status='" + status + '\'' +
                ", employer=" + employer +
                ", company=" + company +
                '}';
    }
}
