/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rocks.xprs.mail;

import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.imap.IMAPStore;
import jakarta.mail.Folder;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Store;
import jakarta.mail.search.SearchTerm;
import java.io.Closeable;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author rborowski
 */
public class ImapClient implements Closeable {

  private final Session mailSession;
  private final String username;
  private final String password;

  private IMAPStore store = null;
  private Map<String, IMAPFolder> openFolders = new HashMap<>();

  public ImapClient(Session mailSession, String username, String password) throws MessagingException {
    this.mailSession = mailSession;
    this.username = username;
    this.password = password;
    connect();
  }

  public Message[] list(String folderName) throws MessagingException {
    Folder folder = store.getFolder(folderName);

    if (!folder.exists()) {
      throw new MessagingException(String.format("Folder %s doesn't exist.", folderName));
    }
    
    return folder.getMessages();
  }

  public Message[] list(String folderName, SearchTerm searchTerm) throws MessagingException {
    IMAPFolder folder = getFolder(folderName);

    if (!folder.exists()) {
      throw new MessagingException(String.format("Folder %s doesn't exist.", folderName));
    }

    return folder.search(searchTerm);
  }

  public void save(String folderName, Message... messages) throws MessagingException {
    IMAPFolder folder = getFolder(folderName);

    if (!folder.exists()) {
      throw new MessagingException(String.format("Folder %s doesn't exist.", folderName));
    }

    folder.appendMessages(messages);

  }

  public void move(String sourceFolderName, String targetFolderName, Message... messages)
          throws MessagingException {

    IMAPFolder sourceFolder = getFolder(sourceFolderName);
    IMAPFolder targetFolder = getFolder(targetFolderName);

    if (!sourceFolder.exists()) {
      throw new MessagingException(
              String.format("Source folder %s doesn't exist.", sourceFolderName));
    }

    if (!targetFolder.exists()) {
      throw new MessagingException(
              String.format("Target folder %s doesn't exist.", targetFolderName));
    }

    sourceFolder.moveMessages(messages, targetFolder);
  }

  public IMAPFolder getFolder(String name) throws MessagingException {
    if (!openFolders.containsKey(name)) {
      IMAPFolder folder = (IMAPFolder) store.getFolder(name);
      if (folder.exists()) {
        folder.open(Folder.READ_WRITE);
        openFolders.put(name, folder);
      }
      return folder;
    }
    return openFolders.get(name);
  }

  public IMAPFolder createFolder(String name) throws MessagingException {
    IMAPFolder folder = getFolder(name);
    if (!folder.exists()) {
      if (folder.create(Folder.HOLDS_MESSAGES)) {
        folder.setSubscribed(true);
        return getFolder(name);
      } else {
        throw new MessagingException(String.format("Folder %s could not be created.", name));
      }
    } else {
      return folder;
    }
  }

  public IMAPFolder[] listFolders() throws MessagingException {
    return (IMAPFolder[]) store.getDefaultFolder().list();
  }

  private Store connect() throws MessagingException {
    if (store == null) {
      store = (IMAPStore) mailSession.getStore("imaps");
      store.connect(username, password);
    }
    return store;
  }

  @Override
  public void close() throws IOException {
    for (IMAPFolder f : openFolders.values()) {
      try {
        f.close();
      } catch (MessagingException ex) {
        // ignore, probably already closed
      }
    }
    if (store != null) {
      try {
        store.close();
      } catch (MessagingException ex) {
        throw new IOException("Could not close IMAP store.", ex);
      }
    }
  }

}
