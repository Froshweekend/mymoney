package br.com.ivanfsilva.mymoneyapi;

import br.com.ivanfsilva.mymoneyapi.config.property.MyMoneyApiProperty;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(MyMoneyApiProperty.class)
public class MymoneyApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(MymoneyApiApplication.class, args);
    }

}
