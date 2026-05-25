import java.io.*;
public class Driver {
    public static void main(String [] args) {
        Polynomial p = new Polynomial();
        System.out.println(p.evaluate(3));
        double [] c1 = {6,0,0,5};
        Polynomial p1 = new Polynomial(c1);
        double [] c2 = {0,-2,0,0,-9};
        Polynomial p2 = new Polynomial(c2);
        Polynomial s = p1.add(p2);
        System.out.println("s(0.1) = " + s.evaluate(0.1));
        if(s.hasRoot(1))
            System.out.println("1 is a root of s");
        else
            System.out.println("1 is not a root of s");

        // ADDED TESTING FOR EXERCISE 2
        // multiplication
        Polynomial prod = p1.multiply(p2);

        // file methods (check files manually or use cat or diff with a sample expected output file)
        File file = new File("./tester.txt");
        Polynomial p3 = new Polynomial(file);
        p3.saveToFile(file);

        // printing output (visual testing)
        System.out.println(p);
        System.out.println(p1);
        System.out.println(p2);
        System.out.println(s);
        System.out.println(prod);
    }
}