package ru.surovcevnv.cockroachraces.classes.exceptions;

public class ResourceNotInitialisedException extends RuntimeException {
    public ResourceNotInitialisedException() {
        super();
    }

    public ResourceNotInitialisedException(String msg) {
        super(msg);
    }
}
