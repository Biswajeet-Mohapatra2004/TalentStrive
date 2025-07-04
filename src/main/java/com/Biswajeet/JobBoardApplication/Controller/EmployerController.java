package com.Biswajeet.JobBoardApplication.Controller;
import com.Biswajeet.JobBoardApplication.DTO.ApplicationDTO;
import com.Biswajeet.JobBoardApplication.DTO.AssessmentDTO;
import com.Biswajeet.JobBoardApplication.DTO.JobPostDTO;
import com.Biswajeet.JobBoardApplication.DTO.MailTemplate;
import com.Biswajeet.JobBoardApplication.Model.Employer;
import com.Biswajeet.JobBoardApplication.Model.File;
import com.Biswajeet.JobBoardApplication.Model.JobPostSchema;
import com.Biswajeet.JobBoardApplication.Model.Users;
import com.Biswajeet.JobBoardApplication.Services.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class EmployerController {

    @Autowired
    private EmployerService service;

    @Autowired
    private FileService fileService;

    @Autowired
    private UserService userService;

    @Autowired
    private JobPostSchemaServices jobService;

    @Autowired
    private ApplicationServices applicationServices;

    @Autowired
    private JwtTokenServices jwtTokenServices;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private EmailService emailService;

    @Autowired
    private MailTemplate template;


    //helper function to extract user_ids from the jwt
    private String extractToken(HttpServletRequest request) {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);  // Remove 'Bearer ' prefix
        }
        throw new RuntimeException("JWT Token is missing");
    }

    @PostMapping(path = "/employer/register",consumes = "application/json")
    public ResponseEntity<String> register(@RequestBody Employer employer){
        if(employer.getUsername()==null||employer.getName()==null||employer.getPassword()==null||employer.getCompany()==null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error!!. Employer profile data cannot be empty.");
        }
        service.CreateEmployee(employer);

        try{
            emailService.sendMail(employer.getUsername(),"TalenStrive profile created",template.Register());
        }catch (Exception e){
            System.out.println(e);
        }
        return ResponseEntity.status(HttpStatus.OK).body("Employer registered successfully. Confirmation Email has been sent.");
    }

    @PostMapping(path = "/employer/login" , consumes = "application/json")
    public ResponseEntity<String> Login(@RequestBody Employer employer){
        Authentication authentication=authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(employer.getUsername(),employer.getPassword()));
        if(authentication.isAuthenticated()){
            String token=jwtTokenServices.generateToken(employer.getUsername());
            return ResponseEntity.status(HttpStatus.OK).body(token);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Could not authorise with the credentials provided!!");
    }

    @GetMapping(path = "/employer/profile" , produces = "application/json")
    public ResponseEntity<Employer> employerProfile(HttpServletRequest request){
        String token=extractToken(request);
        String username=jwtTokenServices.extractUsername(token);
        if(username.isEmpty() || service.findEmployerByUserName(username)==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        Employer employer=service.findEmployerByUserName(username);
        return ResponseEntity.status(HttpStatus.OK).body(employer);
    }
    @PutMapping(value = "/employer/update", consumes = "application/json")
    public ResponseEntity<String> updateEmployer(@RequestBody Employer employer, HttpServletRequest request){
        String token=extractToken(request);
        String username=jwtTokenServices.extractUsername(token);
        if(username.isEmpty() || service.findEmployerByUserName(username)==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid JWT Credentials. Employer not found!!");
        }
        service.updateProfile(employer);
        return ResponseEntity.status(HttpStatus.OK).body("Employer profile updated successfully!!");
    }

    @PostMapping(path = "/employer/job/create" , consumes = "application/json")
    public ResponseEntity<String> createJobs(@RequestBody JobPostSchema jobPost, HttpServletRequest request){
        String token=extractToken(request);
        String username=jwtTokenServices.extractUsername(token);
        if(username.isEmpty() || service.findEmployerByUserName(username)==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid JWT Credentials. Employer not found!!");
        }
        try{
            Employer emp=service.findEmployerByUserName(username);
            emailService.sendMail(emp.getUsername(),"Your Job Post Has Been Created - ["+jobPost.getTitle()+"]",template.jobCreateConfirmation(emp.getName(),jobPost));
        }
        catch (Exception e){
            System.out.print(e+"\n Could not send the confirmation mail.");
        }
        service.saveJob(jobPost);
        return ResponseEntity.status(HttpStatus.OK).body("JobPost created successfully!!");
    }
    @PutMapping(path = "/employer/job/update" , consumes = "application/json")
    public ResponseEntity<String> updateJobPost(@RequestBody JobPostSchema jobPost, HttpServletRequest request){
        String token=extractToken(request);
        String username=jwtTokenServices.extractUsername(token);
        if(username.isEmpty() || service.findEmployerByUserName(username)==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid JWT Credentials. Employer not found!!");
        }
        jobService.updateJob(jobPost);
        return ResponseEntity.status(HttpStatus.OK).body("JobPost updated successfully");
    }
    @GetMapping(path = "/employer/jobs", produces = "application/json")
    public ResponseEntity<List<JobPostDTO>> showAllPostedJobs(HttpServletRequest request){
        String token=extractToken(request);
        String username=jwtTokenServices.extractUsername(token);
        if(username.isEmpty() || service.findEmployerByUserName(username)==null){
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        Employer details=service.findEmployerByUserName(username);
        List<JobPostDTO> jobs=jobService.findAllJobsByEmployer(details.getId());
        return ResponseEntity.status(HttpStatus.OK).body(jobs);
    }
    @GetMapping(path = "/employer/view/{id}")
    public ResponseEntity<JobPostDTO> findAJob(@PathVariable Long id, HttpServletRequest request){
        String token=extractToken(request);
        String username=jwtTokenServices.extractUsername(token);
        if(username.isEmpty() || service.findEmployerByUserName(username)==null){
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        JobPostDTO job=jobService.findJobById(id);
        return ResponseEntity.status(HttpStatus.OK).body(job);
    }

    @DeleteMapping(path = "/employer/remove/{id}")
    public ResponseEntity<String> removeJobPost(@PathVariable Long id, HttpServletRequest request){
        String token=extractToken(request);
        String username=jwtTokenServices.extractUsername(token);
        if(username.isEmpty() || service.findEmployerByUserName(username)==null){
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid JWT Credentials. Employer not found!!");
        }
        try{
            JobPostDTO jobPost=jobService.findJobById(id);
            Employer emp=service.findEmployerByUserName(username);
            emailService.sendMail(emp.getUsername(),"Your Job Post For - ["+jobPost.getTitle()+"]",template.jobDeleteConfirmation(emp.getName(),jobPost));
        }
        catch (Exception e){
            System.out.print(e+"\n Could not send the confirmation mail.");
        }

        jobService.deleteJobById(id);
        return ResponseEntity.status(HttpStatus.OK).body("JobPost removed successfully!!");
    }

    @GetMapping(path = "/employer/applications", produces = "application/json")
    public ResponseEntity<List<ApplicationDTO>> showAllApplications(HttpServletRequest request){
        String token=extractToken(request);
        String username=jwtTokenServices.extractUsername(token);
        if(username.isEmpty() || service.findEmployerByUserName(username)==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        Employer details=service.findEmployerByUserName(username);
        List<ApplicationDTO> applications= applicationServices.showApplicationsOfEmployer(details.getId());
        return ResponseEntity.status(HttpStatus.OK).body(applications);
    }

    @GetMapping(path = "/employer/application/{id}", produces = "application/json")
    public ResponseEntity<ApplicationDTO> showApplication(@PathVariable Long id, HttpServletRequest request){
        String token=extractToken(request);
        String username=jwtTokenServices.extractUsername(token);
        if(username.isEmpty() || service.findEmployerByUserName(username)==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        ApplicationDTO application=applicationServices.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(application);
    }
    @PostMapping(path="/employer/job/{id}/assessment" , consumes = "application/json")
    public ResponseEntity<String> sendAssessmentLink(@PathVariable Long id, @RequestBody AssessmentDTO assessmentDTO, HttpServletRequest request){
        String token=extractToken(request);
        String username=jwtTokenServices.extractUsername(token);

        if(username.isEmpty() || service.findEmployerByUserName(username)==null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Un-authorised access!! detected!!");
        }
        try{
            service.sendAssessment(id,assessmentDTO.getUrl());
            return ResponseEntity.status(HttpStatus.OK).body("Assessment link has been shared to candidates.");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Could not send the assessment link!");
        }
    }

    @GetMapping(path="/employer/applicant/{id}/resume",produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> showApplicantResume(@PathVariable Long id,HttpServletRequest request){
        String token=extractToken(request);
        String username=jwtTokenServices.extractUsername(token);

        if(username.isEmpty() || service.findEmployerByUserName(username)==null){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
        try{
            Employer employer=service.findEmployerByUserName(username);
            ApplicationDTO application=applicationServices.findById(id);
            File resume=fileService.getFileById(application.getUserId());
            if(!resume.getUserId().equals(application.getUserId())){
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
            }
            return ResponseEntity.ok()
                    .header("Content-Disposition","attachment; filename=\""+resume.getFileName()+ "\"")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(resume.getData());
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping(path = "/employer/application/{id}/setStatus",consumes = "application/json")
    public ResponseEntity<String> setApplicationStatus(@PathVariable Long id , @RequestBody Map<String,String> request, HttpServletRequest httpServletRequest){
        String token=extractToken(httpServletRequest);
        String username=jwtTokenServices.extractUsername(token);
        if(username.isEmpty() || service.findEmployerByUserName(username)==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid JWT Credentials. Employer not found!!");
        }

        ApplicationDTO applicationDTO=applicationServices.findById(id);
        Long userid=applicationDTO.getUserId();
        Optional<Users> users=userService.findUserById(userid);

        String status=request.get("status");
        applicationServices.updateApplication(id,status);
        try{
            emailService.sendMail(users.get().getUsername(),"Application Status Updated!!",template.sendUpdateToApplicants(applicationDTO));
        }
        catch (Exception e){
            System.out.println("Confirmation mail not sent to applicant");
        }
        return ResponseEntity.status(HttpStatus.OK).body("Application status updated!!");
    }
    @PutMapping(path = "/employer/update/password",consumes = "application/json")
    public ResponseEntity<String> updatePassword(@RequestBody Map<String, String> requestBody, HttpServletRequest request){
        String token=extractToken(request);
        String username=jwtTokenServices.extractUsername(token);
        if(username.isEmpty() || service.findEmployerByUserName(username)==null){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User not found");
        }
        String password=requestBody.get("password");
        Employer details=service.findEmployerByUserName(username);
        service.updatePassword(details.getId(),password);
        try{
            String emailID=details.getUsername();
            String name=details.getName();
            emailService.sendMail(emailID,"Your TalentStrive Password Has Been Updated",template.passwordUpdate(emailID,name));
        }
        catch (Exception e){
            System.out.println(e);
        }
        return ResponseEntity.status(HttpStatus.OK).body("Password Updated!!");
    }
}



