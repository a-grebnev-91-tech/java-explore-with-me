CREATE TABLE IF NOT EXISTS stat
(
    id   BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    app  VARCHAR                  NOT NULL,
    uri  VARCHAR                  NOT NULL,
    ip   VARCHAR                  NOT NULL,
    time TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE INDEX IF NOT EXISTS stat_index ON stat (uri, ip, time);