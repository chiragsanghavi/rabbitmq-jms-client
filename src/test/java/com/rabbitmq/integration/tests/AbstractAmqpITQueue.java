/* Copyright (c) 2013 GoPivotal, Inc. All rights reserved. */
package com.rabbitmq.integration.tests;

import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;

import org.junit.After;
import org.junit.Before;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public abstract class AbstractAmqpITQueue {
    private QueueConnectionFactory connFactory;
    protected QueueConnection queueConn;

    private ConnectionFactory rabbitConnFactory = new ConnectionFactory();
    protected Connection rabbitConn;

    @Before
    public void setUp() throws Exception {
        this.connFactory = (QueueConnectionFactory) AbstractTestConnectionFactory.getTestConnectionFactory()
                .getConnectionFactory();
        this.queueConn = connFactory.createQueueConnection();

        this.rabbitConn = rabbitConnFactory.newConnection();

    }

    @After
    public void tearDown() throws Exception {
        if (this.queueConn != null) this.queueConn.close();
        if (this.rabbitConn != null) this.rabbitConn.close();
    }

}
