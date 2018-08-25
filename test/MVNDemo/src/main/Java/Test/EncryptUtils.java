package Test;

import org.apache.commons.lang.RandomStringUtils;

import java.util.Random;

/**
 * @ClassName 常用加密算法工具类(测试)
 * @Description
 * @Author zhujun
 * @Date 2018/8/20 13:51
 */
public class EncryptUtils {
    private static final Random RANDOM = new Random();

    /**
     * 加密算法
     */
    public final static String algorithmName = "SHA-256";
    /**
     * 加密散列次数
     */
    public static final int hashIterations = 1;

    public static String random(int count, boolean letters, boolean numbers) {
        return random(count, 0, 0, letters, numbers);
    }

    public static String randomAlphanumeric(int count) {
        return random(count, true, true);
    }

    public static String random(int count, int start, int end, boolean letters, boolean numbers) {
        return random(count, start, end, letters, numbers, (char[]) null, RANDOM);
    }

    public static String random(int count, int start, int end, boolean letters, boolean numbers, char[] chars, Random random) {
        if (count == 0) {
            return "";
        } else if (count < 0) {
            throw new IllegalArgumentException("Requested random string length " + count + " is less than 0.");
        } else if (chars != null && chars.length == 0) {
            throw new IllegalArgumentException("The chars array must not be empty");
        } else {
            if (start == 0 && end == 0) {
                if (chars != null) {
                    end = chars.length;
                } else if (!letters && !numbers) {
                    end = 2147483647;
                } else {
                    end = 123;
                    start = 32;
                }
            } else if (end <= start) {
                throw new IllegalArgumentException("Parameter end (" + end + ") must be greater than start (" + start + ")");
            }

            char[] buffer = new char[count];
            int gap = end - start;

            while (true) {
                while (true) {
                    while (count-- != 0) {
                        char ch;
                        if (chars == null) {
                            ch = (char) (random.nextInt(gap) + start);
                        } else {
                            ch = chars[random.nextInt(gap) + start];
                        }

                        if (letters && Character.isLetter(ch) || numbers && Character.isDigit(ch) || !letters && !numbers) {
                            if (ch >= '\udc00' && ch <= '\udfff') {
                                if (count == 0) {
                                    ++count;
                                } else {
                                    buffer[count] = ch;
                                    --count;
                                    buffer[count] = (char) ('\ud800' + random.nextInt(128));
                                }
                            } else if (ch >= '\ud800' && ch <= '\udb7f') {
                                if (count == 0) {
                                    ++count;
                                } else {
                                    buffer[count] = (char) ('\udc00' + random.nextInt(128));
                                    --count;
                                    buffer[count] = ch;
                                }
                            } else if (ch >= '\udb80' && ch <= '\udbff') {
                                ++count;
                            } else {
                                buffer[count] = ch;
                            }
                        } else {
                            ++count;
                        }
                    }

                    return new String(buffer);
                }
            }
        }
    }


    public static void main(String[] args) {
        int i = 3;
        switch (i) {
            case 1:
                System.out.println("正常");
                break;
            case 2:
                System.out.println("正常");
                break;
            case 5:
                System.out.println("正常");
                break;
            default:
                assert false : "i的值无效";       //如果i的值不是你想要的，程序就警告退出
        }
        System.out.println("如果断言正常，我就被打印");
    }
}
