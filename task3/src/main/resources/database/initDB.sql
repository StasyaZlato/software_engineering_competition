CREATE TABLE IF NOT EXISTS promo (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR NOT NULL,
    description VARCHAR);

CREATE TABLE IF NOT EXISTS prizes (
    id BIGSERIAL PRIMARY KEY,
    description VARCHAR,
    promo_id BIGINT NOT NULL,
    CONSTRAINT fk_promo
        FOREIGN KEY(promo_id)
            REFERENCES promo(id)
            ON DELETE CASCADE);

CREATE TABLE IF NOT EXISTS participants (
    id BIGSERIAL PRIMARY KEY,
    description VARCHAR,
    promo_id BIGINT NOT NULL,
    CONSTRAINT fk_promo
        FOREIGN KEY(promo_id)
            REFERENCES promo(id)
            ON DELETE CASCADE
);