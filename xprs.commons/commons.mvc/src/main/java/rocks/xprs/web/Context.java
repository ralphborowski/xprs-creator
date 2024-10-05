package rocks.xprs.web;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import rocks.xprs.security.PasswordHash;
import rocks.xprs.security.RandomCodeGenerator;
import rocks.xprs.util.Translator;
import rocks.xprs.util.CounterMap;
import rocks.xprs.util.MultiValuedMap;

/**
 *
 * @author Ralph Borowski
 */
public class Context {

  private static final ThreadLocal<Context> CURRENT = new ThreadLocal<>();
  public static Context get() {
    return CURRENT.get();
  }

  public static void set(Context context) {
    CURRENT.set(context);
  }

  private final String SATTR_NOTIFICATIONS = "rocks.xprs.notifications";
  private final String SATTR_USER = "rocks.xprs.user";
  private final String SATTR_CSRF_TOKEN_BASE = "rocks.xprs.csrf-token";

  private final Application application;
  private final HttpServletRequest request;
  private final HttpServletResponse response;
  private final MultiValuedMap<String, String> getParameters;
  private final MultiValuedMap<String, String> postParameters;
  private MultiValuedMap<String, String> pathParameters;

  private User user;
  private Locale locale;
  private final List<Notification> notifications = new ArrayList<>();

  private final CounterMap<String> elementCounterMap = new CounterMap<>();

  public Context(Application application, HttpServletRequest request,
          HttpServletResponse response) {

    this.application = application;
    this.request = request;
    this.response = response;

    // get user
    if (request.getSession(false) != null
            && request.getSession(false).getAttribute(SATTR_USER) != null
            && request.getSession(false).getAttribute(SATTR_USER) instanceof User) {

      user = (User) request.getSession().getAttribute(SATTR_USER);
      locale = user.getLocale() != null ? user.getLocale() : request.getLocale();
      if (locale == null) {
        locale = Locale.ENGLISH;
      }
    } else {
      locale = request.getLocale();
      if (locale == null) {
        locale = Locale.ENGLISH;
      }
    }

    // generate parameter maps
    getParameters = new MultiValuedMap<>(parseQueryString(request.getQueryString()));
    postParameters = new MultiValuedMap<>();
    for (String key : request.getParameterMap().keySet()) {
      if (getParameters.get(key) == null || getParameters.get(key).isEmpty()) {
        postParameters.put(key, Arrays.asList(request.getParameterValues(key)));
      } else {
        List<String> getValues = getParameters.get(key);

        for (String v : request.getParameterValues(key)) {
          if (!getValues.contains(v)) {
            postParameters.addValue(key, v);
          }
        }
      }
    }

    // recover notifications from session
    List<Notification> recoveredNotifications
            = (List<Notification>) request.getSession().getAttribute(SATTR_NOTIFICATIONS);
    if (recoveredNotifications != null) {
      notifications.addAll(recoveredNotifications);
    }
  }

  public void destroy() {
    request.getSession().setAttribute(SATTR_NOTIFICATIONS, notifications);
    request.getSession().setAttribute(SATTR_USER, user);
  }

  //<editor-fold defaultstate="collapsed" desc="Notification handling">
  public void addNotification(Notification.Type type, String message) {
    notifications.add(new Notification(type, message));
  }

  public List<Notification> listNotifications() {
    return Collections.unmodifiableList(notifications);
  }

  public void clearNotifications() {
    notifications.clear();
  }
  //</editor-fold>

  //<editor-fold defaultstate="collapsed" desc="Getters">
  public Application getApplication() {
    return this.application;
  }

  public boolean isPostback() {
    return request.getMethod().equalsIgnoreCase("POST");
  }

  public String getUrl(Class<? extends Controller> controller, Object... parameters) {
    return application.getUrl(controller, parameters);
  }

  public String getId(String type) {
    return type + "-" + elementCounterMap.add(type);
  }

  /**
   * @return the request
   */
  public HttpServletRequest getRequest() {
    return request;
  }

  public String getRelativeUrl() {
    return request.getRequestURI().substring(request.getContextPath().length()
            + request.getServletPath().length());
  }

  /**
   * @return the response
   */
  public HttpServletResponse getResponse() {
    return response;
  }

  public HttpSession getSession() {
    return request.getSession();
  }

  /**
   * @return the getParameters
   */
  public MultiValuedMap<String, String> getGetParameters() {
    return getParameters;
  }

  /**
   * @return the postParameters
   */
  public MultiValuedMap<String, String> getPostParameters() {
    return postParameters;
  }

  public String getPathParameter(String key) {
    if (pathParameters.get(key) != null && !pathParameters.get(key).isEmpty()) {
      return pathParameters.get(key).get(0);
    }
    return null;
  }

  public MultiValuedMap<String, String> getPathParameters() {
    return pathParameters;
  }

  public List<String> getPathParameters(String key) {
    return pathParameters.get(key);
  }

  public void setPathParameters(MultiValuedMap<String, String> pathParameters) {
    this.pathParameters = pathParameters;
  }

  /**
   * @return the user
   */
  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  /**
   * @return the locale
   */
  public Locale getLocale() {
    return locale;
  }
  //</editor-fold>

  //<editor-fold defaultstate="collapsed" desc="Auxiliary methods">
  private Map<String, List<String>> parseQueryString(String queryString) {

    // return empty map if query string is null
    if (queryString == null || queryString.isEmpty()) {
      return Collections.EMPTY_MAP;
    }

    Map<String, List<String>> result = new HashMap<>();

    for (String pair : queryString.split("&")) {
      try {
        int separatorPos = pair.indexOf("=");

        // ignore values without keys
        if (separatorPos < 1) {
          continue;
        }

        // decode key and value
        String key = URLDecoder.decode(pair.substring(0, separatorPos), "UTF-8");
        String value = URLDecoder.decode(pair.substring(separatorPos + 1), "UTF-8");

        // add to result
        if (!result.containsKey(key)) {
          result.put(key, new LinkedList<>());
        }

        result.get(key).add(value);
      } catch (UnsupportedEncodingException ex) {
        // this should never happen, UTF-8 is Java's standard encoding
        Logger.getLogger(this.getClass().getCanonicalName())
                .log(Level.SEVERE, "Unable to decode string. UTF-8 not available.", ex);
      }
    }

    return result;
  }
  //</editor-fold>

  private String generateCsrfTokenToken(String action) {
    String csrfBase = (String) request.getSession().getAttribute(SATTR_CSRF_TOKEN_BASE);
    if (csrfBase == null) {
      csrfBase = RandomCodeGenerator.generateAlphaNumeric(20);
      request.getSession().setAttribute(SATTR_CSRF_TOKEN_BASE, csrfBase);
    }
    return csrfBase + "::" + action;
  }

  public String getCsrfToken(String action) {
    return PasswordHash.createHash(generateCsrfTokenToken(action));
  }

  public boolean checkCsrfToken(String token, String action) {
    if (token == null || token.isBlank()) {
      return false;
    }
    return PasswordHash.verifyPassword(generateCsrfTokenToken(action), token);
  }

  public String t(String key) {
    return Translator.get(locale).get(key);
  }

  public String t(String key, Object... values) {
    return String.format(Translator.get(locale).get(key), values);
  }

  public <T> T parse(String value, Class<T> type) {
    return application.getConverters().parse(value, type, locale);
  }

  public String format(Object o) {
    return application.getConverters().format(o, locale);
  }
}
