CREATE TABLE event_publication
(
    id               UUID NOT NULL,
    completion_date  TIMESTAMP,
    event_type       VARCHAR(255),
    publisher_type   VARCHAR(255),
    serialized_event TEXT,
    timestamp        TIMESTAMP,
    listener_id      VARCHAR(255),
    publication_date TIMESTAMP,
    PRIMARY KEY (id)
);