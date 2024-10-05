/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rocks.xprs.web.filters;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Sets the CORS header for REST requests by a web browser.
 *
 * @author Ralph Borowski
 */
public class CorsFilter implements Filter {

  @Override
  public void init(FilterConfig filterConfig) {
  }

  @Override
  public void destroy() {
  }

  @Override
  public void doFilter(ServletRequest servletRequest,
          ServletResponse servletResponse,
          FilterChain filterChain)
          throws IOException, ServletException {

    if (servletResponse instanceof HttpServletRequest) {
      
      HttpServletRequest request = (HttpServletRequest) servletRequest;
      HttpServletResponse response = (HttpServletResponse) servletResponse;
      
            // add access control headers if CORS
      if (request.getHeader("Origin") != null) {
        response.addHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
        response.addHeader("Access-Control-Allow-Headers", 
                "Content-Type, Access-Control-Allow-Headers, Authorization, Accept");
        response.addHeader("Access-Control-Allow-Methods", "GET,PUT,POST,DELETE,OPTIONS,HEAD");
        response.addHeader("Access-Control-Allow-Credentials", "true");
      }
    }

  }
}
