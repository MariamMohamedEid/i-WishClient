--
-- Sequence for PRODUCTS table
CREATE SEQUENCE WISHDB.PRODUCTS_SEQ
START WITH 1
  MAXVALUE 999999999999999999999999999
  MINVALUE 1
  NOCYCLE
  CACHE 20
  NOORDER;
-- Trigger
CREATE TRIGGER WISHDB.PRODUCTS_TRG
BEFORE INSERT
ON WISHDB.PRODUCTS
REFERENCING NEW AS New OLD AS Old
FOR EACH ROW
BEGIN
-- For Toad:  Highlight column PRODUCT_ID
  :new.PRODUCT_ID := PRODUCTS_SEQ.nextval;
END PRODUCTS_TRG;
/
-----------------------------------------------------------------------------------------------------------
-----------------------------------------------------------------------------------------------------------
-- Sequence for NOTIFICATION table
CREATE SEQUENCE WISHDB.NOTIFICATION_SEQ
START WITH 1
  MAXVALUE 999999999999999999999999999
  MINVALUE 1
  NOCYCLE
  CACHE 20
  NOORDER;
-- Trigger
CREATE TRIGGER WISHDB.NOTIFICATION_TRG
BEFORE INSERT
ON WISHDB.NOTIFICATION
REFERENCING NEW AS New OLD AS Old
FOR EACH ROW
BEGIN
-- For Toad:  Highlight column NOTIFICATION_ID
  :new.NOTIFICATION_ID := NOTIFICATION_SEQ.nextval;
END NOTIFICATION_TRG;
/
-----------------------------------------------------------------------------------------------------------
-----------------------------------------------------------------------------------------------------------
-- Sequence for WISH table
CREATE SEQUENCE WISHDB.WISH_SEQ
START WITH 1
  MAXVALUE 999999999999999999999999999
  MINVALUE 1
  NOCYCLE
  CACHE 20
  NOORDER;
-- Trigger
CREATE TRIGGER WISHDB.WISH_TRG
BEFORE INSERT
ON WISHDB.WISH
REFERENCING NEW AS New OLD AS Old
FOR EACH ROW
BEGIN
-- For Toad:  Highlight column WISH_ID
  :new.WISH_ID := WISH_SEQ.nextval;
END WISH_TRG;
/
-----------------------------------------------------------------------------------------------------------
-----------------------------------------------------------------------------------------------------------
-- Sequence for CONTRIBUTION table
CREATE SEQUENCE WISHDB.CONTRIBUTION_SEQ
START WITH 1
  MAXVALUE 999999999999999999999999999
  MINVALUE 1
  NOCYCLE
  CACHE 20
  NOORDER;

-- Trigger
CREATE TRIGGER WISHDB.CONTRIBUTION_TRG
BEFORE INSERT
ON WISHDB.CONTRIBUTION
REFERENCING NEW AS New OLD AS Old
FOR EACH ROW
BEGIN
-- For Toad: Highlight column CONTRIBUTION_ID
  :new.CONTRIBUTION_ID := CONTRIBUTION_SEQ.nextval;
END CONTRIBUTION_TRG;
/
