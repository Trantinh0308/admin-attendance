package com.example.attendance.errors;
public class BadRequestAlertException extends RuntimeException {

    private final String entityName;
    private final String errorKey;

    public BadRequestAlertException(String defaultMessage, String entityName, String errorKey) {
        super(defaultMessage);
        this.entityName = entityName;
        this.errorKey = errorKey;
    }

    public String getEntityName() {
        return entityName;
    }

    public String getErrorKey() {
        return errorKey;
    }
}
