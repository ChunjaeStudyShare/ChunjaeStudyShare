package net.fullstack7.studyShare.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.springframework.stereotype.Component;
import java.io.IOException;

@Component
public class AdminCheckFilter implements Filter {
    private static final String ADMIN_URL_PATTERN = "/admin/*";
    @Override   
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String requestURI = httpRequest.getRequestURI();
        if(requestURI.matches(ADMIN_URL_PATTERN)) {
            String userId = (String) httpRequest.getAttribute("userId");
            if(userId == null) {
                httpResponse.sendRedirect("/admin/login");
                return;
            }
        }
        chain.doFilter(request, response);
    }
}