CREATE TABLE IF NOT EXISTS conversations (
                                             id         SERIAL PRIMARY KEY,
                                             title      VARCHAR(255)        NOT NULL,
    model_name VARCHAR(100)        NOT NULL,
    created_at TIMESTAMP DEFAULT NOW()
    );

CREATE TABLE IF NOT EXISTS messages (
                                        id              SERIAL PRIMARY KEY,
                                        conversation_id INTEGER REFERENCES conversations(id),
    role            VARCHAR(20)  NOT NULL,
    content         TEXT         NOT NULL,
    created_at      TIMESTAMP DEFAULT NOW()
    );