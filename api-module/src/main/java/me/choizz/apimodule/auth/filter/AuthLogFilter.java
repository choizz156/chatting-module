package me.choizz.apimodule.auth.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import me.choizz.apimodule.aop.MdcKey;
import net.logstash.logback.encoder.org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
public class AuthLogFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
        final HttpServletRequest request,
        final HttpServletResponse response,
        final FilterChain filterChain
    ) throws ServletException, IOException {
        String requestId = request.getHeader("X-RequestID");
        log.error("{}", requestId);
        MDC.put(MdcKey.TRACE_ID.name(),
            StringUtils.defaultString(
                requestId,
                UUID.randomUUID().toString()
            )
        );
        filterChain.doFilter(request, response);
        MDC.clear();
    }
}
