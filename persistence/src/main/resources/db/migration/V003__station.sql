CREATE TABLE station (
    id UUID PRIMARY KEY,
    created TIMESTAMPTZ NOT NULL,
    updated TIMESTAMPTZ NOT NULL,
    externalId UUID UNIQUE NOT NULL,
    name VARCHAR(200) NOT NULL,
    brand VARCHAR(200) NOT NULL,
    place VARCHAR(200) NOT NULL,
    street VARCHAR(200) NOT NULL,
    coordinate POINT NOT NULL,
    houseNumber VARCHAR(200) NOT NULL,
    postCode INT NOT NULL
);

-- triggers
CREATE TRIGGER station_set_created_updated_dates
     BEFORE INSERT OR UPDATE ON price
    FOR EACH ROW
EXECUTE PROCEDURE set_created_updated_dates();

CREATE TRIGGER station_check_create_change
    BEFORE UPDATE OF created ON price
    FOR EACH ROW
EXECUTE PROCEDURE check_created_change();

CREATE TRIGGER station_check_update_change
    BEFORE UPDATE OF updated ON price
    FOR EACH ROW
EXECUTE PROCEDURE check_updated_change();