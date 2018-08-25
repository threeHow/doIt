package Test;

import org.apache.commons.lang.RandomStringUtils;

/**
 * @ClassName RandomStringUtil
 * @Description
 * @Author zhujun
 * @Date 2018/8/20 14:45
 */
public class RandomStringUtil {

    public static String salt = RandomStringUtils.random(10);

    public static void main(String[] args) {
        System.out.println(salt);
    }
}
