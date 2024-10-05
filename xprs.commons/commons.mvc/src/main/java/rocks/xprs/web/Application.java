package rocks.xprs.web;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import rocks.xprs.exceptions.AccessDeniedException;
import rocks.xprs.exceptions.InvalidDataException;
import rocks.xprs.exceptions.ResourceNotFoundException;
import rocks.xprs.serialization.Converter;
import rocks.xprs.serialization.Converters;
import rocks.xprs.serialization.PrimitivesConverter;
import rocks.xprs.util.MultiValuedMap;

/**
 *
 * @author Ralph Borowski
 */
public abstract class Application implements Filter {

  private String contextPath = "";
  private final Map<String, Class<? extends Page>> pageMappings = new HashMap<>();
  private final Map<Class<? extends Extension>, Object> extensions = new HashMap<>();
  private final Converters converters = new Converters();

  public abstract void setup();

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {

    String rootPath = "";
    Path pathAnnotation = this.getClass().getAnnotation(Path.class);
    if (pathAnnotation != null) {
      rootPath = pathAnnotation.value()[0];
    }

    // build context path
    contextPath = UrlBuilder.combinePaths(filterConfig.getServletContext().getContextPath(), rootPath);
    if (contextPath.endsWith("/")) {
      contextPath = contextPath.substring(0, contextPath.length() - 1);
    }

    converters.register(PrimitivesConverter.class);
    setup();
  }

  @Override
  public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
          throws IOException, ServletException {

    HttpServletResponse response = (HttpServletResponse) res;

    HttpServletRequestWrapper requestWrapper = new HttpServletRequestWrapper((HttpServletRequest) req) {

      @Override
      public String getContextPath() {
        return contextPath;
      }

    };

    // drop context path
    URL requestUrl = new URL(requestWrapper.getRequestURL().toString());
    String uri = URLDecoder.decode(requestUrl.getPath()
            .substring(contextPath.length()), StandardCharsets.UTF_8);

    // drop queryString
    uri = uri.contains("?") ? uri.substring(0, uri.indexOf("?")) : uri;

    Class<? extends Controller> pageClass = resolveUrl(uri);

    if (pageClass != null) {
      try {

        requestWrapper.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        Context context = new Context(this, requestWrapper, response);
        context.setPathParameters(getPathParams(pageClass, uri));
        Context.set(context);

        Controller controller = pageClass.getDeclaredConstructor().newInstance();
        controller.init(context);
        controller.process();

        // TODO wrap response to chain to other filters

        context.destroy();
        return;

      } catch (Exception exception) {

        // check if exception was wrapped
        if (exception instanceof InvocationTargetException) {

          if (!(exception.getCause() instanceof Exception)) {
            // send 500
            Logger.getLogger(Application.class.getName()).log(Level.SEVERE,
                    "Exception in Dispatcher for uri " + uri, exception);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, exception.getMessage());
            return;
          } else {
            exception = (Exception) exception.getCause();
          }
        }

        // switch cases and choose proper return code
        if (exception instanceof ResourceNotFoundException) {

          // send 404
          response.setStatus(HttpServletResponse.SC_NOT_FOUND);
          Logger.getLogger(this.getClass().getName()).log(Level.INFO,
                  "Not found:" + uri, exception);

        } else if (exception instanceof AccessDeniedException) {

          // send 403
          response.setStatus(HttpServletResponse.SC_FORBIDDEN);
          Logger.getLogger(this.getClass().getName()).log(Level.INFO,
                  "Forbidden: " + uri, exception);

        } else if (exception instanceof InvalidDataException) {

          // send 400
          response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
          Logger.getLogger(this.getClass().getName()).log(Level.INFO,
                  "Bad request: " + uri, exception);

        } else {

          // send 500
          Logger.getLogger(Application.class.getName()).log(Level.SEVERE,
                  "Exception in Dispatcher for uri " + uri, exception);
          response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, exception.getMessage());
        }

        return;
      }
    }

    // continue in filter chain
    chain.doFilter(req, res);
  }

  @Override
  public void destroy() {

  }

  public void register(Class clazz) {

    if (Controller.class.isAssignableFrom(clazz)) {
      // get path annotation
      Path pathAnnotation = (Path) clazz.getAnnotation(Path.class);
      if (pathAnnotation != null) {
        for (String p : pathAnnotation.value()) {
          p = p.replaceAll("\\{.*?\\}", "([^/]+)");
          pageMappings.put(p, clazz);
        }
      }
    }

    if (Converter.class.isAssignableFrom(clazz)) {
      converters.register(clazz);
    }

    if (Extension.class.isAssignableFrom(clazz)) {
      try {
        Extension e = (Extension) clazz.getDeclaredConstructor().newInstance();
        e.init();
        extensions.put(clazz, e);
      } catch (ReflectiveOperationException ex) {
        Logger.getLogger(Application.class.getName()).log(Level.SEVERE,
                String.format("Could not load extension %s.", clazz.getName()),
                ex);
      }
    }
  }

  public String getUrl(Class<? extends Controller> Controller, Object... parameters) {
    String[] urls = Controller.getAnnotation(Path.class).value();

    Pattern placeholderPattern = Pattern.compile("\\{(.*?)\\}");
    List<String> placeholders = new LinkedList<>();

    for (String url : urls) {

      // get parameters
      placeholders.clear();
      Matcher placeholderMatcher = placeholderPattern.matcher(url);
      while (placeholderMatcher.find()) {
        placeholders.add(placeholderMatcher.group(1));
      }

      // check if found
      if (parameters.length == placeholders.size()) {
        String resultUrl = url;
        for (Object p : parameters) {
          placeholderMatcher.reset(resultUrl);
          resultUrl = placeholderMatcher.replaceFirst(
                  URLEncoder.encode(String.valueOf(p), StandardCharsets.UTF_8));
        }
        return UrlBuilder.combinePaths(contextPath, resultUrl);
      }
    }

    return null;
  }

  public Class<? extends Controller> resolveUrl(String url) {
    for (String pattern : pageMappings.keySet()) {
      if (url.equals(pattern)) {
        return pageMappings.get(pattern);
      }
    }

    for (String pattern : pageMappings.keySet()) {
      if (url.matches(pattern)) {
        return pageMappings.get(pattern);
      }
    }
    return null;
  }

  public MultiValuedMap<String, String> getPathParams(
      Class<? extends Controller> controllerClass, String url) {

    // find the correct annotation
    Pattern pattern = null;
    String path = null;
    for (String p : controllerClass.getAnnotation(Path.class).value()) {

      String pathPattern = p.replaceAll("\\{.*?\\}", "([^/]+)");
      if (url.matches(pathPattern)) {
        path = p;
        pattern = Pattern.compile(pathPattern);
        break;
      }
    }

    if (pattern == null || path == null) {
      return new MultiValuedMap<>();
    }

    // get parameter names
    Matcher matcher = pattern.matcher(path);
    List<String> parameterNames = new LinkedList<>();
    if (matcher.find() && matcher.groupCount() > 0) {
      for (int i = 1; i <= matcher.groupCount(); i++) {
        parameterNames.add(matcher.group(i).replace("{", "").replace("}", ""));
      }
    }

    // build multi value map
    MultiValuedMap<String, String> result = new MultiValuedMap<>();

    if (!parameterNames.isEmpty()) {
      matcher.reset(url);
      if (matcher.find() && matcher.groupCount() > 0) {
        for (int i = 1; i <= matcher.groupCount(); i++) {
          String parameterValue = matcher.group(i);
          result.addValue(parameterNames.get(i - 1), parameterValue);
        }
      }
    }

    return result;
  }

  public Converters getConverters() {
    return converters;
  }
}
