package br.com.jilherme.todolist.filter;

import java.io.IOException;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import at.favre.lib.crypto.bcrypt.BCrypt;
import br.com.jilherme.todolist.user.IUserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// Annotation para o Spring gerenciar
// Componente é a classe mais genérica do Spring
@Component
public class FilterTaskAuth extends OncePerRequestFilter {

  @Autowired
  private IUserRepository userRepository;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    var servletPath = request.getServletPath();
    if (servletPath.startsWith("/tasks/")) {
      // Pegar a autenticação (usuário e senha)
      var authorization = request.getHeader("Authorization");

      // deixa somente o base64
      var authEncoded = authorization.substring("Basic".length()).trim();

      byte[] authDecode = Base64.getDecoder().decode(authEncoded);

      var authString = new String(authDecode);

      String[] credentials = authString.split(":");
      var username = credentials[0];
      var password = credentials[1];

      // Validar se o usuário existe
      var user = this.userRepository.findByUsername(username);

      if (user == null) {
        response.sendError(401);
      } else {
        // Validar se a senha está correta
        var passwordVerify = BCrypt.verifyer().verify(password.toCharArray(), user.getPassword());
        if (passwordVerify.verified) {
          // Continuar a requisição
          request.setAttribute("idUser", user.getId());
          filterChain.doFilter(request, response);
        } else {
          response.sendError(401);
        }
      }

    } else {
      filterChain.doFilter(request, response);
    }

  }

}
