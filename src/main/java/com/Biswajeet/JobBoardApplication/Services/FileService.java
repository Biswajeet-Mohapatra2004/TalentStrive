package com.Biswajeet.JobBoardApplication.Services;

import com.Biswajeet.JobBoardApplication.Model.AssessmentReport;
import com.Biswajeet.JobBoardApplication.Model.File;
import com.Biswajeet.JobBoardApplication.Repository.AssessmentReportRepository;
import com.Biswajeet.JobBoardApplication.Repository.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class FileService {

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private AssessmentReportRepository assessmentReportRepository;

    public void uploadFile(Long id, MultipartFile file) throws IOException {
        System.out.println("UserID:"+id);
        if(!file.getContentType().equals("application/pdf")){
            throw new IllegalArgumentException("Only pdf file allowed!!");
        }
        if(getFileById(id)!=null){
            File existing_resume=getFileById(id);
            existing_resume.setFileName(file.getOriginalFilename());
            existing_resume.setContentType(file.getContentType());
            existing_resume.setData(file.getBytes());
            existing_resume.setUserId(id);
            fileRepository.save(existing_resume);
        }
        else{
            File resume=new File();
            resume.setUserId(id);
            resume.setFileName(file.getOriginalFilename());
            resume.setContentType(file.getContentType());
            resume.setData(file.getBytes());
            fileRepository.save(resume);
        }
    }
    public File getFileById(Long userId){
        List<File> resumes=fileRepository.findAll();
        File resume=new File();
        for(File files:resumes){
            if(files.getUserId().equals(userId)){
                resume.setUserId(files.getUserId());
                resume.setId(files.getId());
                resume.setFileName(files.getFileName());
                resume.setContentType(files.getContentType());
                resume.setData(files.getData());
                break;
            }
        }
        return resume;
    }

    public void removeResume(Long id) {
        File resume=getFileById(id);
        fileRepository.delete(resume);
    }
    public AssessmentReport getReportById(Long empId,Long jobId){
         List<AssessmentReport> reports=assessmentReportRepository.findAll();
         AssessmentReport rep=new AssessmentReport();
         for(AssessmentReport report:reports){
             if(report.getEmpId().equals(empId) && report.getJobId().equals(jobId)){
                 rep.setEmpId(report.getEmpId());
                 rep.setFileName(report.getFileName());
                 rep.setData(report.getData());
                 rep.setJobId(report.getJobId());
                 rep.setContentType(report.getContentType());
                 rep.setId(report.getId());
                 break;
             }
         }
         return rep;

    }
    public void uploadReport(Long empId, Long jobId, MultipartFile report) throws IOException {
        System.out.println("empId:"+empId);
        if(!report.getContentType().equals("application/pdf")){
            throw new IllegalArgumentException("Only pdf file allowed!!");
        }
        if(getReportById(empId,jobId)!=null){
            AssessmentReport existing_report=getReportById(empId,jobId);
            existing_report.setFileName(report.getOriginalFilename());
            existing_report.setContentType(report.getContentType());
            existing_report.setData(report.getBytes());
            existing_report.setEmpId(empId);
            existing_report.setJobId(jobId);
            assessmentReportRepository.save(existing_report);
        }
        else{
            AssessmentReport rep=new AssessmentReport();
            rep.setEmpId(empId);
            rep.setJobId(jobId);
            rep.setFileName(report.getOriginalFilename());
            rep.setContentType(report.getContentType());
            rep.setData(report.getBytes());
            assessmentReportRepository.save(rep);
        }
    }

    public void removeAssessmentReport(Long empId, Long jobId) {
        AssessmentReport report=getReportById(empId,jobId);
        assessmentReportRepository.delete(report);
    }
}
