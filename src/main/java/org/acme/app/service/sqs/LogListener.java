package org.acme.app.service.sqs;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;
import java.util.concurrent.CompletableFuture;

@ApplicationScoped
public class LogListener {

    public static final Logger LOG = Logger.getLogger(LogListener.class);

    private SQSService sqsService;

    @Inject
    public LogListener(SQSService sqsService){
        this.sqsService = sqsService;
    }

    public void init() {
        LOG.info("Subscribed to Log Queue");
        CompletableFuture.runAsync(() -> consume());
    }

    public void consume() {
        while(true){
            // confluentbit
            try {
                sqsService.receiveMessages(QueueEnum.LogQueue).stream()
                        .peek(msg -> System.out.println("Log: " + msg.body()))
                        .forEach(msg -> sqsService.deleteMessage(QueueEnum.LogQueue, msg.receiptHandle()));
                Thread.sleep(500);
            } catch (InterruptedException e) {
                break;
            }
        }
    }
}
