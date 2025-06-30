package com.Biswajeet.JobBoardApplication.Services;

import com.Biswajeet.JobBoardApplication.DTO.ApplicationDTO;
import com.Biswajeet.JobBoardApplication.DTO.JobPostDTO;
import com.Biswajeet.JobBoardApplication.DTO.MailTemplate;
import com.Biswajeet.JobBoardApplication.Model.Application;
import com.Biswajeet.JobBoardApplication.Model.JobPostSchema;
import com.Biswajeet.JobBoardApplication.Model.Users;
import com.Biswajeet.JobBoardApplication.Repository.ApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ApplicationServices {

    @Autowired
    private MailTemplate mailTemplate;

    @Autowired
    private ApplicationRepository repo;

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserService userService;

    @Autowired
    private JobPostSchemaServices jobPostSchemaServices;

    public List<ApplicationDTO>  showAllApplications() {
         List<ApplicationDTO> applications=new ArrayList<>();


          List<Application> appliedList= repo.findAll();
          for(Application forms:appliedList){
              ApplicationDTO dto= new ApplicationDTO();
              dto.setId(forms.getId());
              dto.setUserId(forms.getUser().getId());
              dto.setJobPostId(forms.getJobPost().getId());
              JobPostSchema post=forms.getJobPost();
              dto.setTitle(post.getTitle());
              dto.setStatus(forms.getStatus());
              dto.setEmployerId(forms.getJobPost().getEmployer().getId());
              dto.setCompany(forms.getJobPost().getCompany().getName());
              // setting the client name
              dto.setApplicantName(forms.getUser().getName());

              applications.add(dto);
          }
          return applications;
    }

    public List<ApplicationDTO> showApplicationsByUser(Long userid){
        List<ApplicationDTO> applied_jobs=new ArrayList<>();
        List<Application> applicationsByUser=repo.findApplicationByUserId(userid);

        for(Application application:applicationsByUser){
            ApplicationDTO applicationDTO;
            applicationDTO = new ApplicationDTO();

            applicationDTO.setId(application.getId());
            applicationDTO.setUserId(application.getUser().getId());
            applicationDTO.setJobPostId(application.getJobPost().getId());

            JobPostSchema post=application.getJobPost();

            applicationDTO.setTitle(post.getTitle());
            applicationDTO.setStatus(application.getStatus());
            applicationDTO.setEmployerId(application.getJobPost().getEmployer().getId());
            applicationDTO.setCompany(application.getJobPost().getCompany().getName());
            // setting the client name
            applicationDTO.setApplicantName(application.getUser().getName());
            applied_jobs.add(applicationDTO);
        }
        return applied_jobs;

    }

    public void DeleteAllApplications(Long id){
        repo.deleteByUserId(id);
    }

    public List<ApplicationDTO> showApplicationsOfEmployer(Long id) {

        List<Application> data=repo.findAll();
        List<ApplicationDTO> applications= new ArrayList<>();

        for(Application forms:data){
            if(forms.getJobPost().getEmployer().getId().equals(id)){
                ApplicationDTO dto = new ApplicationDTO();

                dto.setEmployerId(id);
                dto.setUserId(forms.getUser().getId());
                dto.setApplicantName(forms.getUser().getName());
                dto.setStatus(forms.getStatus());
                dto.setTitle(forms.getJobPost().getTitle());
                dto.setId(forms.getId());
                dto.setJobPostId(forms.getJobPost().getId());
                applications.add(dto);
            }
        }
        return  applications;
    }

    public ApplicationDTO findById(Long id) {
        Optional<Application> form=repo.findById(id);

        if(form.isPresent()){
            Application application=form.get();

            ApplicationDTO dto = new ApplicationDTO();

            dto.setEmployerId(application.getJobPost().getEmployer().getId());
            dto.setUserId(application.getUser().getId());
            dto.setApplicantName(application.getUser().getName());
            dto.setStatus(application.getStatus());
            dto.setTitle(application.getJobPost().getTitle());
            dto.setId(application.getId());
            dto.setJobPostId(application.getJobPost().getId());
            return dto;
        }
        else {
            return null;
            //   throw new EntityNotFoundException("Application with ID " + id + " not found.");
        }
    }

    public void updateApplication(Long id, String status) {
        Optional<Application> form = repo.findById(id);

        if (form.isPresent()) {
            Application application = form.get();
            application.setStatus(status);
            repo.save(application);
        } else {
              System.out.println("Application not found");
//            throw new EntityNotFoundException("Application with ID " + id + " not found.");
        }
    }


    public void findApplicationsByJobPost(Long jobPostId,String url) {
        List<ApplicationDTO> applications=showAllApplications();
        List<Long> userIds=new ArrayList<>();
        String jobName="";
        for(ApplicationDTO application:applications){
            if(application.getJobPostId().equals(jobPostId) && application.getStatus().equals("ACCEPTED")){
                userIds.add(application.getUserId());
                jobName=application.getTitle();
            }
        }
        for(Long id:userIds){
            Optional<Users> user=userService.findUserById(id);
            emailService.sendMail(user.get().getUsername(),"Complete the assessment for "+jobName,mailTemplate.assessmentToApplicants(user.get().getName(),url));
        }

    }
}
