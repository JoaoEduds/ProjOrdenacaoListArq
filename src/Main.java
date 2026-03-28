import Lista.List;

public class Main {
    public static void main(String[] args) {
        List L = new List();

        L.insert(6);
        L.insert(5);
        L.insert(3);
        L.insert(9);
        L.insert(2);
        L.insert(7);

        L.Counting_Sort();

        L.exibi();
    }
}