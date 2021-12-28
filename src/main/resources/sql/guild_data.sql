CREATE TABLE IF NOT EXISTS guild_data
(
    guild_id        BIGINT NOT NULL PRIMARY KEY,
    owner_id        BIGINT NOT NULL,
    ticket_manager  BIGINT NOT NULL default 0,
    ticket_channel  BIGINT NOT NULL default 0,
    ticket_category BIGINT NOT NULL default 0,
    log_channel     BIGINT NOT NULL default 0,
    report_channel  BIGINT NOT NULL default 0
);