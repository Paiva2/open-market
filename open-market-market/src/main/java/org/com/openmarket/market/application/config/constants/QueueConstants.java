package org.com.openmarket.market.application.config.constants;

//todo: move to env vars
public class QueueConstants {
    public final static String MARKET_QUEUE = "market-queue";
    public final static String MARKET_TOPIC_EXCHANGE = "market-topic";
    public final static String MARKET_ROUTING_KEY = "market.rk";

    public static class MarketUserData {
        public final static String USER_DATA_MARKET_QUEUE = "user-data-market";
        public final static String USER_DATA_MARKET_TOPIC_EXCHANGE = "user-data-market-topic";
        public final static String USER_DATA_MARKET_ROUTING_KEY = "user.data.market.rk";
    }

    public static class Dlq {
        public final static String DEAD_LETTER_QUEUE = "open-market-dlq";
        public final static String DEAD_LETTER_QUEUE_TOPIC_EXCHANGE = "open-market-dlq-topic";
        public final static String DEAD_LETTER_QUEUE_ROUTING_KEY = "open.market.dlq.rk";
    }
}
