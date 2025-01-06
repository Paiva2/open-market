package org.com.openmarket.items.application.config.constants;

//todo: move to env vars
public class QueueConstants {
    public final static String ITEM_QUEUE = "item-queue";

    public static class Market {
        public final static String MARKET_QUEUE = "market-queue";
    }

    public static class Dlq {
        public final static String DEAD_LETTER_QUEUE = "open-market-dlq";
        public final static String DEAD_LETTER_QUEUE_TOPIC_EXCHANGE = "open-market-dlq-topic";
        public final static String DEAD_LETTER_QUEUE_ROUTING_KEY = "open.market.dlq.rk";
    }
}
