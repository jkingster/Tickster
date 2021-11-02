CREATE TABLE IF NOT EXISTS guild_settings
(
    guild_id        BIGINT NOT NULL PRIMARY KEY,

    -- LOGGING STATUS
    message_logging BOOL   NOT NULL default false,
    alt_logging     BOOL   NOT NULL default false,
    pfp_logging     BOOL   NOT NULL default false,

-- AUTO KICK STATUS
    auto_mod        BOOL   NOT NULL default false,
    auto_alt        BOOL   NOT NULL default false,
    auto_pfp        BOOL   NOT NULL default false,
    auto_message    BOOL   NOT NULL default false
);
