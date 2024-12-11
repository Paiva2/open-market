package org.com.openmarket.users.application.config.constants;

//todo: move to env vars
public class QueueConstants {
    public static class User {
        public final static String USER_DATA_QUEUE = "user-data";
        public final static String USER_DATA_TOPIC_EXCHANGE = "user-data-topic";
        public final static String USER_DATA_ROUTING_KEY = "user.data.rk";
    }

    public static class UserItem {
        public final static String USER_DATA_ITEM_QUEUE = "user-data-item";
        public final static String USER_DATA_ITEM_TOPIC_EXCHANGE = "user-data-item-topic";
        public final static String USER_DATA_ITEM_ROUTING_KEY = "user.data.item.rk";
    }

    public static class UserWallet {
        public final static String USER_DATA_WALLET_QUEUE = "user-data-wallet";
        public final static String USER_DATA_WALLET_TOPIC_EXCHANGE = "user-data-wallet-topic";
        public final static String USER_DATA_WALLET_ROUTING_KEY = "user.data.wallet.rk";
    }
}
