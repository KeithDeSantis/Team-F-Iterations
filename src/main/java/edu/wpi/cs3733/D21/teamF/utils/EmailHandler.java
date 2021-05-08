package edu.wpi.cs3733.D21.teamF.utils;

import javax.mail.*;

public class EmailHandler {
    public EmailHandler(){

    }



    private static class emailSingletonHelper {
        private static final EmailHandler emailHandler = new EmailHandler();
    }

    public static EmailHandler getEmailHandler() {
        return EmailHandler.emailSingletonHelper.emailHandler;
    }
}
