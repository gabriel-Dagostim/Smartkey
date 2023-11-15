package fag.gh.demo.tools;

public class TextTools {

    public static String CheckNullField(String word) {
        String removeNullSpace = "";
        for (int i = 0; i < word.length(); i++) {
            char letter = word.charAt(i);
            if (letter != '\0') {
                removeNullSpace += letter;
            }
        }

        return removeNullSpace;
    }
}
