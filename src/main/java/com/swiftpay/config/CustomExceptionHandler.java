package com.swiftpay.config;

import com.swiftpay.enums.AlertMessageTag;
import com.swiftpay.exception.IllegalOperationException;
import com.swiftpay.exception.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler({ResourceNotFoundException.class, IllegalOperationException.class})
    public String handleResourceAndIllegalOperationException(HttpServletRequest request, Exception ex, RedirectAttributes redirectAttributes) {

        if(isPostRequest(request)) {
            return redirect(request.getHeader("Referer"), AlertMessageTag.ERROR.toString(), ex, redirectAttributes);
        }

        return redirect("/", "resourceNotFound", ex, redirectAttributes);
    }

    private boolean isPostRequest(HttpServletRequest request) {
        return request.getMethod().equalsIgnoreCase("POST");
    }

    private static String redirect(String url, String flashAttribute, Exception ex, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute(flashAttribute, ex.getMessage());
        return "redirect:" + url;
    }
}
