CREATE TABLE IF NOT EXISTS guild_data
(
    guild_id         BIGINT NOT NULL PRIMARY KEY,
    owner_id         BIGINT NOT NULL,
    logs_id          BIGINT NOT NULL default 0,
    moderator_id     BIGINT NOT NULL default 0,
    notifications_id BIGINT NOT NULL default 0,
    ticket_id        BIGINT NOT NULL default 0
);