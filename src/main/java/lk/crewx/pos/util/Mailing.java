//package lk.crewx.pos.util;
//
//import javax.mail.*;
//import javax.mail.internet.InternetAddress;
//import javax.mail.internet.MimeMessage;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.text.SimpleDateFormat;
//import java.time.LocalDateTime;
//import java.util.Calendar;
//import java.util.Properties;
//
//public class Mailing {
//    private static Thread t1;
//
//    public static void startThread() {
//        Calendar cal = Calendar.getInstance();
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//        String today = sdf.format(cal.getTime());
//
//        try {
//            ResultSet rs = CrudUtil.execute("SELECT * FROM orders WHERE DATE(date) = ?", today);
//            int totalOrders = 0;
//
//            while (rs.next()) {
//                totalOrders++;
//            }
//
//            ResultSet response = CrudUtil.execute("SELECT balance FROM money WHERE id=0");
//            double balance = 0;
//            if (response.next()) {
//                balance = response.getDouble(1);
//            }
//            ResultSet res = CrudUtil.execute("SELECT email FROM users WHERE role=?", "admin");
//            if (res.next()) {
//                String email = res.getString("email");
//                String subject = "PD Traders Daily Report";
//                String message = "Howdy Admin! \n Here is how your shop done today! :) \n \n  \n Total Orders : " + totalOrders + "" +
//                        " \n Cashier Balance : " + balance + "" +
//                        "\n \n Today is " + LocalDateTime.now() + "\n \n \n Thanks for using CrewX Systems";
//
//                t1 = new Thread(() -> sendEmail(email, subject, message));
//                t1.start();
//            }
//
//        } catch (SQLException | ClassNotFoundException e) {
//            e.printStackTrace();
//        }
//
//
//    }
//
//    public static void stopThread() {
//        try {
//            t1.stop();
//        } catch (NullPointerException e) {
//            System.out.println("Sending error");
//        }
//    }
//
//
//    public static void sendEmail(String toEmail, String subject, String msg) {
//
//        Properties properties = new Properties();
//        properties.put("mail.smtp.auth", true);
//        properties.put("mail.smtp.starttls.enable", true);
//        properties.put("mail.smtp.port", "587");
//        properties.put("mail.smtp.host", "smtp.gmail.com");
//
//        String username = "crewxemailing@gmail.com";
//        String password = "rgssenuaweqdvigj";
//
//
//        //session
//        Session session = Session.getInstance(properties, new Authenticator() {
//            @Override
//            protected PasswordAuthentication getPasswordAuthentication() {
//                return new PasswordAuthentication(username, password);
//            }
//        });
//
//        try {
//
//            Message message = new MimeMessage(session);
//            message.setRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
//            message.setFrom(new InternetAddress("crewxemailing@gmail.com"));
//            message.setSubject(subject);
//            message.setText(msg);
//            Transport.send(message);
//        } catch (Exception e) {
//            System.out.println(e);
//            e.printStackTrace();
//        } finally {
//            System.out.println("sent");
//            stopThread();
//        }
//
//    }
//}

package lk.crewx.pos.util;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Properties;

public class Mailing {
    private static Thread t1;

    public static void startThread() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String today = sdf.format(cal.getTime());

        try {
            ResultSet rs = CrudUtil.execute("SELECT * FROM orders WHERE DATE(date) = ?", today);
            int totalOrders = 0;

            while (rs.next()) {
                totalOrders++;
            }

            ResultSet response = CrudUtil.execute("SELECT balance FROM money WHERE id=0");
            double balance = 0;
            if (response.next()) {
                balance = response.getDouble(1);
            }
            ResultSet res = CrudUtil.execute("SELECT email FROM users WHERE role=?", "admin");
            if (res.next()) {
                String email = res.getString("email");
                String subject = "PD Traders Daily Report";
                String message = "Howdy Admin! \n Here is how your shop done today! :) \n \n  \n Total Orders : " + totalOrders + "" +
                        " \n Cashier Balance : " + balance + "" +
                        "\n \n Today is " + LocalDateTime.now() + "\n \n \n Thanks for using CrewX Systems";

                t1 = new Thread(() -> sendEmail(email, subject, message));
                t1.start();
            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }


    }

    public static void stopThread() {
        try {
            t1.stop();
        } catch (NullPointerException e) {
            System.out.println("Sending error");
        }
    }


    public static void sendEmail(String toEmail, String subject, String msg) {

        System.out.println("sending mail");
        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.starttls.required", "true");
        properties.put("mail.smtp.ssl.protocols", "TLSv1.2");
        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

        String username = "crewxemailing@gmail.com";
        String password = "rgssenuaweqdvigj";


        //session
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {

            Message message = new MimeMessage(session);
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
            message.setFrom(new InternetAddress("crewxemailing@gmail.com"));
            message.setSubject(subject);
            message.setText(msg);
            Transport.send(message);
        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
        } finally {
            System.out.println("sent");
            stopThread();
        }

        System.out.println("sent mail");

    }
}