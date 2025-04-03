import psycopg2
from psycopg2 import sql

# PostgreSQL connection string
DB_URL = "postgresql://postgres:NqlVODXIobgaOsHmwWHqXllPtOVOZril@shinkansen.proxy.rlwy.net:58078/railway"

# File paths to upload (e.g., CV and Fingerprint files)
CV_FILE_PATH = "C:/tmp/BolBol.pdf"  # Replace with the path to your CV file
FINGERPRINT_FILE_PATH = "C:/tmp/finger1.BMP" 

# Function to insert employee data into PostgreSQL database
def insert_employee_data(full_name, email, phone, address, salary, password):
    connection = None
    try:
        # Connect to the PostgreSQL database using the connection string
        connection = psycopg2.connect(DB_URL)

        cursor = connection.cursor()

        # Open the CV file and FingerPrint file, and convert them into binary format
        with open(CV_FILE_PATH, 'rb') as cv_file, open(FINGERPRINT_FILE_PATH, 'rb') as fp_file:
            cv_data = cv_file.read()
            fingerprint_data = fp_file.read()

        # SQL query to insert data into the Employees table
        insert_employee_query = """
        INSERT INTO Employees (FullName, Email, Phone, Address, Salary, CV, Password, FingerPrint)
        VALUES (%s, %s, %s, %s, %s, %s, %s, %s)
        RETURNING EmployeeID;
        """

        # Execute the query to insert employee data
        cursor.execute(insert_employee_query, (full_name, email, phone, address, salary, cv_data, password, fingerprint_data))

        # Get the EmployeeID of the newly inserted employee
        employee_id = cursor.fetchone()[0]

        # Optionally, insert attendance and salary data
        insert_attendance_query = """
        INSERT INTO Attendance (EmployeeID, AttendDate)
        VALUES (%s, NOW());
        """
        
        insert_salary_query = """
        INSERT INTO Salary (EmployeeID, ActualSalary, DifferenceAmount, DeductionOrRaise, Reason)
        VALUES (%s, %s, %s, %s, %s);
        """

        # Example attendance and salary data (replace with actual values)
        cursor.execute(insert_attendance_query, (employee_id,))
        cursor.execute(insert_salary_query, (employee_id, salary, 0.0, 'None', 'Initial Salary'))

        # Commit the transaction
        connection.commit()

        print(f"Employee successfully inserted with EmployeeID {employee_id}")

    except Exception as error:
        print(f"Error: {error}")
    finally:
        if connection:
            cursor.close()
            connection.close()

# Call the function to insert employee data
insert_employee_data(
    full_name="John Doe",
    email="johndoe@example.com",
    phone="1234567890",
    address="123 Main St, Springfield, USA",
    salary=50000.00,
    password="securepassword123"
)
