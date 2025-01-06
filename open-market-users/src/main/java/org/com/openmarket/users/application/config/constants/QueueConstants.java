package org.com.openmarket.users.application.config.constants;

//todo: move to env vars
public class QueueConstants {
    public static class User {
        public final static String USER_QUEUE = "user-queue";
    }

    public static class ItemService {
        public final static String ITEM_QUEUE = "item-queue";
    }

    public static class WalletService {
        public final static String WALLET_QUEUE = "wallet-queue";
    }

    public static class MarketService {
        public final static String MARKET_QUEUE = "market-queue";
    }

    public static class Dlq {
        public final static String DEAD_LETTER_QUEUE = "open-market-dlq";
        public final static String DEAD_LETTER_QUEUE_TOPIC_EXCHANGE = "open-market-dlq-topic";
        public final static String DEAD_LETTER_QUEUE_ROUTING_KEY = "open.market.dlq.rk";
    }
}
