package dev.ianrich.hermes.model;

import redis.clients.jedis.Jedis;

public interface RedisCommand<T> {

    T execute(Jedis jedis);

}

