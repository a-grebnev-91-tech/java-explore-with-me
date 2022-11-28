CREATE TABLE IF NOT EXISTS users (
    telegram_id BIGINT PRIMARY KEY,
    ewm_id BIGINT,
    user_name VARCHAR,
    first_name VARCHAR,
    last_name VARCHAR,
    registered_at TIMESTAMP WITHOUT TIME ZONE,
    notify_event_published BOOLEAN DEFAULT FALSE NOT NULL,
    notify_my_event BOOLEAN DEFAULT FALSE NOT NULL,
    notify_incoming_event BOOLEAN DEFAULT FALSE NOT NULL,
    notify_my_participation BOOLEAN DEFAULT FALSE NOT NULL,
    notify_participation_request BOOLEAN DEFAULT FALSE NOT NULL
);