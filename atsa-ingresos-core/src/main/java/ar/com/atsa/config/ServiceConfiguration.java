package ar.com.atsa.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import ar.com.atsa.services.impl.EchoService;

@Configuration
@EnableAsync
@EnableScheduling
@ComponentScan({"ar.com.atsa.services", "ar.com.atsa.commons.builder"})
@Import({JPAConfiguration.class})
 public class ServiceConfiguration {

    @Bean
    public EchoService createService() {
        return new EchoService();
    }

}
