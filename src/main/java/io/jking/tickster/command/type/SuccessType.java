package io.jking.tickster.command.type;

public enum SuccessType {

    UPDATED("%s was successfully \nupdated to %s.");

    private final String successType;

    SuccessType(String successType) {
        this.successType = successType;
    }

    public String getSuccessType(Object... objects) {
        return successType.formatted(objects);
    }

    public String getName() {
        return name().charAt(0) + name().substring(1).toLowerCase();
    }
}
