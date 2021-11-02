CREATE TABLE IF NOT EXISTS guild_tickets
(
    id            INT PRIMARY KEY NOT NULL,
    guild_id      BIGINT          NOT NULL,
    creator_id    BIGINT          NOT NULL,
    ticket_text   TEXT            NOT NULL,
    timestamp     TEXT            NOT NULL,
    ticket_number INT             NOT NULL
);
