package org.acme.app.service.sqs;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class SQSService {

    public static final Logger LOG = Logger.getLogger(SQSService.class);

    @ConfigProperty(name = "aws.region")
    String region;
    @ConfigProperty(name = "aws.access.key")
    String accessKey;
    @ConfigProperty(name = "aws.secret.key")
    String secretKey;


    SqsClient sqsClient;
    LogListener listener;

    @Inject
    public SQSService(SqsClient client, LogListener listener){
        this.sqsClient = client;
        this.listener = listener;
    }

    @PostConstruct
    public void init(){
        listener.init();
        createQueueIfNotExists();
    }

    public void createQueueIfNotExists() {
        for(QueueEnum qEnum : QueueEnum.values()) {
            try{
                GetQueueUrlResponse queueUrlResponse = sqsClient.getQueueUrl(GetQueueUrlRequest.builder().queueName(qEnum.getQueueName()).build());
                String queueUrl = queueUrlResponse.queueUrl();
                LOG.info("Get Queue: "+ qEnum.getQueueName() + " url:" + queueUrl);
                qEnum.setQueueUrl(queueUrl);
            }catch (QueueDoesNotExistException e) {
                CreateQueueRequest build = CreateQueueRequest.builder()
                        .queueName(qEnum.getQueueName())
                        .attributes(Map.of(QueueAttributeName.VISIBILITY_TIMEOUT, "30"))
                        .build();
                CreateQueueResponse createQueueResponse = sqsClient.createQueue(build);
                String queueUrl = createQueueResponse.queueUrl();
                LOG.info("Create Queue: "+ qEnum.getQueueName() + " url:" + queueUrl);
                qEnum.setQueueUrl(queueUrl);
            }

        }
    }

    public List<Message> receiveMessages(QueueEnum queueEnum) {
        ReceiveMessageRequest receiveRequest = ReceiveMessageRequest.builder()
                .queueUrl(queueEnum.getQueueUrl())
                .maxNumberOfMessages(10)
                .build();
        ReceiveMessageResponse receiveMessageResponse = sqsClient.receiveMessage(receiveRequest);
        return receiveMessageResponse.messages();
    }

    public String sendMessage(QueueEnum queueEnum, String message) {
        SendMessageRequest sendMessageRequest = SendMessageRequest.builder()
                .queueUrl(queueEnum.getQueueUrl())
                .messageBody(message)
                .build();
        SendMessageResponse sendMessageResponse = sqsClient.sendMessage(sendMessageRequest);
        return sendMessageResponse.messageId();
    }

    public void deleteMessage(QueueEnum queueEnum, String receiptHandle) {
        DeleteMessageRequest deleteMessageRequest = DeleteMessageRequest.builder().queueUrl(queueEnum.getQueueUrl()).receiptHandle(receiptHandle).build();
        sqsClient.deleteMessage(deleteMessageRequest);
    }

    public void LOG(String msg){
        sendMessage(QueueEnum.LogQueue, LocalDateTime.now()+": "+ msg);
    }
}
