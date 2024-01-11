package org.unibl.etf.advices;

import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.unibl.etf.exceptions.AccountBlockedException;
import org.unibl.etf.exceptions.AlreadyExistsException;
import org.unibl.etf.exceptions.NotApprovedException;
import org.unibl.etf.exceptions.UnauthorizedException;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler{

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationExceptions(MethodArgumentNotValidException exception) {
        Map<String, String> errors = new HashMap<>();
        exception.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<Object> handleUnauthorizedException(UnauthorizedException e)
    {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
    }
   @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<Object> handleExpiredJwtException(ExpiredJwtException e)
    {
        return new ResponseEntity<>(e.getMessage(),HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AlreadyExistsException.class)
    public ResponseEntity<Object> handleAlreadyExistsException(AlreadyExistsException e)
    {
        return new ResponseEntity<>(e.getMessage(),HttpStatus.CONFLICT);
    }

    @ExceptionHandler(NotApprovedException.class)
    public ResponseEntity<Object> handleNotApprovedException(NotApprovedException e)
    {
        return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_ACCEPTABLE);// 406-NIJE VERIFIKOVAN
    }//401 nepostojeci klijent ili username

    @ExceptionHandler(AccountBlockedException.class)
    public ResponseEntity<Object> handleNotApprovedException(AccountBlockedException e)
    {
        return new ResponseEntity<>(e.getMessage(),HttpStatus.FORBIDDEN);
    }


}