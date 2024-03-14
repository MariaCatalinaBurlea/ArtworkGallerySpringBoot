package artgallery.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;

//@Service
//public class EmailUtils {
//    @Autowired
//    private JavaMailSender emailSender;
//
//    public void sendSimpleMessage(String to, String subject, String text, List<String> ccList){
//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setFrom("burleamariacatalina@gmail.com");
//        message.setTo(to);
//        message.setSubject(subject);
//        message.setText(text);
//
//        String[] ccArray = ccList.toArray(new String[0]);
//        message.setCc(ccArray);
//
//        emailSender.send(message);
//    }
//
//
//}
