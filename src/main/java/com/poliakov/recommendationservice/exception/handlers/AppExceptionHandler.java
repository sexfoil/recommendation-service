package com.poliakov.recommendationservice.exception.handlers;

import com.poliakov.recommendationservice.exception.InvalidDataException;
import com.poliakov.recommendationservice.exception.NoValuesException;
import com.poliakov.recommendationservice.exception.UnsupportedValueException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.IOException;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class AppExceptionHandler extends ResponseEntityExceptionHandler {

    private static final String INCORRECT_REQUEST = "INCORRECT_REQUEST";
    private static final String UNSUPPORTED_VALUE = "UNSUPPORTED_VALUE";
    private static final String IO_EXCEPTION = "CAN_NOT_READ_FILE";
    private static final String EMPTY_DATA = "NO_VALUES_IN_RANGE";
    private static final String BAD_CSV = "INVALID_CSV_FILE";


    @ExceptionHandler(DateTimeParseException.class)
    public final ResponseEntity<ErrorResponse> handleDateTimeParseException(DateTimeParseException ex, WebRequest request) {
        return new ResponseEntity<>(getErrorResponse(INCORRECT_REQUEST, ex), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IOException.class)
    public final ResponseEntity<ErrorResponse> handleIOException(IOException ex, WebRequest request) {
        return new ResponseEntity<>(getErrorResponse(IO_EXCEPTION, ex), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidDataException.class)
    public final ResponseEntity<ErrorResponse> handleInvalidDataException(InvalidDataException ex, WebRequest request) {
        return new ResponseEntity<>(getErrorResponse(BAD_CSV, ex), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UnsupportedValueException.class)
    public final ResponseEntity<ErrorResponse> handleUnsupportedValueException(UnsupportedValueException ex, WebRequest request) {
        return new ResponseEntity<>(getErrorResponse(UNSUPPORTED_VALUE, ex), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NoValuesException.class)
    public final ResponseEntity<ErrorResponse> handleNoValuesException(NoValuesException ex, WebRequest request) {
        return new ResponseEntity<>(getErrorResponse(EMPTY_DATA, ex), HttpStatus.NOT_FOUND);
    }

    private ErrorResponse getErrorResponse(String message, Throwable ex) {
        List<String> details = new ArrayList<>();
        details.add(ex.getLocalizedMessage());
        return new ErrorResponse(message, details);
    }
}
