package Test;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisConnectionException;

/**
 * @ClassName RedisTest
 * @Description
 * @Author zhujun
 * @Date 2018/8/24 9:58
 */
public class RedisTest {

    private JedisPool jedisPool;

    /**
     *
     * 功能描述:连接缓存
     *
     * @param
     * @return:
     * @auther: ZhuJun
     * @date: 2018/8/24 10:39
     */
    public Jedis getJedis() {
        Jedis jedis = null;
        try {
            jedis = new Jedis("localhost");
            return jedis;
        } catch (JedisConnectionException e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 存放缓存String类型的数据并设置超时时间
     *
     * @param key     key
     * @param value   value
     * @param timeout 超时时间
     * @return 返回是否缓存成功
     */
    public boolean setString(String key, String value, int timeout) throws Exception {
        String result;
        Jedis jedis = null;
        try {
            jedis = getJedis();
            result = jedis.set(key, value);
            if (timeout != -1) {
                jedis.expire(key, timeout);
            }
            System.out.println(result);
            if ("OK".equals(result)) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            throw e;
        } finally {
            releaseRedis(jedis);
        }
    }

    /**
     *
     * 功能描述:根据存储在缓存中的key值查找到存储的vaule值
     *
     * @param
     * @return:
     * @auther: ZhuJun
     * @date: 2018/8/24 10:40
     */
    public String getString(String key) throws Exception {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            return jedis.get(key);
        } catch (Exception e) {
            throw e;
        } finally {
            releaseRedis(jedis);
        }
    }

    /**
     * Jedis 对象释放
     *
     * @param jedis
     */
    public void releaseRedis(Jedis jedis) {
        if (jedis != null) {
            jedis.close();
        }
    }


    public static void main(String[] args) {
        //连接本地的 Redis 服务
        Jedis jedis = new Jedis("localhost");
        //连接本地的redis服务
        System.out.println("连接成功");
        //设置字符串数据
        jedis.set("runoobkey", "测试字符串");
        //获取存储的数据并输出
//        System.out.println("redis存储的字符串为：" + jedis.get("runoobkey"));
        //存储数据到列表中
//        jedis.lpush("site-list", "a");
//        jedis.lpush("site-list", "b");
//        jedis.lpush("site-list", "c");
//        //获取存储的数据并输出
//        List<String> list=jedis.lrange("site-list",0,2);
//        for(String key:list){
//            System.out.println("列表项为："+key);
//        }
        //获取数据并输出
//        Set<String> keys = jedis.keys("*");
//        for (Iterator<String> it = keys.iterator(); it.hasNext(); ) {
//            String key = it.next();
//            System.out.println(key);
//        }
        RedisTest redisTest = new RedisTest();
        try {
            System.out.println(redisTest.getString("runoobkey"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
