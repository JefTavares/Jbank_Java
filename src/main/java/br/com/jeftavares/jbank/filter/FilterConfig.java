package br.com.jeftavares.jbank.filter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {
    private final IpFilter ipFilter;

    public FilterConfig(IpFilter ipFilter) {
        this.ipFilter = ipFilter;
    }

    //faz o registro do filtro para dentro do spring
    @Bean
    public FilterRegistrationBean<IpFilter> ipFilterRegistrationBean() {
        var registrationBean = new FilterRegistrationBean<IpFilter>();
        registrationBean.setFilter(ipFilter);
        registrationBean.setOrder(0);
        //registrationBean.setUrlPatterns(); //executa o filter em uma determinada url ou rota
        return registrationBean;
    }
}