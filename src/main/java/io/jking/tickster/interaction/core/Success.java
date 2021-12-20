package io.jking.tickster.interaction.core;

public enum Success {

    UPDATE  ("%s was updated successfully."),
    ACTION  ("That action was successfully processed."),
    CREATED ("%s was created successfully.");

    private final String description;

    Success(String description) {
        this.description = description;
    }

    public String getDesc(Object... objects) {
        return description.formatted(objects);
    }
}
