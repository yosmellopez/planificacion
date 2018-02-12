package com.planning.util;

import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

public class MailMail {
    
    private String to;
    
    private MailSender mailSender;
    
    private SimpleMailMessage simpleMailMessage;
    
    public void setSimpleMailMessage(SimpleMailMessage simpleMailMessage) {
        this.simpleMailMessage = simpleMailMessage;
    }
    
    public void setMailSender(MailSender mailSender) {
        this.mailSender = mailSender;
    }
    
    public void sendMail(String content) {
        SimpleMailMessage message = new SimpleMailMessage(simpleMailMessage);
        message.setSubject("Recuperación de su cuenta");
        message.setTo(to);
        message.setText(content);
        mailSender.send(message);
    }
    
    public String getTo() {
        return to;
    }
    
    public void setTo(String to) {
        this.to = to;
    }
}
