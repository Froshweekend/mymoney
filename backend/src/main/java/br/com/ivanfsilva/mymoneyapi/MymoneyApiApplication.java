package br.com.ivanfsilva.mymoneyapi;

import br.com.ivanfsilva.mymoneyapi.config.property.MyMoneyApiProperty;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
@EnableConfigurationProperties(MyMoneyApiProperty.class)
public class MymoneyApiApplication {

    private static ApplicationContext APPLICATION_CONTEXT;

    public static void main(String[] args) {
        SpringApplication.run(MymoneyApiApplication.class, args);
    }

    public static <T> T getBean(Class<T> type) {
        return APPLICATION_CONTEXT.getBean(type);
    }

}
