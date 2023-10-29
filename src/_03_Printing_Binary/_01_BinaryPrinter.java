package _03_Printing_Binary;

public class _01_BinaryPrinter {
    /*
     * Complete the methods below so they print the passed in parameter in binary.
     * Do not use the Integer.toBinaryString method!
     * Use the main method to test your methods.
     */

    public void printByteBinary(byte b) {
        // We first want to print the bit in the one's place
        for (int i = 7; i >= 0; i--) {
            // Shift b seven bits to the right
            byte bit = (byte) (b >> i);

            // Use the & operator to "mask" the bit in the one's place
            // This can be done by "anding" (&) it with the value of 1
            bit &= 1;

            // Print the result using System.out.print (NOT System.out.println)
            System.out.print(bit);
        }
    }

    public void printShortBinary(short s) {
        // Create 2 byte variables
        byte b1 = (byte) (s >> 8);
        byte b2 = (byte) s;

        // Call printByteBinary twice using the two bytes
        // Make sure they are in the correct order
        printByteBinary(b1);
        printByteBinary(b2);
    }

    public void printIntBinary(int i) {
        // Create 2 short variables
        short s1 = (short) (i >> 16);
        short s2 = (short) i;

        // Call printShortBinary twice using the two short variables
        // Make sure they are in the correct order
        printShortBinary(s1);
        printShortBinary(s2);
    }

    public void printLongBinary(long l) {
        // Create 2 int variables
        int i1 = (int) (l >> 32);
        int i2 = (int) l;

        // Call printIntBinary twice using the two int variables
        // Make sure they are in the correct order
        printIntBinary(i1);
        printIntBinary(i2);
    }

    public static void main(String[] args) {
        _01_BinaryPrinter printer = new _01_BinaryPrinter();
        
        System.out.print("Byte: ");
        printer.printByteBinary((byte) 127);  // Example: 01111111
        System.out.println();
        
        System.out.print("Short: ");
        printer.printShortBinary((short) 32767);  // Example: 0111111111111111
        System.out.println();
        
        System.out.print("Int: ");
        printer.printIntBinary(2147483647);  // Example: 01111111111111111111111111111111
        System.out.println();
        
        System.out.print("Long: ");
        printer.printLongBinary(9223372036854775807L);  // Example: 0111111111111111111111111111111111111111111111111111111111111111
        System.out.println();
    }
}
