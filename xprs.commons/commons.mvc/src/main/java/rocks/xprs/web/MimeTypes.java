/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rocks.xprs.web;

/**
 *
 * @author borowski
 */
public class MimeTypes {
  
  public static String guess(String filename) {
    
    filename = filename.toLowerCase();
    
    
    // switch file type
    
    // browser related
    if (filename.endsWith(".css")) {
      return "text/css";
    }

    if (filename.endsWith(".json")) {
      return "application/json";
    }

    if (filename.endsWith(".js")) {
      return "text/javascript";
    }
    
    if (filename.endsWith(".html")) {
      return "text/html";
    }
    
    
    // images
    if (filename.endsWith(".jpg") || filename.endsWith(".jpeg")) {
      return "image/jpeg";
    }

    if (filename.endsWith(".png")) {
      return "image/png";
    }

    if (filename.endsWith(".gif")) {
      return "image/gif";
    }
    
    if (filename.endsWith(".svg") || filename.endsWith(".svgz")) {
      return "image/svg+xml";
    }
    
    // videos
    if (filename.endsWith(".mpeg2") || filename.endsWith(".mpg") || filename.endsWith(".vob") || filename.endsWith(".mpe") ) {
      return "video/mpeg";
    }
    
    if (filename.endsWith(".mov")) {
      return "video/quicktime";
    }
    
    if (filename.endsWith(".avi")) {
      return "video/x-msvideo";
    }
    
    // common document types
    if (filename.endsWith(".pdf")) {
      return "application/pdf";
    }
    
    if (filename.endsWith(".csv")) {
      return "text/comma-separated-values";
    }
    
    if (filename.endsWith(".txt")) {
      return "text/plain";
    }
    
    if (filename.endsWith(".zip")) {
      return "application/zip";
    }
    
    if (filename.endsWith(".gz")) {
      return "application/gzip";
    }
    
    // unknown filetype
    return "application/octet-stream";
  }
  
}
