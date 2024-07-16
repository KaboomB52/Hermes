package me.kaboom.hermes.model;

import redis.clients.jedis.Jedis;

public interface RedisCommand<T> {

    T execute(Jedis jedis);

}

