package org.com.openmarket.wallet.application.config.constants;

//todo: move to env vars
public class QueueConstants {
    public final static String WALLET_QUEUE = "wallet-queue";

    public static class Dlq {
        public final static String DEAD_LETTER_QUEUE = "open-market-dlq";
        public final static String DEAD_LETTER_QUEUE_TOPIC_EXCHANGE = "open-market-dlq-topic";
        public final static String DEAD_LETTER_QUEUE_ROUTING_KEY = "open.market.dlq.rk";
    }
}
