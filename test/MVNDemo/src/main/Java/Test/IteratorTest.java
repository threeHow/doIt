package Test;/**
 * Created by ZhuJun on 2018/8/24.
 */

import java.util.*;

/**
 * @ClassName IteratorTest
 * @Description
 * @Author zhujun
 * @Date 2018/8/24 13:58
 */
public class IteratorTest {

    public static void main(String[] args) {
        List<Map<String,String>> testMap=new ArrayList<Map<String, String>>();
        Map<String,String> map=null;
        for(int i=0;i<10;i++){
            map=new HashMap<String, String>();
            map.put(String.valueOf(i),"测试用例"+i+"号");
            testMap.add(map);
        }

        for(Iterator it=testMap.iterator();it.hasNext();){
            System.out.println(it.next());
        }
        //set是乱序的所以需要用到迭代器
        Set<Map<String, String>> testSet=new HashSet<Map<String, String>>();
        testSet.addAll(testMap);
        for(Iterator it=testSet.iterator();it.hasNext();){
            System.out.println(it.next());
        }
    }
}
