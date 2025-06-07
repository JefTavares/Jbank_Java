package br.com.jeftavares.jbank.interceptors;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    private final AuditInterceptor auditInterceptor;

    public InterceptorConfig(AuditInterceptor auditInterceptor) {
        this.auditInterceptor = auditInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //Faz o registro do interceptor para dentro do spring
        registry.addInterceptor(auditInterceptor)
                .order(0);
        //.addPathPatterns("/**") //executa o interceptor em uma determinada url ou rota;
        //.pathMatcher()
        //registry.addInterceptor(new AuditInterceptor());
    }
}