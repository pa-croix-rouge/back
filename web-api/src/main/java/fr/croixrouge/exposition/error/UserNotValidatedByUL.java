package fr.croixrouge.exposition.error;

public class UserNotValidatedByUL extends RuntimeException {
    public UserNotValidatedByUL(String message) {
        super(message);
    }
}
