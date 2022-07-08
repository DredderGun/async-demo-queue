package dev.avyguzov.queue;

import java.util.Objects;

/**
 * Object that holds task result information
 */
public class TaskResult {
    private final String id;

    private String result;

    /**
     * Was task ended with error?
     */
    private boolean isError;

    public TaskResult(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public boolean isError() {
        return isError;
    }

    public void setError(boolean error) {
        isError = error;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaskResult that = (TaskResult) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
