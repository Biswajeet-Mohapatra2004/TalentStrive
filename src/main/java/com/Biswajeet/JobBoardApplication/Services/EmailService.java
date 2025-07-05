package com.Biswajeet.JobBoardApplication.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendMail(String destination,String subject, String body){
        SimpleMailMessage msg= new SimpleMailMessage();

        msg.setTo(destination);
        msg.setSubject(subject);
        msg.setText(body);

        msg.setFrom("talent.strive.careers@gmail.com");
        // send the mail using the mail sender
        mailSender.send(msg);
    }
}
