package com.adlternative.tinyhacknews.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hello")
public class HelloController {

  @GetMapping
  public String hello() {
    return "Hello, Spring Boot!";
  }

  @PostMapping
  public String echo(@RequestBody String body) {
    return body;
  }
}
