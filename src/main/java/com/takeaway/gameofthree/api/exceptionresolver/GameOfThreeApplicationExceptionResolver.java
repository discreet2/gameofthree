package com.takeaway.gameofthree.api.exceptionresolver;


import com.takeaway.gameofthree.api.endpoint.GameEndPoint;
import com.takeaway.gameofthree.api.endpoint.ServiceAvailabilityEndPoint;
import com.takeaway.gameofthree.exception.SecondPlayerNotAvailableException;
import com.takeaway.gameofthree.util.ErrorCode;
import com.takeaway.gameofthree.util.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ControllerAdvice(basePackageClasses = {GameEndPoint.class, ServiceAvailabilityEndPoint.class})
public class GameOfThreeApplicationExceptionResolver {


    @ResponseBody
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler({SecondPlayerNotAvailableException.class})
    public ErrorResponse handleSecondPlayerNotAvailableException(SecondPlayerNotAvailableException e) {
        log.error("Mandatory request param missing in body ", e);
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setReason(ErrorCode.SecondPlayerNotAvailable.getCode());
        errorResponse.setMessage(ErrorCode.SecondPlayerNotAvailable.getMessage());
        return errorResponse;
    }


    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ErrorResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("Mandatory request param missing in body ", e);
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setReason(ErrorCode.MandatoryRequestParamMissing.getCode());
        errorResponse.setMessage(ErrorCode.MandatoryRequestParamMissing.getMessage());
        return errorResponse;
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({Exception.class})
    public ErrorResponse handleException(Exception e) {
        log.error("Unknown error happened", e);
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setReason(ErrorCode.UnknownReason.getCode());
        errorResponse.setMessage(ErrorCode.UnknownReason.getMessage());
        return errorResponse;
    }
}
