package com.Biswajeet.JobBoardApplication.Services;

import com.Biswajeet.JobBoardApplication.DTO.JobPostDTO;
import com.Biswajeet.JobBoardApplication.Model.JobPostSchema;
import com.Biswajeet.JobBoardApplication.Repository.JobPostSchemaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class JobPostSchemaServices {
    @Autowired
    private JobPostSchemaRepository repo;

    @Autowired
    private GeminiIntegrationService geminiIntegrationService;

    public List<JobPostDTO> findAllJobs() {
        List<JobPostSchema> jobs=repo.findAll();
        List<JobPostDTO> jobDtos= new ArrayList<>();
        for(JobPostSchema job:jobs){
            JobPostDTO dto= new JobPostDTO();
            dto.setId(job.getId());
            dto.setTitle(job.getTitle());
            dto.setLocation(job.getLocation());
            dto.setJobType(job.getJobType());
            dto.setCategory(job.getCategory());
            dto.setStatus(job.getStatus());
            dto.setEmployer(job.getEmployer().getName());
            dto.setCompany(job.getCompany().getName());
            jobDtos.add(dto);
        }
        return jobDtos;
    }

    public void updateJob(JobPostSchema jobPost) {
        repo.save(jobPost);
    }

    public JobPostDTO findJobById(Long id) {
        Optional<JobPostSchema> job=repo.findById(id);

        if (job.isPresent()){
           JobPostSchema jobDetails= job.get();

            JobPostDTO dto= new JobPostDTO();
            dto.setId(jobDetails.getId());
            dto.setTitle(jobDetails.getTitle());
            dto.setLocation(jobDetails.getLocation());
            dto.setJobType(jobDetails.getJobType());
            dto.setCategory(jobDetails.getCategory());
            dto.setStatus(jobDetails.getStatus());
            dto.setEmployer(jobDetails.getEmployer().getName());
            dto.setCompany(jobDetails.getCompany().getName());
            return dto;
        }
        else{
            return null;
        }
    }

    public void deleteJobById(Long id) {
        repo.deleteById(id);
    }

    public List<JobPostDTO> findAllJobsByEmployer(Long id) {
        List<JobPostSchema> jobs= repo.findAll();
        List<JobPostDTO> associatedJobs= new ArrayList<>();

        for(JobPostSchema job:jobs){
            if(job.getEmployer().getId().equals(id)){
                JobPostDTO dto= new JobPostDTO();
                dto.setId(job.getId());
                dto.setTitle(job.getTitle());
                dto.setLocation(job.getLocation());
                dto.setJobType(job.getJobType());
                dto.setCategory(job.getCategory());
                dto.setStatus(job.getStatus());
                dto.setEmployer(job.getEmployer().getName());
                dto.setCompany(job.getCompany().getName());

                associatedJobs.add(dto);

            }
        }
        return associatedJobs;

    }

    public List<JobPostDTO> findReleventJobsByTitle(String title) {
        List<JobPostSchema> jobs=repo.findAll();
        List<JobPostDTO> jobByTitle= new ArrayList<>();

        for(JobPostSchema job:jobs){
            if(job.getTitle().contains(title)){

                JobPostDTO dto= new JobPostDTO();
                dto.setId(job.getId());
                dto.setTitle(job.getTitle());
                dto.setLocation(job.getLocation());
                dto.setJobType(job.getJobType());
                dto.setCategory(job.getCategory());
                dto.setStatus(job.getStatus());
                dto.setEmployer(job.getEmployer().getName());
                dto.setCompany(job.getCompany().getName());

                jobByTitle.add(dto);

            }
        }
        return jobByTitle;

    }
    public String[] generateJobDescription(Long jobPostId){
        JobPostDTO jobPostDTO=findJobById(jobPostId);
        String message="I am providing a jobPostDTO object that contains key information about a job listing created by a user. Based on this, generate a professional, detailed, and structured job description suitable for inclusion in a job post.\n" +
                "Do not include any company benefits, application instructions, or promotional language.\n" +
                "Focus on the job title, responsibilities, required skills, qualifications, location, job type (e.g., full-time/internship), and experience level.\n" +
                "Return the response strictly as a JSON object with a single field: \"jobDescription\" containing a properly formatted, human-readable string with paragraphs and bullet points where appropriate.\n" +
                "The output should be directly embeddable and require no further editing.";
        return geminiIntegrationService.getQuery(message,jobPostDTO.toString());
    }
}
