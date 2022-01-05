package io.jking.tickster.interaction.core;

public enum Error {

    UNKNOWN     ("An unknown error occurred, could not process."),
    ARGUMENTS   ("You provided invalid arguments, type `/help %s`."),
    INTERACT    ("There was an interaction error, check permissions and hierarchy."),
    PERMISSION  ("**%s** is missing the required permission(s): `%s`."),
    ACCESS      ("**You are not permitted to access that command, do not try again.**"),
    CUSTOM      ("%s");

    private final String description;

    Error(String description) {
        this.description = description;
    }

    public String getDesc(Object... objects) {
        return description.formatted(objects);
    }
}
