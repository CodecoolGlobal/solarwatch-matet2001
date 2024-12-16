package com.codecool.solarwatchmvc.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class SolarWatchControllerAdvice {
//    @ExceptionHandler(MissingServletRequestParameterException.class)
//    public String handleMissingServletRequestParameterException(MissingServletRequestParameterException e, Model model) {
//        String errorMessage = "Please provide a city name";
//        model.addAttribute("error", errorMessage);
//        return "error";
//    }
//
//    @ExceptionHandler(RuntimeException.class)
//    public String handleRunTimeException(RuntimeException e, Model model) {
//        model.addAttribute("error", e.getMessage());
//        return "error";
//    }
}
