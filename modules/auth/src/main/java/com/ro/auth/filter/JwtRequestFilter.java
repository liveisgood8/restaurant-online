package com.ro.auth.filter;

import com.ro.auth.service.JwtUserDetailsService;
import com.ro.auth.utils.JwtTokenUtil;
import io.jsonwebtoken.ExpiredJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

  private static Logger logger = LoggerFactory.getLogger(JwtRequestFilter.class);

  private JwtUserDetailsService jwtUserDetailsService;
  private JwtTokenUtil jwtTokenUtil;

  @Autowired
  public void setJwtUserDetailsService(JwtUserDetailsService jwtUserDetailsService) {
    this.jwtUserDetailsService = jwtUserDetailsService;
  }

  @Autowired
  public void setJwtTokenUtil(JwtTokenUtil jwtTokenUtil) {
    this.jwtTokenUtil = jwtTokenUtil;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request,
                                  HttpServletResponse response,
                                  FilterChain filterChain) throws ServletException, IOException {
    String username = null;
    String accessToken = null;

    String requestTokenHeader = request.getHeader("Authorization");
    if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
      accessToken = requestTokenHeader.substring(7);
      try {
        username = jwtTokenUtil.getUsernameFromToken(accessToken);
      } catch (IllegalArgumentException ex) {
        logger.warn("Invalid jwt access token: {}", requestTokenHeader, ex);
      } catch (ExpiredJwtException ex) {
        logger.warn("Expired jwt access token: {}", requestTokenHeader);
      }
    } else {
      logger.warn("Auth request without bearer header: {}", requestTokenHeader);
    }

    if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
      UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(username);
      if (jwtTokenUtil.validateToken(accessToken, userDetails)) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
      }
    }

    filterChain.doFilter(request, response);
  }

}
