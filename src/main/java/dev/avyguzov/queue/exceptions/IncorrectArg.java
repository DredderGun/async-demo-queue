package dev.avyguzov.queue.exceptions;

public class IncorrectArg extends RuntimeException {
    public IncorrectArg() {
        super("Incorrect input format!");
    }
}
