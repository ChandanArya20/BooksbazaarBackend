package in.ineuron.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
//    @ResponseBody
    public ResponseEntity<ExceptionDetails> handleException(Exception ex) {
    	
    	System.out.println(ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ExceptionDetails(LocalDateTime.now(), ex.getMessage(), "Something went wrong..."));
    }
}






