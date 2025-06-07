package br.com.jeftavares.jbank.interceptors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AuditInterceptor implements HandlerInterceptor {

    private final Logger logger = LoggerFactory.getLogger(AuditInterceptor.class);

    // Antes de passar para o controller, o interceptor vai fazer alguma coisa
    // O boolean do preHandle é para saber se o interceptor vai continuar ou não,
    // vai enviar para o controller ou não
    @Override
    public boolean preHandle(HttpServletRequest request,
            HttpServletResponse response,
            Object handler) throws Exception {

        logger.info("pre-handle: {}", request.getRequestURI());
        return true;
    }

    // É executado depois da controller
    @Override
    public void postHandle(HttpServletRequest request,
            HttpServletResponse response,
            Object handler,
            ModelAndView modelAndView) throws Exception {
        logger.info("post-handle: {}", request.getRequestURI());
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    // Depois do postHandle, depois que o controller já executou
    @Override
    public void afterCompletion(HttpServletRequest request,
            HttpServletResponse response,
            Object handler,
            Exception ex) throws Exception {
        // logger.info("afer-completion: {}", request.getRequestURI());
        logger.info("Audit - Método: {}, Url: {}, StatusCcode: {}, IpAddress: {} ",
                request.getMethod(),
                request.getRequestURI(),
                response.getStatus(),
                request.getAttribute("x-user-ip")); // pega o atributo que foi setado no filtro(filter)
    }
}