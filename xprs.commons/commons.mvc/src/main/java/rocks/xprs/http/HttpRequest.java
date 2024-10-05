/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rocks.xprs.http;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import rocks.xprs.exceptions.InternalErrorException;
import rocks.xprs.exceptions.XprsException;
import rocks.xprs.exceptions.ResourceNotFoundException;

/**
 *
 * @author borowski
 */
public class HttpRequest {

  public enum Method {

    GET, POST, PUT, DELETE
  }

  private String address;
  private final Map<String, String> headers;
  private Method method;
  private final Map<String, String[]> parameters;
  private final List<HttpRequestPart> attachments;

  public HttpRequest(String address, Method method) {
    this.address = address;
    this.method = method;
    headers = new LinkedHashMap<String, String>();
    parameters = new LinkedHashMap<String, String[]>();
    attachments = new ArrayList<HttpRequestPart>();
  }

  public HttpResponse doRequest() throws XprsException {

    address = address.trim();

    HttpURLConnection conn = null;
    HttpResponse response = new HttpResponse();
    response.setUrl(address);

    try {

      // if get request, add params to url
      URL url;

      // if streams attached, force POST
      if (!attachments.isEmpty()) {
        method = Method.POST;
      }

      try {

        // build query string for GET request
        if (method.equals(Method.GET)) {

          if (!parameters.isEmpty()) {
            // check for existing params
            if (address.contains("?")) {
              address += "&";
            } else {
              address += "?";
            }
            address = address + encodeData(parameters);
          }

          url = new URL(address);

        } else {

          // only use the address and put content into body
          url = new URL(address);
        }
      } catch (IOException ex) {
        throw new InternalErrorException("Error creating URL.", ex);
      }

      try {

        // connect to url
        conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod(method.name());
      } catch (IOException ex) {
        throw new InternalErrorException("Error opening url connection.", ex);
      }

      // prepare boundary
      String boundary = "---------------------------03092011";
      byte[] boundaryBytes = ("--" + boundary + "\r\n").getBytes("utf-8");

      // if files attached, add headers
      if (!attachments.isEmpty()) {
        headers.put("Content-Type", "multipart/form-data; boundary=" + boundary);
        headers.put("User-Agent", "vx.elements.HttpRequest");
      }

      // add headers
      for (Iterator<Entry<String, String>> i = headers.entrySet().iterator(); i.hasNext();) {
        Entry<String, String> item = i.next();
        conn.setRequestProperty(item.getKey(), item.getValue());
      }

      // post data
      if (method.equals(Method.POST)) {
        conn.setDoOutput(true);
        OutputStream out = conn.getOutputStream();

        if (attachments.isEmpty()) {
          OutputStreamWriter wr = new OutputStreamWriter(out);
          try {
            // no attachments, just write parameters to request

            wr.write(encodeData(parameters));
          } catch (IOException ex) {
            throw new InternalErrorException("Error writing post stream.", ex);
          } finally {
            try {
              wr.close();
            } catch (IOException ex) {
              throw new InternalErrorException("Error closing post stream.", ex);
            }
          }
        } else {

          // use multipart form data
          // output each parameter as part
          for (Entry<String, String[]> entry : parameters.entrySet()) {
            if (entry.getKey() != null && !entry.getKey().trim().isEmpty()) {
              for (String v : entry.getValue()) {
                // write header
                out.write(boundaryBytes);
                out.write(("Content-Disposition: form-data; name=\"" + entry.getKey() + "\"\r\n").getBytes());
                out.write(("Content-Type: text/plain; charset=utf-8\r\n\r\n").getBytes());
                out.write(v.getBytes("utf-8"));
                out.write(("\r\n").getBytes());
              }
            }
          }

          // output attachments
          for (HttpRequestPart p : attachments) {

            // write header
            out.write(boundaryBytes);
            out.write(("Content-Disposition: form-data; name=\"" + p.getName() + "\"\r\n").getBytes());
            out.write(("Content-Type: " + p.getContentType() + "\r\n\r\n").getBytes());

            // write data
            InputStream in = p.getInputStream();
            byte[] buffer = new byte[4096];
            int bytesRead = 0;
            while ((bytesRead = in.read(buffer)) > 0) {
              out.write(buffer, 0, bytesRead);
            }
            out.write(("\r\n").getBytes());

            // tidy up
            in.close();
          }

          // write final boundary
          out.write(("--" + boundary + "--").getBytes("utf-8"));

          // close output stream
          out.flush();
          out.close();
        }
      }
      // return the response
      return new HttpResponse(conn);
      
    } catch (FileNotFoundException ex) {
      throw new ResourceNotFoundException(ex.getMessage());
    } catch (IOException ex) {
      throw new InternalErrorException("IO Exception occured while fetching " + address, ex);
    } finally {
      // tidy up

      if (conn != null) {
        conn.disconnect();
      }
    }
  }

  public void addHeader(String name, String value) {
    headers.put(name, value);
  }

  public void addParameter(String name, String value) {
    addParameter(name, new String[]{value});
  }

  public void addParameter(String name, String value[]) {

    // ignore if null
    if (name == null || name.trim().isEmpty()) {
      return;
    }

    // check if parameter is new
    if (!parameters.containsKey(name)) {
      parameters.put(name, value);
    } else {

      // add item to existing parameter
      String[] oldValue = parameters.get(name);
      String[] newValue = new String[oldValue.length + value.length];
      System.arraycopy(oldValue, 0, newValue, 0, oldValue.length);
      System.arraycopy(value, 0, newValue, oldValue.length, value.length);

      // put array back to map
      parameters.put(name, newValue);
    }
  }

  public void addAttachment(String name, InputStream stream, String filename, String contentType) {
    HttpRequestPart part = new HttpRequestPart(name, filename, contentType, stream);
    attachments.add(part);
  }

  public void addAttachment(String name, File file) throws IOException {
    HttpRequestPart part = new HttpRequestPart(name, file);
    attachments.add(part);
  }

  public void addAttachment(HttpRequestPart part) {
    attachments.add(part);
  }

  public void addAttachments(List<HttpRequestPart> parts) {
    attachments.addAll(parts);
  }

  public static String encodeData(Map<String, String[]> parameters) {

    // prepare string builder
    StringBuilder sb = new StringBuilder();
    boolean isFirst = true;

    // iterate through map
    for (Entry<String, String[]> entry : parameters.entrySet()) {
      if (entry.getKey() != null && !entry.getKey().trim().isEmpty() && entry.getValue() != null) {
        for (String v : entry.getValue()) {

          if (v == null) {
            continue;
          }

          try {
            // add ampersand?
            if (isFirst) {
              isFirst = false;
            } else {
              sb.append("&");
            }

            // add key and value
            sb.append(entry.getKey()).append("=").append(URLEncoder.encode(v, "utf-8"));
          } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(HttpRequest.class.getName()).log(Level.SEVERE, "utf-8 is not available!", ex);
          }
        }
      }
    }

    // return string
    return sb.toString();
  }

  public static Map<String, String> decodePostData(String data) {

    Map<String, String> results = new LinkedHashMap<String, String>();

    try {
      String[] values = data.split("&");

      for (int i = 0; i < values.length; i++) {
        String[] pair = values[i].split("=");
        if (pair.length == 2) {
          results.put(pair[0], URLDecoder.decode(pair[1], "UTF-8"));
        }
      }
    } catch (UnsupportedEncodingException ex) {
      // TODO error handling
    }

    return results;
  }
}
