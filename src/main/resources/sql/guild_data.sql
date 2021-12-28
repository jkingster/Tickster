CREATE TABLE IF NOT EXISTS guild_data
(
    guild_id    BIGINT NOT NULL PRIMARY KEY,
    owner_id    BIGINT NOT NULL,
    log_id      BIGINT NOT NULL DEFAULT 0,
    support_id  BIGINT NOT NULL DEFAULT 0,
    invite_id   BIGINT NOT NULL DEFAULT 0,
    category_id BIGINT NOT NULL DEFAULT 0,
    ticket_id   BIGINT NOT NULL DEFAULT 0
);