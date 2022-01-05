package io.jking.tickster.interaction.command;

public class CommandFlag {

    public static final long DEVELOPER      = 1;
    public static final long EPHEMERAL      = 1 << 1;
    public static final long TICKET         = 1 << 2;
    public static final long NONE           = 1 << 3;
    public static final long DISABLED       = 1 << 4;

    private final long flags;

    private CommandFlag(long flags) {
        this.flags = flags;
    }

    public static CommandFlag of(long flags) {
        return new CommandFlag(flags);
    }

    public static CommandFlag ofDeveloper() {
        return of(DEVELOPER);
    }

    public static CommandFlag ofEphemeral() {
        return of(EPHEMERAL);
    }

    public static CommandFlag ofTicket() {
        return of(TICKET);
    }

    public static CommandFlag none() {
        return of(NONE);
    }

    public boolean hasFlag(long flag) {
        return (flags & flag) == flag;
    }

    public boolean isDeveloper() {
        return hasFlag(CommandFlag.DEVELOPER);
    }

    public boolean isEphemeral() {
        return hasFlag(CommandFlag.EPHEMERAL);
    }

    public boolean isTicket() {
        return hasFlag(CommandFlag.TICKET);
    }

    public boolean isNone() {
        return hasFlag(CommandFlag.NONE);
    }

    public boolean isDisabled() {
        return hasFlag(CommandFlag.DISABLED);
    };

}
