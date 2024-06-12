package org.acme.app.service.sqs;

public enum QueueEnum {
    SubscriptionQueue("subscription_queue"),
    LogQueue("log_queue");

    private String queueName;
    private String queueUrl;

    QueueEnum(String queueName) {
        this.queueName = queueName;
    }

    public String getQueueName() {
        return queueName;
    }

    public String getQueueUrl() {
        return queueUrl;
    }

    public void setQueueName(String queueName) {
        this.queueName = queueName;
    }

    public void setQueueUrl(String queueUrl) {
        this.queueUrl = queueUrl;
    }
}
