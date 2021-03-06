/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utility;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author elena
 */
public class gmailSendEmailSSL {

    private static final String USERNAME = "ulbs10.recrutari@gmail.com";
    private static final String PASSWORD = "Ulbs10#recrutari";

    /**
     *  Se conecteaza la un cont de gmail, se seteaza parametrii unui email si se trimite
     * catre adresa destinatarului
     *
     * @param mailTo    Adresa de email a destinatarului    
     * @param mailSubject   Subiectul email-ului
     * @param mailText  Continutul email-ului
     * @throws AddressException Daca exista probleme cu adresele setate
     * @throws MessagingException   Daca exista probleme cu mesajele setate
     */
    public void sendMail(String mailTo, String mailSubject,
            String mailText) throws AddressException, MessagingException {
        
        //Seteaza proprietatile serverului de email
        Properties props = System.getProperties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.starttls.enable", "true");// Must issue a STARTTLS command first
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
        
        //Seteaza proprietatile email-ului
        String mailFrom = "ulbs10.recrutari@gmail.com";
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(mailFrom));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(mailTo));
        message.setSubject(mailSubject);
        message.setText(mailText);

        //Trimite email-ul
        Transport.send(message, USERNAME, PASSWORD);
    }
}
