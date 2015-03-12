import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BruteForce {

    private static Logger logger = Logger.getLogger(BruteForce.class.getName());

    private Map<String, List<Char>> addendsMapping = new HashMap<>();
    private String resultWord;
    private List<Char> resultChars;

    private final List<Integer> possibleValuesForFirstChar = Arrays.asList(2, 3, 4, 5, 6, 7, 8, 9);
    private final List<Integer> possibleValuesForRestChars = Arrays.asList(0, 2, 3, 4, 5, 6, 7, 8, 9);

    private List<Char> recurringCharactersList;

    private List<Integer> usedIntegers = new ArrayList<>(10);

    private StringBuilder results = new StringBuilder();

    private void searchResults(String equation) {
        String[] equationSides = equation.split("\\=");
        resultWord = equationSides[1];
        String[] addends = equationSides[0].split("\\+");

        Map<Character, Char> recurringCharacters = new HashMap<>();

        breakAddendsOnChars(addends, recurringCharacters);
        breakResultWordOnChars(recurringCharacters);

        recurringCharactersList = new ArrayList<>(recurringCharacters.values());

        String permutationTarget = buildPermutationInputString();
        String permutationTargetReverse = new StringBuilder(permutationTarget).reverse().toString();
        PermutationUtil.permuteString("", permutationTarget, this, permutationTargetReverse);
    }

    public void handlePermutationResult(String result) {
        checkProcessedResult(result);
    }

    public void printResults() {
        logger.log(Level.INFO, new Formatter().format("\nFollowing are results of calculation:\n%s", results.toString()).toString());
    }

    private void breakAddendsOnChars(String[] addends, Map<Character, Char> recurringCharacters) {
        for (String word : addends) {
            boolean isFirstChar = true;

            char[] chars = word.toCharArray();

            List<Char> charObjects = new ArrayList<>();

            for (int i = 0; i < chars.length; i++) {
                Character character = chars[i];
                Char ch;

                if (!recurringCharacters.containsKey(character)) {
                    ch = new Char();
                    ch.setCharacter(character);
                    if (isFirstChar) {
                        ch.setPossibleValues(possibleValuesForFirstChar);
                        isFirstChar = false;
                    } else {
                        ch.setPossibleValues(possibleValuesForRestChars);
                    }
                    recurringCharacters.put(character, ch);
                } else {
                    ch = recurringCharacters.get(character);
                }

                charObjects.add(ch);
            }

            addendsMapping.put(word, charObjects);
        }
    }

    private void breakResultWordOnChars(Map<Character, Char> recurringCharacters) {
        char[] chars = resultWord.toCharArray();
        Boolean isFirstChar = true;

        resultChars = new ArrayList<>();

        for (int i = 0; i < chars.length; i++) {
            Character character = chars[i];
            Char ch;

            if (!recurringCharacters.containsKey(character)) {
                ch = new Char();
                ch.setCharacter(character);

                if (isFirstChar) {
                    isFirstChar = false;
                } else {
                    ch.setPossibleValues(possibleValuesForRestChars);
                    recurringCharacters.put(character, ch);
                }

            } else {
                ch = recurringCharacters.get(character);
            }

            resultChars.add(ch);
        }
    }

    private String buildPermutationInputString() {
        Iterator<Char> iterator = recurringCharactersList.iterator();
        StringBuilder builder = new StringBuilder();

        while (iterator.hasNext()) {
            Char ch = iterator.next();
            Integer value = ch.nextPossibleValue(usedIntegers);
            if (value != null) {
                usedIntegers.add(value);
                builder.append(value);
            } else {
                throw new NullPointerException("Wrong possible integer value obtained for char: " + ch.getCharacter());
            }
        }

        return builder.toString();
    }

    private void checkProcessedResult(String input) {
        Map<Character, Integer> tempCharIntMapping = new HashMap<>();

        for (int i = 0; i < input.length(); i++) {
            Character currentChar = input.charAt(i);
            Char ch = recurringCharactersList.get(i);
            tempCharIntMapping.put(ch.getCharacter(), Integer.valueOf(currentChar.toString()));
        }

        List<String> addends = new ArrayList<>();

        Integer sum = 0;

        for (String key : addendsMapping.keySet()) {
            List<Char> chars = addendsMapping.get(key);

            StringBuilder addendString = new StringBuilder();

            for (int i = 0; i < chars.size(); i++) {
                Char ch = chars.get(i);
                int possibleValue = tempCharIntMapping.get(ch.getCharacter());
                if (!ch.getPossibleValues().contains(possibleValue)) return;
                addendString.append(tempCharIntMapping.get(ch.getCharacter()));
            }

            addends.add(addendString.toString());
            sum += Integer.valueOf(addendString.toString());
        }

        Iterator<Char> resultIterator = resultChars.iterator();
        StringBuilder resultString = new StringBuilder();
        resultString.append(1);
        resultIterator.next();
        while (resultIterator.hasNext()) {
            Char ch = resultIterator.next();
            resultString.append(tempCharIntMapping.get(ch.getCharacter()));
        }

        Integer result = Integer.valueOf(resultString.toString());

        if (sum.equals(result)) {
            saveResult(addends, result);
        }

    }

    private void saveResult(List<String> addends, Integer sum) {
        for (String integer : addends) {
            results.append(integer);
            results.append("+");
        }
        results.setLength(results.length() - 1);
        results.append("=");
        results.append(sum);
        results.append("\n");
    }

    public static void main(String[] args) {
        String equation = args[0];

        BruteForce force = new BruteForce();
        force.searchResults(equation);
    }

}
