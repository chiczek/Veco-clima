package pl.vecoclima.other;

import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class SendMail {

    public static void send(String name, String senderEmail, String senderPhoneNumber, String messageContent) {

        // Recipient's email ID needs to be mentioned.
        String to = "adam.kowieski@gmail.com";

        // Sender's email ID needs to be mentioned
        String from = "adam.kowieski@gmail.com";

        // Assuming you are sending email from through gmails smtp
        String host = "smtp.gmail.com";

        // Get system properties
        Properties properties = System.getProperties();

        // Setup mail server
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");

        // Get the Session object.// and pass username and password
        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {

            protected PasswordAuthentication getPasswordAuthentication() {

                return new PasswordAuthentication("adam.kowieski@gmail.com", "dddvydjruzzrsenq");

            }

        });

        // Used to debug SMTP issues
        session.setDebug(true);

        try {
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(from));

            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

            // Set Subject: header field
            message.setSubject("Veco-clima - zgłoszenie przez www - " + senderEmail);

            String htmlText = "<font face=\"calibri\"> "
                    + "Otrzymałeś nową prośbę o kontakt od klienta"
                    + "<br><br>"
                    + "<!DOCTYPE html>\r\n" +
                    "<html>\r\n" +
                    "<head>\r\n" +
                    "<style>\r\n" +
                    "table {\r\n" +
                    "  font-family: arial, sans-serif;\r\n" +
                    "  border-collapse: collapse;\r\n" +
                    "  width: 100%;\r\n" +
                    "}\r\n" +
                    "\r\n" +
                    "td, th {\r\n" +
                    "  border: 1px solid #dddddd;\r\n" +
                    "  text-align: left;\r\n" +
                    "  padding: 8px;\r\n" +
                    "}\r\n" +
                    "\r\n" +
                    "tr:nth-child(even) {\r\n" +
                    "  background-color: #dddddd;\r\n" +
                    "}\r\n" +
                    "</style>\r\n" +
                    "</head>\r\n" +
                    "<body>\r\n" +
                    "\r\n" +

                    "\r\n" +
                    "<table>\r\n" +
                    "  <tr>\r\n" +
                    "    <th>Informacje z formularza</th>\r\n" +
                    "    <th>Value</th>\r\n" +
                    "  </tr>\r\n" +
                    "  <tr>\r\n" +
                    "    <td>Imię i nazwisko</td>\r\n" +
                    "    <td>"+name+"</td>\r\n" +
                    "  </tr>\r\n" +
                    "  <tr>\r\n" +
                    "    <td>Numer telefonu</td>\r\n" +
                    "    <td>"+senderPhoneNumber+"</td>\r\n" +
                    "  </tr>\r\n" +
                    "  <tr>\r\n" +
                    "    <td>Email</td>\r\n" +
                    "    <td>"+senderEmail+"</td>\r\n" +

                    "  </tr>\r\n" +
                    "  <tr>\r\n" +
                    "    <td>Wiadomość od klienta</td>\r\n" +
                    "    <td>"+messageContent+"</td>\r\n" +

                    "  </tr>\r\n" +
                    "</table>\r\n" +
                    "\r\n"


                    +""+
                    "</body>\r\n" +
                    "</html>\r\n" +
                    ""

                    + ""
                    ;

            // Now set the actual message
            message.setContent(htmlText, "text/html");

            System.out.println("sending...");
            // Send message
            Transport.send(message);
            Notification.show("Zgłoszenie zostało pomyślnie wysłane", 2500, Notification.Position.MIDDLE).addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }

    }

}