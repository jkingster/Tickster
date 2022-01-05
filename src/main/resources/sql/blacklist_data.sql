CREATE TABLE IF NOT EXISTS blacklist_data
(
    guild_id BIGINT NOT NULL PRIMARY KEY,
    reason   TEXT   NOT NULL
);