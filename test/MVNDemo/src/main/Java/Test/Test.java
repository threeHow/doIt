package Test;

public class Test {

    String a;
    String b;
    String c;

    public String getA() {
        return a;
    }

    public void setA(String a) {
        this.a = a;
    }

    public String getB() {
        return b;
    }

    public void setB(String b) {
        this.b = b;
    }

    public String getC() {
        return c;
    }

    public void setC(String c) {
        this.c = c;
    }

    @Override
    public String toString() {
        return "Test{" +
                "a='" + a + '\'' +
                ", b='" + b + '\'' +
                ", c='" + c + '\'' +
                '}';
    }

    public static void main(String[] args) {
        Test test=new Test();
        test.setA("1");
        test.setB("2");
        Test tes1=test;
        System.out.println(tes1);
        tes1.setA("asd");
        System.out.println(test);
    }

}
