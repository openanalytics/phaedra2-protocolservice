package eu.openanalytics.phaedra.protocolservice.config;

import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;

//@Configuration
//@EnableWebFlux
public class CorsGlobalConfiguration implements WebFluxConfigurer {

//    @Override
    public void addCorsMappings(CorsRegistry corsRegistry) {
        corsRegistry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("")
                .maxAge(3600);
    }
}
