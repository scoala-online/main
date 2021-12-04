package org.scoalaonline.api.mail;

public enum MailTemplate
{
  VALIDATE_ACCOUNT("validate_account"),
  RESET_PASSWORD("reset_password");

  public final String label;

  MailTemplate(String label)
  {
    this.label = label;
  }
}
