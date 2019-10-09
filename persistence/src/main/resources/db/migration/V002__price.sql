CREATE TABLE price (
    id BIGSERIAL PRIMARY KEY,
    created TIMESTAMPTZ NOT NULL,
    updated TIMESTAMPTZ NOT NULL,
    station_id UUID NOT NULL,
    update_id INTEGER NOT NULL,
    update_timestamp TIMESTAMPTZ NOT NULL,
    diesel INTEGER NOT NULL,
    e5 INTEGER NOT NULL,
    e10 INTEGER NOT NULL,
    UNIQUE(station_id, update_id)
);


CREATE INDEX idx_station_id
    ON price(station_id);
CREATE INDEX idx_update_id
    ON price(update_id);
CREATE INDEX idx_update_timestamp
    ON price(update_timestamp);

-- triggers
CREATE TRIGGER price_set_created_updated_dates
     BEFORE INSERT OR UPDATE ON price
    FOR EACH ROW
EXECUTE PROCEDURE set_created_updated_dates();

CREATE TRIGGER price_check_create_change
    BEFORE UPDATE OF created ON price
    FOR EACH ROW
EXECUTE PROCEDURE check_created_change();

CREATE TRIGGER price_check_update_change
    BEFORE UPDATE OF updated ON price
    FOR EACH ROW
EXECUTE PROCEDURE check_updated_change();