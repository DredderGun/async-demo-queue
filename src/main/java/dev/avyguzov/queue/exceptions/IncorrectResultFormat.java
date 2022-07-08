package dev.avyguzov.queue.exceptions;

public class IncorrectResultFormat extends RuntimeException {
    public IncorrectResultFormat() {
        super("Task returned incorrect result format");
    }
}
