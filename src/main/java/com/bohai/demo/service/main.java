package com.bohai.demo.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;

@SpringBootApplication
@RestController
public class main {

    public static void main(String[] args) {
        SpringApplication.run(main.class, args);
    }

    @RequestMapping(value = "/",method = RequestMethod.GET)
    public String sayHello()
    {
        return "Hello World." +
                "This is an example of a spring docker\n";
    }
}
