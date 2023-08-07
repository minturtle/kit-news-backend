package com.likelion.news.controller;


import com.likelion.news.dto.response.ErrorResponse;
import com.likelion.news.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalControllerAdvice {


    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler({ClientException.class, IllegalArgumentException.class})
    public ErrorResponse error400(RuntimeException e){
        return new ErrorResponse(e);
    }


    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    @ExceptionHandler({UnAuthorizedException.class})
    public ErrorResponse error401(UnAuthorizedException e){
        return new ErrorResponse(e);
    }


    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    @ExceptionHandler({ForbiddenException.class})
    public ErrorResponse error403(ForbiddenException e){
        return new ErrorResponse(e);
    }


    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ExceptionHandler({NotFoundException.class})
    public ErrorResponse error404(NotFoundException e){
        return new ErrorResponse(e);
    }



    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({Exception.class, InternalServerException.class})
    public ErrorResponse error500(Exception e){
        return new ErrorResponse(e);
    }

}
