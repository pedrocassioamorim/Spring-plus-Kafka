package net.test.springpluskafka.services.exceptions;

import java.io.Serial;

public class DatabaseViolationException extends RuntimeException{

    @Serial
    private static final long serialVersionUID = 1L;

    public DatabaseViolationException(String message) {
        super(message);
    }
}
