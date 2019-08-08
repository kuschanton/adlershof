
-- triggers
    -- set created/updated dates
CREATE OR REPLACE FUNCTION set_created_updated_dates()
    RETURNS TRIGGER AS
$$
BEGIN
    IF (TG_OP = 'INSERT') THEN
        NEW.created := CURRENT_TIMESTAMP;
        NEW.updated := CURRENT_TIMESTAMP;
    ELSEIF (TG_OP = 'UPDATE') THEN
        NEW.updated := CURRENT_TIMESTAMP;
    END IF;

    RETURN NEW;
END;
$$
LANGUAGE PLPGSQL;

  -- exception on created update
CREATE OR REPLACE FUNCTION check_created_change()
    RETURNS TRIGGER AS
$$
BEGIN
  IF NEW.created IS DISTINCT FROM OLD.created THEN
      RAISE EXCEPTION '"created" column cannot get updated';
  END IF;

  RETURN NEW;
END;
$$
    LANGUAGE PLPGSQL;

-- exception on updated update
CREATE OR REPLACE FUNCTION check_updated_change()
    RETURNS TRIGGER AS
$$
BEGIN
    IF NEW.updated IS DISTINCT FROM OLD.updated THEN
        RAISE EXCEPTION '"updated" column cannot get updated';
    END IF;

    RETURN NEW;
END;
$$
    LANGUAGE PLPGSQL;
