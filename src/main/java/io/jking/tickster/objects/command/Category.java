package io.jking.tickster.objects.command;

public enum Category {

    UTILITY("Useful utility commands."),
    TICKETS("Commands related to tickets."),
    SETUP("Configuration/Settings to help me function."),
    INFO("Informational commands.");

    private final String description;

    Category(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }


}
