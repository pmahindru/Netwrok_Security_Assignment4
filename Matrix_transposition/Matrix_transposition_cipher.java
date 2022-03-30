import java.util.*;
import java.io.*;

public class Matrix_transposition_cipher {
    // making file write public so that i can use in other methods too
    public static FileWriter output;

    public static void main(String[] args) throws IOException {
        Scanner usersInputs = new Scanner(System.in);

        // Read file inputs
        System.out.println();
        System.out.print("Reading a txt file (in .txt file format): ");
        String filename = usersInputs.nextLine().trim();
        File name = new File(filename);
        Scanner inputs = new Scanner(name);

        // taking e or d as input and refers to the encrypt or decrypt from the file input (first line)
        String encrypt_decrypt = inputs.nextLine().trim();

        // taking key from the file input (second line)
        String key = inputs.nextLine().replaceAll("\\(", "").replaceAll("\\)", "");

        // taking plain or cipher text
        String plain_Text_OR_cipher_Text = "";
        while(inputs.hasNextLine()){
            // taking plain text / cipher text (consist of uppercase letters, lowercase letters, numbers)
            // remove all the whitespace and adding % and also remove line break
            plain_Text_OR_cipher_Text += inputs.nextLine().trim().replaceAll("\\n", " ").replaceAll("\\s", "%");
        }
        inputs.close();

        // this is name of the output file and initialize the write with the output_name
        String output_name = filename.replaceAll(".txt","") + "_Output.txt";
        output = new FileWriter(output_name);

        // write file here 
        output.write("e as Encrypt OR d as Decrypt: " + encrypt_decrypt + "\n\n");

        // for the encrypt
        if(encrypt_decrypt.equalsIgnoreCase("e")){
            // write file here 
            output.write("Plain text: " + plain_Text_OR_cipher_Text + "\n\n");

            // get key 
            int[] keyArray = getKey(key);

            // write file here 
            output.write("Secret Key:  " + Arrays.toString(keyArray) + "\n");
            output.write("\n");

            // if first position of KeyArray is -1 then print error other wise give me cipher text 
            if(keyArray[0] != -1){
                // converting plain text to cipher text and write cipher text
                output.write("\nThis is Cipher text: " + generateAndConvertIntoCipher(plain_Text_OR_cipher_Text, keyArray) + "\n\n");
                output.write("END ENCRYPTION (PLAIN-TEXT - CIPHER-TEXT)" + "\n");
            }
            else{
                System.out.println();
                System.out.println("Key Inputs should be more than or equal to 1");
            }
        }
        // for the decrypt
        else if(encrypt_decrypt.equalsIgnoreCase("d")){
            // write file here 
            output.write("Cipher text: " + plain_Text_OR_cipher_Text + "\n\n");

            // get key 
            int[] keyArray = getKey(key);

            // write file here 
            output.write("Secret Key:  " + Arrays.toString(keyArray) + "\n");
            output.write("\n");

            // if first position of KeyArray is -1 then print error other wise give me plain text 
            if(keyArray[0] != -1){
                // converting cipher text to plain text and write plain text
                output.write("\nThis is Plain text: " + generateAndConvertIntoPlain(plain_Text_OR_cipher_Text, keyArray).replaceAll("%", " ") + "\n\n");
                output.write("END DECRYPTION (CIPHER-TEXT - PLAIN-TEXT)\n");
            }
            else{
                System.out.println();
                System.out.println("Key Inputs should be more than or equal to 1");
            }
        }
        else{
            // if the user put other than e or d it will show error
            System.out.println();
            System.out.print("PLEASE PUT e for ENCRYPT OR d FOR DECRYPT ");
        }

        // close the scanner
        usersInputs.close();

        // close file writer 
        output.close();

        System.out.println();
        System.out.println("Output will be in " + output_name);
    }

    // getting user inputs (Key)
    public static int[] getKey(String key) throws IOException {
        //store in string key array then convert into integer Array and then return
        String[] arraykey = key.split(",");
        int[] keyArray = new int[arraykey.length];

        // adding from string Array into integer array 
        for (int i = 0; i < arraykey.length; i++) {
            // converting char to string and then string to integer to add in the integer Array
            int keyinputs = Integer.parseInt(arraykey[i]);

            // checking if the keyinputs should be more 0 and less than length of the key length
            if (keyinputs <= 0 || keyinputs > key.length()) {
                keyArray[i] = 0;
                System.out.println();
                System.out.println("Input Number can't be 0 or less than 0");
                break;
            }
            // add in the integer array
            keyArray[i] = keyinputs;
        }

        // return key Array here
        return keyArray;
    }

    // method 1 plain text to cipher text
    public static String generateAndConvertIntoCipher(String plain_Text, int[] keyArray) throws IOException  {
        // storing max size from the key array.
        int maxKeySize = keyArray[0];
        for(int i = 0; i < keyArray.length; i++){
            if(maxKeySize < keyArray[i]){
                maxKeySize =  keyArray[i];
            }
        }

        // converting string to plain text
        char[] plainTextToCharArray = plain_Text.toCharArray();

        // neww char array
        char[] newCharArray;

        // if the max size key is greater than plain text length then it will make new array with the size of (max size* max size) 
        if(plainTextToCharArray.length < (maxKeySize*maxKeySize)){
            // “Arrays copyof() in java with examples,” GeeksforGeeks, 07-Dec-2018. 
            // [Online]. Available: https://www.geeksforgeeks.org/arrays-copyof-in-java-with-examples/. 
            // [Accessed: 17-Mar-2022]. 
            newCharArray = Arrays.copyOf(plainTextToCharArray , (maxKeySize*maxKeySize));

            // getting the difference b/w new char array and plainTextToCharArray so that we can add that much white space in the end 
            // example if the difference is 3 then it will add 3 white-space in the end
            int difference_Size = newCharArray.length - plainTextToCharArray.length;
            for(int i = 1; i <= difference_Size; i++){
                newCharArray[newCharArray.length-i] = '%';
            }
        }
        else{
            if (maxKeySize % 2 == 0) {
                if(plainTextToCharArray.length % 2 == 1){
                    // “Arrays copyof() in java with examples,” GeeksforGeeks, 07-Dec-2018. 
                    // [Online]. Available: https://www.geeksforgeeks.org/arrays-copyof-in-java-with-examples/. 
                    // [Accessed: 17-Mar-2022]. 
                    // to make it even and add white space in the end
                    newCharArray = Arrays.copyOf(plainTextToCharArray , (plainTextToCharArray.length + 1));
                    int difference_Size = newCharArray.length - plainTextToCharArray.length;
                    for(int i = 1; i <= difference_Size; i++){
                        newCharArray[newCharArray.length-i] = '%';
                    }
                }
                else{
                    newCharArray = Arrays.copyOf(plainTextToCharArray , (plainTextToCharArray.length));
                }
            }
            else{
                if(plainTextToCharArray.length % 2 == 1){
                    // “Arrays copyof() in java with examples,” GeeksforGeeks, 07-Dec-2018. 
                    // [Online]. Available: https://www.geeksforgeeks.org/arrays-copyof-in-java-with-examples/. 
                    // [Accessed: 17-Mar-2022]. 
                    // to make it even and add white space in the end
                    newCharArray = Arrays.copyOf(plainTextToCharArray , (plainTextToCharArray.length + maxKeySize));
                    int difference_Size = newCharArray.length - plainTextToCharArray.length;
                    for(int i = 1; i <= difference_Size; i++){
                        newCharArray[newCharArray.length-i] = '%';
                    }
                }
                else{
                    // “Arrays copyof() in java with examples,” GeeksforGeeks, 07-Dec-2018. 
                    // [Online]. Available: https://www.geeksforgeeks.org/arrays-copyof-in-java-with-examples/. 
                    // [Accessed: 17-Mar-2022]. 
                    // to make it even and add white space in the end
                    newCharArray = Arrays.copyOf(plainTextToCharArray , (plainTextToCharArray.length + 1));
                    int difference_Size = newCharArray.length - plainTextToCharArray.length;
                    for(int i = 1; i <= difference_Size; i++){
                        newCharArray[newCharArray.length-i] = '%';
                    }
                }
            }
        }

        // storing from char array to the 2d Array
        char[][] savePlainTo2DCharArray = new char[newCharArray.length/maxKeySize][maxKeySize];
        int position = 0;

        // writing in the file and adding in the 2d array
        for(int i = 0; i < newCharArray.length/maxKeySize; i++){
            if(i == 0){
                for(int j = 1; j <= maxKeySize; j++ ){
                    output.write("\t" + j);
                }
                output.write("\n");
            }
            for(int k = 0; k < maxKeySize; k++){
                savePlainTo2DCharArray[i][k] = newCharArray[position]; 
                output.write("\t" + savePlainTo2DCharArray[i][k]);
                position++; 
            }
            output.write("\n");
        }
        
        String ciphertext = "";
        // adding in the cipher text
        for(int i = 0; i < keyArray.length; i++){
            for(int j = 0; j < newCharArray.length/maxKeySize; j++){
                ciphertext +=  savePlainTo2DCharArray[j][keyArray[i]-1];
            }
        }

        // retrun cipher text
        return ciphertext;
    }

    // method 2 cipher text to plain text
    public static String generateAndConvertIntoPlain(String cipher_Text, int[] keyArray) throws IOException  {
        // storing max size from the key array.
        int maxKeySize = keyArray[0];
        for(int i = 0; i < keyArray.length; i++){
            if(maxKeySize < keyArray[i]){
                maxKeySize =  keyArray[i];
            }
        }

        // diivide length of cipher text and max key size so that we can get the row 
        int divivdeCipherTextRowText = cipher_Text.length()/maxKeySize;
        char[] cipherTextToCharArray = cipher_Text.toCharArray();
        char[][] savePlainTo2DCharArray = new char[divivdeCipherTextRowText][maxKeySize];

        int position = 0;
        for(int i = 0; i < maxKeySize; i++){
            if(i == 0){
                for(int j = 1; j <= maxKeySize; j++ ){
                    output.write("\t" + j);
                }
                output.write("\n");
            }
            for(int k = 0; k < divivdeCipherTextRowText; k++){
                savePlainTo2DCharArray[k][keyArray[i] - 1] = cipherTextToCharArray[position]; 
                position++; 
            }
        }   

        // store in the plain text and the print the plain text
        String plainText = "";
        for(int i = 0; i < divivdeCipherTextRowText; i++){
            for(int j = 0; j < maxKeySize; j++){
                plainText += savePlainTo2DCharArray[i][j];
                output.write("\t" + savePlainTo2DCharArray[i][j]);
            }
            output.write("\n");
        }

        // return plain text
        return plainText;
    }
}