package org.letuslearn.springkubedemo.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

@Configuration
@RefreshScope
@Data
public class GreetingsConfiguration {

    @Value("${message:Hello Welcome To Static Message}")
    private String message;
}
