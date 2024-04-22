package com.adlternative.tinyhacknews;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class TinyHacknewsApplication {

  public static void main(String[] args) {
    SpringApplication.run(TinyHacknewsApplication.class, args);
  }
}
