package io.jking.tickster.interaction.command;

public enum CommandFlag {

    DEVELOPER(1),
    TICKET(2),
    EPHEMERAL(3),
    DISABLED(4),
    NONE(-1);

    private final int id;

    CommandFlag(int id) {
        this.id = id;
    }

    public CommandFlag getFlagById(int id) {
        for (CommandFlag flag : values())
            if (flag.getId() == id)
                return flag;
        return NONE;
    }

    public int getId() {
        return id;
    }
}
