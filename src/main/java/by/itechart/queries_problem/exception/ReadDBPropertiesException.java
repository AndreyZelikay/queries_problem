package by.itechart.queries_problem.exception;

public class ReadDBPropertiesException extends ApplicationRuntimeException {
    public ReadDBPropertiesException(Throwable throwable) {
        super(String.format("Following exception occurred during db connection properties reading %s", throwable.getMessage()), throwable);
    }
}
