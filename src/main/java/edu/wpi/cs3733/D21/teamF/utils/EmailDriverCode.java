package edu.wpi.cs3733.D21.teamF.utils;

public class EmailDriverCode {
    public static void main(String[] args) {
        int ret = EmailHandler.getEmailHandler().sendEmail("dpmurphy@wpi.edu", "bepis", "conk");
        if (ret == 0) {
            System.out.println("good");
        } else {
            System.out.println("bad");
        }
    }
}
