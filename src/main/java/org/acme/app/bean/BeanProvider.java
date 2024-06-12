package org.acme.app.bean;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.Produces;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;

@ApplicationScoped
public class BeanProvider {

    @ConfigProperty(name = "aws.region")
    String region;

    @Produces
    @ApplicationScoped
    public SqsClient createSQSClient(){
        return SqsClient.builder().region(Region.of(this.region)).build();
    }
}
