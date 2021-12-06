package com.example.MyBookShopApp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.HashMap;

@RestControllerAdvice
public class ExceptionResolver {

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public HashMap<String, String> handleNoHandlerFound(NoHandlerFoundException e) {
        HashMap<String, String> response = new HashMap<>();
        response.put("status", "fail");
        response.put("message", e.getLocalizedMessage());
        response.put("class error", e.getClass().getName());
        response.put("http status", String.valueOf(HttpStatus.NOT_FOUND));
        return response;
    }

    @ExceptionHandler({
            HttpMessageNotReadableException.class,
            MethodArgumentTypeMismatchException.class,
            HttpMediaTypeNotSupportedException.class,
            RecentBookException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public HashMap<String, String> badRequest(Exception ex, HttpServletRequest request) throws ParseException {
        HashMap<String, String> response = new HashMap<>();
        String error = null;
        RecentBookException recentBookException = new RecentBookException(request);
        if (request.getRequestURI().contains("/books/recent"))
            error = recentBookException.checkAll();
        if(error == null)
            error = ex.getMessage();
        response.put("result", "true");
        response.put("error", error);
        return response;
    }

}