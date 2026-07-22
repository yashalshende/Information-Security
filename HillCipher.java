import java.util.Scanner;

public class HillCipher {
    private static final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n===== HILL CIPHER =====");
            System.out.println("1. Encrypt");
            System.out.println("2. Decrypt");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");

            int option;
            try {
                option = Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
                continue;
            }

            if (option == 3) {
                System.out.println("Program ended.");
                break;
            }

            if (option != 1 && option != 2) {
                System.out.println("Invalid option.");
                continue;
            }

            System.out.println("\nChoose matrix size:");
            System.out.println("1. 2x2");
            System.out.println("2. 3x3");
            System.out.print("Enter your choice: ");

            int choice;
            try {
                choice = Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
                continue;
            }

            int size;
            if (choice == 1) {
                size = 2;
            } else if (choice == 2) {
                size = 3;
            } else {
                System.out.println("Only 2x2 and 3x3 are supported.");
                continue;
            }

            System.out.println("Enter the key matrix values (row by row):");
            int[][] keyMatrix = readMatrix(size);

            if (!isInvertibleMod26(keyMatrix)) {
                System.out.println("The key matrix is not invertible modulo 26.");
                System.out.println("Please enter another key matrix.");
                continue;
            }

            System.out.print("Enter the text (letters only): ");
            String text = sc.nextLine().trim();

            if (option == 1) {
                String encrypted = encrypt(text, keyMatrix);
                System.out.println("Encrypted text: " + encrypted);
            } else {
                String decrypted = decrypt(text, keyMatrix);
                System.out.println("Decrypted text: " + decrypted);
            }
        }

        sc.close();
    }

    private static int[][] readMatrix(int size) {
        int[][] matrix = new int[size][size];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                System.out.print("Enter value for row " + (i + 1) + " column " + (j + 1) + ": ");
                matrix[i][j] = Integer.parseInt(sc.nextLine().trim());
            }
        }

        return matrix;
    }

    private static String prepareText(String text) {
        StringBuilder sb = new StringBuilder();

        for (char ch : text.toUpperCase().toCharArray()) {
            if (ch >= 'A' && ch <= 'Z') {
                sb.append(ch);
            }
        }

        return sb.toString();
    }

    private static String encrypt(String plainText, int[][] keyMatrix) {
        String preparedText = prepareText(plainText);
        int size = keyMatrix.length;

        if (preparedText.length() % size != 0) {
            int remaining = size - (preparedText.length() % size);
            for (int i = 0; i < remaining; i++) {
                preparedText += 'X';
            }
        }

        StringBuilder result = new StringBuilder();

        for (int i = 0; i < preparedText.length(); i += size) {
            int[] block = new int[size];
            for (int j = 0; j < size; j++) {
                block[j] = preparedText.charAt(i + j) - 'A';
            }

            int[] encryptedBlock = multiplyMatrixAndVector(keyMatrix, block);
            for (int value : encryptedBlock) {
                result.append((char) ('A' + value));
            }
        }

        return result.toString();
    }

    private static String decrypt(String cipherText, int[][] keyMatrix) {
        String preparedText = prepareText(cipherText);
        int size = keyMatrix.length;
        int[][] inverseKey = inverseMatrixMod26(keyMatrix);

        StringBuilder result = new StringBuilder();

        for (int i = 0; i < preparedText.length(); i += size) {
            int[] block = new int[size];
            for (int j = 0; j < size; j++) {
                block[j] = preparedText.charAt(i + j) - 'A';
            }

            int[] decryptedBlock = multiplyMatrixAndVector(inverseKey, block);
            for (int value : decryptedBlock) {
                result.append((char) ('A' + value));
            }
        }

        return result.toString();
    }

    private static int[] multiplyMatrixAndVector(int[][] matrix, int[] vector) {
        int size = matrix.length;
        int[] result = new int[size];

        for (int i = 0; i < size; i++) {
            int sum = 0;
            for (int j = 0; j < size; j++) {
                sum += matrix[i][j] * vector[j];
            }
            result[i] = mod26(sum);
        }

        return result;
    }

    private static boolean isInvertibleMod26(int[][] matrix) {
        int determinant = determinantMod26(matrix);
        return gcd(determinant, 26) == 1;
    }

    private static int determinantMod26(int[][] matrix) {
        int size = matrix.length;

        if (size == 2) {
            return mod26(matrix[0][0] * matrix[1][1] - matrix[0][1] * matrix[1][0]);
        }

        int a = matrix[0][0];
        int b = matrix[0][1];
        int c = matrix[0][2];
        int d = matrix[1][0];
        int e = matrix[1][1];
        int f = matrix[1][2];
        int g = matrix[2][0];
        int h = matrix[2][1];
        int i = matrix[2][2];

        int det = a * (e * i - f * h) - b * (d * i - f * g) + c * (d * h - e * g);
        return mod26(det);
    }

    private static int[][] inverseMatrixMod26(int[][] matrix) {
        int size = matrix.length;

        if (size == 2) {
            int determinant = determinantMod26(matrix);
            int inverseDeterminant = modularInverse(determinant, 26);

            int[][] inverse = new int[2][2];
            inverse[0][0] = mod26(matrix[1][1] * inverseDeterminant);
            inverse[0][1] = mod26((-matrix[0][1]) * inverseDeterminant);
            inverse[1][0] = mod26((-matrix[1][0]) * inverseDeterminant);
            inverse[1][1] = mod26(matrix[0][0] * inverseDeterminant);
            return inverse;
        }

        int determinant = determinantMod26(matrix);
        int inverseDeterminant = modularInverse(determinant, 26);

        int[][] cofactors = new int[3][3];

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                int[][] minor = new int[2][2];
                int row = 0;

                for (int x = 0; x < 3; x++) {
                    if (x == i) continue;
                    int col = 0;
                    for (int y = 0; y < 3; y++) {
                        if (y == j) continue;
                        minor[row][col++] = matrix[x][y];
                    }
                    row++;
                }

                int minorDet = determinant2x2(minor);
                int sign = ((i + j) % 2 == 0) ? 1 : -1;
                cofactors[i][j] = mod26(sign * minorDet);
            }
        }

        int[][] adjugate = transpose(cofactors);
        int[][] inverse = new int[3][3];

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                inverse[i][j] = mod26(adjugate[i][j] * inverseDeterminant);
            }
        }

        return inverse;
    }

    private static int determinant2x2(int[][] matrix) {
        return matrix[0][0] * matrix[1][1] - matrix[0][1] * matrix[1][0];
    }

    private static int[][] transpose(int[][] matrix) {
        int size = matrix.length;
        int[][] result = new int[size][size];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                result[i][j] = matrix[j][i];
            }
        }

        return result;
    }

    private static int modularInverse(int number, int modulus) {
        for (int i = 1; i < modulus; i++) {
            if ((number * i) % modulus == 1) {
                return i;
            }
        }
        return -1;
    }

    private static int gcd(int a, int b) {
        a = Math.abs(a);
        b = Math.abs(b);

        while (b != 0) {
            int temp = a % b;
            a = b;
            b = temp;
        }

        return a;
    }

    private static int mod26(int value) {
        int result = value % 26;
        if (result < 0) {
            result += 26;
        }
        return result;
    }
}
