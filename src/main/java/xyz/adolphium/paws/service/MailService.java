package xyz.adolphium.paws.service;

public interface MailService {
    void sendMailTo(String sendToEmail, String subject, String text);
}
