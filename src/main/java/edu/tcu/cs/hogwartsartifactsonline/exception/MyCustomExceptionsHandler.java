package edu.tcu.cs.hogwartsartifactsonline.exception;

import edu.tcu.cs.hogwartsartifactsonline.domain.Result;
import edu.tcu.cs.hogwartsartifactsonline.domain.StatusCode;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

// @ControllerAdvice will only kick in if an exception is thrown from within a controller method
// so will inevitably not catch all things
@ControllerAdvice
public class MyCustomExceptionsHandler {
    @ResponseBody
    @ExceptionHandler(ObjectNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    Result handleObjectNotFoundHandler(ObjectNotFoundException ex) {
        return new Result(false, StatusCode.NOT_FOUND, ex.getMessage());
    }

    /**
     * This handles invalid inputs
     * @param ex
     * @return
     */
    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    Result handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<ObjectError> errors = ex.getBindingResult().getAllErrors();
        Map<String, String> map = new HashMap<>(errors.size());
        errors.forEach((error) -> {
            String key = ((FieldError) error).getField();
            String val = error.getDefaultMessage();
            map.put(key, val);
        });
        return new Result(false, StatusCode.INVALID_ARGUMENT, "Provided arguments are invalid, see data for details.", map);
    }

    @ResponseBody
    @ExceptionHandler({UsernameNotFoundException.class, BadCredentialsException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Result handleAuthenticationException(Exception ex) {
        return new Result(false, StatusCode.UNAUTHORIZED, "username or password is incorrect.", ex.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(InvalidBearerTokenException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Result handleInvalidBearerTokenException(InvalidBearerTokenException ex) {
        System.out.println(ex.getError());
        return new Result(false, StatusCode.UNAUTHORIZED, "The access token provided is expired, revoked, malformed, or\n" +
                "         invalid for other reasons.", ex.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public Result handleAccessDeniedException(AccessDeniedException ex) {
        return new Result(false, StatusCode.FORBIDDEN, "No permission.", ex.getMessage());
    }

    /**
     * Fallback handles any unhandled exceptions.
     */
    @ResponseBody
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result handleOtherExceptions(Exception ex) {
        System.out.println(ex.getClass());
        return new Result(false, StatusCode.INTERNAL_SERVER_ERROR, "A server internal error occurs.", ex.getMessage());
    }
}
