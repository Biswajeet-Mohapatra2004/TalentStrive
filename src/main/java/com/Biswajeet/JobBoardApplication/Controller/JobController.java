package com.Biswajeet.JobBoardApplication.Controller;

import com.Biswajeet.JobBoardApplication.DTO.JobPostDTO;
import com.Biswajeet.JobBoardApplication.Model.JobPostSchema;
import com.Biswajeet.JobBoardApplication.Repository.JobPostSchemaRepository;
import com.Biswajeet.JobBoardApplication.Services.JobPostSchemaServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class JobController {



    @Autowired
    private JobPostSchemaServices services;

    @GetMapping(path = "/jobs/viewAll" , produces = "application/json")
    public List<JobPostDTO> getAllJobs(){
        return services.findAllJobs();
    }
}
