package eu.openanalytics.phaedra.protocolservice.client.config;


import eu.openanalytics.phaedra.protocolservice.client.ProtocolServiceClient;
import eu.openanalytics.phaedra.protocolservice.client.impl.HttpProtocolServiceClient;
import eu.openanalytics.phaedra.util.PhaedraRestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProtocolServiceClientAutoConfiguration {

    @Bean
    public ProtocolServiceClient protocolServiceClient(PhaedraRestTemplate phaedraRestTemplate) {
        return new HttpProtocolServiceClient(phaedraRestTemplate);
    }

}
