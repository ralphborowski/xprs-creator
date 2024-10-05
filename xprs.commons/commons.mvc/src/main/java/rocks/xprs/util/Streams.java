/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rocks.xprs.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 *
 * @author borowski
 */
public class Streams {

  public static void copy(InputStream in, OutputStream out) throws IOException {
    copy(in, out, 4096);
  }

  public static void copy(InputStream in, OutputStream out, int bufferSize) throws IOException {

    try (InputStream input = in;
            OutputStream output = out) {

      byte[] buffer = new byte[bufferSize];
      int bytesRead;

      while ((bytesRead = input.read(buffer)) > -1) {
        output.write(buffer, 0, bytesRead);
      }
      output.flush();
    }
  }

  public static void copyAndKeepOpen(InputStream in, OutputStream out) throws IOException {
    copyAndKeepOpen(in, out, 4096);
  }

  public static void copyAndKeepOpen(InputStream in, OutputStream out, int bufferSize)
          throws IOException {

    byte[] buffer = new byte[bufferSize];
    int bytesRead;

    while ((bytesRead = in.read(buffer)) > -1) {
      out.write(buffer, 0, bytesRead);
    }
    out.flush();
  }
}
