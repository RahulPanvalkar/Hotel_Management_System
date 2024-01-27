CREATE DATABASE HotelManagement_Db;
use HotelManagement_Db;

CREATE TABLE AdminDetails (
    Id INT PRIMARY KEY,
    Password VARCHAR(20),
    Name VARCHAR(50) NOT NULL,
    Email VARCHAR(30) NOT NULL UNIQUE,
    Contact VARCHAR(10) NOT NULL UNIQUE,
    Address VARCHAR(100) NOT NULL
);

select * from AdminDetails;

#---------------------------------------------------------------------------------------------------------------------------------------------------------

CREATE TABLE EmployeeDetails (
    EmpId INT PRIMARY KEY AUTO_INCREMENT,
    Password VARCHAR(20),
    Name VARCHAR(50) NOT NULL,
    Email VARCHAR(30) NOT NULL UNIQUE,
    Contact VARCHAR(10) NOT NULL UNIQUE,
    Address VARCHAR(100) NOT NULL,
    Department VARCHAR(50) NOT NULL,
    Job_Title VARCHAR(50) NOT NULL
);
 
select * from EmployeeDetails;

#---------------------------------------------------------------------------------------------------------------------------------------------------------

CREATE TABLE CustomerDetails (
    CustomerId INT PRIMARY KEY AUTO_INCREMENT,
    Name VARCHAR(50) NOT NULL,
    Email VARCHAR(50) NOT NULL UNIQUE,
    MobileNo VARCHAR(10) NOT NULL UNIQUE,
    Password VARCHAR(20) NOT NULL,
    Address VARCHAR(100) NOT NULL
);

select * from customerDetails;

#---------------------------------------------------------------------------------------------------------------------------------------------------------

CREATE TABLE DeptAndJobTitle (
    deptName VARCHAR(100) NOT NULL,
    jobTitleName VARCHAR(100) UNIQUE NOT NULL
);

-- Inserting departments and job titles
INSERT INTO DeptAndJobTitle (deptName, jobTitleName) VALUES
('Front Office','Front Desk Supervisor'),
('Front Office','Receptionist'),

('Housekeeping','Executive Housekeeper'),
('Housekeeping','Room Attendant/Housekeeper'),

('Food and Beverage','Restaurant Manager'),
('Food and Beverage','Waiter/Waitress'),

('Sales and Marketing','Sales Executive'),
('Sales and Marketing','Sales Manager'),

('Finance and Accounting','Accountant'),
('Finance and Accounting','Accounts Payable/Receivable Clerk'),

('Human Resources', 'Human Resources Manager'),
('Human Resources', 'HR Coordinator'),

('Security', 'Director of Security'),
('Security', 'Security Officer');

select * from DeptAndJobTitle;

#---------------------------------------------------------------------------------------------------------------------------------------------------------

CREATE TABLE Rooms (
    RoomNo VARCHAR(3) PRIMARY KEY,
    Room_Type VARCHAR(10) NOT NULL,
    Price DOUBLE NOT NULL,
    Status VARCHAR(15) NOT NULL
);

SELECT * FROM Rooms;

#---------------------------------------------------------------------------------------------------------------------------------------------------------

CREATE TABLE Reservation (
	ReservationId VARCHAR(10) PRIMARY KEY,
    CustomerId INT NOT NULL,
    CustomerName VARCHAR(50) NOT NULL,
    Room_No VARCHAR(10) NOT NULL,
    Room_Type VARCHAR(20) NOT NULL,
    StartDay Date,
    EndDay Date,
    CheckIn_Time DATETIME NOT NULL,
    CheckOut_Time DATETIME,       
    Price_Per_Day DOUBLE NOT NULL,
    Total_Days INT,
    GST DOUBLE NOT NULL,
    Total_Price DOUBLE,	
    FOREIGN KEY (CustomerId) REFERENCES CustomerDetails (CustomerId),
    FOREIGN KEY (Room_No) REFERENCES Rooms (RoomNo)
);

SELECT * FROM Reservation;

#---------------------------------------------------------------------------------------------------------------------------------------------------------















