package org.scoalaonline.api.service;

import org.scoalaonline.api.mail.MailDomain;
import org.scoalaonline.api.mail.MailTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

/**
 * Contains the mail sending related logic needed for the API
 */
@Service
public class MailService {
  private final TemplateEngine templateEngine;

  private final JavaMailSender infoMailSender;

  public MailService(TemplateEngine templateEngine,
                     @Qualifier("getJavaInfoMailSender") JavaMailSender infoMailSender){
    this.templateEngine = templateEngine;
    this.infoMailSender = infoMailSender;
  }

  /**
   * Sends a mail for user validation.
   * @param to the email address of the mail receiver.
   * @param name the name of the mail receiver.
   * @param validationCode the code required for user validation.
   * @throws UnsupportedEncodingException
   * @throws MessagingException
   */
  public void sendValidateAccountEmail(String to, String name, String validationCode) throws UnsupportedEncodingException, MessagingException
  {
    Context context = new Context();
    context.setVariable("name", name);
    context.setVariable("validationCode", validationCode);
    String subject = "Welcome to Scoala Online";
    sendMail(to, MailDomain.INFO, subject, context, MailTemplate.VALIDATE_ACCOUNT);
  }

  /**
   * Sends a mail for password reset.
   * @param to - the email address of the mail receiver.
   * @param name - the name of the mail receiver.
   * @param resetPasswordCode - the code required for password reset.
   * @throws UnsupportedEncodingException
   * @throws MessagingException
   */
  public void sendResetPasswordEmail(String to, String name, String resetPasswordCode) throws UnsupportedEncodingException, MessagingException
  {
    Context context = new Context();
    context.setVariable("name", name);
    context.setVariable("resetPasswordCode", resetPasswordCode);
    String subject = "Reset password";
    sendMail(to, MailDomain.INFO, subject, context, MailTemplate.RESET_PASSWORD);
  }

  /**
   * Sends a mail based on the given parameters.
   * @param to - the email address of the mail receiver.
   * @param from - the mail sender.
   * @param subject - subject of the mail.
   * @param context - a collection of data used inside the content of the mail.
   * @param template - the template for the content of the mail.
   * @throws MessagingException
   * @throws UnsupportedEncodingException
   */
  private void sendMail(String to, MailDomain from, String subject, Context context, MailTemplate template)
    throws MessagingException, UnsupportedEncodingException
  {
    String process = templateEngine.process("emails/" + template.label, context);
    JavaMailSender mailSender = infoMailSender;

    javax.mail.internet.MimeMessage mimeMessage = mailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,
      MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
      StandardCharsets.UTF_8.name());
    helper.setFrom(new InternetAddress(from.label, "ScoalaOnline"));
    helper.setSubject(subject);
    helper.setText(process, true);
    helper.setTo(to);
    mailSender.send(mimeMessage);
  }

}
