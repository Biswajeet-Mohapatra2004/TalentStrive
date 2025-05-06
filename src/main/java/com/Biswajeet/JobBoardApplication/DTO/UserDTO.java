package com.Biswajeet.JobBoardApplication.DTO;

import com.Biswajeet.JobBoardApplication.Model.Application;

import java.util.List;

public class UserDTO {
    private Long id;
    private String username;
    private  String name;
    private String role;
    private int applications;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getApplications() {
        return applications;
    }

    public void setApplications(List<Application> application) {
        this.applications = application.size();
    }
}
