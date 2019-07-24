package com.aws.sns.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.aws.messaging.config.annotation.NotificationMessage;
import org.springframework.cloud.aws.messaging.config.annotation.NotificationSubject;
import org.springframework.cloud.aws.messaging.endpoint.NotificationStatus;
import org.springframework.cloud.aws.messaging.endpoint.annotation.NotificationMessageMapping;
import org.springframework.cloud.aws.messaging.endpoint.annotation.NotificationSubscriptionMapping;
import org.springframework.cloud.aws.messaging.endpoint.annotation.NotificationUnsubscribeConfirmationMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aws.sns.impl.SNSMessageSender;

@RestController
@RequestMapping("/topic-subscriber")
public class SNSEndpointController {

    private static final Logger logger = LoggerFactory.getLogger(SNSEndpointController.class);
    
    @Autowired
    private SNSMessageSender snsmessageSender;
    
    @GetMapping("/send")
    public void sendMsg() {
    	snsmessageSender.send("tricon", "Test message", "Testing");
    	System.out.println("Message sent");
    }

    @NotificationMessageMapping
    public void receiveNotification(@NotificationMessage String message, @NotificationSubject String subject) {
        logger.info("Received message: {}, having subject: {}", message, subject);
    }

    @NotificationUnsubscribeConfirmationMapping
    public void confirmSubscriptionMessage(NotificationStatus notificationStatus) {
        logger.info("Unsubscribed from Topic");
        notificationStatus.confirmSubscription();
    }

    @NotificationSubscriptionMapping
    public void confirmUnsubscribeMessage(NotificationStatus notificationStatus) {
        logger.info("Subscribed to Topic");
        notificationStatus.confirmSubscription();
    }
}