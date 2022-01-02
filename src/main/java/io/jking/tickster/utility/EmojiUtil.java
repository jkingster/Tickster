package io.jking.tickster.utility;

import net.dv8tion.jda.api.entities.Emoji;

public final class EmojiUtil {
    private EmojiUtil(){}

    // Ticket
    public static Emoji LOCK_EMOJI      = Emoji.fromUnicode("\uD83D\uDD12");
    public static Emoji UNLOCK_EMOJI    = Emoji.fromUnicode("\uD83D\uDD13");
    public static Emoji TRANSCRIPT      = Emoji.fromUnicode("\uD83D\uDCC3");
    public static Emoji WARNING         = Emoji.fromUnicode("\uD83D\uDED1");

    // CommandCategory
    public static Emoji BOT_OWNER         = Emoji.fromUnicode("\uD83D\uDEE1️");
    public static Emoji ADMIN             = Emoji.fromUnicode("\uD83D\uDEE0️");
    public static Emoji TICKET            = Emoji.fromUnicode("\uD83C\uDFAB");
    public static Emoji TICKET_MANAGEMENT = Emoji.fromUnicode("\uD83D\uDC51");
    public static Emoji UTILITY           = Emoji.fromUnicode("⚙️");
    public static Emoji INFO              = Emoji.fromUnicode("ℹ️");
}
