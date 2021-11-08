package io.jking.tickster.objects.command;

public enum ErrorType {

    UNKNOWN("An unknown error occurred."),
    CUSTOM("%s"),
    PERMISSION("%s is missing the required permission(s): %s."),
    CANT_INTERACT("%s cannot interact with %s.");

    private final String errorResponse;

    ErrorType(String errorResponse) {
        this.errorResponse = errorResponse;
    }

    public String getErrorResponse() {
        return errorResponse;
    }

    public String getName() {
        return name().charAt(0) + name().substring(1).toLowerCase();
    }
}
