CREATE TABLE IF NOT EXISTS guild_infractions
(
    guild_id    BIGINT NOT NULL,
    target_id   BIGINT NOT NULL,
    -- Auto Increment?
    case_number INT    NOT NULL,
    reason      TEXT   NOT NULL DEFAULT 'No reason provided.',
    issuer_id   BIGINT NOT NULL,
    timestamp   TEXT   NOT NULL
);