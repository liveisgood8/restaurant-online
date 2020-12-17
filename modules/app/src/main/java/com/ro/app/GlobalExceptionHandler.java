package com.ro.app;

import com.ro.core.exceptions.RoIllegalArgumentException;
import com.ro.core.exceptions.RoIllegalStateException;
import com.ro.menu.validation.ApiError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
  private static final Logger logger =
      LoggerFactory.getLogger(ResponseEntityExceptionHandler.class);

  @ExceptionHandler({ RuntimeException.class })
  public ResponseEntity<Object> handleRuntimeException(RuntimeException ex, WebRequest request) {
    logger.error("Runtime exception in controller:", ex);
    return new ResponseEntity<>(new ApiError("Внутренняя ошибка сервера"), HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @Override
  public ResponseEntity<Object> handleBindException(BindException ex,
                                                    HttpHeaders headers,
                                                    HttpStatus status,
                                                    WebRequest request) {
    List<String> errors = ex.getBindingResult()
        .getFieldErrors()
        .stream()
        .map(x -> String.format("Поле %s %s", x.getField(), x.getDefaultMessage()))
        .collect(Collectors.toList());

    String message = String.join("\n", errors);
    return new ResponseEntity<>(new ApiError(message), headers, status);
  }

  @Override
  public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                             HttpHeaders headers,
                                                             HttpStatus status,
                                                             WebRequest request) {
    List<String> errors = ex.getBindingResult()
        .getFieldErrors()
        .stream()
        .map(x -> String.format("Поле %s %s", x.getField(), x.getDefaultMessage()))
        .collect(Collectors.toList());

    String message = String.join("\n", errors);
    return new ResponseEntity<>(new ApiError(message), headers, status);
  }

  @ExceptionHandler(value = { RoIllegalStateException.class, RoIllegalArgumentException.class })
  protected ResponseEntity<Object> handleIllegal(RuntimeException ex, WebRequest request) {
    return new ResponseEntity<>(new ApiError(ex.getMessage()), new HttpHeaders(), HttpStatus.BAD_REQUEST);
  }

  @Override
  protected ResponseEntity<Object> handleExceptionInternal(Exception ex,
                                                           Object body,
                                                           HttpHeaders headers,
                                                           HttpStatus status,
                                                           WebRequest request) {
    logger.error("Unhandled error in controller:", ex);
    return new ResponseEntity<>(new ApiError("Необработанное исключения сервера"), headers, status);
  }
}
