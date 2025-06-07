package br.com.jeftavares.jbank.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class IpFilter extends HttpFilter {

    @Override
    protected void doFilter(HttpServletRequest request,
                            HttpServletResponse response,
                            FilterChain chain) throws IOException, ServletException {

        var ipAddress = request.getRemoteAddr();
        response.setHeader("x-user-ip", ipAddress);
        request.setAttribute("x-user-ip", ipAddress);

        //response.setStatus(503); se for usar o status 503, não pode passar para o próximo filtro. Comentar a linha do chain.DoFilter

        //Ate aqui é código antes do filtro passar a request
        chain.doFilter(request, response);
        //Aqui é o código depois do filtro passar a request

    }
}