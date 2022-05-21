package org.scoalaonline.api.mail;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
@PropertySource(value = { "classpath:application.properties" })
public class MailConfig {
  @Value("${spring.mail.password}")
  private String mailServerPassword;

  /**
   * Creates a mail sender with the info email address.
   * 
   * @return the mail sender.
   */
  @Bean
  public JavaMailSender getJavaInfoMailSender() {
    return configureJavaMailSender(MailDomain.INFO.label);
  }

  /**
   * Configures the mail sender.
   * 
   * @param username - the username of the email address.
   * @return the mail sender.
   */
  private JavaMailSender configureJavaMailSender(String username) {
    JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
    mailSender.setHost("smtp.mail.yahoo.com");
    mailSender.setPort(465);

    mailSender.setUsername(username);
    mailSender.setPassword(mailServerPassword);

    Properties props = mailSender.getJavaMailProperties();
    props.put("mail.transport.protocol", "smtp");
    props.put("mail.smtp.auth", "true");
    props.put("mail.smtp.starttls.enable", "true");
    props.put("mail.smtp.ssl.enable", "true");
    props.put("mail.debug", "true");

    return mailSender;
  }
}
