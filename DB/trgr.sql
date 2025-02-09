-- Delete Wish trgr
CREATE OR REPLACE TRIGGER trg_delete_wish
BEFORE DELETE ON WISH
FOR EACH ROW
DECLARE
    CURSOR cur_contributions IS
        SELECT CONTRIBUTOR_NAME, AMOUNT
        FROM CONTRIBUTION
        WHERE WISH_ID = :OLD.WISH_ID;
BEGIN
    FOR rec IN cur_contributions LOOP
        UPDATE USER_S
        SET POINTS = POINTS + rec.AMOUNT
        WHERE USER_NAME = rec.CONTRIBUTOR_NAME;
    END LOOP;

    DELETE FROM CONTRIBUTION
    WHERE WISH_ID = :OLD.WISH_ID;
END;
/       

-- contribut trgr
CREATE OR REPLACE TRIGGER trg_before_insert_contribution
BEFORE INSERT ON CONTRIBUTION
FOR EACH ROW
DECLARE
    v_current_points NUMBER;
BEGIN
    -- Get the current points of the contributor
    SELECT POINTS
    INTO v_current_points
    FROM USER_S
    WHERE USER_NAME = :NEW.CONTRIBUTOR_NAME;

    -- Check if the user has enough points
    IF v_current_points < :NEW.AMOUNT THEN
        -- Reject the insert if the user's points would become negative
        RAISE_APPLICATION_ERROR(-20001, 'Insufficient points. Contribution amount exceeds available points.');
    ELSE
        -- Deduct the contribution amount from the user's points
        UPDATE USER_S
        SET POINTS = POINTS - :NEW.AMOUNT
        WHERE USER_NAME = :NEW.CONTRIBUTOR_NAME;
    END IF;
END;
/




