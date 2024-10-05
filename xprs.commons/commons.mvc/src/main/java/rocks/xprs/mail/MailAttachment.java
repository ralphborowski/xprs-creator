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

import java.io.File;
import java.io.Serializable;
import java.net.URL;
import rocks.xprs.web.MimeTypes;

/**
 *
 * @author Ralph Borowski<ralph.borowski@exelonix.com>
 */
public class MailAttachment implements Serializable {

  public enum Type {URL, FILE}

  private String name;
  private URL url;
  private File file;
  private Type type;
  private String contentType;
  private Long size;

  public MailAttachment() {

  }

  public MailAttachment(String name, URL url) {
    this.name = name;
    this.url = url;
    this.type = Type.URL;
  }

  public MailAttachment(String name, URL url, String contentType) {
    this.name = name;
    this.url = url;
    this.type = Type.URL;
    this.contentType = contentType;
  }

  public MailAttachment(String name, File file) {
    this.name = name;
    this.file = file;
    this.size = file.length();
    this.contentType = MimeTypes.guess(file.getName());
    this.type = Type.FILE;
  }

  public MailAttachment(String name, File file, String contentType) {
    this.name = name;
    this.file = file;
    this.size = file.length();
    this.contentType = contentType;
    this.type = Type.FILE;
  }

  //<editor-fold defaultstate="collapsed" desc="Getters and Setters">
  /**
   * @return the name
   */
  public String getName() {
    return name;
  }

  /**
   * @return the url
   */
  public URL getUrl() {
    return url;
  }

  /**
   * @return the file
   */
  public File getFile() {
    return file;
  }

  /**
   * @return the type
   */
  public Type getType() {
    return type;
  }

  /**
   * @return the contentType
   */
  public String getContentType() {
    return contentType;
  }

  /**
   * @return the size
   */
  public Long getSize() {
    return size;
  }
  //</editor-fold>
}
