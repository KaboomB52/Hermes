package me.kaboom.hermes.thread;

import me.kaboom.hermes.Hermes;
import me.kaboom.hermes.model.PacketPubSub;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

public class SubscribeThread {

    public static void start(){
        Thread subscribeThread = new Thread(() -> {
                try {
                    try (Jedis jedis = Hermes.jedisPool.getResource()) {
                        JedisPubSub pubSub = new PacketPubSub();
                        String channel = "Hermes:Global";
                        jedis.subscribe(pubSub, channel);
                    }
                } catch (Exception var15) {
                    var15.printStackTrace();
                }

        }, "Hermes - Packet Subscription Thread");
        subscribeThread.setDaemon(true);
        subscribeThread.start();

    }

}
