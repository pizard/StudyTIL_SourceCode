package DataType.Reference;

class MyObject{
    private int index;
    MyObject(int index) {
        this.index = index;
    }
    public int getIndex() {
        return index;
    }
    public void setIndex(int index) {
        this.index = index;
    }
}
public class ClassType {
    public static void main(String[] args) {
        MyObject a = new MyObject(2);
        MyObject b = new MyObject(4);
        System.out.println(a.getIndex()); // "a" result is 2.
        a = b;
        System.out.println(a.getIndex()); // "a" result is 4.
        b.setIndex(6);
        System.out.println(a.getIndex()); // "a" result is 6.
    }
}