package me.kaboom.hermes;

import com.google.gson.Gson;
import me.kaboom.hermes.model.*;
import me.kaboom.hermes.thread.SubscribeThread;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class Hermes {

    public static JedisPool jedisPool;
    public static Gson GSON = new Gson();

    public Hermes(String host, Integer port, String username, String password, Integer db){
        try {
            jedisPool = new JedisPool(new JedisPoolConfig(), host, port, 20000, username, password, db);
        }
        catch (Exception e) {
            jedisPool = null;
            e.printStackTrace();
            System.out.println(("Couldn't connect to a Redis database at " + host + "."));
        }

        SubscribeThread.start();
    }

    public Hermes(String host, Integer port, Integer db){
        try {
            jedisPool = new JedisPool(new JedisPoolConfig(), host, port, 20000,null, db);
        }
        catch (Exception e) {
            jedisPool = null;
            e.printStackTrace();
            System.out.println(("Couldn't connect to a Redis database at " + host + "."));
        }

        SubscribeThread.start();
    }

    public <T> T runRedisCommand(RedisCommand<T> redisCommand) {

        T result;
        try (Jedis jedis = jedisPool.getResource()) {
            result = redisCommand.execute(jedis);
        }

        return result;
    }

    public void setData(String key, String value){
        runRedisCommand(r -> {
            r.set(key, value);
            return null;
        });
    }

    public String getData(String key){
        return runRedisCommand(r -> {
            return r.get(key);
        });
    }

    public Boolean ifExists(String key){
        return runRedisCommand(r -> r.exists(key));
    }

    public static void sendPacket(final RedisPacket packet) {
        packet.onSend();

        Jedis jedis = jedisPool.getResource();
        Throwable var3 = null;

        // Spooky Redis code - Ian

        try {
            String encodedPacket = packet.getClass().getName() + "||" + GSON.toJson(packet);
            jedis.publish("Hermes:Global", encodedPacket);
        } catch (Throwable var12) {
            var12.printStackTrace();
            var3 = var12;
            throw var12;
        } finally {
            if (jedis != null) {
                if (var3 != null) {
                    try {
                        jedis.close();
                    } catch (Throwable var11) {
                        var3.addSuppressed(var11);
                    }
                } else {
                    jedis.close();
                }
            }

        }

    }

}
