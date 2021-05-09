package edu.wpi.cs3733.D21.teamF.utils;

import javax.mail.MessagingException;

public class EmailDriverCode {
    public static void main(String[] args) throws MessagingException {
        int ret = EmailHandler.getEmailHandler().sendEmail("dpmurphy@wpi.edu", "bepis", "conk");
        if (ret == 0) {
            System.out.println("good");
        } else {
            System.out.println("bad");
        }
    }
}
