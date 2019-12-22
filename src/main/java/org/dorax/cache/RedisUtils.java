package org.dorax.cache;

import org.apache.commons.lang3.StringUtils;
import org.dorax.lang.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.exceptions.JedisException;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Jedis Cache 工具类
 *
 * @author wuchunfu
 * @date 2019-12-13
 */
public class RedisUtils {

    private static Logger logger = LoggerFactory.getLogger(RedisUtils.class);

    /**
     * 为key指定过期时间，单位是秒
     */
    public static int SECONDS = 3600 * 24;
    /**
     * 超时时间，单位是秒
     */
    private static final int TIMEOUT = 10000;
    private volatile static JedisPoolConfig jedisPoolConfig;
    private static JedisPool jedisPool;
    public static String ip = "192.168.x.x";
    public static int port = 6379;
    public static int timeout = 2000;

    static {
        jedisPoolConfig = new JedisPoolConfig();
        // 最大连接数
        jedisPoolConfig.setMaxTotal(1024);
        // 允许的最大空闲连接数
        jedisPoolConfig.setMaxIdle(100);
        // 当资源池用尽后，调用者是否要等待。只有当值为tru
        jedisPoolConfig.setBlockWhenExhausted(true);
        // 当资源池连接用尽后，调用者的最大等待时间（单位为毫秒）。
        jedisPoolConfig.setMaxWaitMillis(100);
        // 向资源池借用连接时是否做连接有效性检测（ping）。检测到的无效连接将会被移除。
        jedisPoolConfig.setTestOnBorrow(false);
        // 向资源池归还连接时是否做连接有效性检测（ping）。检测到无效连接将会被移除。
        jedisPoolConfig.setTestOnReturn(false);
        // 是否开启空闲资源检测。
        jedisPoolConfig.setTestWhileIdle(true);
        // 空闲资源的检测周期（单位为毫秒）(-1 不检测)
        jedisPoolConfig.setTimeBetweenEvictionRunsMillis(-1);
        // 资源池中资源的最小空闲时间（单位为毫秒），达到此值后空闲资源将被移除。
        jedisPoolConfig.setMinEvictableIdleTimeMillis(180000);
        // 做空闲资源检测时，每次检测资源的个数。
        jedisPoolConfig.setNumTestsPerEvictionRun(3);
    }

    public static void init(String host, int port, String password) {
        if (jedisPool == null) {
            synchronized (RedisUtils.class) {
                if (jedisPool == null) {
                    jedisPool = new JedisPool(jedisPoolConfig, host, port, TIMEOUT, password);
                }
            }
        }
    }

    /**
     * 添加String类型数据
     *
     * @param key   键
     * @param value 值
     * @return 状态码
     */
    public static String set(String key, String value) {
        String result = null;
        Jedis jedis = null;
        try {
            jedis = getResource();
            if (jedis.exists(key)) {
                logger.warn("set {} is exists.", key);
            }
            result = jedis.set(key, value);
            jedis.expire(key, SECONDS);
        } catch (Exception e) {
            logger.error("set {} = {}", key, value, e);
        } finally {
            closeJedis(jedis);
        }
        return result;
    }

    /**
     * 添加 String 类型的数据
     *
     * @param jedis jedis 对象
     * @param key   键
     * @param value 值
     * @return 状态码
     */
    public static String set(Jedis jedis, String key, String value) {
        String result = null;
        try {
            if (jedis.exists(key)) {
                logger.warn("set {} is exists.", key);
            }
            result = jedis.set(key, value);
            jedis.expire(key, SECONDS);
        } catch (Exception e) {
            logger.error("set {} = {}", key, value, e);
        } finally {
            closeJedis(jedis);
        }
        return result;
    }

    /**
     * 添加 String 类型的数据，并设置过期时间
     *
     * @param jedis        jedis 对象
     * @param key          键
     * @param value        值
     * @param cacheSeconds 过期时间
     * @return 状态码
     */
    public static String set(Jedis jedis, String key, String value, int cacheSeconds) {
        String result = null;
        try {
            if (jedis.exists(key)) {
                logger.warn("set {} is exists.", key);
            }
            result = jedis.set(key, value);
            jedis.expire(key, cacheSeconds);
        } catch (Exception e) {
            logger.error("set {} = {}", key, value, e);
        } finally {
            closeJedis(jedis);
        }
        return result;
    }

    /**
     * 设置string类型数据，并设置过期时间
     *
     * @param key          键
     * @param value        值
     * @param cacheSeconds 超时时间，0为不超时
     * @return 状态码
     */
    public static String set(String key, String value, int cacheSeconds) {
        String result = null;
        Jedis jedis = null;
        try {
            jedis = getResource();
            if (jedis.exists(key)) {
                logger.warn("set {} is exists.", key);
            }
            result = jedis.set(key, value);
            if (cacheSeconds != 0) {
                jedis.expire(key, cacheSeconds);
            }
        } catch (Exception e) {
            logger.error("set {} = {}", key, value, e);
        } finally {
            closeJedis(jedis);
        }
        return result;
    }

    /**
     * 设置 object 类型数据，并设置过期时间
     *
     * @param key          键
     * @param value        值
     * @param cacheSeconds 超时时间，0为不超时
     * @return 状态码
     */
    public static String setObject(String key, Object value, int cacheSeconds) {
        String result = null;
        Jedis jedis = null;
        try {
            jedis = getResource();
            if (jedis.exists(key)) {
                logger.warn("set {} is exists.", key);
            }
            result = jedis.set(getBytesKey(key), toBytes(value));
            if (cacheSeconds != 0) {
                jedis.expire(key, cacheSeconds);
            }
        } catch (Exception e) {
            logger.error("setObject {} = {}", key, value, e);
        } finally {
            closeJedis(jedis);
        }
        return result;
    }

    /**
     * 设置 String 类型的值，并设置过期时间
     *
     * @param key          键
     * @param value        值
     * @param cacheSeconds 超时时间，0为不超时
     * @return 在添加操作之后的 list 元素数量
     */
    public static long setLeftList(String key, String value, int cacheSeconds) {
        long result = 0;
        Jedis jedis = null;
        try {
            jedis = getResource();
            if (jedis.exists(key)) {
                logger.warn("set {} is exists.", key);
            }
            result = jedis.lpush(key, value);
            if (cacheSeconds != 0) {
                jedis.expire(key, cacheSeconds);
            }
        } catch (Exception e) {
            logger.error("setLeftList {} = {}", key, value, e);
        } finally {
            closeJedis(jedis);
        }
        return result;
    }

    /**
     * 设置 List<String> 类型的值，并设置过期时间
     *
     * @param key          键
     * @param value        值
     * @param cacheSeconds 超时时间，0为不超时
     * @return 在添加操作之后的 list 元素数量
     */
    public static long setLeftList(String key, List<String> value, int cacheSeconds) {
        long result = 0;
        Jedis jedis = null;
        try {
            jedis = getResource();
            if (jedis.exists(key)) {
                logger.warn("set {} is exists.", key);
            }
            result = jedis.lpush(key, (String[]) value.toArray());
            if (cacheSeconds != 0) {
                jedis.expire(key, cacheSeconds);
            }
        } catch (Exception e) {
            logger.error("setLeftList {} = {}", key, value, e);
        } finally {
            closeJedis(jedis);
        }
        return result;
    }

    /**
     * 向List缓存中添加值
     *
     * @param key   键
     * @param value 值
     * @return 在添加操作之后的 list 元素数量
     */
    public static long setLeftList(String key, String... value) {
        long result = 0;
        Jedis jedis = null;
        try {
            jedis = getResource();
            if (jedis.exists(key)) {
                logger.warn("set {} is exists.", key);
            }
            result = jedis.lpush(key, value);
        } catch (Exception e) {
            logger.error("setLeftList {} = {}", key, value, e);
        } finally {
            closeJedis(jedis);
        }
        return result;
    }

    /**
     * 向List缓存中添加值
     *
     * @param key   键
     * @param value 值
     * @return 在添加操作之后的 list 元素数量
     */
    public static long setLeftObjectList(String key, Object... value) {
        long result = 0;
        Jedis jedis = null;
        try {
            jedis = getResource();
            List<byte[]> list = new ArrayList<>();
            for (Object o : value) {
                list.add(toBytes(o));
            }
            result = jedis.lpush(getBytesKey(key), (byte[][]) list.toArray());
        } catch (Exception e) {
            logger.error("setLeftObjectList {} = {}", key, value, e);
        } finally {
            closeJedis(jedis);
        }
        return result;
    }

    /**
     * 设置 List<Object> 类型的值，并设置过期时间
     *
     * @param key          键
     * @param value        值
     * @param cacheSeconds 超时时间，0为不超时
     * @return 在添加操作之后的 list 元素数量
     */
    public static long setLeftObjectList(String key, List<Object> value, int cacheSeconds) {
        long result = 0;
        Jedis jedis = null;
        try {
            jedis = getResource();
            if (jedis.exists(getBytesKey(key))) {
                logger.warn("set {} is exists.", key);
            }
            List<byte[]> list = new ArrayList<>();
            for (Object o : value) {
                list.add(toBytes(o));
            }
            result = jedis.lpush(getBytesKey(key), (byte[][]) list.toArray());
            if (cacheSeconds != 0) {
                jedis.expire(key, cacheSeconds);
            }
        } catch (Exception e) {
            logger.error("setLeftObjectList {} = {}", key, value, e);
        } finally {
            closeJedis(jedis);
        }
        return result;
    }

    /**
     * 设置 String 类型的值，并设置过期时间
     *
     * @param key          键
     * @param value        值
     * @param cacheSeconds 超时时间，0为不超时
     * @return 在添加操作之后的 list 元素数量
     */
    public static long setRightList(String key, String value, int cacheSeconds) {
        long result = 0;
        Jedis jedis = null;
        try {
            jedis = getResource();
            if (jedis.exists(key)) {
                logger.warn("set {} is exists.", key);
            }
            result = jedis.rpush(key, value);
            if (cacheSeconds != 0) {
                jedis.expire(key, cacheSeconds);
            }
        } catch (Exception e) {
            logger.error("setRightList {} = {}", key, value, e);
        } finally {
            closeJedis(jedis);
        }
        return result;
    }

    /**
     * 设置 List<String> 类型的值，并设置过期时间
     *
     * @param key          键
     * @param value        值
     * @param cacheSeconds 超时时间，0为不超时
     * @return 在添加操作之后的 list 元素数量
     */
    public static long setRightList(String key, List<String> value, int cacheSeconds) {
        long result = 0;
        Jedis jedis = null;
        try {
            jedis = getResource();
            if (jedis.exists(key)) {
                logger.warn("set {} is exists.", key);
            }
            result = jedis.rpush(key, (String[]) value.toArray());
            if (cacheSeconds != 0) {
                jedis.expire(key, cacheSeconds);
            }
        } catch (Exception e) {
            logger.error("setRightList {} = {}", key, value, e);
        } finally {
            closeJedis(jedis);
        }
        return result;
    }

    /**
     * 向List缓存中添加值
     *
     * @param key   键
     * @param value 值
     * @return 在添加操作之后的 list 元素数量
     */
    public static long setRightList(String key, String... value) {
        long result = 0;
        Jedis jedis = null;
        try {
            jedis = getResource();
            if (jedis.exists(key)) {
                logger.warn("set {} is exists.", key);
            }
            result = jedis.rpush(key, value);
        } catch (Exception e) {
            logger.error("setRightList {} = {}", key, value, e);
        } finally {
            closeJedis(jedis);
        }
        return result;
    }

    /**
     * 向List缓存中添加值
     *
     * @param key   键
     * @param value 值
     * @return 在添加操作之后的 list 元素数量
     */
    public static long setRightObjectList(String key, Object... value) {
        long result = 0;
        Jedis jedis = null;
        try {
            jedis = getResource();
            List<byte[]> list = new ArrayList<>();
            for (Object o : value) {
                list.add(toBytes(o));
            }
            result = jedis.rpush(getBytesKey(key), (byte[][]) list.toArray());
        } catch (Exception e) {
            logger.error("setRightObjectList {} = {}", key, value, e);
        } finally {
            closeJedis(jedis);
        }
        return result;
    }

    /**
     * 设置 List<Object> 类型的值，并设置过期时间
     *
     * @param key          键
     * @param value        值
     * @param cacheSeconds 超时时间，0为不超时
     * @return 在添加操作之后的 list 元素数量
     */
    public static long setRightObjectList(String key, List<Object> value, int cacheSeconds) {
        long result = 0;
        Jedis jedis = null;
        try {
            jedis = getResource();
            if (jedis.exists(getBytesKey(key))) {
                logger.warn("set {} is exists.", key);
            }
            List<byte[]> list = new ArrayList<>();
            for (Object o : value) {
                list.add(toBytes(o));
            }
            result = jedis.rpush(getBytesKey(key), (byte[][]) list.toArray());
            if (cacheSeconds != 0) {
                jedis.expire(key, cacheSeconds);
            }
        } catch (Exception e) {
            logger.error("setRightObjectList {} = {}", key, value, e);
        } finally {
            closeJedis(jedis);
        }
        return result;
    }

    /**
     * 添加 Set<String> 类型的值，并设置过期时间
     *
     * @param key          键
     * @param value        值
     * @param cacheSeconds 超时时间，0为不超时
     * @return 在添加操作之后的 list 元素数量
     */
    public static long setSet(String key, Set<String> value, int cacheSeconds) {
        long result = 0;
        Jedis jedis = null;
        try {
            jedis = getResource();
            if (jedis.exists(key)) {
                logger.warn("set {} is exists.", key);
            }
            result = jedis.sadd(key, (String[]) value.toArray());
            if (cacheSeconds != 0) {
                jedis.expire(key, cacheSeconds);
            }
        } catch (Exception e) {
            logger.error("setSet {} = {}", key, value, e);
        } finally {
            closeJedis(jedis);
        }
        return result;
    }

    /**
     * 添加 Set<Object> 类型的值，并设置过期时间
     *
     * @param key          键
     * @param value        值
     * @param cacheSeconds 超时时间，0为不超时
     * @return 在添加操作之后的 list 元素数量
     */
    public static long setObjectSet(String key, Set<Object> value, int cacheSeconds) {
        long result = 0;
        Jedis jedis = null;
        try {
            jedis = getResource();
            if (jedis.exists(getBytesKey(key))) {
                logger.warn("set {} is exists.", key);
            }
            Set<byte[]> set = new HashSet<>();
            for (Object o : value) {
                set.add(toBytes(o));
            }
            result = jedis.sadd(getBytesKey(key), (byte[][]) set.toArray());
            if (cacheSeconds != 0) {
                jedis.expire(key, cacheSeconds);
            }
        } catch (Exception e) {
            logger.error("setObjectSet {} = {}", key, value, e);
        } finally {
            closeJedis(jedis);
        }
        return result;
    }

    /**
     * 添加 String 类型的值，并设置过期时间
     *
     * @param key   键
     * @param value 值
     * @return 在添加操作之后的 list 元素数量
     */
    public static long setSet(String key, String... value) {
        long result = 0;
        Jedis jedis = null;
        try {
            jedis = getResource();
            if (jedis.exists(key)) {
                logger.warn("set {} is exists.", key);
            }
            result = jedis.sadd(key, value);
        } catch (Exception e) {
            logger.error("setSet {} = {}", key, value, e);
        } finally {
            closeJedis(jedis);
        }
        return result;
    }

    /**
     * 添加 Object 类型的值，并设置过期时间
     *
     * @param key   键
     * @param value 值
     * @return 在添加操作之后的 list 元素数量
     */
    public static long setSetObject(String key, Object... value) {
        long result = 0;
        Jedis jedis = null;
        try {
            jedis = getResource();
            if (jedis.exists(key)) {
                logger.warn("set {} is exists.", key);
            }
            Set<byte[]> set = new HashSet<>();
            for (Object o : value) {
                set.add(toBytes(o));
            }
            result = jedis.rpush(getBytesKey(key), (byte[][]) set.toArray());
        } catch (Exception e) {
            logger.error("setSetObject {} = {}", key, value, e);
        } finally {
            closeJedis(jedis);
        }
        return result;
    }

    /**
     * 添加 Map<String, String> 类型的值
     *
     * @param key   键
     * @param value 值
     * @return 如果 hash 为空，则返回 OK 或 Exception
     */
    public static String setMap(String key, Map<String, String> value) {
        String result = null;
        Jedis jedis = null;
        try {
            jedis = getResource();
            if (jedis.exists(key)) {
                logger.warn("set {} is exists.", key);
            }
            result = jedis.hmset(key, value);
            jedis.expire(key, SECONDS);
        } catch (Exception e) {
            logger.error("setMap {} = {}", key, value, e);
        } finally {
            closeJedis(jedis);
        }
        return result;
    }

    /**
     * 添加 Map<String, String> 类型的值，并设置过期时间
     *
     * @param key          键
     * @param value        值
     * @param cacheSeconds 超时时间，0为不超时
     * @return 如果 hash 为空，则返回 OK 或 Exception
     */
    public static String setMap(String key, Map<String, String> value, int cacheSeconds) {
        String result = null;
        Jedis jedis = null;
        try {
            jedis = getResource();
            if (jedis.exists(key)) {
                logger.warn("set {} is exists.", key);
            }
            result = jedis.hmset(key, value);
            if (cacheSeconds != 0) {
                jedis.expire(key, cacheSeconds);
            }
        } catch (Exception e) {
            logger.error("setMap {} = {}", key, value, e);
        } finally {
            closeJedis(jedis);
        }
        return result;
    }

    /**
     * 添加 Map<String, Object> 类型的值
     *
     * @param key   键
     * @param value 值
     * @return 如果 hash 为空，则返回 OK 或 Exception
     */
    public static String setObjectMap(String key, Map<String, Object> value) {
        String result = null;
        Jedis jedis = null;
        try {
            jedis = getResource();
            if (jedis.exists(key)) {
                logger.warn("set {} is exists.", key);
            }
            Map<byte[], byte[]> map = new HashMap<>();
            for (Map.Entry<String, Object> e : value.entrySet()) {
                map.put(getBytesKey(e.getKey()), toBytes(e.getValue()));
            }
            result = jedis.hmset(getBytesKey(key), map);
            jedis.expire(key, SECONDS);
        } catch (Exception e) {
            logger.error("setObjectMap {} = {}", key, value, e);
        } finally {
            closeJedis(jedis);
        }
        return result;
    }

    /**
     * 添加 Map<String, Object> 类型的值，并设置过期时间
     *
     * @param key          键
     * @param value        值
     * @param cacheSeconds 超时时间，0为不超时
     * @return 如果 hash 为空，则返回 OK 或 Exception
     */
    public static String setObjectMap(String key, Map<String, Object> value, int cacheSeconds) {
        String result = null;
        Jedis jedis = null;
        try {
            jedis = getResource();
            if (jedis.exists(key)) {
                logger.warn("set {} is exists.", key);
            }
            Map<byte[], byte[]> map = new HashMap<>();
            for (Map.Entry<String, Object> e : value.entrySet()) {
                map.put(getBytesKey(e.getKey()), toBytes(e.getValue()));
            }
            result = jedis.hmset(getBytesKey(key), map);
            if (cacheSeconds != 0) {
                jedis.expire(key, cacheSeconds);
            }
        } catch (Exception e) {
            logger.error("setObjectMap {} = {}", key, value, e);
        } finally {
            closeJedis(jedis);
        }
        return result;
    }

    /**
     * 根据 key 获取 String 类型的值
     *
     * @param jedis jedis 对象
     * @param key   key
     * @return String 类型的值
     */
    public static String get(Jedis jedis, String key) {
        String value = null;
        try {
            if (jedis.exists(key)) {
                value = jedis.get(key);
                value = StringUtils.isNotBlank(value) && !"nil".equalsIgnoreCase(value) ? value : null;
            }
        } catch (Exception e) {
            logger.error("get {} = {}", key, value, e);
        } finally {
            closeJedis(jedis);
        }
        return value;
    }

    /**
     * 根据 key 获取 String 类型的值
     *
     * @param key 键
     * @return String 类型的值
     */
    public static String get(String key) {
        String value = null;
        Jedis jedis = null;
        try {
            jedis = getResource();
            if (jedis.exists(key)) {
                value = jedis.get(key);
                value = StringUtils.isNotBlank(value) && !"nil".equalsIgnoreCase(value) ? value : null;
            }
        } catch (Exception e) {
            logger.error("get {} = {}", key, value, e);
        } finally {
            closeJedis(jedis);
        }
        return value;
    }

    /**
     * 根据 key 获取 object 类型的值
     *
     * @param key 键
     * @return Object 类型的值
     */
    public static Object getObject(String key) {
        Object value = null;
        Jedis jedis = null;
        try {
            jedis = getResource();
            if (jedis.exists(getBytesKey(key))) {
                value = toObject(jedis.get(getBytesKey(key)));
            }
        } catch (Exception e) {
            logger.error("getObject {} = {}", key, value, e);
        } finally {
            closeJedis(jedis);
        }
        return value;
    }

    /**
     * 根据 key 获取 List<String> 类型的数据
     *
     * @param jedis jedis 对象
     * @param key   键
     * @return List<String> 类型的值
     */
    public static List<String> getList(Jedis jedis, String key) {
        List<String> value = null;
        try {
            long l = jedis.llen(key);
            if (l == 0) {
                return null;
            }
            if (jedis.exists(key)) {
                value = jedis.lrange(key, 0, -1);
            }
        } catch (Exception e) {
            logger.error("getList {} = {}", key, value, e);
        } finally {
            closeJedis(jedis);
        }
        return value;
    }

    /**
     * 根据 key 获取 List<String> 类型的数据
     *
     * @param key 键
     * @return List<String> 类型的值
     */
    public static List<String> getList(String key) {
        List<String> value = null;
        Jedis jedis = null;
        try {
            jedis = getResource();
            long l = jedis.llen(key);
            if (l == 0) {
                return null;
            }
            if (jedis.exists(key)) {
                value = jedis.lrange(key, 0, -1);
            }
        } catch (Exception e) {
            logger.error("getList {} = {}", key, value, e);
        } finally {
            closeJedis(jedis);
        }
        return value;
    }

    /**
     * 根据 key 获取 List<Object> 类型的数据
     *
     * @param key 键
     * @return List<Object> 类型的值
     */
    public static List<Object> getObjectList(String key) {
        List<Object> value = null;
        Jedis jedis = null;
        try {
            jedis = getResource();
            if (jedis.exists(getBytesKey(key))) {
                List<byte[]> list = jedis.lrange(getBytesKey(key), 0, -1);
                value = new ArrayList<>();
                for (byte[] bs : list) {
                    value.add(toObject(bs));
                }
            }
        } catch (Exception e) {
            logger.error("getObjectList {} = {}", key, value, e);
        } finally {
            closeJedis(jedis);
        }
        return value;
    }

    /**
     * 根据 key 获取 Set<String> 类型的值
     *
     * @param key 键
     * @return Set<String> 类型的值
     */
    public static Set<String> getSet(String key) {
        Set<String> value = null;
        Jedis jedis = null;
        try {
            jedis = getResource();
            if (jedis.exists(key)) {
                value = jedis.smembers(key);
            }
        } catch (Exception e) {
            logger.error("getSet {} = {}", key, value, e);
        } finally {
            closeJedis(jedis);
        }
        return value;
    }

    /**
     * 根据 key 获取 Set<Object> 类型的值
     *
     * @param key 键
     * @return Set<Object> 类型的值
     */
    public static Set<Object> getObjectSet(String key) {
        Set<Object> value = null;
        Jedis jedis = null;
        try {
            jedis = getResource();
            if (jedis.exists(getBytesKey(key))) {
                value = new HashSet<>();
                Set<byte[]> set = jedis.smembers(getBytesKey(key));
                for (byte[] bs : set) {
                    value.add(toObject(bs));
                }
            }
        } catch (Exception e) {
            logger.error("getObjectSet {} = {}", key, value, e);
        } finally {
            closeJedis(jedis);
        }
        return value;
    }

    /**
     * 根据 key 获取 Map<String, String> 类型的值
     *
     * @param key 键
     * @return Map<String, String> 类型的值
     */
    public static Map<String, String> getMap(String key) {
        Map<String, String> value = null;
        Jedis jedis = null;
        try {
            jedis = getResource();
            if (jedis.exists(key)) {
                value = jedis.hgetAll(key);
            }
        } catch (Exception e) {
            logger.error("getMap {} = {}", key, value, e);
        } finally {
            closeJedis(jedis);
        }
        return value;
    }

    /**
     * 根据 key 获取 Map<String, Object> 类型的值
     *
     * @param key 键
     * @return Map<String, Object> 类型的值
     */
    public static Map<String, Object> getObjectMap(String key) {
        Map<String, Object> value = null;
        Jedis jedis = null;
        try {
            jedis = getResource();
            if (jedis.exists(getBytesKey(key))) {
                value = new HashMap<>();
                Map<byte[], byte[]> map = jedis.hgetAll(getBytesKey(key));
                for (Map.Entry<byte[], byte[]> e : map.entrySet()) {
                    value.put(new String(e.getKey(), StandardCharsets.UTF_8), toObject(e.getValue()));
                }
            }
        } catch (Exception e) {
            logger.error("getObjectMap {} = {}", key, value, e);
        } finally {
            closeJedis(jedis);
        }
        return value;
    }

    /**
     * 移除Map缓存中的值
     *
     * @param key    键
     * @param mapKey 值
     * @return 如果哈希中存在该字段，则删除该字段并返回1，否则返回0且不执行任何操作。
     */
    public static long delMap(String key, String mapKey) {
        long result = 0;
        Jedis jedis = null;
        try {
            jedis = getResource();
            result = jedis.hdel(key, mapKey);
        } catch (Exception e) {
            logger.error("delMap {} {}", key, mapKey, e);
        } finally {
            closeJedis(jedis);
        }
        return result;
    }

    /**
     * 移除Map缓存中的值
     *
     * @param key    键
     * @param mapKey 值
     * @return 如果 hash 中存在该字段，则删除该字段并返回1，否则返回0且不执行任何操作。
     */
    public static long delObjectMap(String key, String mapKey) {
        long result = 0;
        Jedis jedis = null;
        try {
            jedis = getResource();
            result = jedis.hdel(getBytesKey(key), getBytesKey(mapKey));
        } catch (Exception e) {
            logger.error("delObjectMap {} {}", key, mapKey, e);
        } finally {
            closeJedis(jedis);
        }
        return result;
    }

    /**
     * 判断Map缓存中的Key是否存在
     *
     * @param key    键
     * @param mapKey 值
     * @return 如果 hash 中存在指定的字段则返回 true,否则返回 false
     */
    public static boolean mapIsExists(String key, String mapKey) {
        boolean result = false;
        Jedis jedis = null;
        try {
            jedis = getResource();
            result = jedis.hexists(key, mapKey);
        } catch (Exception e) {
            logger.error("mapIsExists {} {}", key, mapKey, e);
        } finally {
            closeJedis(jedis);
        }
        return result;
    }

    /**
     * 判断Map缓存中的Key是否存在
     *
     * @param key    键
     * @param mapKey 值
     * @return 如果 hash 中存在指定的字段则返回 true,否则返回 false
     */
    public static boolean objectMapIsExists(String key, String mapKey) {
        boolean result = false;
        Jedis jedis = null;
        try {
            jedis = getResource();
            result = jedis.hexists(getBytesKey(key), getBytesKey(mapKey));
        } catch (Exception e) {
            logger.error("objectMapIsExists {} {}", key, mapKey, e);
        } finally {
            closeJedis(jedis);
        }
        return result;
    }

    /**
     * 根据 key 删除 String 类型的值
     *
     * @param key 键
     * @return 如果删除成功则返回删除的元素数
     */
    public static long deleteValueOfList(String key, String value, int count) {
        long result = 0;
        Jedis jedis = null;
        try {
            jedis = getResource();
            if (jedis.exists(key)) {
                result = jedis.lrem(key, count, value);
            } else {
                logger.error("deleteValueOfList {} not exists", key);
            }
        } catch (Exception e) {
            logger.error("deleteValueOfList {}", key, e);
        } finally {
            closeJedis(jedis);
        }
        return result;
    }

    /**
     * 根据 key 删除 String 类型的值
     *
     * @param key 键
     * @return 如果删除成功则返回 0，否则返回大于 0 的整数
     */
    public static long del(Jedis jedis, String key) {
        long result = 0;
        try {
            if (jedis.exists(key)) {
                result = jedis.del(key);
            } else {
                logger.error("del {} not exists", key);
            }
        } catch (Exception e) {
            logger.error("del {}", key, e);
        } finally {
            closeJedis(jedis);
        }
        return result;
    }

    /**
     * 根据 key 删除 String 类型的值
     *
     * @param key 键
     * @return 如果删除成功则返回 0，否则返回大于 0 的整数
     */
    public static long del(String key) {
        long result = 0;
        Jedis jedis = null;
        try {
            jedis = getResource();
            if (jedis.exists(key)) {
                result = jedis.del(key);
            } else {
                logger.error("del {} not exists", key);
            }
        } catch (Exception e) {
            logger.error("del {}", key, e);
        } finally {
            closeJedis(jedis);
        }
        return result;
    }

    /**
     * 根据 key 删除 object 类型的值
     *
     * @param key 键
     * @return 如果删除成功则返回 0，否则返回大于 0 的整数
     */
    public static long delObject(String key) {
        long result = 0;
        Jedis jedis = null;
        try {
            jedis = getResource();
            if (jedis.exists(getBytesKey(key))) {
                result = jedis.del(getBytesKey(key));
            } else {
                logger.error("delObject {} not exists", key);
            }
        } catch (Exception e) {
            logger.error("delObject {}", key, e);
        } finally {
            closeJedis(jedis);
        }
        return result;
    }

    /**
     * 判断key是否存在
     *
     * @param jedis jedis 对象
     * @param key   键
     * @return 如果为 true 则为存在，为 false 则为不存在
     */
    public static boolean exists(Jedis jedis, String key) {
        boolean result = false;
        try {
            if (jedis.exists(key)) {
                result = true;
            }
        } catch (Exception e) {
            logger.error("exists {}", key, e);
        } finally {
            closeJedis(jedis);
        }
        return result;
    }

    /**
     * 判断 key 是否存在
     *
     * @param key 键
     * @return 如果为 true 则为存在，为 false 则为不存在
     */
    public static boolean isExists(String key) {
        boolean result = false;
        Jedis jedis = null;
        try {
            jedis = getResource();
            if (jedis.exists(key)) {
                result = true;
            }
        } catch (Exception e) {
            logger.error("isExists {}", key, e);
        } finally {
            closeJedis(jedis);
        }
        return result;
    }

    /**
     * 判断 key 是否存在
     *
     * @param key 键
     * @return 如果为 true 则为存在，为 false 则为不存在
     */
    public static boolean objectIsExists(String key) {
        boolean result = false;
        Jedis jedis = null;
        try {
            jedis = getResource();
            result = jedis.exists(getBytesKey(key));
        } catch (Exception e) {
            logger.error("objectIsExists {}", key, e);
        } finally {
            closeJedis(jedis);
        }
        return result;
    }

    /**
     * 清空当前库
     */
    public static void flushDB() {
        Jedis jedis = null;
        try {
            jedis = getResource();
            jedis.flushDB();
        } catch (Exception e) {
            logger.error("flushDB: ", e);
        } finally {
            closeJedis(jedis);
        }
    }

    /**
     * 获取资源
     *
     * @return jedis 对象
     */
    public static Jedis getResource() {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
        } catch (JedisException e) {
            logger.error("getResource.", e);
        }
        return jedis;
    }

    /**
     * 释放资源
     *
     * @param jedis jedis
     */
    public static void closeJedisPool(Jedis jedis) {
        try {
            if (jedis != null) {
                jedisPool.close();
            }
        } catch (Exception e) {
            logger.error("close: ", e);
        }
    }

    /**
     * 释放资源
     *
     * @param jedis jedis
     */
    public static void closeJedis(Jedis jedis) {
        try {
            if (jedis != null) {
                jedis.close();
            }
        } catch (Exception e) {
            logger.error("close: ", e);
        }
    }

    /**
     * 在 Jedis Pool 以外强行销毁 Jedis
     *
     * @param jedis jedis
     */
    public static void destroyJedis(Jedis jedis) {
        if (jedis != null) {
            try {
                jedis.quit();
            } catch (Exception e) {
                logger.error("quit: ", e);
            }
            try {
                jedis.disconnect();
            } catch (Exception e) {
                logger.error("disconnect: ", e);
            }
        }
    }

    /**
     * 获取byte[]类型Key
     *
     * @param object object 对象
     * @return 字节数组
     */
    public static byte[] getBytesKey(Object object) {
        if (object instanceof String) {
            return getBytes((String) object);
        } else {
            return ObjectUtils.serialize(object);
        }
    }

    /**
     * 转换为字节数组
     *
     * @param str 字符串
     * @return 字节数组
     */
    public static byte[] getBytes(String str) {
        if (str != null) {
            return str.getBytes(StandardCharsets.UTF_8);
        } else {
            return null;
        }
    }

    /**
     * 获取byte[]类型Key
     *
     * @param key 字节数组
     * @return object 对象
     */
    public static Object getObjectKey(byte[] key) {
        try {
            return new String(key, StandardCharsets.UTF_8);
        } catch (UnsupportedOperationException e) {
            try {
                return RedisUtils.toObject(key);
            } catch (UnsupportedOperationException ex) {
                logger.error("toObject {}", key, ex);
            }
        }
        return null;
    }

    /**
     * Object转换byte[]类型
     *
     * @param object object 对象
     * @return 字节数组
     */
    public static byte[] toBytes(Object object) {
        return ObjectUtils.serialize(object);
    }

    /**
     * byte[]型转换Object
     *
     * @param bytes 字节数组
     * @return object 对象
     */
    public static Object toObject(byte[] bytes) {
        return ObjectUtils.unserialize(bytes);
    }
}
