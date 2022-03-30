import java.util.*;
import java.io.*;

public class Playfair_substitution_cipher{
    // making file write public so that i can use in other methods too
    public static FileWriter output;

    // making public boolean for checking I and J letters because they are same and no need to repeat them in the key 
    // checkJ is for checking J and not add I
    // check2I is for checking I and not add J
    public static boolean checkJ = false; 
    public static boolean check2I = false;

    public static void main(String[] args) throws IOException {
        Scanner usersInputs = new Scanner(System.in);

        // Read file inputs
        System.out.println();
        System.out.print("Reading a txt file (in .txt file format): ");
        String filename = usersInputs.nextLine().trim();
        File name = new File(filename);
        Scanner inputs = new Scanner(name);

        // taking key from the file input (first line) and changing into uppercase and remove all whitespace
        String key = inputs.nextLine().toUpperCase().replaceAll("\\s", "");

        // taking e or d as input and refers to the encrypt or decrypt from the file input (second line)
        String encrypt_decrypt = inputs.nextLine().trim();

        // taking plain or cipher text
        String plain_Text_OR_cipher_Text = "";
        while(inputs.hasNextLine()){
            // taking plain text / Cipher text input
            // changing into uppercase and remove all whitespace, punctuations (\\w remove all non-word char) and line break
            plain_Text_OR_cipher_Text += inputs.nextLine().trim().toUpperCase().replaceAll("\\s", "").replaceAll("\\W", "").replaceAll("\\n", "");
        }
        inputs.close();

        // this is name of the output file and initialize the write with the output_name
        String output_name = filename.replaceAll(".txt","") + "_Output.txt";
        output = new FileWriter(output_name);

        // write file here 
        output.write("Secret Key:  " + key + "\n\n");
        output.write("e as Encrypt OR d as Decrypt: " + encrypt_decrypt + "\n\n");
        
        // for the encrypt
        if(encrypt_decrypt.equalsIgnoreCase("e")){
            output.write("Plain text: " + plain_Text_OR_cipher_Text + "\n\n");

            // generate key and store in the 2D Array called keyArray
            char[][] keyArray =  generateKey(key);
            output.write("\n");
    
            // converting plain text to cipher text and write cipher text in the output file 
            output.write("\n\nThis is Cipher text\n\n" + generateParisAndConvertIntoCipher(plain_Text_OR_cipher_Text, keyArray));
            output.write("\n\n" + "END ENCRYPTION (PLAIN-TEXT - CIPHER-TEXT)\n");
        }
        // for the decrypt
        else if(encrypt_decrypt.equalsIgnoreCase("d")){
            output.write("Cipher text: " + plain_Text_OR_cipher_Text + "\n\n");

            // generate key and store in the 2D Array called keyArray
            char[][] keyArray =  generateKey(key);
            output.write("\n");

            // converting cipher text to plain text and write plain text
            output.write("\n\nThis is Plain text\n\n" + generateParisAndConvertIntoPlain(plain_Text_OR_cipher_Text, keyArray));
            output.write("\n\n" + "END DECRYPTION (CIPHER-TEXT - PLAIN-TEXT)\n");
        }
        else{
            // if the user put other than e or d it will show error
            System.out.println("  PLEASE PUT e for ENCRYPT OR d FOR DECRYPT  ");
        }
        // close the scanner
        usersInputs.close();

        // close file writer 
        output.close();

        System.out.println();
        System.out.println("Output will be in " + output_name);
    }

    // generate key and write then return the 2D Array (chars) method 1
    public static char[][] generateKey(String key)throws IOException {
        // Key String
        String KeyAlphabetString = key + "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

        // converting key String to Char Array of key
        char[] KeyAlphabetArray = KeyAlphabetString.toCharArray();

        // New Array with no duplicate char
        char[] newArrayOfKeyAlphabet = new char[26];

        int addingElement = 0;
        int count = 0;
        
        // store char in the new array without repeating them
        // Refer to the given link
        // “Remove duplicates from a given string,” GeeksforGeeks, 23-Feb-2022. 
        // [Online]. Available: https://www.geeksforgeeks.org/remove-duplicates-from-a-given-string/. 
        // [Accessed: 15-Mar-2022]. 
        for(int i = 0; i < KeyAlphabetArray.length; i++){
            // if char is J and check2I is false then checkJ is true
            if(KeyAlphabetArray[i] == 'J' && check2I == false){
                checkJ = true;
            }
            // if char is I and checkJ is false then check2I is true
            if(KeyAlphabetArray[i] == 'I' && checkJ == false){
                check2I = true;
            }

            // checking if char is already present in the array 
            // then break the loop and count wiil be 0
            for(int j = 0; j < i; j++){
                if(KeyAlphabetArray[i] == KeyAlphabetArray[j]){
                    count=0;
                    break;
                }
                count++;
            }

            // i equals count then add otherwise go to next char
            if(i == count){
                // if checkJ is true AND char is I it wont add 
                // because I and J are same (it is already saved J)
                if(checkJ){
                    if(KeyAlphabetArray[i] != 'I'){
                        newArrayOfKeyAlphabet[addingElement++] =  KeyAlphabetArray[i];
                        count=0;
                    }
                }
                // if check2I is true AND char is 'J' it wont add
                // because I and J are same (it is already saved I)
                else if(check2I){
                    if(KeyAlphabetArray[i] != 'J'){
                        newArrayOfKeyAlphabet[addingElement++] =  KeyAlphabetArray[i];
                        count=0;
                    }
                }
                else{
                    newArrayOfKeyAlphabet[addingElement++] =  KeyAlphabetArray[i];
                    count=0;
                }
                count=0;
            }
        }

        // making 2D Array of char 
        char[][] keyArray = new char[5][5];

        // after removing duplicate elements it will store in the 2D char Array called keyArray
        int position = 0; 
        for(int i = 0; i < keyArray.length;i++){
            for(int j=0; j < keyArray.length; j++){
                keyArray[i][j] = newArrayOfKeyAlphabet[position];
                position++;
            }
        }

        // write keyArray (2D Array of char) in the output file
        output.write("This is Key Array\n\n");
        for(int i = 0; i<keyArray.length;i++){
            for(int j=0; j<keyArray.length; j++){
                output.write("\t" + keyArray[i][j]);
            }
            output.write("\n");
        }
    
        // return the keyArray (2D Array of char)
        return keyArray;
    }

    // plain text to cipher text ENCRYPTION (Method 2)
    public static String generateParisAndConvertIntoCipher(String plain_Text, char[][] keyArray)throws IOException {
        String plaintextString = "";

        // index starting at 1 to check if first and second element are same, if yes then add X or Q
        int index = 1;
        for(int i = 0; i < plain_Text.length(); i++){
            if(i < plain_Text.length()-1){
                if(plain_Text.charAt(i) == plain_Text.charAt(index)){
                    // if the element like XX then it will split by Q, if not then it will split by X
                    if(plain_Text.charAt(i) == 'X' && plain_Text.charAt(index) == 'X' ){
                        plaintextString +=  plain_Text.charAt(i) + "Q";
                    }
                    else{
                        plaintextString +=  plain_Text.charAt(i) + "X";
                    }
                }
                else{
                    plaintextString +=  plain_Text.charAt(i);
                }
            }
            else{
                plaintextString +=  plain_Text.charAt(i);
            }
            index++;
        }

        // if the length of plain text is odd then add Z in the end
        if(plaintextString.length() % 2 == 1){
            // if the last letter is Z then add X in the end
            if(plaintextString.charAt(plaintextString.length()-1) == 'Z'){
                plaintextString += "X";
            }
            // else add Z
            else{
                plaintextString += "Z";
            }
        }

        // save cipher text
        String cipherText = "";

        //writing pair of the plain-Text 
        output.write("This is Plain Text\n\n");

        index = 1;
        for (int i = 0; i < plaintextString.length(); i++) {
            // getting position as row and column of each char in pair
            int pRFirstletter = positionRow(plaintextString.charAt(i), keyArray);
            int pCFirstletter = positionColumn(plaintextString.charAt(i), keyArray);
            int pRSecondletter =positionRow(plaintextString.charAt(index), keyArray);
            int pCSecondletter =positionColumn(plaintextString.charAt(index), keyArray);

            // rows and columns are not same then add in the cipher text (just swap the column to get other side)
            if(pRFirstletter != pRSecondletter && pCFirstletter != pCSecondletter){
                cipherText += keyArray[pRFirstletter][pCSecondletter] + "" + keyArray[pRSecondletter][pCFirstletter] + "\t";
            }
            else{   
                // if rows are same then add 1 in columns 
                // if columns is 5 then we will set that specific column to 0 
                if(pRFirstletter == pRSecondletter){
                    pCFirstletter++;
                    pCSecondletter++;

                    if(pCFirstletter > 4){
                        pCFirstletter = 0;
                    }
                    if(pCSecondletter > 4){
                        pCSecondletter = 0;
                    }
                    // then add to cipher text
                    cipherText += keyArray[pRFirstletter][pCFirstletter] + "" + keyArray[pRSecondletter][pCSecondletter] + "\t";
                }
                // if columns are same then add 1 in rows
                // if rows is 5 then we will set that specific row to 0 
                else{
                    pRFirstletter++;    
                    pRSecondletter++;

                    if(pRFirstletter > 4){
                        pRFirstletter = 0;
                    }
                    if(pRSecondletter > 4){
                        pRSecondletter = 0;
                    }
                    // then add to cipher text
                    cipherText +=  keyArray[pRFirstletter][pCFirstletter] + "" + keyArray[pRSecondletter][pCSecondletter] + "\t";
                }
            }
            // writing pairs of the plain text
            output.write(plaintextString.charAt(i) + "" + plaintextString.charAt(index) + "\t");
            index += 2;
            i++;
        }

        // return cipher text
        return cipherText;
    }

    // cipher text to plain text (Method 3)
    private static String generateParisAndConvertIntoPlain(String cipher_Text, char[][] keyArray)throws IOException {
        String plainText = "";

        //writing pair of the Cipher-Text 
        output.write("This is Cipher Text\n\n");
        
        int index = 1;
        for (int i = 0; i < cipher_Text.length(); i++) {
            // getting position as row and column of each char in pair
            int pRFirstletter = positionRow(cipher_Text.charAt(i), keyArray);
            int pCFirstletter = positionColumn(cipher_Text.charAt(i), keyArray);
            int pRSecondletter =positionRow(cipher_Text.charAt(index), keyArray);
            int pCSecondletter =positionColumn(cipher_Text.charAt(index), keyArray);

            // rows and columns are not same then add in the plain text (just swap the column to get other side)
            if(pRFirstletter != pRSecondletter && pCFirstletter != pCSecondletter){
                plainText += keyArray[pRFirstletter][pCSecondletter] + "" + keyArray[pRSecondletter][pCFirstletter] + "\t";
            }
            else{   
                // if rows are same then subtract 1 in columns 
                // if columns is -1 then we will set that specific column to 4 
                if(pRFirstletter == pRSecondletter){
                    pCFirstletter--;
                    pCSecondletter--;
                    if(pCFirstletter < 0){
                        pCFirstletter = 4;
                    }
                    if(pCSecondletter < 0){
                        pCSecondletter = 4;
                    }
                    // then add to plain text
                    plainText += keyArray[pRFirstletter][pCFirstletter] + "" + keyArray[pRSecondletter][pCSecondletter] + "\t";
                }
                // if columns are same then subtract 1 in rows
                // if rows is -1 then we will set that specific row to 4 
                else{
                    pRFirstletter--; 
                    pRSecondletter--;

                    if(pRFirstletter < 0){
                        pRFirstletter = 4;
                    }
                    if(pRSecondletter < 0){
                        pRSecondletter = 4;
                    }
                    // then add to plain text
                    plainText +=  keyArray[pRFirstletter][pCFirstletter] + "" + keyArray[pRSecondletter][pCSecondletter] + "\t";
                }
            }

            // writing pairs of the cipher text
            output.write(cipher_Text.charAt(i) + "" + cipher_Text.charAt(index) + "\t");
            index += 2;
            i++;
        }

        // return plain text
        return plainText;
    }

    // get row position
    public static int positionRow(char letter, char[][] keyArray){
        int row = 0;
        for(int i = 0; i<keyArray.length;i++){
            for(int j=0; j<keyArray.length; j++){
                // if letter is I and if checkJ is true then it will return the position of the J
                if(letter == 'I'){
                    if(checkJ){
                        if(keyArray[i][j] == 'J'){
                            row += i;
                            break;
                        }
                    }
                    // if checkJ is false then it will return the position of the I
                    else{
                        if(keyArray[i][j] == letter){
                            row += i;
                            break;
                        }
                    }
                }
                // if letter is J and if check2I is true then it will return the position of the I
                else if(letter == 'J'){
                    if(check2I){
                        if(keyArray[i][j] == 'I'){
                            row += i;
                            break;
                        }
                    }
                    // if check2I is false then it will return the position of the J
                    else{
                        if(keyArray[i][j] == letter){
                            row += i;
                            break;
                        }
                    } 
                }
                else{
                    if(keyArray[i][j] == letter){
                        row += i;
                        break;
                    }
                }
            }
        }
        return row;
    }

    // get column position 
    public static int positionColumn(char letter, char[][] keyArray){
        int column = 0;
        for(int i = 0; i<keyArray.length;i++){
            for(int j=0; j<keyArray[i].length; j++){
                // if letter is I and if checkJ is true then it will return the position of the J 
                if(letter == 'I'){
                    if(checkJ){
                        if(keyArray[i][j] == 'J'){
                            column += j;
                            break;
                        }
                    }
                    // if checkJ is false then it will return the position of the I
                    else{
                        if(keyArray[i][j] == letter){
                            column += j;
                            break;
                        }
                    }
                }
                // if letter is J and if check2I is true then it will return the position of the I
                else if(letter == 'J'){
                    if(check2I){
                        if(keyArray[i][j] == 'I'){
                            column += j;
                            break;
                        }
                    }
                    // if check2I is false then it will return the position of the J
                    else{
                        if(keyArray[i][j] == letter){
                            column += j;
                            break;
                        }
                    } 
                }
                else{
                    if(keyArray[i][j] == letter){
                        column += j;
                        break;
                    }
                }
            }
        }
        return column;
    }
}