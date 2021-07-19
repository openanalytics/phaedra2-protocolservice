package eu.openanalytics.phaedra.protocolservice.config;

import com.moesif.servlet.MoesifFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.servlet.Filter;

public class MoesifConfig extends WebMvcConfigurerAdapter {

    @Bean
    public Filter moesifFilter() {
        return new MoesifFilter("eyJhcHAiOiI5MzoxNzMiLCJ2ZXIiOiIyLjAiLCJvcmciOiIyNjI6MjAzIiwiaWF0IjoxNjIyNTA1NjAwfQ.J8PbwotTIwRyOQwY6EAoMNO6Jm0s9nctEBKzND_TTMk");
    }

}
