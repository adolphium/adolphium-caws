package xyz.adolphium.paws.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {

    public final JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    private String fromMail;

    @Override
    public void sendMailTo(String sendToEmail,String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromMail);
        message.setTo(sendToEmail);
        message.setSubject(subject);
        message.setText(text);
        javaMailSender.send(message);
    }
}
