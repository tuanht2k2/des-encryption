import java.util.Arrays;

public class DES {
    private static final int[] IP = { 58, 50, 42, 34, 26, 18, 10, 2,
            60, 52, 44, 36, 28, 20, 12, 4,
            62, 54, 46, 38, 30, 22, 14, 6,
            64, 56, 48, 40, 32, 24, 16, 8,
            57, 49, 41, 33, 25, 17, 9, 1,
            59, 51, 43, 35, 27, 19, 11, 3,
            61, 53, 45, 37, 29, 21, 13, 5,
            63, 55, 47, 39, 31, 23, 15, 7 };

    private static final int[] PC1 = { 57, 49, 41, 33, 25, 17, 9,
            1, 58, 50, 42, 34, 26, 18,
            10, 2, 59, 51, 43, 35, 27,
            19, 11, 3, 60, 52, 44, 36,
            63, 55, 47, 39, 31, 23, 15,
            7, 62, 54, 46, 38, 30, 22,
            14, 6, 61, 53, 45, 37, 29,
            21, 13, 5, 28, 20, 12, 4 }; // 56 bit

    private static final int[] PC2 = { 14, 17, 11, 24, 1, 5,
            3, 28, 15, 6, 21, 10,
            23, 19, 12, 4, 26, 8,
            16, 7, 27, 20, 13, 2,
            41, 52, 31, 37, 47, 55,
            30, 40, 51, 45, 33, 48,
            44, 49, 39, 56, 34, 53,
            46, 42, 50, 36, 29, 32 }; // 48 bit

    private static final int[] E_BIT_SELECTION = { 32, 1, 2, 3, 4, 5,
            4, 5, 6, 7, 8, 9,
            8, 9, 10, 11, 12, 13,
            12, 13, 14, 15, 16, 17,
            16, 17, 18, 19, 20, 21,
            20, 21, 22, 23, 24, 25,
            24, 25, 26, 27, 28, 29,
            28, 29, 30, 31, 32, 1 };

    private static final int[][][] S_BOX = {
            {
                    {14, 4, 13, 1, 2, 15, 11, 8, 3, 10, 6, 12, 5, 9, 0, 7},
                    {0, 15, 7, 4, 14, 2, 13, 1, 10, 6, 12, 11, 9, 5, 3, 8},
                    {4, 1, 14, 8, 13, 6, 2, 11, 15, 12, 9, 7, 3, 10, 5, 0},
                    {15, 12, 8, 2, 4, 9, 1, 7, 5, 11, 3, 14, 10, 0, 6, 13}
            },
            {
                    {15, 1, 8, 14, 6, 11, 3, 4, 9, 7, 2, 13, 12, 0, 5, 10},
                    {3, 13, 4, 7, 15, 2, 8, 14, 12, 0, 1, 10, 6, 9, 11, 5},
                    {0, 14, 7, 11, 10, 4, 13, 1, 5, 8, 12, 6, 9, 3, 2, 15},
                    {13, 8, 10, 1, 3, 15, 4, 2, 11, 6, 7, 12, 0, 5, 14, 9}
            },
            {
                    {10, 0, 9, 14, 6, 3, 15, 5, 1, 13, 12, 7, 11, 4, 2, 8},
                    {13, 7, 0, 9, 3, 4, 6, 10, 2, 8, 5, 14, 12, 11, 15, 1},
                    {13, 6, 4, 9, 8, 15, 3, 0, 11, 1, 2, 12, 5, 10, 14, 7},
                    {1, 10, 13, 0, 6, 9, 8, 7, 4, 15, 14, 3, 11, 5, 2, 12}
            },
            {
                    {7, 13, 14, 3, 0, 6, 9, 10, 1, 2, 8, 5, 11, 12, 4, 15},
                    {13, 8, 11, 5, 6, 15, 0, 3, 4, 7, 2, 12, 1, 10, 14, 9},
                    {10, 6, 9, 0, 12, 11, 7, 13, 15, 1, 3, 14, 5, 2, 8, 4},
                    {3, 15, 0, 6, 10, 1, 13, 8, 9, 4, 5, 11, 12, 7, 2, 14}
            },
            {
                    {2, 12, 4, 1, 7, 10, 11, 6, 8, 5, 3, 15, 13, 0, 14, 9},
                    {14, 11, 2, 12, 4, 7, 13, 1, 5, 0, 15, 10, 3, 9, 8, 6},
                    {4, 2, 1, 11, 10, 13, 7, 8, 15, 9, 12, 5, 6, 3, 0, 14},
                    {11, 8, 12, 7, 1, 14, 2, 13, 6, 15, 0, 9, 10, 4, 5, 3}
            },
            {
                    {12, 1, 10, 15, 9, 2, 6, 8, 0, 13, 3, 4, 14, 7, 5, 11},
                    {10, 15, 4, 2, 7, 12, 9, 5, 6, 1, 13, 14, 0, 11, 3, 8},
                    {9, 14, 15, 5, 2, 8, 12, 3, 7, 0, 4, 10, 1, 13, 11, 6},
                    {4, 3, 2, 12, 9, 5, 15, 10, 11, 14, 1, 7, 6, 0, 8, 13}
            },
            {
                    {4, 11, 2, 14, 15, 0, 8, 13, 3, 12, 9, 7, 5, 10, 6, 1},
                    {13, 0, 11, 7, 4, 9, 1, 10, 14, 3, 5, 12, 2, 15, 8, 6},
                    {1, 4, 11, 13, 12, 3, 7, 14, 10, 15, 6, 8, 0, 5, 9, 2},
                    {6, 11, 13, 8, 1, 4, 10, 7, 9, 5, 0, 15, 14, 2, 3, 12}
            },
            {
                    {13, 2, 8, 4, 6, 15, 11, 1, 10, 9, 3, 14, 5, 0, 12, 7},
                    {1, 15, 13, 8, 10, 3, 7, 4, 12, 5, 6, 11, 0, 14, 9, 2},
                    {7, 11, 4, 1, 9, 12, 14, 2, 0, 6, 10, 13, 15, 3, 5, 8},
                    {2, 1, 14, 7, 4, 10, 8, 13, 15, 12, 9, 0, 3, 5, 6, 11}
            }
    };

    private static final int[] P = { 16, 7, 20, 21,
            29, 12, 28, 17,
            1, 15, 23, 26,
            5, 18, 31, 10,
            2, 8, 24, 14,
            32, 27, 3, 9,
            19, 13, 30, 6,
            22, 11, 4, 25 };

    private static final int[] FP = { 40, 8, 48, 16, 56, 24, 64, 32,
            39, 7, 47, 15, 55, 23, 63, 31,
            38, 6, 46, 14, 54, 22, 62, 30,
            37, 5, 45, 13, 53, 21, 61, 29,
            36, 4, 44, 12, 52, 20, 60, 28,
            35, 3, 43, 11, 51, 19, 59, 27,
            34, 2, 42, 10, 50, 18, 58, 26,
            33, 1, 41, 9, 49, 17, 57, 25 };

    private static final int BLOCK_SIZE = 64;
    private static final int KEY_SIZE = 56;
    private static final int SUBKEY_SIZE = 48;
    private static final int HALF_BLOCK_SIZE = BLOCK_SIZE / 2;
    private static final int ROUND_COUNT = 16;

    public static void main(String[] args) {
        // Example usage
//        String plaintext = "hello ae nhe";
//        String key = "12341e71";
//        String encrypted = encrypt(plaintext, key);
//        System.out.println("Encrypted: " + encrypted);
//        String decrypted = decrypt(encrypted, key);
//        decrypted = decrypted.replaceAll("\u0000", "");
//        System.out.println("Decrypted: " + decrypted);
    }



    public static String encrypt (String plainText, String key) {

        if (plainText.isEmpty()) {
            System.out.println("Chuoi rong");
            return "";
        }

        String plaintextBinary = toBinaryString(plainText);
        while (plaintextBinary.length() % 64 != 0) {
            plaintextBinary = "0" + plaintextBinary;
        }

        String keyBinary = toBinaryString(key);

        if (keyBinary.length() != 64) {
            System.out.println("Key is invalid");
            return "";
        }

        int[][] subkeys = generateSubKeys(keyBinary);


        int block = 0;
        StringBuilder cipherText = new StringBuilder();

        while (block < plaintextBinary.length()) {
            String blockPlainTextBinary = plaintextBinary.substring(block, block + 64);
            String blockEncrypted = encryptBlock(blockPlainTextBinary, subkeys);
            cipherText.append(blockEncrypted);
            block += 64;
        }

        return fromBinaryString(cipherText.toString());

    }

    public static String decrypt (String cipherText, String key) {

        if (cipherText.isEmpty()) {
            System.out.println("Chuoi rong");
            return "";
        }

        String cipherTextBinary = toBinaryString(cipherText);
        while (cipherTextBinary.length() % 64 != 0) {
            cipherTextBinary = "0" + cipherTextBinary;
        }

        String keyBinary = toBinaryString(key);

        if (keyBinary.length() != 64) {
            System.out.println("Key is invalid");
            return "";
        }

        int[][] subkeys = generateSubKeys(keyBinary);

        int block = 0;
        StringBuilder plainText = new StringBuilder();

        while (block < cipherTextBinary.length()) {
            String blockCipherTextBinary = cipherTextBinary.substring(block, block + 64);
            String blockEncrypted = decryptBlock(blockCipherTextBinary, subkeys);
            plainText.append(blockEncrypted);
            block += 64;
        }

        return fromBinaryString(plainText.toString());

    }

    public static String encryptBlock(String plaintextBinary, int[][] subkeys) {

        String permutedText = permute(plaintextBinary, IP);

        String leftHalf = permutedText.substring(0, HALF_BLOCK_SIZE);
        String rightHalf = permutedText.substring(HALF_BLOCK_SIZE);

        for (int round = 0; round < ROUND_COUNT; round++) {
            String temp = rightHalf;
            rightHalf = xor(leftHalf, feistel(rightHalf, subkeys[round]));
            leftHalf = temp;
        }

        // Final permutation
        String ciphertextBinary = permute(rightHalf + leftHalf, FP);

        // Convert binary ciphertext to string
//        return fromBinaryString(ciphertextBinary);
        return ciphertextBinary;
    }

    public static String decryptBlock(String cipherTextBinary, int[][] subkeys) {


        String permutedText = permute(cipherTextBinary, IP);

        String rightHalf = permutedText.substring(0, HALF_BLOCK_SIZE);
        String leftHalf = permutedText.substring(HALF_BLOCK_SIZE);

        for (int round = ROUND_COUNT - 1; round >= 0; round--) {
            String temp = leftHalf;
            leftHalf = xor(rightHalf, feistel(leftHalf, subkeys[round]));
            rightHalf = temp;
        }

        String plaintextBinary = permute(leftHalf + rightHalf, FP);

        return plaintextBinary;
    }

    private static String toBinaryString(String input) {
        StringBuilder binaryString = new StringBuilder();
        for (char c : input.toCharArray()) {
            String binaryChar = Integer.toBinaryString(c);
            while (binaryChar.length() < 8) {
                binaryChar = "0" + binaryChar;
            }
            binaryString.append(binaryChar);
        }
        return binaryString.toString();
    }

    private static String fromBinaryString(String input) {
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < input.length(); i += 8) {
            String byteString = input.substring(i, i + 8);
            int byteValue = Integer.parseInt(byteString, 2);
            text.append((char) byteValue);
        }
        return text.toString();
    }

    private static String permute(String input, int[] permutationTable) {
        StringBuilder permutedString = new StringBuilder();
        for (int index : permutationTable) {
            permutedString.append(input.charAt(index - 1));
        }
        return permutedString.toString();
    }

    private static String xor(String a, String b) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < a.length(); i++) {
            result.append(a.charAt(i) ^ b.charAt(i));
        }
        return result.toString();
    }

    private static String feistel(String rightHalf, int[] subkey) {
// Expand right half
        String expanded = permute(rightHalf, E_BIT_SELECTION); // 32 bits to 48 bits

        // XOR with subkey
        StringBuilder stringSubkey = new StringBuilder();
        for (int number : subkey) {
            stringSubkey.append(number);
        }
        String xored = xor(expanded, stringSubkey.toString());

        // Apply S-boxes
        StringBuilder sBoxOutput = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            int start = i * 6;
            if (start + 6 <= xored.length()) {
                String binaryStr = xored.substring(start, start + 6); // Lấy 6 ký tự từ xored
                int row = Integer.parseInt(binaryStr.substring(0, 1) + binaryStr.substring(5), 2);
                int col = Integer.parseInt(binaryStr.substring(1, 5), 2);
                int value = S_BOX[i][row][col];
                String binaryValue = Integer.toBinaryString(value);
                while (binaryValue.length() < 4) {
                    binaryValue = "0" + binaryValue;
                }
                sBoxOutput.append(binaryValue);
            }
        }
//         Permute using P-box
        return permute(sBoxOutput.toString(), P);
    }

    private static int[][] generateSubKeys(String key) {
        int[][] subkeys = new int[ROUND_COUNT][SUBKEY_SIZE];

        // Permute key using PC1
        String permutedKey = permute(key, PC1);

        // Split the permuted key into left and right halves
        String leftHalf = permutedKey.substring(0, KEY_SIZE / 2);
        String rightHalf = permutedKey.substring(KEY_SIZE / 2);

        // Generate subkeys for each round
        for (int round = 0; round < ROUND_COUNT; round++) {
            // Circular left shift

            leftHalf = leftShift(leftHalf, round);
            rightHalf = leftShift(rightHalf, round);

            String combinedKey = leftHalf + rightHalf;
            // Permute using PC2 to generate subkey
            subkeys[round] = Arrays.stream(permute(combinedKey, PC2).split(""))
                    .mapToInt(Integer::parseInt)
                    .toArray();
        }

        return subkeys;
    }

    private static String leftShift(String input, int round) {
        int[] shifts = {1, 1, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 1};
        int shiftAmount = shifts[round];
        return input.substring(shiftAmount) + input.substring(0, shiftAmount);
    }
}
