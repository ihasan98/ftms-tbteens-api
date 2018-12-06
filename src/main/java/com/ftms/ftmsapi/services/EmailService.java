package com.ftms.ftmsapi.services;

import com.ftms.ftmsapi.model.Job;
import com.ftms.ftmsapi.payload.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

@Service
public class EmailService {

    private JavaMailSender mailSender;

    @Autowired
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void prepareAndSend(String recipient, String subject, String message) {
        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setFrom("trueblueteens@gmail.com", "Norweld FTMS");
            messageHelper.setTo(recipient);
            messageHelper.setSubject(subject);
            messageHelper.setText(message);
        };
        try {
            mailSender.send(messagePreparator);
        } catch (MailException e) {
            System.out.println("Error in sending email!");
        }
    }

    // Send an email with the recovery code to the person who requested it
    public ResponseEntity<?> sendEmail(String name, String email, String content, String subject) {
        try {
            // properties of the email
            Properties props = new Properties();

            // gmail host
            props.put("mail.smtp.host", "smtp.gmail.com");

            // gmail username
            props.put("mail.smtp.user", "trueblueteens");

            // gmail password
            props.put("mail.smtp.password", "Warehouse");

            // i have no idea what this does
            props.put("mail.debug", "false");

            // port
            props.put("mail.smtp.port", "587");

            // authenticate before sending email
            props.put("mail.smtp.auth", "true");

            // enable starttls
            props.put("mail.smtp.starttls.enable", "true");

            // set current session
            Session session = Session.getInstance(props);

            // initiates MimeMessage with current session
            MimeMessage message = new MimeMessage(session);

            // address of sender
            InternetAddress from = new InternetAddress("trueblueteens", "Norweld FTMS");

            // address of recipient
            InternetAddress to = new InternetAddress(email);

            // set from, to, subject
            message.setFrom(from);
            message.setRecipient(Message.RecipientType.TO, to);
            message.setSubject(subject);

            // set content to the html message
            message.setContent(content, "text/html");

            // prepare transport
            Transport transport = session.getTransport("smtp");

            // login
            transport.connect("smtp.gmail.com", "trueblueteens", "Warehouse");

            // send
            transport.sendMessage(message, message.getAllRecipients());

            // return success!
            return new ResponseEntity<Object>(new ApiResponse(true, "Email sent successfully!"),
                    HttpStatus.OK);
        } catch (UnsupportedEncodingException error) {
            System.out.println("BAD1");
            error.printStackTrace();
            return new ResponseEntity<Object>(new ApiResponse(false, error.toString()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (MessagingException error) {
            System.out.println("BAD2");
            error.printStackTrace();
            return new ResponseEntity<Object>(new ApiResponse(false, error.toString()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }



    }

    // HTML message for when some requests a password recovery code
    public String getRecoveryByEmailContent(String name, String code) {
        return "<html>\n" +
                        "\t<body style=\"font-family: sans-serif;\">\n" +
                        " \t\t<h1 style=\"color: red; font-size: 150%\">\n" +
                        "            Reset your Nor-Weld FTMS account's password\n" +
                        "        <h1>\n" +
                        "        <p style=\"font-size: 60%; color: #4B4848\">\n" +
                        "            Dear " + name + ", <br/> <br/>\n" +
                        "\n" +
                        "            A request has been made to recover the password of the Nor-Weld FTMS account " +
                        "associated with this email. Please enter the following code to proceed: <br/> <br/>\n" +
                        "\n" +
                        "            <code style=\"color: #0099ff\">\n" +
                        "            \t" + code + "\n" +
                        "            </code>\n" +
                        "             <br/> <br/>\n" +
                        "\n" +
                        "            If you encounter any further problems, or were not the one who submitted this request, " +
                        "please contact support!\n" +
                        "            <hr/>\n" +
                        "            <footer style=\"font-size: 50%; color: grey\">\n" +
                        "            \t© Nor-Weld <br/> <br/>\n" +
                        "            \tThis email contains confidential information and is intended only for its rightful " +
                        "recipient. If you believe that this email was not for you, please delete it promptly. " +
                        "<br/> <br/>\n" +
                        "\n" +
                        "            \t<i>Note that this email is automatically sent, any replies to this " +
                        "email will not be processed. If you need assistance, please contact support!<i/>\n" +
                        "            </footer>\n" +
                        "        <p>\n" +
                        "    </body>\n" +
                        "</html>";
    }

    // HTML message for when a job is assigned to someone
    public String getJobAssignmentContent(String name, Job job) {
        String jobName = job.getJobTitle();
        String jobDescription = job.getDescription();
        String jobSite = job.getSiteName();

        return (
                "<html>\n" +
                        "\t<body style=\"font-family: sans-serif;\">\n" +
                        " \t\t<h1 style=\"color: red; font-size: 150%\">\n" +
                        "            New Job!\n" +
                        "        <h1>\n" +
                        "        <p style=\"font-size: 60%; color: #4B4848\">\n" +
                        "            Dear " + name + ", <br/> <br/>\n" +
                        "\n" +
                        "            A new job has been added to your job queue. <br/> <br/>\n" +
                        "\n" +
                        "            Job's Name: " + jobName + "<br/>\n" +
                        "            Jos's Description: " + jobDescription + "<br/> \n" +
                        "            Job's Site: " + jobSite + "<br/>\n" +
                        "\n" +
                        "             <br/>\n" +
                        "            Please visit your account in the Nor-Weld FTMS for more details!\n" +
                        "            <hr/>\n" +
                        "            <footer style=\"font-size: 50%; color: grey\">\n" +
                        "            \t© Nor-Weld <br/> <br/>\n" +
                        "            \tThis email contains confidential information and is intended only for its " +
                        "rightful recipient. If you believe that this email was not for you, please delete it " +
                        "promptly. <br/> <br/>\n" +
                        "\n" +
                        "            \t<i>Note that this email is automatically sent, any replies to this email will " +
                        "not be processed. If you need assistance, please contact support!<i/>\n" +
                        "            </footer>\n" +
                        "        <p>\n" +
                        "    </body>\n" +
                        "</html>\n"
                );
    }

    public String getPasswordChangedContent(String name) {
        return "<html>\n" +
                "\t<body style=\"font-family: sans-serif;\">\n" +
                " \t\t<h1 style=\"color: red; font-size: 150%\">\n" +
                "            Password Changed!\n" +
                "        <h1>\n" +
                "        <p style=\"font-size: 60%; color: #4B4848\">\n" +
                "            Dear " + name + ", <br/> <br/>\n" +
                "\n" +
                "            The password to your Nor-Weld FTMS account has recently been changed. <br/> <br/>\n" +
                "\n" +
                "             <br/>\n" +
                "            If you did not make this change, please contact support immediately!\n" +
                "            <hr/>\n" +
                "            <footer style=\"font-size: 50%; color: grey\">\n" +
                "            \t© Nor-Weld <br/> <br/>\n" +
                "            \tThis email contains confidential information and is intended only for its rightful recipient. If you believe that this email was not for you, please delete it promptly. <br/> <br/>\n" +
                "\n" +
                "            \t<i>Note that this email is automatically sent, any replies to this email will not be processed. If you need assistance, please contact support!<i/>\n" +
                "            </footer>\n" +
                "        <p>\n" +
                "    </body>\n" +
                "</html>\n";
    }

    // HTML message for when a user registration request is made
    public String getUserRegistrationContent(String name, String link) {
        return (
                "<html>\n" +
                        "\t<body style=\"font-family: sans-serif;\">\n" +
                        " \t\t<h1 style=\"color: red; font-size: 150%\">\n" +
                        "            Nor-Weld Account Registration\n" +
                        "        <h1>\n" +
                        "        <p style=\"font-size: 60%; color: #4B4848\">\n" +
                        "            Dear Name, <br/> <br/>\n" +
                        "\n" +
                        "            We are excited to welcome you to the company. Please follow the link below to set up your account: <br/> <br/>\n" +
                        "\n" +
                        "            <a href=" + link + " style=\"color: #0099ff; text-decoration: none\">\n" +
                        "            \tRegister here\n" +
                        "            </a>\n" +
                        "             <br/> <br/>\n" +
                        "\n" +
                        "            Should you encounter any problem with the registration process, please contact support!\n" +
                        "            <hr/>\n" +
                        "            <footer style=\"font-size: 50%; color: grey\">\n" +
                        "            \t© Nor-Weld <br/> <br/>\n" +
                        "            \tThis email contains confidential information and is intended only for its rightful recipient. If you believe that this email was not for you, please delete it promptly. <br/> <br/>\n" +
                        "\n" +
                        "            \t<i>Note that this email is automatically sent, any replies to this email will not be processed. If you need assistance, please contact support!<i/>\n" +
                        "            </footer>\n" +
                        "        <p>\n" +
                        "    </body>\n" +
                        "</html>\n"
                );
    }

    // HTML content for when a company registration request is made
    public String getCompanyRegistrationContent(String companyName, String link) {
        return (
                "<html>\n" +
                        "\t<body style=\"font-family: sans-serif;\">\n" +
                        " \t\t<h1 style=\"color: red; font-size: 150%\">\n" +
                        "            Nor-Weld Account Registration\n" +
                        "        <h1>\n" +
                        "        <p style=\"font-size: 60%; color: #4B4848\">\n" +
                        "            Dear Name, <br/> <br/>\n" +
                        "\n" +
                        "            We are excited you chose Nor-Weld. Please follow the link below to set up your company account: <br/> <br/>\n" +
                        "\n" +
                        "            <a href=" + link + " style=\"color: #0099ff; text-decoration: none\">\n" +
                        "            \tRegister here\n" +
                        "            </a>\n" +
                        "             <br/> <br/>\n" +
                        "\n" +
                        "            Should you encounter any problem with the registration process, please contact support!\n" +
                        "            <hr/>\n" +
                        "            <footer style=\"font-size: 50%; color: grey\">\n" +
                        "            \t© Nor-Weld <br/> <br/>\n" +
                        "            \tThis email contains confidential information and is intended only for its rightful recipient. If you believe that this email was not for you, please delete it promptly. <br/> <br/>\n" +
                        "\n" +
                        "            \t<i>Note that this email is automatically sent, any replies to this email will not be processed. If you need assistance, please contact support!<i/>\n" +
                        "            </footer>\n" +
                        "        <p>\n" +
                        "    </body>\n" +
                        "</html>\n"
                );
    }

}
