package rocks.xprs.web.filters;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import rocks.xprs.web.Context;
import rocks.xprs.web.UrlBuilder;

/**
 *
 * @author Ralph Borowski
 */
public abstract class LoginFilter implements Filter {

  public abstract String getLoginPath();

  public abstract String getLogoutPath();

  public abstract boolean isExcluded(String path);

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {

  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
          throws IOException, ServletException {

    HttpServletRequest httpRequest = (HttpServletRequest) request;
    HttpServletResponse httpResponse = (HttpServletResponse) response;

    String requestedPath = httpRequest.getRequestURI().substring(
            httpRequest.getContextPath().length());
    File staticFile = new File(httpRequest.getServletContext().getRealPath(requestedPath));

    Context context = new Context(null, httpRequest, httpResponse);
    if ((staticFile.exists() && staticFile.isFile())
            || context.getUser() != null
            || context.getRelativeUrl().equals(getLoginPath())
            || context.getRelativeUrl().equals(getLogoutPath())
            || isExcluded(context.getRelativeUrl())) {

      chain.doFilter(request, response);
    } else {
      httpResponse.sendRedirect(UrlBuilder.getAbsoluteLink(httpRequest, getLoginPath())
              + "?returnUrl=" + URLEncoder.encode(UrlBuilder.getRequestUrl(httpRequest),
                      StandardCharsets.UTF_8.name()));
    }
  }

  @Override
  public void destroy() {

  }

}
