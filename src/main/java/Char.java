import java.util.List;

public class Char {

    private Character character;

    private List<Integer> possibleValues;

    public Character getCharacter() {
        return this.character;
    }

    public void setCharacter(Character character) {
        this.character = character;
    }

    public List<Integer> getPossibleValues() {
        return possibleValues;
    }

    public void setPossibleValues(List<Integer> possibleValues) {
        this.possibleValues = possibleValues;
    }

    public Integer nextPossibleValue(List<Integer> used) {
        int currentPossibleValueIndex = 0;
        while (currentPossibleValueIndex < this.possibleValues.size()) {
            int possibleValue = this.possibleValues.get(currentPossibleValueIndex);
            if (!used.contains(possibleValue)) {
                return possibleValue;
            }
            currentPossibleValueIndex++;
        }
        return null;
    }
}
