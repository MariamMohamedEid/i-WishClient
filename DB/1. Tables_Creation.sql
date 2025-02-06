--How to connect Sys from Toad?

--From Menu Bar above choose Session --> 
--New Connection --> User: Sys & Password: oracl  & Connect as: sys
-- Run only the User Creation without Tables

--------------------------------------------------------------In Sys User Run this------------------------------------------------------------------
-- DROP USER
DROP USER wishDB CASCADE;

-- Description: creates a new database and a new user with all permissions as sysdba
CREATE USER wishDB
IDENTIFIED BY 123
DEFAULT TABLESPACE users
TEMPORARY TABLESPACE temp
QUOTA UNLIMITED ON users;

--Grant all permissions
GRANT ALL PRIVILEGES TO wishDB;
----------------------------------------------------------------------------------------------------------------------------------------------------------
-----------------------------------------------------------------------------------------------------------------------------------------------------------
---------------------------------------------------Inside wishDB User Run this------------------------------------------------------------------------
--Connect to wishDB User

-- User_s Table (user_name as Primary Key)
CREATE TABLE User_s (
    User_Name VARCHAR2(50) PRIMARY KEY,
    Password VARCHAR2(50) NOT NULL,
    Full_Name VARCHAR2(100),
    Age NUMBER,
    Gender VARCHAR2(10),
    Phone VARCHAR2(15),
    Points NUMBER DEFAULT 0
);

-- Friend Table (References User_s via User_Name)
CREATE TABLE Friends (
    User_Name VARCHAR2(50) NOT NULL,
    Friend_Name VARCHAR2(50) NOT NULL
);

-- Product Table
CREATE TABLE Products (
    Product_ID NUMBER PRIMARY KEY,
    Name VARCHAR2(100) NOT NULL,
    Price NUMBER(10, 2) NOT NULL
);

-- Friend Request Table (References User_s via User_Name)
CREATE TABLE FriendRequest (
    Sender_Name VARCHAR2(50) NOT NULL,
    Receiver_Name VARCHAR2(50) NOT NULL,
    Request_DateTime TIMESTAMP DEFAULT SYSTIMESTAMP NOT NULL
);

-- Wish Table (References User_s via User_Name and Products)
CREATE TABLE Wish (
    Wish_ID NUMBER PRIMARY KEY,
    Owner_Name VARCHAR2(50) NOT NULL,
    Product_ID NUMBER NOT NULL,
    Wish_DateTime TIMESTAMP DEFAULT SYSTIMESTAMP NOT NULL
);

-- Contribution Table (References User_s via User_Name and Wish)
CREATE TABLE Contribution (
    Contribution_ID NUMBER PRIMARY KEY,
    Wish_ID NUMBER NOT NULL,
    Contributor_Name VARCHAR2(50) NOT NULL,
    Contribution_DateTime TIMESTAMP NOT NULL,
    Amount NUMBER NOT NULL
);

-- Notification Table (References User_s via User_Name)
CREATE TABLE Notification (
    Notification_ID NUMBER PRIMARY KEY,
    Context VARCHAR2(600) NOT NULL,
    Receiver_Name VARCHAR2(50) NOT NULL
);

-- Add Constraints for Friends Table
ALTER TABLE Friends
    ADD CONSTRAINT Friends_PK PRIMARY KEY (User_Name, Friend_Name);

ALTER TABLE Friends
    ADD CONSTRAINT Friends_User_FK1 FOREIGN KEY (User_Name)
        REFERENCES User_s(User_Name)
        ON DELETE CASCADE;

ALTER TABLE Friends
    ADD CONSTRAINT Friends_User_FK2 FOREIGN KEY (Friend_Name)
        REFERENCES User_s(User_Name)
        ON DELETE CASCADE;

-- Add Constraints for FriendRequest Table
ALTER TABLE FriendRequest
    ADD CONSTRAINT FriendRequest_PK PRIMARY KEY (Sender_Name, Receiver_Name);

ALTER TABLE FriendRequest
    ADD CONSTRAINT FriendRequest_Sender_FK FOREIGN KEY (Sender_Name)
        REFERENCES User_s(User_Name)
        ON DELETE CASCADE;

ALTER TABLE FriendRequest
    ADD CONSTRAINT FriendRequest_Receiver_FK FOREIGN KEY (Receiver_Name)
        REFERENCES User_s(User_Name)
        ON DELETE CASCADE;

-- Add Constraints for Wish Table
ALTER TABLE Wish
    ADD CONSTRAINT Wish_User_FK FOREIGN KEY (Owner_Name)
        REFERENCES User_s(User_Name)
        ON DELETE CASCADE;

ALTER TABLE Wish
    ADD CONSTRAINT Wish_Product_FK FOREIGN KEY (Product_ID)
        REFERENCES Products(Product_ID)
        ON DELETE CASCADE;

-- Add Constraints for Contribution Table
ALTER TABLE Contribution
    ADD CONSTRAINT fk_wish FOREIGN KEY (Wish_ID)
        REFERENCES Wish(Wish_ID);

ALTER TABLE Contribution
    ADD CONSTRAINT fk_user FOREIGN KEY (Contributor_Name)
        REFERENCES User_s(User_Name);

-- Add Constraints for Notification Table
ALTER TABLE Notification
    ADD CONSTRAINT Notification_User_FK FOREIGN KEY (Receiver_Name)
        REFERENCES User_s(User_Name)
        ON DELETE CASCADE;
        
