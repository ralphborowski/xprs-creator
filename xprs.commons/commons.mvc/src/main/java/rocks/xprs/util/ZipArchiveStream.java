/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rocks.xprs.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 *
 * @author borowski
 */
public class ZipArchiveStream {
  
  public static void write(List<File> files, OutputStream outputStream) throws IOException {
    
    try (ZipOutputStream out = new ZipOutputStream(outputStream)) {

      byte[] buffer = new byte[4096];
      int bytesRead;

      for (File f : files) {
        
        out.putNextEntry(new ZipEntry(f.getName()));

        try (InputStream in = new FileInputStream(f)) {

          while ((bytesRead = in.read(buffer)) > 0) {
            out.write(buffer, 0, bytesRead);
          }
        }
      }
      
      out.flush();
      out.closeEntry();
    }
  }
  
  public static void read(InputStream in, File targetFolder) throws IOException {
    
    try (ZipInputStream zipInput = new ZipInputStream(in, StandardCharsets.ISO_8859_1)) {
      ZipEntry zipEntry;
      while((zipEntry = zipInput.getNextEntry()) != null) {
        File targetFile = new File(targetFolder, zipEntry.getName());
        
        if (!targetFile.getCanonicalPath().startsWith(targetFolder.getCanonicalPath())) {
          throw new IOException("Invalid filename in zip file.");
        }
        
        if (!zipEntry.isDirectory()) {
          if (targetFile.getParentFile().exists() || targetFile.getParentFile().mkdirs()) {
            try (FileOutputStream fos = new FileOutputStream(targetFile)) {
              byte[] buffer = new byte[4096];
              int bytesRead;
              while ((bytesRead = zipInput.read(buffer)) > -1) {
                fos.write(buffer, 0, bytesRead);
              }
            }
          }
        }
      }
    }
  }
  
}
