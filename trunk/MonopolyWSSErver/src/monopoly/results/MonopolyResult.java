package monopoly.results;

/**
 * User: Liron Blecher
 * Date: 3/28/11
 */
public class MonopolyResult {
    private boolean hasError;
    private String errorMessage;

    public MonopolyResult() {
        this (false, null);
    }

    public MonopolyResult(String errorMessage) {
        this (true, errorMessage);
    }

    public MonopolyResult(boolean hasError, String errorMessage) {
        this.hasError = hasError;
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public boolean isError() {
        return hasError;
    }

    public static MonopolyResult error(String message) {
        return new MonopolyResult(message);
    }
}
