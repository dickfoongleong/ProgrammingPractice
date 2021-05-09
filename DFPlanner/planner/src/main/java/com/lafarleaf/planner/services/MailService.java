package com.lafarleaf.planner.services;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.lafarleaf.planner.utils.Exceptions.EmailNotSentException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

@Service
public class MailService {
    private static final String FROM = "no-reply@lafarleaf.com";
    private static final String NAME = "laFar Leaf";

    private final JavaMailSender mailSender;

    @Autowired
    public MailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendMail(String to, String subject, String text, String pathToAttachment) throws EmailNotSentException {
        String errMsg = null;
        try {
            MimeMessage message = mailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom(new InternetAddress(FROM, NAME));
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text);

            if (pathToAttachment != null) {
                FileSystemResource file = new FileSystemResource(
                        ResourceUtils.getFile("classpath:" + pathToAttachment));
                helper.addAttachment("Invoice", file);
            }

            mailSender.send(message);
        } catch (FileNotFoundException fnfe) {
            errMsg = String.format("File Not Found: %s", pathToAttachment);
        } catch (MessagingException me) {
            errMsg = "Failed to create message";
        } catch (UnsupportedEncodingException e) {
            errMsg = "Failed to encode sender Email";
        } finally {
            if (errMsg != null) {
                throw new EmailNotSentException(errMsg);
            }
        }

    }
}
