package dev.ianrich.hermes.thread;

import dev.ianrich.hermes.Hermes;
import dev.ianrich.hermes.model.PacketPubSub;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;
import redis.clients.jedis.exceptions.JedisConnectionException;

public class SubscribeThread {

    public static void start(){
        Thread subscribeThread = new Thread(() -> {
                try {
                    try (Jedis jedis = Hermes.jedisPool.getResource()) {
                        JedisPubSub pubSub = new PacketPubSub();
                        String channel = "Hermes:Global";
                        jedis.subscribe(pubSub, channel);
                    }
                } catch (JedisConnectionException ex){
                    // nothing since we already connected once this is not important - Ian
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

        }, "Hermes - Packet Subscription Thread");
        subscribeThread.setDaemon(true);
        subscribeThread.start();

    }

}
