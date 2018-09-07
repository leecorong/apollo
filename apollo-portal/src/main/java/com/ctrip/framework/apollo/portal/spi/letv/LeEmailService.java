package com.ctrip.framework.apollo.portal.spi.letv;

import com.ctrip.framework.apollo.portal.component.config.PortalConfig;
import com.ctrip.framework.apollo.portal.entity.bo.Email;
import com.ctrip.framework.apollo.portal.spi.EmailService;
import com.ctrip.framework.apollo.portal.spi.defaultimpl.DefaultEmailService;
import com.google.common.base.Joiner;
import org.simplejavamail.email.EmailBuilder;
import org.simplejavamail.mailer.MailerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by David.Liu on 2018/9/6.
 */
public class LeEmailService implements EmailService {

  private final Joiner TO_JOINER = Joiner.on(",");

  private final Logger logger = LoggerFactory.getLogger(DefaultEmailService.class);

  @Autowired
  private PortalConfig portalConfig;

  @Override
  public void send(Email email) {
    String smtpServer = portalConfig.getValue("smtp.server");
    int smtpPort = portalConfig.getIntProperty("smtp.port", 25);
    String smtpUser = portalConfig.getValue("smtp.user");
    String smtpPassword = portalConfig.getValue("smtp.password");
    logger.info(
        "Send email args: sender:{}, receiver:{}, smtp[server:{}, port:{}, user:{}, passwd:{}]",
        email.getSenderEmailAddress(), email.getRecipients(), smtpServer, smtpPort, smtpUser,
        smtpPassword);

    org.simplejavamail.email.Email emailx = EmailBuilder.startingBlank()
        .from(email.getSenderEmailAddress())
        .to(TO_JOINER.join(email.getRecipients().toArray()))
        .withSubject(email.getSubject())
        .withHTMLText(email.getBody())
        .buildEmail();

    MailerBuilder.withSMTPServer(smtpServer, smtpPort, smtpUser, smtpPassword)
        .buildMailer()
        .sendMail(emailx);
  }
}
