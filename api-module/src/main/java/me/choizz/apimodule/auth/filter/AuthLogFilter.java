package me.choizz.apimodule.auth.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import net.logstash.logback.encoder.org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
public class AuthLogFilter extends OncePerRequestFilter {

    private static final String TRACE_ID = "TRACE_ID";

    @Override
    protected void doFilterInternal(
        final HttpServletRequest request,
        final HttpServletResponse response,
        final FilterChain filterChain
    ) throws ServletException, IOException {
        String requestId = request.getHeader("X-RequestID");
        log.error("{}", requestId);
        MDC.put(TRACE_ID,
            StringUtils.defaultString(
                requestId,
                UUID.randomUUID().toString()
            )
        );
        filterChain.doFilter(request, response);
        MDC.clear();
    }
}
