package util;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

public class EmailUtil {

    public static void sendEmail(String toEmail, String subject, String body) throws MessagingException {
        String fromEmail = "your_email@gmail.com";   // your email
        String password = "your_app_password";       // Gmail app password

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        });

        Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(fromEmail));
        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
        msg.setSubject(subject);
        msg.setText(body);

        Transport.send(msg);
    }
}
