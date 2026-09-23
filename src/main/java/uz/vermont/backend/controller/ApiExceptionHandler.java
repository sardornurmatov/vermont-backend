package uz.vermont.backend.controller;
import org.springframework.http.*;import org.springframework.web.bind.MethodArgumentNotValidException;import org.springframework.web.bind.annotation.*;import java.util.*;
@RestControllerAdvice
public class ApiExceptionHandler {
 @ExceptionHandler(MethodArgumentNotValidException.class) ResponseEntity<?> validation(MethodArgumentNotValidException e){String message=e.getBindingResult().getFieldErrors().stream().findFirst().map(x->x.getField()+" noto‘g‘ri").orElse("Ma’lumot noto‘g‘ri");return ResponseEntity.badRequest().body(Map.of("error",message));}
 @ExceptionHandler(NoSuchElementException.class) ResponseEntity<?> missing(){return ResponseEntity.status(404).body(Map.of("error","Ma’lumot topilmadi"));}
 @ExceptionHandler({IllegalArgumentException.class,IllegalStateException.class}) ResponseEntity<?> bad(RuntimeException e){return ResponseEntity.badRequest().body(Map.of("error",e.getMessage()));}
}
