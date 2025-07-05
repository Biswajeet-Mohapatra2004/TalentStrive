package com.Biswajeet.JobBoardApplication.DTO;
import com.Biswajeet.JobBoardApplication.Model.JobPostSchema;
import com.Biswajeet.JobBoardApplication.Model.Users;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
@Service
public class MailTemplate {
    //helper function
    public static String getCurrentDate() {
        // Get current date
        LocalDate currentDate = LocalDate.now();
        // Format the date in a readable format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return currentDate.format(formatter);
    }

    public String Register(){
        return "Welcome to TalentStrive – where we strive to connect the best talent with leading employers. We are excited to have you on board and look forward to supporting you throughout your journey with us.\n" +
                "\n" +
                "At TalentStrive, we are committed to providing a platform that not only helps you achieve your goals but also empowers you with tools and resources to excel.\n" +
                "\n" +
                "For Job Seekers:\n" +
                "Explore Opportunities: Browse through a wide range of job openings tailored to your skills and career aspirations.\n" +
                "Stay Updated: Receive real-time job alerts and recommendations based on your profile.\n" +
                "Easy Applications: Submit applications with just a few clicks and track your progress effortlessly.\n" +
                "For Employers:\n" +
                "Access Top Talent: Discover qualified candidates that align with your company’s vision and needs.\n" +
                "Streamlined Hiring: Manage applications, review profiles, and track hiring status – all in one place.\n" +
                "Efficiency and Support: With TalentStrive, hiring the right talent has never been easier.\n" +
                "If you have any questions or need assistance, our support team is here to help. Feel free to reach out at any time.\n\n"+"Best regards\n"+"TalentStrive Team\n"+"talent.strive.careers@gmail.com";
    }



    public String applicationSubmit(String name,String title, String company) {

        return "Dear"+name+"\n" +
                "\n" +
                "Thank you for applying through TalentStrive! We’re excited to inform you that your application for the position of [Job Title] at [Company Name] has been successfully submitted.\n" +
                "\n" +
                "What Happens Next:\n" +
                "The [Company Name] hiring team will review your application.\n" +
                "You will be notified through email if there are any updates regarding your application status.\n" +
                "In the meantime, feel free to continue exploring more opportunities that match your skills and career goals.\n" +
                "Application Summary:\n" +
                "Position Applied: "+title+"\n"+
                "Company: "+company+"\n" +
                "Date Submitted: [Submission Date]\n" +
                "If you have any questions or need further assistance, our support team is here to help. Feel free to reach out to us anytime at [support@talentstrive.com].\n" +
                "\n" +
                "We wish you the best of luck with your application and look forward to supporting you throughout your job search journey!\n" +
                "\n" +
                "Best regards,\n" +
                "The TalentStrive Team\n"+"talent.strive.careers@gmail.com";
    }

    public String passwordUpdate(String clientID,String name){
        return "Dear " + name + "\n" +
                "\n" +
                "We wanted to inform you that your password for your TalentStrive account has been successfully updated.\n" +
                "\n" +
                "If you initiated this change, no further action is required.\n" +
                "\n" +
                "However, if you did not make this change, please contact our support team immediately at [support@talentstrive.com] to ensure your account remains secure.\n" +
                "\n" +
                "Password Update Summary:\n" +
                "Username:" + clientID + "\n" +
                "Name: " + name + "\n" +
                "Date Updated: " + getCurrentDate() + "\n" +
                "\n" +
                "We recommend using a unique password that you haven’t used across other platforms.\n" +
                "\n" +
                "If you have any questions or need further assistance, feel free to reach out to us anytime at [support@talentstrive.com].\n" +
                "\n" +
                "Best regards,\n" +
                "The TalentStrive Team\n" +
                "talent.strive.careers@gmail.com";

    }

    public String jobCreateConfirmation(String Name, JobPostSchema jobPost) {

        return "Dear " + Name + "\n" +
                "\n" +
                "We’re excited to inform you that your job post for the position of " + jobPost.getTitle() + " has been successfully created and is now visible to potential candidates.\n" +
                "\n" +
                "Job Details:\n" +
                "Position: " + jobPost.getTitle() + "\n" +
                "Company: " + jobPost.getCompany().getName() + "\n" +
                "Type: " + jobPost.getJobType() + "\n" +
                "Category: " + jobPost.getCategory() + "\n" +
                "Location: " + jobPost.getLocation() + "\n" +
                "\n" +
                "If you have any questions or need further assistance, feel free to reach out to us at [support@talentstrive.com].\n" +
                "\n" +
                "Best regards,\n" +
                "The TalentStrive Team\n" +
                "talent.strive.careers@gmail.com";


    }

    public String jobDeleteConfirmation(String name, JobPostDTO jobPost) {
        return "Dear " + name + "\n" +
                "\n" +
                "We wanted to confirm that your job post for the title: " + jobPost.getTitle() + " position has been successfully deleted.\n" +
                "\n" +
                "If you would like to post a new job, please visit: [Link to Post a Job]\n" +
                "\n" +
                "If you have any questions or need further assistance, feel free to reach out to us at [support@talentstrive.com].\n" +
                "\n" +
                "Best regards,\n" +
                "The TalentStrive Team\n" +
                "talent.strive.careers@gmail.com";

    }

    public String sendUpdateToApplicants(ApplicationDTO applicationDTO) {
        return "Dear " + applicationDTO.getApplicantName() + "\n" +
                "\n" +
                "We wanted to update you on your application for the " + applicationDTO.getTitle() + " position "+"\n" +
                "\n" +
                "Your application status has been updated to: " + applicationDTO.getStatus() + ".\n" +
                "\n" +
                "If you have any questions, feel free to contact our support team at [support@talentstrive.com].\n" +
                "\n" +
                "Best regards,\n" +
                "The TalentStrive Team\n" +
                "talent.strive.careers@gmail.com";

    }
    public String assessmentToApplicants(String name,String url){
        return "Dear " +name+ "\n" +
                "\n" +
                "You are required to go through the assessment for further steps"+"\n"+
                "Assessment URL: "+url+" \n"+
                "Best regards,\n" +
                "The TalentStrive Team\n" +
                "talent.strive.careers@gmail.com";
    }
    public String sendAssessmentReport(String title,String CandidateName) {
        return "Dear " + CandidateName + ",\n" +
                "\n" +
                "Congratulations! We are pleased to inform you that you have successfully cleared the first round of the assessment for the " + title + " position.\n" +
                "\n" +
                "At this stage, the company will be reviewing your results further, and they will reach out to you directly regarding the next steps in the hiring process.\n" +
                "\n" +
                "We wish you the best of luck as you progress in the recruitment process. If you have any questions or need further assistance, feel free to contact our support team at [talent.strive.careers@gmail.com].\n" +
                "\n" +
                "Thank you for your interest in this opportunity, and we appreciate the time you’ve invested in the application process.\n" +
                "\n" +
                "Best regards,\n" +
                "The TalentStrive Team\n" +
                "talent.strive.careers@gmail.com";
    }
    public String interviewScheduled(String title, String candidateName, String interviewDate, String interviewUrl) {
        return "Dear " + candidateName + ",\n\n"
                + "Congratulations! Your interview for the " + title + " position has been scheduled on " + interviewDate + ".\n"
                + "Interview Link: " + interviewUrl + "\n\n"
                + "Please join the interview at the scheduled time. If you have any questions, contact us at talent.strive.careers@gmail.com.\n\n"
                + "Best regards,\n"
                + "The TalentStrive Team\n"
                + "talent.strive.careers@gmail.com";
    }
    public String accepted(String title, String candidateName) {
        return "Dear " + candidateName + ",\n\n"
                + "Congratulations! We are delighted to inform you that you have accepted the offer for the " + title + " position.\n"
                + "Welcome to the team! The company HR department will contact you soon with onboarding details.\n\n"
                + "If you have any questions, feel free to reach out to us at talent.strive.careers@gmail.com.\n\n"
                + "Best regards,\n"
                + "The TalentStrive Team\n"
                + "talent.strive.careers@gmail.com";
    }

    public String rejected(String title, String candidateName) {
        return "Dear " + candidateName + ",\n\n"
                + "Thank you for your interest in the " + title + " position.\n"
                + "After careful consideration, we regret to inform you that you have not been selected for this role.\n"
                + "We wish you all the best in your future endeavors.\n\n"
                + "Best regards,\n"
                + "The TalentStrive Team\n"
                + "talent.strive.careers@gmail.com";
    }
}
