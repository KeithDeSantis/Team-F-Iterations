package edu.wpi.cs3733.D21.teamF.utils;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailHandler {
    public EmailHandler(){

    }

    public int sendEmail(String recipient, String subject, String body){
        String pattern = "^(.+)@(.+)$";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(recipient);

        if (m.find()) {
            String emailTo = recipient;
            String emailFrom = "fuschiafalcons@gmail.com";
            String smtpServer = "smtp.gmail.com";
            Properties properties = System.getProperties();

            properties.put("mail.smtp.host", smtpServer);
            properties.put("mail.smtp.port", "587");
            properties.put("mail.smtp.ssl.enable", "true");
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.starttls.enable", "true");
            properties.put("mail.smtps.socketFactory.port", "587");

            Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(emailFrom, "");
                }
            });

            try {
                MimeMessage message = new MimeMessage(session);
                message.setFrom(new InternetAddress(emailFrom));
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(emailTo));
                message.setSubject(subject);
                message.setText(body);
                Transport.send(message);
            } catch (MessagingException mex) {
                mex.printStackTrace();
            }
        }
        else{
            return -1;
        }

        return 0;
    }

    private static class emailSingletonHelper {
        private static final EmailHandler emailHandler = new EmailHandler();
    }

    public static EmailHandler getEmailHandler() {
        return EmailHandler.emailSingletonHelper.emailHandler;
    }
}
