cd C:\Users\nagay\Desktop\Employee-Payroll-Management-System
.\mvnw.cmd -version
.\mvnw.cmd clean compile
.\mvnw.cmd spring-boot:run


To Change or add the data:
java -cp target\classes Main

POSTMAN::


Method: POST
URL: http://localhost:8000/api/employees
Headers tab:
Content-Type = application/json
Body tab:
Select raw, then choose JSON (not Text)
Paste this body:
{
"id": 109,
"name": "Rahul Verma",
"department": "IT",
"designation": "Developer",
"employeeType": "REGULAR",
"baseSalary": 50000,
"leaveTaken": 2
}
Click Send