package by.itechart.queries_problem.exception;

public class ApplicationRuntimeException extends RuntimeException {
    public ApplicationRuntimeException(Throwable throwable) {
        super(throwable);
    }

    public ApplicationRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public ApplicationRuntimeException(String message) {
        super(message);
    }
}
