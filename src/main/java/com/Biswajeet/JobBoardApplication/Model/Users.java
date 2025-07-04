package com.Biswajeet.JobBoardApplication.Model;

import com.Biswajeet.JobBoardApplication.DTO.ApplicationDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password; // Optional if using OAuth2
    private String name;
    private String role; // e.g., JOB_SEEKER, ADMIN

    @OneToMany(mappedBy = "user")
    private List<Application> applications = new ArrayList<>();



    public Users() {
    }

    public Users(String username, String password, String name, String role) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.role = role;
    }

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

   public List<Application> getApplications(){
        return applications;
   }
   public void setApplications(Application application){
        applications.add(application);
   }
   public void removeApplication(Application application){
        applications.remove(application);
   }

    @Override
    public String toString() {
        return "Users{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", role='" + role + '\'' +
                ", applications=" + applications +
                '}';
    }
}
