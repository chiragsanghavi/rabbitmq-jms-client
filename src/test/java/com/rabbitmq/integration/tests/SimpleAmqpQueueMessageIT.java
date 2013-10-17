/* Copyright (c) 2013 GoPivotal, Inc. All rights reserved. */
package com.rabbitmq.integration.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import javax.jms.DeliveryMode;
import javax.jms.Queue;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.junit.Test;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.GetResponse;
import com.rabbitmq.jms.admin.RMQDestination;

/**
 * Integration test for simple point-to-point messaging with target AMQP queue.
 */
public class SimpleAmqpQueueMessageIT extends AbstractAmqpITQueue {

    private static final String QUEUE_NAME = "test.queue."+SimpleAmqpQueueMessageIT.class.getCanonicalName();
    private static final String MESSAGE = "Hello " + SimpleAmqpQueueMessageIT.class.getName();

    @Test
    public void testSendAndReceiveTextMessage() throws Exception {

        Channel channel = rabbitConn.createChannel();
        channel.queueDeclare(QUEUE_NAME,
                             false, // durable
                             true,  // exclusive
                             true,  // autoDelete
                             null   // options
                             );

        queueConn.start();
        QueueSession queueSession = queueConn.createQueueSession(false, Session.DUPS_OK_ACKNOWLEDGE);
        Queue queue = (Queue) new RMQDestination(QUEUE_NAME, "", QUEUE_NAME, null);  // write-only AMQP-mapped queue
        QueueSender queueSender = queueSession.createSender(queue);
        queueSender.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
        TextMessage message = queueSession.createTextMessage(MESSAGE);
        queueSender.send(message);
        queueConn.close();

        GetResponse response = channel.basicGet(QUEUE_NAME, false);
        assertNotNull(response);

        byte[] body = response.getBody();
        assertNotNull(body);

        String messageText = new String(body, "UTF-8");

        assertEquals("Message received not identical to message sent", MESSAGE,  messageText);
    }
}
