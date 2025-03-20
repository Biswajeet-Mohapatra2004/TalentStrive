package com.Biswajeet.JobBoardApplication.Services;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class PdfExtractionService {

    @Autowired
    private GeminiIntegrationService geminiIntegrationService;

    public String[] extractTextFromPdf(MultipartFile report) throws IOException {
        String text="";
        String[] res;
        try{
            PDDocument document=PDDocument.load(report.getInputStream());
            if(!document.isEncrypted()){
                PDFTextStripper pdfTextStripper= new PDFTextStripper();
                text=pdfTextStripper.getText(document);
                String cmd="find the email id's of all the candidates from the assessment report. only provide the email id's of those candidates whose status is pass.\n\n";
                res=geminiIntegrationService.getQuery(text,cmd);
                return res;
            }
            else{
                return new String[]{"File is encrypted. could not strip down the text data!!"};
            }
        }
        catch (Exception e){
            return new String[]{"An error occurred"};
        }
    }

    public String[] extractSkillsFromResume(MultipartFile report) throws IOException {
        String text="";
        String[] res;
        try{
            PDDocument document=PDDocument.load(report.getInputStream());
            if(!document.isEncrypted()){
                PDFTextStripper pdfTextStripper= new PDFTextStripper();
                text=pdfTextStripper.getText(document);
                String cmd="from the text extracted from resume, find all the technical and soft skill present(if any). provide them to me only the skills.\n\n";
                res=geminiIntegrationService.getQuery(text,cmd);
                return res;
            }
            else{
                return new String[]{"File is encrypted. could not strip down the text data!!"};
            }
        }
        catch (Exception e){
            return new String[]{"An error occurred"};
        }
    }
}
