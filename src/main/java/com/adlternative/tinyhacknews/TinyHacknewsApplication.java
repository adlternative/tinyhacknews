package com.adlternative.tinyhacknews;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
// @MapperScan("com.adlternative.tinyhacknews.mapper")
public class TinyHacknewsApplication {

  public static void main(String[] args) {
    SpringApplication.run(TinyHacknewsApplication.class, args);
  }
}
