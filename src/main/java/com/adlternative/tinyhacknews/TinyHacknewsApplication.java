package com.adlternative.tinyhacknews;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableConfigurationProperties
@EnableScheduling
// @MapperScan("com.adlternative.tinyhacknews.mapper")
public class TinyHacknewsApplication {

  public static void main(String[] args) {
    SpringApplication.run(TinyHacknewsApplication.class, args);
  }
}
