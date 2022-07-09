package io.jking.tickster.logging;

import org.slf4j.Logger;

public enum LogType {

    GENERIC                 ("%s"),

    GUILD_JOIN              ("Tickster has joined a new guild: [Guild ID: %s | Owner ID: %s]"),
    GUILD_LEAVE             ("Tickster has left a guild. [Guild ID: %s]"),
    GUILD_REGISTER          ("Registered new guild into GUILD_DATA table: [Guild ID: %s | Owner ID: %s]"),

    TICKSTER_READY          ("Tickster is ready!"),
    TICKSTER_DISCONNECTED   ("Ticket has disconnected."),
    TICKSTER_RECONNECTED    ("Tickster has reconnected."),
    TICKSTER_SHUTDOWN       ("Tickster has shutdown.");

    private final String logMessage;

    LogType(String logMessage) {
        this.logMessage = logMessage;
    }

    public String getLogMessage() {
        return logMessage;
    }
}
