package com.banking.mock.banking.mappers.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {"com.banking.mock.banking.db.converters"})
public class MapperTestConfiguration {
}
