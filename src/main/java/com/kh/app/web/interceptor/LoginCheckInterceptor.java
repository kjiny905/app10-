package com.kh.app.web.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.net.URLEncoder;

@Slf4j
public class LoginCheckInterceptor implements HandlerInterceptor {
  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    String redirectUrl = null;
    String requestURI = request.getRequestURI(); //uri가 url을 포함하고 있음
    log.info("requestURI={}", requestURI);

    if (request.getQueryString() != null) {
      String queryString = URLEncoder.encode(request.getQueryString(), "UTF-8");
      StringBuffer sb = new StringBuffer();
      redirectUrl = sb.append(requestURI)
          .append("&")
          .append(queryString)
          .toString();
    } else {
      redirectUrl = requestURI;
    }

    //세션체크(여부를 체크하는거니 없으면 만들지 않게 false사용)
    HttpSession session = request.getSession(false);
    if (session == null) {
      log.info("미인증 요청");
      response.sendRedirect("/login?redirectUrl=" + redirectUrl); //세션없으면login페이지로이동
    }

    return true;
  }

  @Override
  public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
  }

  @Override
  public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
  }
}
