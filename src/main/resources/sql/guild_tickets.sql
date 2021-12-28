CREATE TABLE IF NOT EXISTS guild_tickets
(
    guild_id         BIGINT        NOT NULL,
    channel_id       BIGINT UNIQUE NOT NULL,
    creator_id       BIGINT        NOT NULL,
    ticket_timestamp TIMESTAMP     NOT NULL DEFAULT now(),
    status           BOOLEAN       NOT NULL DEFAULT TRUE
);