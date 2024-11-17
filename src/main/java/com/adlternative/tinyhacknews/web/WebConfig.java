package com.adlternative.tinyhacknews.web;

import static org.zalando.logbook.core.HeaderFilters.removeHeaders;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.zalando.logbook.Logbook;
import org.zalando.logbook.Sink;
import org.zalando.logbook.Strategy;
import org.zalando.logbook.core.DefaultHttpLogWriter;
import org.zalando.logbook.core.DefaultSink;
import org.zalando.logbook.core.DefaultStrategy;
import org.zalando.logbook.core.ResponseFilters;
import org.zalando.logbook.json.JsonHttpLogFormatter;

@Configuration
public class WebConfig implements WebMvcConfigurer {

  @Bean
  public Logbook logbook() {
    final Strategy strategy = new DefaultStrategy();
    final Sink sink = new DefaultSink(new JsonHttpLogFormatter(), new DefaultHttpLogWriter());

    return Logbook.builder()
        .responseFilter(ResponseFilters.replaceBody(body -> ""))
        .headerFilter(removeHeaders((key, value) -> true))
        .strategy(strategy)
        .sink(sink)
        .build();
  }
}