package dev.ianrich.hermes.model;

import dev.ianrich.hermes.Hermes;
import redis.clients.jedis.JedisPubSub;

public class PacketPubSub extends JedisPubSub {
    @Override
    public void onMessage(final String channel, final String message) {
        final int packetMessageSplit = message.indexOf("||");
        final String packetClassStr = message.substring(0, packetMessageSplit);
        final String messageJson = message.substring(packetMessageSplit + "||".length());
        Class<?> packetClass;

        try {
            packetClass = Class.forName(packetClassStr);
        } catch (ClassNotFoundException ignored) {
            return;
        }

        final RedisPacket packet = (RedisPacket) Hermes.GSON.fromJson(messageJson, packetClass);

        // Execute the packet...

        packet.onReceive();
    }
}
