/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rocks.xprs.http;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import rocks.xprs.web.MimeTypes;

/**
 *
 * @author Ralph Borowski<ralph.borowski@exelonix.com>
 */
public class HttpRequestPart {
  
  private String name;
  private String filename;
  private String contentType;
  private InputStream inputStream;
  
  public HttpRequestPart() {
    
  }
  
  public HttpRequestPart(String name, String filename, String contentType, InputStream stream) {
    this.name = name;
    this.filename = filename;
    this.contentType = contentType;
    this.inputStream = stream;
  }
  
  public HttpRequestPart(String name, File file) throws IOException {
    this.name = name;
    this.filename = file.getName();
    this.contentType = MimeTypes.guess(file.getName());
    this.inputStream = new FileInputStream(file);
  }

  //<editor-fold defaultstate="collapsed" desc="Getters and Setters">
  /**
   * @return the name
   */
  public String getName() {
    return name;
  }
  
  /**
   * @param name the name to set
   */
  public void setName(String name) {
    this.name = name;
  }
  
  /**
   * @return the filename
   */
  public String getFilename() {
    return filename;
  }
  
  /**
   * @param filename the filename to set
   */
  public void setFilename(String filename) {
    this.filename = filename;
  }
  
  /**
   * @return the mimeType
   */
  public String getContentType() {
    return contentType;
  }
  
  /**
   * @param contentType the mimeType to set
   */
  public void setContentType(String contentType) {
    this.contentType = contentType;
  }
  
  /**
   * @return the inputStream
   */
  public InputStream getInputStream() {
    return inputStream;
  }
  
  /**
   * @param inputStream the inputStream to set
   */
  public void setInputStream(InputStream inputStream) {
    this.inputStream = inputStream;
  }
  //</editor-fold>
}