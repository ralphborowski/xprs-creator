/*
 * The MIT License
 *
 * Copyright 2017 BOROWSKI IT GmbH.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package rocks.xprs.mail;

import jakarta.activation.DataHandler;
import jakarta.activation.FileDataSource;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;
import jakarta.mail.Part;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import java.io.IOException;

/**
 *
 * @author Ralph Borowski<ralph.borowski@exelonix.com>
 */
public class MailClient {

  private final Session mailSession;

  public MailClient(Session mailSession) {
    this.mailSession = mailSession;
  }

  public Mail compose() {
    return new Mail();
  }

  public Message compileMimeMessage(Mail mail) throws MessagingException, IOException {

    // create new mime message
    MimeMessage message = new MimeMessage(mailSession);

    // add recipients
    message.setSender(new InternetAddress(mail.getFrom()));
    message.setFrom(mail.getFrom());

    if (!mail.getTo().isEmpty()) {
      for (String a : mail.getTo()) {
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(a));
      }
    }

    if (!mail.getCc().isEmpty()) {
      for (String a : mail.getCc()) {
        message.addRecipient(Message.RecipientType.CC, new InternetAddress(a));
      }
    }

    if (!mail.getBcc().isEmpty()) {
      for (String a : mail.getBcc()) {
        message.addRecipient(Message.RecipientType.BCC, new InternetAddress(a));
      }
    }

    // set subject
    message.setSubject(mail.getSubject(), "UTF-8");

    // build message part
    // multipart message with text, html and attachments
    if (mail.getText() != null && mail.getHtml() != null) {

      // build message part
      Multipart messagePart = new MimeMultipart("alternative");

      MimeBodyPart textPart = new MimeBodyPart();
      textPart.setText(mail.getText(), "utf-8");

      MimeBodyPart htmlPart = new MimeBodyPart();
      htmlPart.setContent(mail.getHtml(), "text/html; charset=utf-8");

      messagePart.addBodyPart(textPart);
      messagePart.addBodyPart(htmlPart);

      // add attachments if any
      if (!mail.getAttachments().isEmpty()) {

        // wrap up into a new multipart
        Multipart messageWrapper = new MimeMultipart("mixed");

        // add message part
        MimeBodyPart msgPart = new MimeBodyPart();
        msgPart.setContent(messagePart);
        messageWrapper.addBodyPart(msgPart);

        for (MailAttachment a : mail.getAttachments()) {

          // add to attachment part
          MimeBodyPart attPart = new MimeBodyPart();
          attPart.setDataHandler(getDataHandler(a));
          attPart.setFileName(a.getName());
          attPart.setDisposition(Part.ATTACHMENT);
          messageWrapper.addBodyPart(attPart);
        }

        messagePart = messageWrapper;
      }

      // add to message
      message.setContent(messagePart);

    } else {

      // add attachments if any
      if (!mail.getAttachments().isEmpty()) {

        // build message part
        Multipart messageWrapper = new MimeMultipart("mixed");

        // add message
        MimeBodyPart messagePart = new MimeBodyPart();
        if (mail.getText() != null) {
          messagePart.setText(mail.getText(), "utf-8");
        } else {
          messagePart.setText(mail.getHtml(), "utf-8", "html");
        }

        // add to wrapper
        messageWrapper.addBodyPart(messagePart);

        // add attachments
        for (MailAttachment a : mail.getAttachments()) {

          // add to attachment part
          MimeBodyPart attPart = new MimeBodyPart();
          if (a.getType() == MailAttachment.Type.FILE) {
            attPart.attachFile(a.getFile(), a.getContentType(), "base64");
          } else {
            attPart.setDataHandler(getDataHandler(a));
            attPart.setFileName(a.getName());
            attPart.setDisposition(Part.ATTACHMENT);
          }

          messageWrapper.addBodyPart(attPart);
        }

        message.setContent(messageWrapper);
      } else {

        // only add a text part
        if (mail.getText() != null) {
          message.setText(mail.getText(), "utf-8");
        } else {
          message.setText(mail.getHtml(), "utf-8", "html");
        }
      }
    }

    return message;
  }


  public Message send(Mail mail) throws MessagingException, IOException {
    return send(compileMimeMessage(mail));
  }

  public Message send(Message message) throws MessagingException {
    String protocol = mailSession.getProperty("mail.transport.protocol");

    try(Transport transport = mailSession.getTransport(protocol)) {

      if (mailSession.getProperty("mail." + protocol + ".username") != null) {
        transport.connect(mailSession.getProperty("mail." + protocol + ".username"),
                mailSession.getProperty("mail." + protocol + ".password"));
      } else {
        transport.connect();
      }

      transport.sendMessage(message, message.getAllRecipients());
    }

    return message;
  }

  private DataHandler getDataHandler(MailAttachment attachment) {
    switch (attachment.getType()) {
      case URL:
        return new DataHandler(attachment.getUrl());
      case FILE:
        return new DataHandler(new FileDataSource(attachment.getFile()));
      default:
        return new DataHandler(attachment.getUrl());
    }
  }
}
