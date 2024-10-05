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

import jakarta.mail.internet.AddressException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 *
 * @author Ralph Borowski<ralph.borowski@exelonix.com>
 */
public class Mail implements Serializable {

  private static final long serialVersionUID = 1L;

  private String from;
  private List<String> to = new ArrayList<>();
  private List<String> cc = new ArrayList<>();;
  private List<String> bcc = new ArrayList<>();;

  private String subject;

  private String text;
  private String html;

  private List<MailAttachment> attachments = new ArrayList<>();

  public Mail() {

  }

  public void addTo(String address) {
    to.add(address);
  }

  public void addCc(String address) throws AddressException {
    cc.add(address);
  }

  public void addBcc(String address) throws AddressException {
    bcc.add(address);
  }

  public void addAttachment(MailAttachment attachment) {
    attachments.add(attachment);
  }

  //<editor-fold defaultstate="collapsed" desc="Getters and Setters">
  /**
   * @return the from
   */
  public String getFrom() {
    return from;
  }

  /**
   * @param from the from to set
   */
  public void setFrom(String from) {
    this.from = from;
  }

  /**
   * @return the to
   */
  public List<String> getTo() {
    return to;
  }

  /**
   * @param to the to to set
   */
  public void setTo(List<String> to) {
    this.to = to;
  }

  /**
   * @return the cc
   */
  public List<String> getCc() {
    return cc;
  }

  /**
   * @param cc the cc to set
   */
  public void setCc(List<String> cc) {
    this.cc = cc;
  }

  /**
   * @return the bcc
   */
  public List<String> getBcc() {
    return bcc;
  }

  /**
   * @param bcc the bcc to set
   */
  public void setBcc(List<String> bcc) {
    this.bcc = bcc;
  }

  /**
   * @return the subject
   */
  public String getSubject() {
    return subject;
  }

  /**
   * @param subject the subject to set
   */
  public void setSubject(String subject) {
    this.subject = subject;
  }

  /**
   * @return the text
   */
  public String getText() {
    return text;
  }

  /**
   * @param text the text to set
   */
  public void setText(String text) {
    this.text = text;
  }

  /**
   * @return the html
   */
  public String getHtml() {
    return html;
  }

  /**
   * @param html the html to set
   */
  public void setHtml(String html) {
    this.html = html;
  }

  /**
   * @return the attachments
   */
  public List<MailAttachment> getAttachments() {
    return attachments;
  }

  /**
   * @param attachments the attachments to set
   */
  public void setAttachments(List<MailAttachment> attachments) {
    this.attachments = attachments;
  }
  //</editor-fold>
}
