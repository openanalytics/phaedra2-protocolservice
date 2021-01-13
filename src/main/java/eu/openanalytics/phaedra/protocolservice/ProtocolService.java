package eu.openanalytics.phaedra.protocolservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ProtocolService {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(ProtocolService.class);
        app.run(args);
    }
}
