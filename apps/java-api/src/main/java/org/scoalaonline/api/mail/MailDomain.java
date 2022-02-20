package org.scoalaonline.api.mail;

public enum MailDomain
{
  INFO("contact.scoalaonline@yahoo.com");

  public final String label;

  MailDomain(String label)
  {
    this.label = label;
  }
}
