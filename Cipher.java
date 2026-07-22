import java.util.Scanner;

public class Cipher {

    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {

        int choice;

        do {
            System.out.println("\n===== CIPHER MENU =====");
            System.out.println("1. Additive Cipher");
            System.out.println("2. Multiplicative Cipher");
            System.out.println("3. Affine Cipher");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");

            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {

                case 1:
                    additiveMenu();
                    break;

                case 2:
                    multiplicativeMenu();
                    break;

                case 3:
                    affineMenu();
                    break;

                case 4:
                    System.out.println("Program terminated.");
                    break;

                default:
                    System.out.println("Invalid choice.");
            }

        } while (choice != 4);

        sc.close();
    }

    // Additive Cipher Menu
    static void additiveMenu() {

        System.out.println("\n1. Encryption");
        System.out.println("2. Decryption");
        System.out.print("Enter operation: ");

        int operation = sc.nextInt();
        sc.nextLine();

        System.out.print("Enter text: ");
        String text = sc.nextLine();

        System.out.print("Enter additive key: ");
        int key = sc.nextInt();

        if (operation == 1) {
            String ciphertext = additiveEncryption(text, key);
            System.out.println("Ciphertext: " + ciphertext);
        } else if (operation == 2) {
            String plaintext = additiveDecryption(text, key);
            System.out.println("Plaintext: " + plaintext);
        } else {
            System.out.println("Invalid operation.");
        }
    }

    // Separate Additive Encryption Method
    static String additiveEncryption(String plaintext, int key) {

        String result = "";
        key = key % 26;

        for (char ch : plaintext.toCharArray()) {

            if (Character.isUpperCase(ch)) {
                result += (char) ((ch - 'A' + key) % 26 + 'A');
            } else if (Character.isLowerCase(ch)) {
                result += (char) ((ch - 'a' + key) % 26 + 'a');
            } else {
                result += ch;
            }
        }

        return result;
    }

    // Separate Additive Decryption Method
    static String additiveDecryption(String ciphertext, int key) {

        String result = "";
        key = key % 26;

        for (char ch : ciphertext.toCharArray()) {

            if (Character.isUpperCase(ch)) {
                result += (char) ((ch - 'A' - key + 26) % 26 + 'A');
            } else if (Character.isLowerCase(ch)) {
                result += (char) ((ch - 'a' - key + 26) % 26 + 'a');
            } else {
                result += ch;
            }
        }

        return result;
    }

    // Multiplicative Cipher Menu
    static void multiplicativeMenu() {

        System.out.println("\n1. Encryption");
        System.out.println("2. Decryption");
        System.out.print("Enter operation: ");

        int operation = sc.nextInt();
        sc.nextLine();

        System.out.print("Enter text: ");
        String text = sc.nextLine();

        System.out.print("Enter multiplicative key: ");
        int key = sc.nextInt();

        if (gcd(key, 26) != 1) {
            System.out.println("Invalid key. Key must be coprime with 26.");
            return;
        }

        if (operation == 1) {
            String ciphertext = multiplicativeEncryption(text, key);
            System.out.println("Ciphertext: " + ciphertext);
        } else if (operation == 2) {
            String plaintext = multiplicativeDecryption(text, key);
            System.out.println("Plaintext: " + plaintext);
        } else {
            System.out.println("Invalid operation.");
        }
    }

    static String multiplicativeEncryption(String plaintext, int key) {

        String result = "";

        for (char ch : plaintext.toCharArray()) {

            if (Character.isUpperCase(ch)) {
                int value = ch - 'A';
                result += (char) ((value * key) % 26 + 'A');
            } else if (Character.isLowerCase(ch)) {
                int value = ch - 'a';
                result += (char) ((value * key) % 26 + 'a');
            } else {
                result += ch;
            }
        }

        return result;
    }

    static String multiplicativeDecryption(String ciphertext, int key) {

        String result = "";
        int inverseKey = modularInverse(key);

        for (char ch : ciphertext.toCharArray()) {

            if (Character.isUpperCase(ch)) {
                int value = ch - 'A';
                result += (char) ((value * inverseKey) % 26 + 'A');
            } else if (Character.isLowerCase(ch)) {
                int value = ch - 'a';
                result += (char) ((value * inverseKey) % 26 + 'a');
            } else {
                result += ch;
            }
        }

        return result;
    }

    // Affine Cipher Menu
    static void affineMenu() {

        System.out.println("\n1. Encryption");
        System.out.println("2. Decryption");
        System.out.print("Enter operation: ");

        int operation = sc.nextInt();
        sc.nextLine();

        System.out.print("Enter text: ");
        String text = sc.nextLine();

        System.out.print("Enter multiplicative key (a): ");
        int a = sc.nextInt();

        System.out.print("Enter additive key (b): ");
        int b = sc.nextInt();

        if (gcd(a, 26) != 1) {
            System.out.println("Invalid key a. It must be coprime with 26.");
            return;
        }

        if (operation == 1) {
            String ciphertext = affineEncryption(text, a, b);
            System.out.println("Ciphertext: " + ciphertext);
        } else if (operation == 2) {
            String plaintext = affineDecryption(text, a, b);
            System.out.println("Plaintext: " + plaintext);
        } else {
            System.out.println("Invalid operation.");
        }
    }

    static String affineEncryption(String plaintext, int a, int b) {

        String result = "";

        for (char ch : plaintext.toCharArray()) {

            if (Character.isUpperCase(ch)) {
                int value = ch - 'A';
                result += (char) ((a * value + b) % 26 + 'A');
            } else if (Character.isLowerCase(ch)) {
                int value = ch - 'a';
                result += (char) ((a * value + b) % 26 + 'a');
            } else {
                result += ch;
            }
        }

        return result;
    }

    static String affineDecryption(String ciphertext, int a, int b) {

        String result = "";
        int inverseA = modularInverse(a);

        for (char ch : ciphertext.toCharArray()) {

            if (Character.isUpperCase(ch)) {
                int value = ch - 'A';
                int decryptedValue = inverseA * (value - b);

                decryptedValue = (decryptedValue % 26 + 26) % 26;

                result += (char) (decryptedValue + 'A');
            } else if (Character.isLowerCase(ch)) {
                int value = ch - 'a';
                int decryptedValue = inverseA * (value - b);

                decryptedValue = (decryptedValue % 26 + 26) % 26;

                result += (char) (decryptedValue + 'a');
            } else {
                result += ch;
            }
        }

        return result;
    }

    // Find Modular Inverse
    static int modularInverse(int key) {

        key = key % 26;

        for (int i = 1; i < 26; i++) {
            if ((key * i) % 26 == 1) {
                return i;
            }
        }

        return -1;
    }

    // Find GCD
    static int gcd(int a, int b) {

        while (b != 0) {
            int temp = b;
            b = a % b;
            a = temp;
        }

        return Math.abs(a);
    }
}