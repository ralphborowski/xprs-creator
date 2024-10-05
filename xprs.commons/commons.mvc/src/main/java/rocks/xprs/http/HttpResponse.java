/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rocks.xprs.http;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.List;
import java.util.Map;
import rocks.xprs.exceptions.AccessDeniedException;
import rocks.xprs.exceptions.InternalErrorException;
import rocks.xprs.exceptions.InvalidDataException;
import rocks.xprs.exceptions.XprsException;
import rocks.xprs.exceptions.ResourceNotFoundException;

/**
 *
 * @author borowski
 */
public class HttpResponse {

  private String url;
  private Map<String, List<String>> headers;
  private String data;
  private InputStream stream;
  private Object object;
  private String contentType;

  public HttpResponse() {

  }

  public HttpResponse(HttpURLConnection conn) throws XprsException, IOException {

    int responseCode = 0;
    url = conn.getURL().toString();

    try {
      responseCode = conn.getResponseCode();
    } catch (IOException ex) {
      throw new InternalErrorException("Error getting response code.", ex);
    }

    if (responseCode != 200) {
      
      if (conn.getContentType() == null) {
        switch (responseCode) {
          case 400:
            throw new InvalidDataException(url + " replied: " + conn.getResponseMessage());
          case 403:
            throw new AccessDeniedException(url + " replied: " + conn.getResponseMessage());
          case 404:
            throw new ResourceNotFoundException(url + " replied: " + conn.getResponseMessage());
          default:
            throw new InternalErrorException(url + " replied: " + conn.getResponseMessage());
        }
      }
    }

    // set headers
    headers = conn.getHeaderFields();

    // parse response data
    if (conn.getContentType() != null) {

      contentType = conn.getContentType().toLowerCase();

      // check for plain text
      if (contentType.startsWith("text/")) {

        // object for reply
        StringBuilder reply = new StringBuilder();

        BufferedReader rd;
        if (conn.getResponseCode() == 200) {
          rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
          rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }
        
        // read text
        try {
          char[] charBuffer = new char[100];
          int charsRead = 0;

          while ((charsRead = rd.read(charBuffer)) > 0) {
            reply.append(charBuffer, 0, charsRead);
          }
        } catch (IOException ex) {
          throw new InternalErrorException("Error reading buffer.", ex);
        } finally {
          try {
            rd.close();
          } catch (IOException ex) {
            throw new InternalErrorException("Error closing buffer.", ex);
          }
        }

        data = reply.toString();

      } else {

        // copy stream to temp file
        //InputStream is = new BufferedInputStream(conn.getInputStream());
        InputStream is = conn.getInputStream();

        File tempFile = File.createTempFile("tmp", "");
        FileOutputStream fos = new FileOutputStream(tempFile);
        byte[] buffer = new byte[4096];
        int bytesRead = 0;
        while ((bytesRead = is.read(buffer)) > 0) {
          fos.write(buffer, 0, bytesRead);
        }

        // tidy up
        is.close();
        fos.close();

        // return stream
        stream = new FileInputStream(tempFile);
        tempFile.delete();
      }
    }

  }

  //<editor-fold defaultstate="collapsed" desc="Getters and Setters">
  /**
   * @return the url
   */
  public String getUrl() {
    return url;
  }

  /**
   * @param url the url to set
   */
  public void setUrl(String url) {
    this.url = url;
  }

  /**
   * @return the headers
   */
  public Map<String, List<String>> getHeaders() {
    return headers;
  }

  /**
   * @param headers the headers to set
   */
  public void setHeaders(Map<String, List<String>> headers) {
    this.headers = headers;
  }

  /**
   * @return the xml
   */
  public String getData() {
    return data;
  }

  /**
   * @param data the xml to set
   */
  public void setData(String data) {
    this.data = data;
  }

  /**
   * @return the stream
   */
  public InputStream getStream() {
    return stream;
  }

  /**
   * @param stream the stream to set
   */
  public void setStream(InputStream stream) {
    this.stream = stream;
  }

  /**
   * @return the contentType
   */
  public String getContentType() {
    return contentType;
  }

  /**
   * @param contentType the contentType to set
   */
  public void setContentType(String contentType) {
    this.contentType = contentType;
  }
  //</editor-fold>
}
