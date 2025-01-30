-- Description: creates a new database and a new user with all permissions as sysdba
CREATE USER wishDB
IDENTIFIED BY 123
DEFAULT TABLESPACE users
TEMPORARY TABLESPACE temp
QUOTA UNLIMITED ON users;

--Grant all permissions
GRANT ALL PRIVILEGES TO wishDB;

-- then connect to wishDB and run the following script to create the tables

--User_s Table
CREATE TABLE User_s (
    user_id NUMBER PRIMARY KEY,
    User_Name VARCHAR2(50) NOT NULL,
    Password VARCHAR2(50) NOT NULL,
    Full_Name VARCHAR2(100),
    Email VARCHAR2(100),
    Age NUMBER,
    Gender VARCHAR2(10),
    Phone VARCHAR2(15),
    points Number default 0
);

--Friend Table
CREATE TABLE Friends (
    user_id NUMBER NOT NULL,
    Friend_ID NUMBER NOT NULL,
    CONSTRAINT Friends_PK PRIMARY KEY (user_id, Friend_ID),
    CONSTRAINT Friends_User_FK1 FOREIGN KEY (user_id)
        REFERENCES User_s(user_id)
        ON DELETE CASCADE,
    CONSTRAINT Friends_User_FK2 FOREIGN KEY (Friend_ID)
        REFERENCES User_s(user_id)
        ON DELETE CASCADE
);

--Product Table
CREATE TABLE Products (
    Product_ID NUMBER PRIMARY KEY,
    Name VARCHAR2(100) NOT NULL,
    Price NUMBER(10, 2) NOT NULL
);

--Friend request Table
CREATE TABLE FriendRequest (
    SenderID NUMBER NOT NULL,
    ReceiverID NUMBER NOT NULL,
    Request_DateTime TIMESTAMP DEFAULT SYSTIMESTAMP NOT NULL,
    CONSTRAINT FriendRequest_PK PRIMARY KEY (SenderID, ReceiverID),
    CONSTRAINT FriendRequest_Sender_FK FOREIGN KEY (SenderID)
        REFERENCES User_s(user_ID)
        ON DELETE CASCADE,
    CONSTRAINT FriendRequest_Receiver_FK FOREIGN KEY (ReceiverID)
        REFERENCES User_s(user_ID)
        ON DELETE CASCADE
);

--Wish Table
CREATE TABLE Wish (
    Wish_ID NUMBER PRIMARY KEY,
    Bought NUMBER DEFAULT 0 NOT NULL,
    Remaining NUMBER DEFAULT 0 NOT NULL,
    user_id NUMBER NOT NULL,
    Product_ID NUMBER NOT NULL,
    Wish_DateTime TIMESTAMP DEFAULT SYSTIMESTAMP NOT NULL,
    CONSTRAINT Wish_User_FK FOREIGN KEY (user_id)
        REFERENCES User_s(user_ID)
        ON DELETE CASCADE,
    CONSTRAINT Wish_Product_FK FOREIGN KEY (Product_ID)
        REFERENCES Products(Product_ID)
        ON DELETE CASCADE
);


--Contribution Table
CREATE TABLE Contributions (
    Wish_ID NUMBER PRIMARY KEY,
    Amount NUMBER NOT NULL,
    CONSTRAINT Contributions_Wish_FK FOREIGN KEY (Wish_ID)
        REFERENCES Wish(Wish_ID)
        ON DELETE CASCADE
);


