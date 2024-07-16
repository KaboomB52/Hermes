package me.kaboom.hermes.model;

public interface RedisPacket {
    void onReceive();
    void onSend();
}
