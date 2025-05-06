package com.Biswajeet.JobBoardApplication.Controller;
import com.Biswajeet.JobBoardApplication.DTO.ApplicationDTO;
import com.Biswajeet.JobBoardApplication.DTO.JobPostDTO;
import com.Biswajeet.JobBoardApplication.DTO.MailTemplate;
import com.Biswajeet.JobBoardApplication.DTO.UserDTO;
import com.Biswajeet.JobBoardApplication.Model.Application;
import com.Biswajeet.JobBoardApplication.Model.CustomMultipartFile;
import com.Biswajeet.JobBoardApplication.Model.File;
import com.Biswajeet.JobBoardApplication.Model.Users;
import com.Biswajeet.JobBoardApplication.Services.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.print.attribute.standard.Media;


@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService service;

    @Autowired
    private JobPostSchemaServices jobService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenServices jwtTokenServices;

    @Autowired
    private ApplicationServices applicationServices;

    @Autowired
    private EmailService emailService;

    @Autowired
    private MailTemplate template;

    @Autowired
    private FileService fileService;

    @Autowired
    private PdfExtractionService pdfExtractionService;


    //helper function to extract username from the jwt
    private String extractToken(HttpServletRequest request) {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);  // Remove 'Bearer ' prefix
        }
        throw new RuntimeException("JWT Token is missing");
    }


    @PostMapping(path = "/user/register", consumes = "application/json")
    public ResponseEntity<String> register(@RequestBody Users user){
        if(user.getUsername()==null||user.getName()==null||user.getPassword()==null||user.getRole()==null){
           return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error!!. User profile data cannot be empty.");
        }
        service.registerUser(user);

        try{
            emailService.sendMail(user.getUsername(),"TalenStrive profile created",template.Register());
        }catch (Exception e){
            System.out.println(e);
        }
       return ResponseEntity.status(HttpStatus.OK).body("User registered successfully. Confirmation Email has been sent.");
    }

    @PostMapping(path = "/user/login" , consumes = "application/json")
    public ResponseEntity<String> Login(@RequestBody Users user){
       Authentication authentication=authenticationManager
               .authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(),user.getPassword()));
       if(authentication.isAuthenticated()){
           String token=jwtTokenServices.generateToken(user.getUsername());
           return ResponseEntity.status(HttpStatus.OK).body(token);
       }
       return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Could not authorise with the credentials provided!!");
    }
     @GetMapping(path = "/user/profile",produces = "application/json")
     public ResponseEntity<UserDTO> getProfileData(HttpServletRequest request){
         String token= extractToken(request);
         String username=jwtTokenServices.extractUsername(token);
         if(username.isEmpty() || service.findUserByUserName(username)==null){
             return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
         }
         UserDTO userDetails=service.getProfileDto(username);
         return ResponseEntity.status(HttpStatus.ACCEPTED).body(userDetails);
     }


    @PutMapping(path = "/user/update",consumes = "application/json")
    public ResponseEntity<String> updateUser(@RequestBody Users user, HttpServletRequest request){

        String token= extractToken(request);
        String username=jwtTokenServices.extractUsername(token);
        if(username.isEmpty() || service.findUserByUserName(username) == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User with the username not found!!");
        }
        service.updateUser(service.findUserByUserName(username),user);
        return ResponseEntity.ok("User Updated successfully!!");
    }

    @DeleteMapping(path = "/user/delete/")
    public ResponseEntity<String> deleteUser(HttpServletRequest request){
        String token= extractToken(request);
        String username=jwtTokenServices.extractUsername(token);
        if(username.isEmpty() || service.findUserByUserName(username) == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User with the ID not found!!");
        }
        Users user=service.findUserByUserName(username);
        service.deleteUser(user.getId());
        return ResponseEntity.ok("User Deleted!!");
    }
    @DeleteMapping(path = "/user/application/delete",consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> deleteApplication(@RequestBody ApplicationDTO application, HttpServletRequest request){
        String token=extractToken(request);
        String username=jwtTokenServices.extractUsername(token);
        if(username.isEmpty() || service.findByUsername(username)==null){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid JWT Credentials. Login again!!");
        }
        Long userId=service.findByUsername(username).getId();
        service.deleteApplication(userId,application);
        return ResponseEntity.status(HttpStatus.OK).body("The application has been deleted!!");
    }

    @DeleteMapping(path = "/user/application/deleteAll")
    public ResponseEntity<String> DeleteAllApplications(HttpServletRequest request){
        String token= extractToken(request);
        String username=jwtTokenServices.extractUsername(token);
        if(username.isEmpty() || service.findUserByUserName(username) == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User with the ID not found!!");
        }
        Users user=service.findUserByUserName(username);
        applicationServices.DeleteAllApplications(user.getId());
        return ResponseEntity.ok("ALl applications have been deleted");
    }

    @PostMapping(value = "/user/job/apply" , consumes = "application/json")
    public ResponseEntity<String> applyJob(@RequestBody Application application, HttpServletRequest request){

        String token=extractToken(request);
        String username=jwtTokenServices.extractUsername(token);

        if(username.isEmpty() || service.findUserByUserName(username)==null){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User not found");
        }
        try{
            Users user=service.findUserByUserName(username);
            Users applicationForm=new Users();
            applicationForm.setId(user.getId());
            application.setUser(applicationForm);
            service.applyForJob(application,user);
            String emailID=application.getUser().getUsername();
            String clientName=application.getUser().getName();
            String company=application.getJobPost().getCompany().getName();
            String title=application.getJobPost().getTitle();
            emailService.sendMail(emailID,"Application Received",template.applicationSubmit(clientName,title,company));
            return ResponseEntity.ok("Application submitted!!");
        }
        catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to apply for the job. Try again later!!");
        }
    }

    @GetMapping(path = "/user/applications" , produces = "application/json")
    public ResponseEntity<List<ApplicationDTO>> showApplication(HttpServletRequest request){
        String token=extractToken(request);
        String username=jwtTokenServices.extractUsername(token);

        if(username.isEmpty() || service.findUserByUserName(username)==null){
            return  ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
       return ResponseEntity.status(HttpStatus.OK).body(applicationServices.showAllApplications());
    }

    @GetMapping(path = "user/job/{title}",produces = "application/json")
    public List<JobPostDTO> showRelevantJobs(@PathVariable String title){
        return jobService.findReleventJobsByTitle(title);
    }

    @PostMapping(path = "/user/password",consumes = "application/json")
    public ResponseEntity<String> updatePassword(@RequestBody Map<String, String> requestBody, HttpServletRequest request){
        String token=extractToken(request);
        String username=jwtTokenServices.extractUsername(token);
        if(username.isEmpty() || service.findUserByUserName(username)==null){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User not found");
        }
        String password=requestBody.get("password");
        Users details=service.findUserByUserName(username);
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

    @GetMapping(path = "/user/skills",produces="application/json")
    public ResponseEntity<String[]> getUserSkills(HttpServletRequest request) throws IOException {
        String token=extractToken(request);
        String username=jwtTokenServices.extractUsername(token);

        if(username.isEmpty() || service.findUserByUserName(username)==null){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new String[] {"Could not find your account!!"});
        }
        try{
            Users user=service.findUserByUserName(username);
            File resume=fileService.getFileById(user.getId());
            CustomMultipartFile mockResume = new CustomMultipartFile("file",resume.getFileName(),resume.getContentType(),resume.getData());
            String[] skills=pdfExtractionService.extractSkillsFromResume(mockResume);
            return ResponseEntity.status(HttpStatus.OK).body(skills);
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new String[]{e.toString()});
        }
    }


}
