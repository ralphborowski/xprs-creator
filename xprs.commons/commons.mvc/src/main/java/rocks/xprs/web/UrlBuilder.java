/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rocks.xprs.web;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import rocks.xprs.http.HttpRequest;
import rocks.xprs.util.MultiValuedMap;

/**
 *
 * @author borowski
 */
public class UrlBuilder {

  public static String getAbsoluteLink(HttpServletRequest request, String path) {

    StringBuilder sb = new StringBuilder();

    // add path string
    // relative to root
    if (path.length() > 0 && path.charAt(0) == '/') {
      // get schema
      if (request.isSecure()) {
        sb.append("https://");
      } else {
        sb.append("http://");
      }

      // add server and port
      sb.append(request.getServerName());
      if (request.getServerPort() != 80 && request.getServerPort() != 443) {
        sb.append(":").append(request.getServerPort());
      }

      // add context path?
      if (!path.startsWith(request.getContextPath())) {
        sb.append(request.getContextPath());
      }
    } else {
      // relative to current page
      sb.append(request.getRequestURL());
    }

    sb.append(encodePath(path));

    return sb.toString();
  }

  public static String getAbsoluteLink(URL url, String path) {

    // check if path is valid url
    try {
      URL pathUrl = new URL(path);
      return path;

    } catch (MalformedURLException ex) {

      // relative to root
      if (path.length() > 0) {

        // check if only protocol is missing
        if (path.startsWith("//")) {
          return url.getProtocol() + ":" + path;
        }

        // prepare string builder
        StringBuilder sb = new StringBuilder();

        // get protocol
        sb.append(url.getProtocol()).append("://");

        // add server and port
        sb.append(url.getHost());
        if (url.getPort() != 80 && url.getPort() != 443 && url.getPort() > 0) {
          sb.append(":").append(url.getPort());
        }

        if (path.charAt(0) != '/') {
          sb.append("/");
        }

        // add path
        sb.append(encodePath(path));

        return sb.toString();
      }

      // if path is empty, return url
      return url.toString();
    }
  }

  public static String getRequestUrl(HttpServletRequest request) {
    if (request.getQueryString() != null) {
      return request.getRequestURL().append("?").append(request.getQueryString()).toString();
    } else {
      return request.getRequestURL().toString();
    }
  }

  private static String encodePath(String path) {

    StringBuilder sb = new StringBuilder();

    if (path.startsWith("/")) {
      sb.append("/");
    }

    for (String s : path.split("/")) {
      if (sb.length() > 1) {
        sb.append("/");
      }
      sb.append(URLEncoder.encode(s, StandardCharsets.UTF_8));
    }

    if (path.length() > 1 && path.endsWith("/")) {
      sb.append("/");
    }

    return sb.toString();
  }

    public static String buildNormalizedGet(String url, MultiValuedMap<String, String> parameters) {

    MultiValuedMap<String, String> orderedParameters = new MultiValuedMap<>(parameters);

    // order parameters
    for (Map.Entry<String,List<String>> i : orderedParameters.entrySet()) {
      if (i.getValue() != null) {
        i.getValue().sort(String.CASE_INSENSITIVE_ORDER);
      }
    }

    // order keys and call buildGet
    return buildGet(url, orderedParameters);
  }

  public static String buildGet(String url, MultiValuedMap<String, String> parameters) {

    // add parameters if any
    if (parameters != null) {
      if (url.contains("?")) {
        return url + "&" + encodeParameters(parameters);
      } else {
        return url + "?" + encodeParameters(parameters);
      }
    }

    // return url only
    return url;
  }

  public static String buildGet(HttpServletRequest request,
      MultiValuedMap<String, String> parameters) {

    return UrlBuilder.buildGet(request.getRequestURL().toString(), parameters);
  }

  public static String combinePaths(String path1, String path2) {
    if (!path1.endsWith("/")) {
      path1 += "/";
    }

    if (path2.startsWith("/")) {
      path2 = path2.substring(1);
    }

    return path1 + path2;
  }

  public static String encodeParameters(MultiValuedMap<String, String> parameters) {

    // prepare target
    StringBuilder data = new StringBuilder();
    boolean isFirst = true;

    // iterate through parameters
    for (Map.Entry<String, List<String>> parameter : parameters.entrySet()) {

      // check if parameter is empty
      if (parameter.getKey() != null && parameter.getValue() != null) {

        // iterate through values
        for (String value : parameter.getValue()) {

          if (value != null) {
            try {
              if (isFirst) {
                isFirst = false;
              } else {
                data.append("&");
              }

              data.append(parameter.getKey());
              data.append("=");
              data.append(URLEncoder.encode(value,"utf-8"));
            } catch (UnsupportedEncodingException ex) {

              // should never happen
              Logger.getLogger(HttpRequest.class.getName()).log(Level.SEVERE, "UTF-8 is not supportet.", ex);
            }
          }
        }
      }
    }

    // return result
    return data.toString();
  }

  public static MultiValuedMap<String, String> decodeParameters(String parameterString) {

    // init result object
    MultiValuedMap<String, String> result = new MultiValuedMap<>();

    // split query stirng into key-value-pairs
    String[] pairs = parameterString.split("&");

    // iterate through pairs
    for (String pair : pairs) {

      // split key and value
      String[] keyValue = pair.split("=");
      if (keyValue.length == 2) {

        try {
          result.addValue(keyValue[0], URLDecoder.decode(keyValue[1], "UTF-8"));
        } catch (UnsupportedEncodingException ex) {
          Logger.getLogger(UrlBuilder.class.getName()).log(Level.SEVERE,
                  "Could not decode UTF-8 from query string.", ex);
        }
      }
    }

    // return result
    return result;
  }

}
