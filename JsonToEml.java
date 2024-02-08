<dependency>
    <groupId>javax.mail</groupId>
    <artifactId>mail</artifactId>
    <version>1.4.7</version>
</dependency>


import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Properties;

public class JsonToEml {

    public static void main(String[] args) {
        String jsonFilePath = "path/to/your/file.json"; // Replace with your JSON file path
        String emlFilePath = "path/to/your/output/file.eml"; // Replace with your desired EML file path

        try {
            createEmlWithJsonAttachment(jsonFilePath, emlFilePath);
            System.out.println("EML file created successfully.");
        } catch (MessagingException | IOException e) {
            e.printStackTrace();
        }
    }

    public static void createEmlWithJsonAttachment(String jsonFilePath, String emlFilePath) throws MessagingException, IOException {
        // Set up the mail session
        Properties props = new Properties();
        Session session = Session.getInstance(props, null);

        // Create a new email message
        Message message = new MimeMessage(session);
        message.setSubject("JSON File Attachment");
        message.setFrom(new InternetAddress("no-reply@example.com")); // Set a dummy from address
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse("recipient@example.com")); // Set a dummy recipient

        // Set the email text body
        MimeBodyPart textPart = new MimeBodyPart();
        textPart.setText("Hi PFA the json file", "utf-8");

        // Attach the JSON file
        MimeBodyPart attachmentPart = new MimeBodyPart();
        File jsonFile = new File(jsonFilePath);
        attachmentPart.attachFile(jsonFile);
        attachmentPart.setFileName(jsonFile.getName());

        // Create a multipart message for email body and attachment
        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(textPart);
        multipart.addBodyPart(attachmentPart);

        // Set the complete message parts
        message.setContent(multipart);

        // Write the EML to a file
        try (java.io.OutputStream os = Files.newOutputStream(new File(emlFilePath).toPath())) {
            message.writeTo(os);
        }
    }
}
