/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rocks.xprs.xml;

import rocks.xprs.serialization.XmlSerializer;
import java.io.*;
import javax.xml.transform.*;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

/**
 *
 * @author borowski
 */
public class Template {

  private final StringBuilder data = new StringBuilder();
  private OutputStream output;
  private Source xsl;
  private String rootName = "page";
  private String xslFilename; // just for debugging

  public Template(OutputStream output) {
    this.output = output;
  }

  public void add(String alias, Object o) {
    data.append("<").append(alias).append(">");
    data.append(XmlSerializer.serialize(o));
    data.append("</").append(alias).append(">");
  }

  public void bypass(String s) {
    data.append(s);
  }

  public void render() throws IOException {
    String dataString = "<" + rootName + ">" + data.toString() + "</" + rootName + ">";

    if (xsl == null) {
      output.write(dataString.getBytes());
      return;
    }

    try {

      // get the stream
      Source xml = new StreamSource(new StringReader(dataString));
      Result result = new StreamResult(output);

      // merge attribute sheet with stylesheet and write result to output stream
      TransformerFactory tf = TransformerFactory.newInstance();
      Transformer t = tf.newTransformer(xsl);
      t.transform(xml, result);
    } catch (TransformerException ex) {
      throw new IOException(
              String.format("Could not transform template %s (xml root name %s)",
                      xslFilename, rootName),
              ex);
    }
  }

  /**
   * @return the output
   */
  public OutputStream getOutput() {
    return output;
  }

  /**
   * @param output the output to set
   */
  public void setOutput(OutputStream output) {
    this.output = output;
  }

  /**
   * @param stylesheet the stylesheet to set
   * @throws java.io.FileNotFoundException
   */
  public void setStylesheet(File stylesheet) throws FileNotFoundException {
    this.xsl = new StreamSource(stylesheet);
    this.xslFilename = stylesheet.getName();
  }

  public void setStylesheet(InputStream stylesheet) {
    this.xsl = new StreamSource(stylesheet);
  }

  public void setRootName(String rootName) {
    this.rootName = rootName;
  }
}