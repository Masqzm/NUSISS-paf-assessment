package vttp.batch5.paf.movies.models.exception;

public class SQLInsertionException extends RuntimeException {
    public SQLInsertionException() {
        super();
    }
    public SQLInsertionException(String message) {
        super(message);
    }
    public SQLInsertionException(String message, Throwable cause) {
        super(message, cause);  
    }
}
