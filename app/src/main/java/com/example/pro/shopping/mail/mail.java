package com.example.pro.shopping.mail;

import android.os.AsyncTask;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * Created by Amr on 07/03/2018.
 */
public class mail {
	// ارسال ايميل 
    private Session session = null;
    private String rec, subject, textMessage;

    public void sendMail(String rec, String subject, String textMessage  ) {

	
        this.rec = rec;
        this.subject = subject;
        this.textMessage = textMessage;
		// تعريف البروتوكول تبع الإيميل 
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");
	
	// ادخال الباسورد والإيميل
        session = Session.getDefaultInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("shared.program2019@gmail.com", "program123456");
            }
        });

        RetreiveFeedTask task = new RetreiveFeedTask();
        task.execute();

    }



    class RetreiveFeedTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            try {
				// ارسال رساله الى الإيميل 
                Message message = new MimeMessage(session);
				// source
                message.setFrom(new InternetAddress("shared.program2019@gmail.com"));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(rec));
				// العنوان
                message.setSubject(subject);
                message.setContent(textMessage, "text/html; charset=utf-8");
				// ارسال
                Transport.send(message);
            } catch (MessagingException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

    }

}
