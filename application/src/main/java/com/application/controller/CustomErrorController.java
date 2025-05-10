//package com.application.controller;
//
//import org.springframework.boot.web.error.ErrorAttributeOptions;
//import org.springframework.boot.web.servlet.error.ErrorController;
//import org.springframework.http.HttpStatus;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;
//
//import jakarta.servlet.http.HttpServletRequest;
//
//@Controller
//public class CustomErrorController implements ErrorController {
//
//    @RequestMapping("/error")
//    public String handleError(HttpServletRequest request) {
//        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
//        if (statusCode != null) {
//            switch (statusCode) {
//                case 403:
//                    return "denied";
//                case 404:
//                    return "notfound";
//                case 500:
//                    return "error";
//                default:
//                    return "error";
//            }
//        }
//        return "error";
//    }
//}
