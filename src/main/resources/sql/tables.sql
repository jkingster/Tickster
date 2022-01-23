CREATE TABLE IF NOT EXISTS GUILD_DATA
(
    guild_id          BIGINT NOT NULL PRIMARY KEY,
    owner_id          BIGINT NOT NULL,
    invite_channel_id BIGINT NOT NULL DEFAULT (0),
    log_channel_id    BIGINT NOT NULL DEFAULT (0)
);

CREATE TABLE IF NOT EXISTS TICKET_DATA
(
    channel_id BIGINT    NOT NULL PRIMARY KEY,
    guild_id   BIGINT    NOT NULL REFERENCES GUILD_DATA (guild_id) ON DELETE CASCADE,
    creator_id BIGINT    NOT NULL,
    creation   TIMESTAMP NOT NULL DEFAULT (current_timestamp),
    expiry     TIMESTAMP NOT NULL DEFAULT (current_timestamp + interval '48 HOURS')
);

CREATE TABLE IF NOT EXISTS TICKET_SETTINGS
(
    guild_id               BIGINT NOT NULL REFERENCES GUILD_DATA (guild_id) ON DELETE CASCADE PRIMARY KEY,
    category_id            BIGINT NOT NULL DEFAULT (0),
    ticket_channel_id      BIGINT NOT NULL DEFAULT (0),
    ticket_support_role_id BIGINT NOT NULL DEFAULT (0)
);

