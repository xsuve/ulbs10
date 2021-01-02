/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utility;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;

/**
 *
 * @author elena
 */
public class gmailSendEmailSSL {

    private static final String USERNAME = "ulbs10.recrutari@gmail.com";
    private static final String PASSWORD = "Ulbs10#recrutari";

    public static void main(String[] args) throws Exception {
        // Email information such as from, to, subject and contents.
        String mailFrom = "ulbs10.recrutari@gmail.com";
        String mailTo = "elena.raicu@ulbsibiu.ro";
        String mailSubject = "ULBS10";
        String mailText = "O facuram si pe asta";

        gmailSendEmailSSL gmail = new gmailSendEmailSSL();
    }

    public void sendMail(String mailFrom, String mailTo, String mailSubject,
            String mailText) throws Exception {
        Properties props = System.getProperties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");
        props.put("mail.smtp.socketFactory.port", "587");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        //props.put("mail.smtp.auth", "true");
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(USERNAME, PASSWORD);
            }
        });
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(mailFrom));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(mailTo));
        message.setSubject(mailSubject);
        message.setText(mailText);

        Transport.send(message, USERNAME, PASSWORD);
    }
}
