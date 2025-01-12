package org.com.openmarket.market.application.config.constants;

//todo: move to env vars
public class QueueConstants {
    public final static String MARKET_QUEUE = "market-queue";
    public final static String MARKET_TOPIC_EXCHANGE = "market-topic";
    public final static String MARKET_ROUTING_KEY = "market.rk";

    public static class Wallet {
        public final static String WALLET_QUEUE = "wallet-queue";
    }

    public static class Item {
        public final static String ITEM_QUEUE = "item-queue";
    }

    public static class Dlq {
        public final static String DEAD_LETTER_QUEUE = "open-market-dlq";
        public final static String DEAD_LETTER_QUEUE_TOPIC_EXCHANGE = "open-market-dlq-topic";
        public final static String DEAD_LETTER_QUEUE_ROUTING_KEY = "open.market.dlq.rk";
    }
}
