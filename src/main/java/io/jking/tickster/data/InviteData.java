package io.jking.tickster.data;

import net.dv8tion.jda.api.entities.Invite;

public class InviteData {

    private final String inviteCode;
    private int uses;

    public InviteData(Invite invite) {
        this.inviteCode = invite.getCode();
        this.uses = invite.getUses();
    }

    public String getCode() {
        return inviteCode;
    }

    public InviteData incrementUses() {
        this.uses++;
        return this;
    }

    public int getUses() {
        return uses;
    }

}
