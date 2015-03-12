public class PermutationUtil {

    public static void permuteString(String beginningString, String endingString, BruteForce handler, String finalResult) {
        if (endingString.length() <= 1) {
            handler.handlePermutationResult(beginningString + endingString);
            if (finalResult.equals(beginningString + endingString)) {
                handler.printResults();
            }
        } else {
            for (int i = 0; i < endingString.length(); i++) {
                String newString = endingString.substring(0, i) + endingString.substring(i + 1);
                permuteString(beginningString + endingString.charAt(i), newString, handler, finalResult);
            }
        }
    }
}
