package org.com.openmarket.items.application.config.constants;

//todo: move to env vars
public class QueueConstants {
    public static class UserItem {
        public final static String USER_DATA_ITEM_QUEUE = "user-data-item";
        public final static String USER_DATA_ITEM_TOPIC_EXCHANGE = "user-data-item-topic";
        public final static String USER_DATA_ITEM_ROUTING_KEY = "user.data.item.rk";
    }

    public static class Dlq {
        public final static String DEAD_LETTER_QUEUE = "open-market-dlq";
        public final static String DEAD_LETTER_QUEUE_TOPIC_EXCHANGE = "open-market-dlq-topic";
        public final static String DEAD_LETTER_QUEUE_ROUTING_KEY = "open.market.dlq.rk";
    }
}
