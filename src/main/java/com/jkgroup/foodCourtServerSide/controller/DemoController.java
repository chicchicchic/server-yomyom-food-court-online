package com.jkgroup.foodCourtServerSide.controller;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/hello")
public class DemoController {
    @RequestMapping(value = "",method = RequestMethod.GET)
    public String hello() {
        return "Hello, World!";
    }

}
