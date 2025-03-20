package com.Biswajeet.JobBoardApplication.Services;

import com.Biswajeet.JobBoardApplication.DTO.EmployerDTO;
import com.Biswajeet.JobBoardApplication.DTO.JobPostDTO;
import com.Biswajeet.JobBoardApplication.DTO.MailTemplate;
import com.Biswajeet.JobBoardApplication.Model.Employer;
import com.Biswajeet.JobBoardApplication.Model.JobPostSchema;
import com.Biswajeet.JobBoardApplication.Model.Users;
import com.Biswajeet.JobBoardApplication.Repository.CompanyRepository;
import com.Biswajeet.JobBoardApplication.Repository.EmployerRepository;
import com.Biswajeet.JobBoardApplication.Repository.JobPostSchemaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployerService {

    @Autowired
    private EmailService emailService;

    @Autowired
    private EmployerRepository repo;

    @Autowired
    private JobPostSchemaRepository jobRepo;

    @Autowired
    private CompanyRepository comRepo;

    @Autowired
    private ApplicationServices applicationServices;

    @Autowired
    private JobPostSchemaServices jobPostSchemaServices;

    @Autowired
    private UserService userService;

    @Autowired
    private MailTemplate mailTemplate;

    private BCryptPasswordEncoder encoder= new BCryptPasswordEncoder(12);
    public void CreateEmployee(Employer emp) {
        emp.setPassword(encoder.encode(emp.getPassword()));
        repo.save(emp);
    }

    public List<Employer> findAll() {
        return repo.findAll();
    }

    public void saveJob(JobPostSchema jobPost) {

       jobRepo.save(jobPost);
    }

    public EmployerDTO findEmployerById(Long id) {
        Optional<Employer> employer=repo.findById(id);

        if(employer.isPresent()){
            Employer emp= employer.get();
            EmployerDTO dto= new EmployerDTO();

            dto.setName(emp.getName());
            dto.setId(emp.getId());
            dto.setUsername(emp.getUsername());
            dto.setPassword(emp.getPassword());
            dto.setCompany(emp.getCompany().getName());

            return dto;
        }
        else{
            return null;
        }

    }
    public void updateProfile(Employer employer) {
        Optional<Employer> emp=repo.findById(employer.getId());

        if(emp.isPresent()){
            Employer existingEmployer = emp.get();
            existingEmployer.setName(employer.getName());
            existingEmployer.setCompany(employer.getCompany());
            existingEmployer.setPassword(encoder.encode(employer.getPassword()));
            existingEmployer.setUsername(employer.getUsername());

            repo.save(existingEmployer);
        }
        System.out.println("No employer found");
    }

    public Employer findEmployerByUserName(String username) {
        List<Employer> list= repo.findAll();
        Employer employerDetails= new Employer();
        for(Employer emp:list){
            if(emp.getUsername().equals(username)){
                employerDetails=emp;
                break;
            }
        }
        return employerDetails;


    }
     public void updatePassword(Long id, String password) {
        Optional<Employer> employer=repo.findById(id);
        if(employer.isPresent()){
            Employer details=employer.get();
            details.setPassword(encoder.encode(password));
            repo.save(details);
        }
    }

    public void sendAssessment(Long jobPostId,String url) {
         applicationServices.findApplicationsByJobPost(jobPostId,url);
    }

    public void sendReportMail(String[] usernames, Long jobId) {
        JobPostDTO job=jobPostSchemaServices.findJobById(jobId);
        String title=job.getTitle();
        for(String username:usernames){
            Users user=userService.findUserByUserName(username);
            String CandidateName=user.getName();
            emailService.sendMail(username,"Update on the application for the post of"+job.getTitle(),mailTemplate.sendAssessmentReport(title,CandidateName));
        }

    }
}
