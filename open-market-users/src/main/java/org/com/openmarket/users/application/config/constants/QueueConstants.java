package org.com.openmarket.users.application.config.constants;

public class QueueConstants {
    public static class User {
        public final static String USER_CREATED_QUEUE = "user-created";
        public final static String USER_CREATED_TOPIC_EXCHANGE = "user-created-topic";
        public final static String USER_CREATED_ROUTING_KEY = "user.created.rk";

        public final static String USER_UPDATED_QUEUE = "user-updated";
        public final static String USER_UPDATED_TOPIC_EXCHANGE = "user-updated-topic";
        public final static String USER_UPDATED_ROUTING_KEY = "user.updated.rk";
    }
}
