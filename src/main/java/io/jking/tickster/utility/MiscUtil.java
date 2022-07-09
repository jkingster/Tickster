package io.jking.tickster.utility;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;

public final class MiscUtil {
    private MiscUtil() {
    }

    public static boolean hasRole(Member member, long roleId) {
        for (Role role : member.getRoles())
            if (role.getIdLong() == roleId)
                return true;
        return false;
    }
}
