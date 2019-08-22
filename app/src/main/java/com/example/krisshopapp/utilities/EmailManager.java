package com.example.krisshopapp.utilities;

import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


/**
 * Created by Han on 3/12/17.
 */

public class EmailManager {

    public int sendMail(String to, String from, String message, String subject, String smtpServ) {
        try {
            Properties props = System.getProperties();
            props.put("mail.transport.protocol", "smtp");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", smtpServ);
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.port", "587");
            //new
            props.put("mail.smtp.debug", "true");
            Session session = Session.getInstance(props, new javax.mail.Authenticator() {
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication("EMAIL", "PASSWORD");
                        }
                    }
            );

            // -- Create a new message --
            Message msg = new MimeMessage(session);
            // -- Set the FROM and TO fields --
            msg.setFrom(new InternetAddress(from));
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to, false));
            msg.setSubject(subject);
            msg.setText(message);
            // -- Set some other header information --
            msg.setHeader("MyMail", "Mr. XYZ");
            msg.setSentDate(new Date());
            // -- Send the message --
            Transport.send(msg);
            System.out.println("Message sent to" + to + " OK.");
            return 0;
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Exception " + ex);
            return -1;
        }
    }

    public String secureEmail(String email) {
        SecurityManager secure = new SecurityManager();
        String hashedEmail = secure.doMD5Hashing(email);

        return hashedEmail;
    }
}
