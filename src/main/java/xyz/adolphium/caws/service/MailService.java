package xyz.adolphium.caws.service;

public interface MailService {
    void sendMailTo(String sendToEmail, String subject, String text);
}
