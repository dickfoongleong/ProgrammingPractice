package com.df.email_sender;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class Sender
{
  public static final String SENDER = System.getenv("WORK_GMAIL");
  public static final String PASSWORD = System.getenv("WORK_GMAIL_PASSWORD");
  public static final String HOST = "smtp.gmail.com";
  public static final String PORT = "465";
  public static final Properties PROP = System.getProperties();

  private Session session = null;
  public Sender() {
      PROP.put("mail.smtp.host", HOST);
      PROP.put("mail.smtp.port", PORT);
      PROP.put("mail.smtp.ssl.enable", "true");
      PROP.put("mail.smtp.auth", "true");

      Authenticator authenticator = new Authenticator() {
        @Override
        protected PasswordAuthentication getPasswordAuthentication() {
          return new PasswordAuthentication(SENDER, PASSWORD);
        }
      };
      session = Session.getInstance(PROP, authenticator);
  }

  public void send(String[] toEmails, String[] ccEmails, String[] bccEmails) {
    try {
      MimeMessage message = new MimeMessage(session);
      message.setFrom(new InternetAddress(SENDER));

      // Set TO
      for (String email : toEmails) {
        message.addRecipient(RecipientType.TO, new InternetAddress(email));
      }

      // Set CC
      for (String email : ccEmails) {
        message.addRecipient(RecipientType.CC, new InternetAddress(email));
      }

      // Set BCC
      for (String email : bccEmails) {
        message.addRecipient(RecipientType.BCC, new InternetAddress(email));
      }

      // Set Subject
      message.setSubject("DF Greeting Email");

      // Set body texts
      message.setText("Hello from the USA by Dick Foong Leong! :D");

      // Send message
      Transport.send(message);
    } catch (MessagingException me) {
      me.printStackTrace();
    } catch (NullPointerException npe) {
      npe.printStackTrace();
    }
  }

  public static void main( String[] args )
  {
    List<String> toEmails = new ArrayList<String>();
    List<String> ccEmails = new ArrayList<String>();
    List<String> bccEmails = new ArrayList<String>();

    int type = 0;
    for (String arg : args) {
      if (arg.equals("-t")) {
        type = 0;
      } else if (arg.equals("-c")) {
        type = 1;
      } else if (arg.equals("-b")) {
        type = 2;
      } else {
        switch (type) {
          case 1:
            ccEmails.add(arg);
            break;
          case 2:
            bccEmails.add(arg);
            break;
          default:
            toEmails.add(arg);
        }
      }
    }

    new Sender().send(
      toEmails.toArray(new String[0]),
      ccEmails.toArray(new String[0]),
      bccEmails.toArray(new String[0])
    );
  }
}
