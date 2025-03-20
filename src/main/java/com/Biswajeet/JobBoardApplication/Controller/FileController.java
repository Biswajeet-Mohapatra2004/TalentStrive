package com.Biswajeet.JobBoardApplication.Controller;

import com.Biswajeet.JobBoardApplication.Model.CustomMultipartFile;
import com.Biswajeet.JobBoardApplication.Model.Employer;
import com.Biswajeet.JobBoardApplication.Model.File;
import com.Biswajeet.JobBoardApplication.Model.Users;
import com.Biswajeet.JobBoardApplication.Services.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class FileController {
    @Autowired
    private JwtTokenServices jwtTokenServices;

    @Autowired
    private FileService fileService;

    @Autowired
    private UserService userService;

    @Autowired
    private EmployerService employerService;

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

    @PostMapping(path="/user/resume/upload",consumes=MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadResume(HttpServletRequest request,@RequestParam("file")MultipartFile file){
        String token=extractToken(request);
        String username=jwtTokenServices.extractUsername(token);

        if(username.isEmpty()|| userService.findUserByUserName(username)==null){
            return  ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Jwt token invalid. login again!!");
        }
       try{
           Users users=userService.findUserByUserName(username);
           fileService.uploadFile(users.getId(),file);
           return ResponseEntity.status(HttpStatus.OK).body("File Uploaded successfully");
       }
       catch (Exception e){
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unable to upload the file."+e);
       }

    }

    @GetMapping(path = "/user/resume/download", produces =MediaType.APPLICATION_PDF_VALUE )
    public ResponseEntity<byte[]> downloadResume(HttpServletRequest request){
        String token=extractToken(request);
        String username=jwtTokenServices.extractUsername(token);

        if(username.isEmpty() || userService.findUserByUserName(username)==null){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
        try{
            Users user=userService.findUserByUserName(username);
            File resume=fileService.getFileById(user.getId());
            if(!resume.getUserId().equals(user.getId())){
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
            }
            return ResponseEntity.ok()
                    .header("Content-Disposition","inline; filename=\""+resume.getFileName()+ "\"")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(resume.getData());
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    @DeleteMapping(path = "/user/resume/delete", produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> deleteResume(HttpServletRequest request){
        String token=extractToken(request);
        String username=jwtTokenServices.extractUsername(token);

        if(username.isEmpty() || userService.findUserByUserName(username)==null){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
        try{
            Users user=userService.findUserByUserName(username);
            fileService.removeResume(user.getId());
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("Resumed removed!!");
        }
        catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Could not remove/delete the resume!! "+e);
        }
    }

    @PostMapping(path = "/employer/job/{id}/report",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadAssessmentReport(@PathVariable Long id, HttpServletRequest request, @RequestParam("report")MultipartFile report){
        String token=extractToken(request);
        String username= jwtTokenServices.extractUsername(token);

        if(username.isEmpty() || employerService.findEmployerByUserName(username)==null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Jwt token invalid!! login again.");
        }
        try{
            Employer emp=employerService.findEmployerByUserName(username);
            fileService.uploadReport(emp.getId(),id,report);
            String[] usernames=pdfExtractionService.extractTextFromPdf(report);
            employerService.sendReportMail(usernames,id);
            return ResponseEntity.status(HttpStatus.OK).body("Assessment report has been uploaded successfully.\nAI will process this request and find out the eligible candidates and mailing will be done.");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Could not upload the assessment report to the server");
        }
    }
    @DeleteMapping(path = "/employer/job/{id}/remove", produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> removeAssessmentReport(@PathVariable Long id,HttpServletRequest request){
        String token=extractToken(request);
        String username=jwtTokenServices.extractUsername(token);

        if(username.isEmpty() || employerService.findEmployerByUserName(username)==null){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
        try{
           Employer emp=employerService.findEmployerByUserName(username);
           fileService.removeAssessmentReport(emp.getId(),id);
           return ResponseEntity.status(HttpStatus.OK).body("Assessment Report has been deleted");
        }
        catch(Exception e){
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Could not remove/delete the Assessment Report!! "+e);
        }
    }



}
