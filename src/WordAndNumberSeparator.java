import java.util.ArrayList;
import java.util.Scanner;

public class WordAndNumberSeparator {

    public static void main(String[] args) {
        new WordAndNumberSeparator().run();
    }

    void run() {
        System.out.print("Enter a text: ");
        String input = new Scanner(System.in).nextLine() + " "; //last space char is for detect the last word in input.

        ArrayList<String> strArray = new ArrayList<>();
        ArrayList<Integer> intArray = new ArrayList<>();

        StringBuilder tempStr = new StringBuilder();

        for (int i = 0; i < input.length(); i++) {
            //this run till a space char and memorize the string till that space character.
            if (input.charAt(i) != 32)
                tempStr.append(input.charAt(i));//if the char is not space then keep adding char to stringBuilder.
            else {//if the char is space
                //tempStr is a word or number.
                try {//if number then there is no exception.
                    intArray.add(Integer.parseInt(tempStr.toString()));
                } catch (Exception e) {//if not a number raise an exception and store it in strArray.
                    //checks for redundant space char.
                    if (!tempStr.isEmpty()) strArray.add(tempStr.toString());
                }
                tempStr.delete(0, tempStr.length());//clear temp string
            }
        }

        StringBuilder resultStr = new StringBuilder();
        StringBuilder resultInt = new StringBuilder();

        for (String i : strArray) resultStr.append(i).append(" ");//this will add a space char after a word.
        for (int i : intArray) resultInt.append(i).append(" ");//this will add a space char after a number.

        if (!resultStr.isEmpty()) {
            resultStr.deleteCharAt(resultStr.length() - 1); //remove last space char.
            System.out.println("\nmyString is: " + resultStr);
        }
        if (!resultInt.isEmpty()) {
            resultInt.deleteCharAt(resultInt.length() - 1);//remove last space char.
            System.out.println("myInt is: " + resultInt);
        }
        if (resultStr.isEmpty() && resultInt.isEmpty()) System.out.println("There is no number or word.");

    }

}