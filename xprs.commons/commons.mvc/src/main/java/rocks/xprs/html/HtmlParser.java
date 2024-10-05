package rocks.xprs.html;

import java.io.File;
import java.io.IOException;
import java.util.Stack;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author Ralph Borowski
 */
public class HtmlParser extends DefaultHandler {
  
  private static final String TAG_HTML = "html";

  private HtmlElement root;
  private final Stack<HtmlElement> parents = new Stack<>();

  public HtmlElement process(File template) throws IOException {
    
    try {
      SAXParserFactory spf = SAXParserFactory.newInstance();
      spf.setValidating(true);
      spf.setNamespaceAware(false);
      SAXParser saxParser = spf.newSAXParser();
      saxParser.parse(template, this);
      
      return parents.peek();
      
    } catch(ParserConfigurationException | SAXException ex) {
      throw new IOException("Could not parse file " + template.getAbsolutePath(), ex);
    }
  }

  @Override
  public void startDocument() throws SAXException {
    parents.clear();
  }

  @Override
  public void startElement(String namespaceUri, String localName, String qName, Attributes atts)
          throws SAXException {
    
    HtmlElement current;
    
    // distinguish between document or fragment    
    if (root == null) {
      current = new HtmlDocument(qName);
      root = current;
    } else {
      current = new HtmlElement(qName);
      current.setDocumentRoot(root);
    }
    
    // copy attributes
    for (int i = 0; i < atts.getLength(); i++) {
      current.setAttribute(atts.getLocalName(i), atts.getValue(i));
    }
    
    // add to child nodes of the parent
    if (!parents.empty()) {
      parents.peek().addChildNode(current);
    }
    
    // set root
    
    
    // set document root
    if (root == null) {
      root = current;
    }
    
    // become the next parent
    parents.push(current);
  }

  @Override
  public void endElement(String namespaceUri, String localName, String qName) throws SAXException {
    
    // climb up one level in hierarchy
    if (parents.size() > 1) {
      parents.pop();
    }
  }

  @Override
  public void characters(char ch[], int start, int length) throws SAXException {
    parents.peek().addChildNode(new HtmlTextNode(new String(ch, start, length)));
  }
}
