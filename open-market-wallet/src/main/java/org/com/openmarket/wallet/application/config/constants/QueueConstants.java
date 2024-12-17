package org.com.openmarket.wallet.application.config.constants;

//todo: move to env vars
public class QueueConstants {
    public static class Wallet {
        public final static String WALLET_DATA = "wallet-data";
    }

    public static class UserWallet {
        public final static String USER_DATA_WALLET_QUEUE = "user-data-wallet";
        public final static String USER_DATA_WALLET_TOPIC_EXCHANGE = "user-data-wallet-topic";
        public final static String USER_DATA_WALLET_ROUTING_KEY = "user.data.wallet.rk";
    }

    public static class Dlq {
        public final static String DEAD_LETTER_QUEUE = "open-market-dlq";
        public final static String DEAD_LETTER_QUEUE_TOPIC_EXCHANGE = "open-market-dlq-topic";
        public final static String DEAD_LETTER_QUEUE_ROUTING_KEY = "open.market.dlq.rk";
    }
}
