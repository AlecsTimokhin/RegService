package com.myorg.mainpack.exception;

import com.myorg.mainpack.dto.RestResponce;
import com.myorg.mainpack.exception.types.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import javax.servlet.http.HttpServletRequest;


@RestControllerAdvice(annotations = RestController.class)
@Order(Ordered.HIGHEST_PRECEDENCE + 1)    // Обязательно, чтобы не конфликтовало с @ControllerAdvice(annotations = Controller.class) в ControllerExceptionHandler
public class RestControllerExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(RestControllerExceptionHandler.class);



    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    @Order(Ordered.HIGHEST_PRECEDENCE + 6)
    @ExceptionHandler(value = NoAccessException.class)
    public RestResponce forbidden403(HttpServletRequest request, NoAccessException ex){
        log.warn("Ошибка доступа. " + "url:( " + request.getRequestURL() + " ). " +
                ex.getRestResponse() + ex.toString());
        return ex.getRestResponse();
    }


    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @Order(Ordered.HIGHEST_PRECEDENCE + 7)
    @ExceptionHandler(value = NotFoundException.class)
    public RestResponce notFound404(HttpServletRequest request, NotFoundException ex){
        log.warn("Ресурс не найден. " + "url:( " + request.getRequestURL() + " ). " +
                ex.getRestResponse() + ex.toString());
        return ex.getRestResponse();
    }


    @ResponseStatus(value = HttpStatus.CONFLICT)
    @Order(Ordered.HIGHEST_PRECEDENCE + 8)
    @ExceptionHandler(value = ConflictException.class)
    public RestResponce conflict409(HttpServletRequest request, ConflictException ex){
        log.warn("Конфликт при записи данных в БД. " + "url:( " + request.getRequestURL() + " ). " +
                ex.getRestResponse() + ex.toString());
        return ex.getRestResponse();
    }


    @ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
    @Order(Ordered.HIGHEST_PRECEDENCE + 9)
    @ExceptionHandler(value = BadValidateException.class)
    public RestResponce badValidate422(HttpServletRequest request, BadValidateException ex){
        log.warn("Ошибка валидации. " + "url:( " + request.getRequestURL() + " ). " +
                ex.getRestResponse() + ex.toString());
        return ex.getRestResponse();
    }


    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @Order(Ordered.HIGHEST_PRECEDENCE + 11)
    @ExceptionHandler(value = Exception.class)
    public RestResponce internalServerError500(HttpServletRequest request, Exception ex){
        log.error("Ошибка сервера. " + "url:( " + request.getRequestURL() + " ). " + ex.toString());
        return new RestResponce("Error 500. Код ошибки: 2", "error", 2);
    }



}
