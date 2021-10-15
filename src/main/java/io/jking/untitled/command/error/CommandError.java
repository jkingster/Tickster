package io.jking.untitled.command.error;

public enum CommandError {

    INTERACTION("%s cannot interact with %s."),
    PERMISSION("%s is missing the required permission(s)."),
    UNKNOWN("An unknown error occurred. %s"),
    COOLDOWN("You are on cooldown for that command."),
    ARGUMENTS("You did not provide valid command argument(s). Try again."),
    CUSTOM("%s");

    private final String errorResponse;

    CommandError(String errorResponse) {
        this.errorResponse = errorResponse;
    }

    public String getErrorResponse() {
        return errorResponse;
    }

    public String getErrorResponse(Object... objects) {
        return getErrorResponse().formatted(objects);
    }

    public String getName() {
        return name().charAt(0) + name().substring(1).toLowerCase();
    }
}


