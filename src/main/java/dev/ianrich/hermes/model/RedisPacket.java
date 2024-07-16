package dev.ianrich.hermes.model;

public interface RedisPacket {
    void onReceive();
    void onSend();
}
