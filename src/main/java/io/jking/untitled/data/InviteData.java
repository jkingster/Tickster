package io.jking.untitled.data;

import net.dv8tion.jda.api.entities.Invite;

public class InviteData {

    private final String code;
    private int uses;

    public InviteData(Invite invite) {
        this.code = invite.getCode();
        this.uses = invite.getUses();
    }

    public InviteData incrementUses() {
        this.uses++;
        return this;
    }

    public String getCode() {
        return code;
    }

    public int getUses() {
        return uses;
    }
}
