package rocks.xprs.auth;

import java.util.List;

/**
 *
 * @author rborowski
 */
public class PasswordAndTanAuthenticator {

  public enum States {START, TAN}

  private String username;
  private char[] password;
  private String sessionToken;

  private States currentState = States.START;

  public List<Field> getFields() {
    return null;
  }

  public List<Field> validate(List<Field> fields) {
    return null;
  }

  public boolean isAuthenticated() {
    return false;
  }

  public String getUsername() {
    return username;
  }

  public static class Field {



  }

}
