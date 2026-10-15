Blood Bank Inventory & Donor Matcher
A console-based Java application for managing blood bank inventory and matching compatible donors to recipients. Built as an academic project to demonstrate core Java concepts including data types, loops, collections, streams, file I/O, and custom classes.

Features
Donor Management — Register, view, search, and sort registered donors
Blood Stock Management — Add and issue blood units across all 8 blood groups (O-, O+, A-, A+, B-, B+, AB-, AB+)
Compatibility Matching — Find registered donors whose blood groups are compatible with a recipient, based on ABO/Rh red-cell compatibility rules
Sorting — Sort donors by name (natural ordering), age, or blood group using Comparable and Comparator
Reports — Generate summary reports including donor counts by blood group, age-eligible filters, and low-stock alerts
Persistent Storage — Save and load donor and stock data to CSV files using Java NIO (Files, Stream)
Project Structure
BloodBankInventoryDonorMatcher.java   — Main application class (single-file project)
bloodbank-data/
    ├── donors.csv                    — Donor records (ID, Name, BloodGroup, Age, Phone, City)
    └── stock.csv                     — Blood stock records (BloodGroup, Units)
How It Works
The application runs as a menu-driven console loop. Each menu option maps to a specific feature:

#	Menu Option	Description
1	Register Donor	Add a new donor with ID validation
2	View All Donors	List every registered donor
3	Search Donor	Filter donors by ID, name, blood group, or city
4	Add Blood Stock	Increase units for a specific blood group
5	Issue Blood	Decrease units for a specific blood group
6	View Blood Inventory	Display current stock for all 8 blood groups
7	Find Compatible Donors	Match registered donors and available stock to a recipient's blood group
8	Sort Donors	Sort by name, age, or blood group
9	Generate Reports	Summary statistics and low-stock alerts
10	Save Data	Persist donors and stock to CSV
0	Exit	Save data and terminate
Data Compatibility
The compatibility lookup is a simplified ABO/Rh red-cell model for classroom demonstration:

Recipient	Compatible Donor Groups
O-	O-
O+	O-, O+
A-	O-, A-
A+	O-, O+, A-, A+
B-	O-, B-
B+	O-, O+, B-, B+
AB-	O-, A-, B-, AB-
AB+	O-, O+, A-, A+, B-, B+, AB-, AB+
Disclaimer: Matching is for educational use only. Qualified blood-bank staff must verify compatibility before any real-world transfusion.

Prerequisites
Java 17 or later (for Path.of())
A standard JDK with standard libraries
Building & Running
bash

Copy
# Compile
javac BloodBankInventoryDonorMatcher.java

# Run
java BloodBankInventoryDonorMatcher
The first run creates the bloodbank-data/ directory. Subsequent runs automatically load any previously saved data.

What's Inside
The code demonstrates the following Java concepts:

Data types, conditions, and logical operators — Age validation, compatibility checks
ArrayLists and enhanced for loops — Donor list iteration
Fixed arrays and loops — Blood group indexing and stock management
String methods, streams, and filtering — Donor search functionality
Comparable and Comparator — Sorting donors by name, age, or blood group
Streams, groupingBy, and counting — Report generation
Java NIO file I/O — Reading and writing CSV files using Files.lines() and try-with-resources
Custom classes and encapsulation — Donor inner class with Comparable<Donor>
Sample CSV Formats
donors.csv

ID,Name,BloodGroup,Age,Phone,City
D001,Alice,AB+,28,555-0101,Mumbai
stock.csv

BloodGroup,Units
O-,12
O+,20
A-,5
A+,15
B-,8
B+,10
AB-,3
AB+,7
