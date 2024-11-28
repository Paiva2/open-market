package org.com.openmarket.users.application.config.constants;

//todo: move to env vars
public class QueueConstants {
    public static class User {
        public final static String USER_DATA_QUEUE = "user-data";
        public final static String USER_DATA_TOPIC_EXCHANGE = "user-data-topic";
        public final static String USER_DATA_ROUTING_KEY = "user.data.rk";
    }
}
