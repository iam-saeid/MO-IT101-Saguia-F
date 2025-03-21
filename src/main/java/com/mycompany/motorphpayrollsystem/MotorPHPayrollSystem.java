
package com.mycompany.motorphpayrollsystem;

import static com.mycompany.motorphpayrollsystem.Deductions.deductSSS;
import java.io.*; 
import java.time.*; 
import java.time.format.DateTimeFormatter;
import java.util.*;

public class MotorPHPayrollSystem {

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        
        while (true) {    
            System.out.println("\n*****************************************************************************");
            System.out.println("PAYROLL MANAGEMENT SYSTEM");
            System.out.println("\n1. View Employee Details");
            System.out.println("2. Process Payroll");
            System.out.println("3. Exit");
            System.out.println("*****************************************************************************");

            System.out.print("Enter your choice (1-3): ");
            
            try {
            int choice = Integer.parseInt(scan.nextLine());

            switch(choice){
                case 1:
                    viewEmployeeDetails(scan);
                    break;
                case 2:
                    processPayroll(scan);
                    break;
                case 3:
                    System.out.println("Exiting the Payroll System..."); 
                    scan.close();
                    return; //To exit the program. 
                default:
                    System.out.println("\nChoice is Invalid. Please enter 1, 2, or 3.");
            }
            }  catch (NumberFormatException e) { 
                System.out.println("\nInvalid input! Please enter a number (1-3).");
                    }
        }
     }
    
   public static void viewEmployeeDetails(Scanner scan) {
        String file = "C:\\Users\\farha\\Desktop\\EmployeeInfo.csv"; //This is the CSV file path containing all the Employee Information.
        BufferedReader reader = null; // To read the file.
        String line = "";
        
        System.out.print("Search Employee ID (5-digits): ");
        String searchEmployeeID = scan.nextLine();
        
        try {
            reader = new BufferedReader (new FileReader(file)); 
            boolean valid = false;  /// Flag to check if employee is found.
            String header = reader.readLine(); 
           
           //To read each line inside the CSV.
           while ((line = reader.readLine()) !=null) {
           String[] row = line.split(","); // To split by commas.
           
                 // Check if Employee ID matches the search ID.
                 if (row[0].equals(searchEmployeeID)) {
                     
                     System.out.println("\n----------------------------------------------------------------------------");
                     System.out.printf("%-15s %-20s %-20s %-20s %-15s %-20s %-15s %n",
                                       "Employee ID", "First Name", "Last Name", "Birthday", "Status", "Position", "Basic Salary"); 
                     System.out.println("\n----------------------------------------------------------------------------");         
                     
                     
                     String employeeID = row[0]; 
                     String lastName = row[1]; 
                     String firstName = row[2];
                     String dateOfBirth = row[3]; 
                     String status = (row[9].replaceAll("\"", "").trim());
                     String position = (row[10].replaceAll("\"", "").trim()); 
                     String basicSalary = (row[11].replaceAll("\"", "").trim()); 

                // Display only the selected details.
                System.out.printf("%-15s %-20s %-20s %-20s %-15s %-20s %7s %n", 
                                  employeeID, firstName, lastName, dateOfBirth, status, position, basicSalary);
                     valid = true;
                     break; 
                 } 
           }
           
           //This shows an ERROR message for invalid employee IDs.
           if (!valid) {
               System.out.println("\nEmployee with ID number: " + searchEmployeeID + " Not Found."); 
               System.out.println("Double-check the Employee ID.");
               System.out.println("Make sure it is a 5-DIGIT NUMBER.");
               return;
           }
           
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        
        finally {        
        }
    }
   
   //This validates the date format entered by the user and displays error message if it is invalid.
   public static LocalDate getValidDate(Scanner scan, DateTimeFormatter dateFormatter) {
    LocalDate date = null;
    while (date == null) {
        try {
            String input = scan.nextLine();
            date = LocalDate.parse(input, dateFormatter); //to parse the input
        } catch (Exception e) {
            System.out.print("Invalid date format. Please enter again (MM/dd/yyyy): ");
        }
    }
    return date;
}
   // Process payroll based on attendance records.        
   public static void processPayroll(Scanner scan) {
    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy"); 
    String attendanceFile = "C:\\Users\\farha\\Desktop\\Attendance.csv"; 
    String employeeFile = "C:\\Users\\farha\\Desktop\\EmployeeInfo.csv"; 
   
    
    String employeeName = ""; 
    String position = "";
    String status = "";
    boolean employeeFound = false;
    int totalWorkedDays = 0;
    int totalWorkedMinutes =0;
    int totalStandardMinutes = 0;
    int totalOTMinutes = 0;
    int totalLateMinutes =0;
    float totalRegularHours;
    float totalOvertimeHours;
    float totalLateDeductions = 0.0f;
    float hourlyRate = 0.0f; 
    float basicSalary = 0.0f;
    
    System.out.print("\nEnter Employee ID: ");
    String searchEmployeeID = scan.nextLine();

    // Read the EmployeeInfo CSV file.
    try (BufferedReader reader = new BufferedReader(new FileReader(employeeFile))) {
        String line;
        reader.readLine(); 

        while ((line = reader.readLine()) != null) {
            String[] row = line.split(",");
            if (row[0].trim().equals(searchEmployeeID)) {
                employeeFound = true;
                employeeName = row[2] + " " + row[1]; 
                position = row[10];
                status = (row[9].replaceAll("\"", "").trim());
                hourlyRate = Float.parseFloat(row[16].replaceAll("\"", "").trim()); 
                basicSalary = Float.parseFloat(row[11].replaceAll("\"", "").trim());
                                               
                break;
            }
        }
    } catch (IOException | NumberFormatException e) {
        System.out.println("Error reading employee details.");
        e.printStackTrace();
        return;
    }
    
    if (!employeeFound) {
     System.out.println("\nERROR: Employee ID " + searchEmployeeID + " not found. Please check and try again.");
     System.out.println("Make sure it is a 5-DIGIT NUMBER.");
     return;
    }
    
    //Get payroll start and end dates
    System.out.print("\nEnter the start date of the month (MM/dd/yyyy): ");
    LocalDate startDate = getValidDate(scan, dateFormatter);
    
    System.out.print("Enter the end date of the month (MM/dd/yyyy): ");
    LocalDate endDate = getValidDate(scan, dateFormatter);
    
    // Displays error message if end date is before the start date.
    if (endDate.isBefore(startDate)) {
        System.out.println("ERROR: End date cannot be before start date. Please try again.");
        System.out.println(" ");
        return; 
    }

    // Read the Attendance CSV file.
    try (BufferedReader reader = new BufferedReader(new FileReader(attendanceFile))) {
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("h:mm a"); 
        String line;
        reader.readLine(); 
        
        boolean foundAttendance = false;
        
        while ((line = reader.readLine()) != null) {
            String[] row = line.split(",");

            LocalDate date = LocalDate.parse(row[3].trim(), dateFormatter); 
            
            //Search the employee ID and entered dates.
            if (row[0].trim().equals(searchEmployeeID) &&  
                (date.isEqual(startDate) || date.isEqual(endDate) || 
                (date.isAfter(startDate) && date.isBefore(endDate)))) { 
                
                 if (!foundAttendance) {  
                System.out.println("\nEmployee Name: " + employeeName);
                System.out.println("\n----------------------TOTAL HOURS WORKED------------------------");
                System.out.println("----------------------------------------------------------------");
                System.out.printf("| %-12s | %-10s | %-10s | %-20s |\n", "Date", "Log in", "Log out", "Hours Worked");
                System.out.println("----------------------------------------------------------------");
                foundAttendance = true; // Date is valid.
                }
                 
                totalWorkedDays++; 
                
                
                LocalTime loginTime = LocalTime.parse(row[4].trim().toUpperCase(), timeFormatter); 
                LocalTime logoutTime = LocalTime.parse(row[5].trim().toUpperCase(), timeFormatter);
                LocalTime gracePeriod = LocalTime.of(8, 10); 
                int minimumWorkMinutes = 8 * 60; // Regular 8 hours converted to 480 minutes.
                
                // Check if the login time is before the logout time.
                if (logoutTime.isBefore(loginTime)) {
                    System.out.println("ERROR: Logout time is before login time. Check the database.");
                    return; // Exit.
                    }
                
                //To caculate total work minutes for the day.
                int dailyWorkedMinutes = (int) Duration.between(loginTime, logoutTime).toMinutes(); 
                totalWorkedMinutes += dailyWorkedMinutes;
                
                //To calculate the REGULAR work minutes. (Capped at 8 hours)
                int standardMinutes;
                    if (dailyWorkedMinutes < minimumWorkMinutes) {
                    standardMinutes = (int) dailyWorkedMinutes;
                    } else {
                    standardMinutes = (int) minimumWorkMinutes;
                } 
                
                //To calculate the OVERTIME minutes. (Total minutes - 480 minutes)
                int overtimeMinutes;
                    if (dailyWorkedMinutes > minimumWorkMinutes) {
                        overtimeMinutes = dailyWorkedMinutes - minimumWorkMinutes;
                        } else {
                        overtimeMinutes = 0;
                    }
                
                // Add to total work time.
                totalStandardMinutes += standardMinutes;
                totalOTMinutes += overtimeMinutes; 
                
                //To calculate the LATE deductions.
                if (loginTime.isAfter(gracePeriod)) {
                    int lateMinutes = (int) Duration.between(gracePeriod, loginTime).toMinutes();
                    totalLateMinutes += lateMinutes;
                    totalLateDeductions += (lateMinutes / 60.0f) * hourlyRate;
                }
                
                System.out.printf("| %-12s | %-10s | %-10s | %2d hours and %2d minutes |\n", date, loginTime, logoutTime, (dailyWorkedMinutes / 60), (dailyWorkedMinutes % 60));

            }
            
          }
        
        //Displays error message if dates entered have no data.
        if (!foundAttendance) {
        System.out.println("ERROR: No attendance records found between " + startDate + " and " + endDate);
        System.out.println("Please check the dates and try again.");
        return; 
        }
        
    } catch (IOException e) {
        System.out.println("Error reading attendance file."); 
        e.printStackTrace();
        return;
    }
    
    
    //Convert overtime and regular minutes to hours. In order to display the total hours.
    totalRegularHours = totalStandardMinutes / 60f;
    totalOvertimeHours = totalOTMinutes / 60f;
    float totalTardinessHours = totalLateMinutes/60f;
    
    //To calculate all Gross Pay, Deductions, and Net Salary.
    float overallTotalHours = (float)  totalWorkedMinutes / 60f;
    float regularPay = totalRegularHours * hourlyRate;
    float overtimePay = totalOvertimeHours * hourlyRate * 1.25f;
    float grossSalary = regularPay + overtimePay - totalLateDeductions;
    float totalDeductions = Deductions.deductSSS(basicSalary) + Deductions.deductPhilHealth(basicSalary) + Deductions.deductPagibig(basicSalary);
    float taxableIncome = grossSalary - totalDeductions;
    float netSalary = taxableIncome - Deductions.withholdingTax(taxableIncome);
   
    //Display all total hours.
    System.out.println("----------------------------------------------------------------");
    System.out.println("Total Regular Hours: "  + totalRegularHours);
    System.out.println("Total Overtime Hours: "  + totalOvertimeHours);
    System.out.println("Total Tardiness Hours: " + totalTardinessHours);
    System.out.println("----------------------------------------------------------------");
    
    //Display payslip.
    System.out.println("\n----------------------------------------------");
    System.out.println("|        MOTORPH EMPLOYEE PAYSLIP            |");
    System.out.println("----------------------------------------------");
    System.out.println("Employee Name: " + employeeName);
    System.out.println("Employee ID: " + searchEmployeeID);
    System.out.println("Status: " + status);
    System.out.println("Position: " + position);
    System.out.println("----------------------------------------------");
    System.out.printf("|               GROSS SALARY                 |");
    System.out.println("\n----------------------------------------------");
    System.out.printf("| %-25s | %-14.2f |\n", "Total Hours Worked", overallTotalHours);
    System.out.printf("| %-25s | PHP %-10.2f |\n","Hourly Rate", hourlyRate);
    System.out.printf("| %-25s | PHP %-10.2f |\n","Overtime Pay",  overtimePay);
    System.out.printf("| %-25s | -PHP %-9.2f |\n","Tardiness Deduction", totalLateDeductions);
    System.out.printf("| %-25s | PHP %-10.2f |\n","Gross Salary",  grossSalary);
    System.out.println("\n----------------------------------------------");
    System.out.print("|         GOVERNMENT MANDATED DEDUCTIONS     |");
    System.out.println("\n----------------------------------------------");
    System.out.printf("| %-25s | PHP %-10.2f |\n", "SSS Contribution", Deductions.deductSSS(basicSalary));
    System.out.printf("| %-25s | PHP %-10.2f |\n", "PhilHealth Contribution", Deductions.deductPhilHealth(basicSalary));
    System.out.printf("| %-25s | PHP %-10.2f |\n", "Pag-IBIG Contribution", Deductions.deductPagibig(basicSalary));
    System.out.printf("| %-25s | PHP %-10.2f |\n", "Total Deductions", totalDeductions);
    System.out.printf("| %-25s | PHP %-10.2f |\n", "Withholding Tax", Deductions.withholdingTax(taxableIncome));
    System.out.println("\n----------------------------------------------");
    System.out.print("|                  SUMMARY                   |");
    System.out.println("\n----------------------------------------------");
    System.out.printf("| %-25s | PHP %-10.2f |\n","Gross Salary",  grossSalary);
    System.out.printf("| %-25s | -PHP %-9.2f |\n", "Total Deductions", totalDeductions);
    System.out.printf("| %-25s | -PHP %-9.2f |\n", "Withholding Tax", Deductions.withholdingTax(taxableIncome));
    System.out.printf("| %-25s | PHP %-10.2f |\n","TAKE HOME PAY",  netSalary);
    System.out.println("\n----------------------------------------------");

  }
}
