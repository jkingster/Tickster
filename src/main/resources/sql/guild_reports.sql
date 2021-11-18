CREATE TABLE IF NOT EXISTS guild_reports
(
    guild_id         BIGINT    NOT NULL,
    reported_users   BIGINT[]  NOT NULL,
    issuer_id        BIGINT    NOT NULL,
    report_reason    TEXT      NOT NULL,
    report_timestamp TIMESTAMP NOT NULL
);