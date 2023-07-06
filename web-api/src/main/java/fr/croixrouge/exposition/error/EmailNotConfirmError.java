package fr.croixrouge.exposition.error;

public class EmailNotConfirmError extends RuntimeException {
    public EmailNotConfirmError(String message) {
        super(message);
    }
}
