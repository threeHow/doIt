package Test;/**
 * Created by ZhuJun on 2018/8/24.
 */

import java.util.HashMap;

/**
 * @ClassName HashCodeTest
 * @Description
 * @Author zhujun
 * @Date 2018/8/24 14:43
 */
public class HashCodeTest {

    private String name;
    private int age;

    public HashCodeTest(String name,int age) {
        this.name = name;
        this.age = age;
    }

    public void setAge(int age){
        this.age = age;
    }

    @Override
    public int hashCode() {
        // TODO Auto-generated method stub
        return name.hashCode()+age;
    }

    @Override
    public boolean equals(Object obj) {
        // TODO Auto-generated method stub
        return this.name.equals(((HashCodeTest)obj).name) && this.age== ((HashCodeTest)obj).age;
    }

    public static void main(String[] args) {
        HashCodeTest t1=new HashCodeTest("测试人员",1);
        System.out.println(t1.hashCode());

        HashMap<HashCodeTest, Integer> hashMap = new HashMap<HashCodeTest, Integer>();
        hashMap.put(t1, 1);

        HashCodeTest t2=new HashCodeTest("测试人员",1);
        System.out.println(t1.equals(t2));

        System.out.println(hashMap.get(new HashCodeTest("测试人员",1)));
    }
}
