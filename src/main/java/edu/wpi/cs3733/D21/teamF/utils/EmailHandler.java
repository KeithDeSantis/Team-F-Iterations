package edu.wpi.cs3733.D21.teamF.utils;

import com.sun.mail.smtp.SMTPTransport;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailHandler {
    public EmailHandler() {

    }

    private Properties setMailServerProperties() {
        Properties props = System.getProperties();
        props.setProperty("mail.smtps.host", "smtp.gmail.com");
        props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.setProperty("mail.smtp.socketFactory.fallback", "false");
        props.setProperty("mail.smtp.port", "465");
        props.setProperty("mail.smtp.socketFactory.port", "465");
        props.setProperty("mail.smtps.auth", "true");
        return props;
    }


    public int sendEmail(String recipient, String subject, String body) throws MessagingException {
        String pattern = "^(.+)@(.+)$";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(recipient);
        String from = "fuschiafalcons@gmail.com";
        String pass = "";

        if (m.find()) {
            Session session = Session.getInstance(setMailServerProperties(), null);
            final MimeMessage msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(from));
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient, false));
            msg.setSubject(subject);
            msg.setText(body);
            SMTPTransport t = (SMTPTransport)session.getTransport("smtps");

            t.connect("smtp.gmail.com", from, pass);
            t.sendMessage(msg, msg.getAllRecipients());
            t.close();
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
