package com.swiftpay.config;

import com.swiftpay.appUtils.FormatterUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorViewResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

@Component
public class WebMvcConfig implements ErrorViewResolver {

    @Override
    public ModelAndView resolveErrorView(HttpServletRequest request, HttpStatus status, Map<String, Object> model) {
        if(status == HttpStatus.NOT_FOUND)
            return new ModelAndView("redirect:/?notFound");

        if(status == HttpStatus.FORBIDDEN)
            return new ModelAndView("redirect:/?forbidden");

        return null;
    }

    @Bean
    public FormatterUtils formatterUtils() {
        return new FormatterUtils();
    }
}
