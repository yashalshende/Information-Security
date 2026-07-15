import java.util.Scanner;

public class ASCII {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        System.out.println("Lowercase letters:");
        for (char ch = 'a'; ch <= 'z'; ch++) {
            System.out.println(ch + " = " + (int) ch);
        }

        System.out.println("\nUppercase letters:");
        for (char ch = 'A'; ch <= 'Z'; ch++) {
            System.out.println(ch + " = " + (int) ch);
        }

        System.out.print("\nEnter your name: ");
        String name = input.nextLine();

        System.out.println("\nASCII values of your name:");
        for (int i = 0; i < name.length(); i++) {
            char ch = name.charAt(i);
            System.out.println(ch + " = " + (int) ch);
        }

        input.close();
    }
}
