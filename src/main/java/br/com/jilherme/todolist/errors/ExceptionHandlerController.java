package br.com.jilherme.todolist.errors;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

// É uma anotação que permite que você escreva classes globais
// que podem ser aplicadas a todo o aplicativo ou a conjuntos de controladores 
// por meio de seletores de tipo.
@ControllerAdvice
public class ExceptionHandlerController {

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<String> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
    // Retorna a mensagem que foi passada no TaskModel
    return ResponseEntity.badRequest().body(ex.getMostSpecificCause().getMessage());
  }
}
