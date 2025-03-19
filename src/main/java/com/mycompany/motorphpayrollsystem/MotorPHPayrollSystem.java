
package com.mycompany.motorphpayrollsystem;

import static com.mycompany.motorphpayrollsystem.Deductions.deductSSS;
import java.io.*; 
import java.time.*; 
import java.time.format.DateTimeFormatter;
import java.util.*;

public class MotorPHPayrollSystem {

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        int choice;
        
        while (true) {    
            System.out.println("\n--------");
            System.out.println("PAYROLL MANAGEMENT SYSTEM");
            System.out.println("\n1. View Employee Details");
            System.out.println("2. Process Payroll");
            System.out.println("3. Exit");
            System.out.println("--------");

            System.out.print("Enter your choice (1-3): ");
            choice = scan.nextInt();
            scan.nextLine();

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
                    return;
                default:
                    System.out.print("Choice is Invalid");
            }
        }
     }
    
   public static void viewEmployeeDetails(Scanner scan) {
        String file = "C:\\Users\\farha\\Desktop\\EmployeeInfo.csv"; //my csv file
        BufferedReader reader = null; //to read the file
        String line = "";
        
        // Get and process User's input
        System.out.print("Search Employee ID (5-digits): ");
        String searchEmployeeID = scan.nextLine();
        
        try {
            reader = new BufferedReader (new FileReader(file)); //to open csv file
            boolean valid = false;  
            
            String header = reader.readLine(); //read header
            System.out.println("\n-------------------------------");
            System.out.print("Employee ID    Name          Birthday");         
            System.out.println("\n-------------------------------");           
           
           //read each line inside the csv 
           while ((line = reader.readLine()) !=null) {
           String[] row = line.split(","); // split by commas
           
           
                 //check if employee ID is valid
                 if (row[0].equals(searchEmployeeID)) {
                     String employeeID = row[0]; 
                     String lastName = row[1]; 
                     String firstName = row[2];
                     String dateOfBirth = row[3]; 

                // Display only the 3 selected columns
                System.out.printf("%-10s %10s %-10s %-15s%n", employeeID, firstName, lastName, dateOfBirth);
                     valid = true;
                     break;
                 } 
           }
           
           
           if (!valid) {
               System.out.println("Employee with ID number: " + searchEmployeeID + " Not Found."); 
               System.out.println("Double-check the Employee ID.");
           }
           
        }
        
        catch (IOException e) {
            e.printStackTrace();
            
        }
        
        finally {        
            
        }
    }
   
   
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
           
   public static void processPayroll(Scanner scan) {
    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy"); 
    
    
    System.out.print("\nEnter Employee ID: ");
    String searchEmployeeID = scan.nextLine();
    
    /*System.out.print("\nEnter the start date of the month (MM/dd/yyyy): ");
    LocalDate startDate = getValidDate(scan, dateFormatter);
    
    System.out.print("Enter the end date of the month (MM/dd/yyyy): ");
    LocalDate endDate = getValidDate(scan, dateFormatter);
    
    if (endDate.isBefore(startDate)) {
        System.out.println("ERROR: End date cannot be before start date. Please enter dates again.");
        return; 
    }
    
    System.out.println(" ");*/
    
        
    String attendanceFile = "C:\\Users\\farha\\Desktop\\Attendance.csv"; 
    String employeeFile = "C:\\Users\\farha\\Desktop\\EmployeeInfo.csv";

    boolean employeeFound = false;
    int workDays = 0; 
    int totalMinutes = 0; //for display
    int totalRegularMinutes = 0;
    int totalOvertimeMinutes = 0;
    float totalRegularHours = 0.0f;
    float totalOvertimeHours = 0.0f;
    float totalTardiness = 0.0f;
    float totalOvertimePay = 0.0f;
    String employeeName = ""; 
    float hourlyRate = 0.0f; 
    float basicSalary = 0.0f;
    

    // Read employee info csv file
    try (BufferedReader reader = new BufferedReader(new FileReader(employeeFile))) {
        String line;
        reader.readLine(); 

        while ((line = reader.readLine()) != null) {
            String[] row = line.split(",");
            if (row[0].trim().equals(searchEmployeeID)) {
                employeeFound = true;
                employeeName = row[2] + " " + row[1]; 
                hourlyRate = Float.parseFloat(row[15].replaceAll("\"", "").trim()); 
                basicSalary = Float.parseFloat(row[10].replaceAll("\"", "").trim());
                                               
                break;
            }
        }
    } catch (IOException | NumberFormatException e) {
        System.out.println("Error reading employee details.");
        e.printStackTrace();
        return;
    }
    
    if (!employeeFound) {
     System.out.println("ERROR: Employee ID " + searchEmployeeID + " not found. Please check and try again.");
     return;
    }
    
    System.out.print("\nEnter the start date of the month (MM/dd/yyyy): ");
    LocalDate startDate = getValidDate(scan, dateFormatter);
    
    System.out.print("Enter the end date of the month (MM/dd/yyyy): ");
    LocalDate endDate = getValidDate(scan, dateFormatter);
    
    if (endDate.isBefore(startDate)) {
        System.out.println("ERROR: End date cannot be before start date. Please try again.");
        return; 
    }
    
    System.out.println(" ");

    // Read the attendance csv file
    try (BufferedReader reader = new BufferedReader(new FileReader(attendanceFile))) {
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("h:mm a"); 
        String line;
        reader.readLine(); 
        
        
        boolean foundAttendance = false;
        
        while ((line = reader.readLine()) != null) {
            String[] row = line.split(",");

            LocalDate date = LocalDate.parse(row[3].trim(), dateFormatter); 
            
            
            if (row[0].trim().equals(searchEmployeeID) &&  
                (date.isEqual(startDate) || date.isEqual(endDate) || 
                (date.isAfter(startDate) && date.isBefore(endDate)))) { 
                
                 if (!foundAttendance) {  
                System.out.println("\nEmployee Name: " + employeeName);
                System.out.println("\n--------------------------------------Hours Worked-------------------------------------\n");
                foundAttendance = true; // date is valid
                }
                 
                workDays++; 
                
                LocalTime loginTime = LocalTime.parse(row[4].trim().toUpperCase(), timeFormatter); 
                LocalTime logoutTime = LocalTime.parse(row[5].trim().toUpperCase(), timeFormatter);
                LocalTime workStart = LocalTime.of(8, 00);
                LocalTime workEnd = LocalTime.of(17, 00);
                LocalTime gracePeriod = LocalTime.of(8, 10); 
               

                int regularMinutes = 0;
                int overtimeMinutes = 0;

                              
                if (loginTime.isBefore(workEnd)) {
                    LocalTime startTime = loginTime.isBefore(workStart) ? workStart : loginTime;
                    LocalTime endTime = logoutTime.isBefore(workEnd) ? logoutTime : workEnd;
                    regularMinutes = (int) Duration.between(startTime, endTime).toMinutes();
                }
                
                if (logoutTime.isAfter(workEnd)) {
                overtimeMinutes = (int) Duration.between(workEnd, logoutTime).toMinutes();
                }
                
                totalRegularMinutes = totalRegularMinutes + regularMinutes;
                totalOvertimeMinutes = totalOvertimeMinutes + overtimeMinutes;
                
                if (loginTime.isAfter(gracePeriod)) {
                    int lateMinutes = (int) Duration.between(gracePeriod, loginTime).toMinutes();
                    float lateHours = lateMinutes/60.0f; //convert to hours
                    float dailyLate = lateHours * hourlyRate;
                    totalTardiness = totalTardiness + dailyLate;
                }
                
                int dailyMinutes = (int) Duration.between(loginTime, logoutTime).toMinutes(); 
                totalMinutes = totalMinutes + dailyMinutes; //for display


                System.out.println("Date: " + date + " | Log in time: " + loginTime + " | Log out time: " + logoutTime + " | Hours worked: " + (dailyMinutes / 60) + " hours and " + (dailyMinutes % 60) + " minutes");
            }
            
          }
        
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

    totalRegularHours = totalRegularMinutes / 60.0f;
    totalOvertimeHours = totalOvertimeMinutes / 60.0f;

    float regularPay = totalRegularHours * hourlyRate;
    float overtimePay = totalOvertimeHours * hourlyRate * 1.25f;
    float grossSalary = regularPay + overtimePay - totalTardiness;
    float totalDeductions = Deductions.deductSSS(basicSalary) + Deductions.deductPhilHealth(basicSalary) + Deductions.deductPagibig(basicSalary);
    float taxableIncome = grossSalary - totalDeductions;
    float netSalary = taxableIncome - Deductions.withholdingTax(taxableIncome);
    
    
    System.out.println("--------------------------------------------------------------------------------------");
    System.out.println("\nGROSS SALARY ");
    System.out.println("\nTotal Hours Worked: " + totalRegularHours + " hours");
    System.out.println("Hourly Rate: PHP" + hourlyRate);
    System.out.println("Overtime Pay: PHP" + overtimePay);
    System.out.println("Tardiness Deduction: -PHP" + totalTardiness);
    System.out.println("Gross Salary: PHP" + String.format("%.2f", grossSalary));
    System.out.println("--------------------------------------------------------------------------------------");
    System.out.println("\nGOVERNMENT MANDATED DEDUCTIONS");
    System.out.println("\nSSS Contribution: PHP" + String.format("%.2f",Deductions.deductSSS(basicSalary)));
    System.out.println("Philhealth Contribution: PHP" + String.format("%.2f",Deductions.deductPhilHealth(basicSalary)));
    System.out.println("PAG-IBIG Contribution: PHP" + String.format("%.2f",Deductions.deductPagibig(basicSalary)));
    System.out.println("Total Deductions: PHP" + String.format("%.2f",totalDeductions));
    System.out.println("\nWithholding Tax: PHP" + String.format("%.2f",Deductions.withholdingTax(taxableIncome)));
    System.out.println("--------------------------------------------------------------------------------------");
    System.out.println("\nNET SALARY ");
    System.out.println("\nNet Salary: PHP" + String.format("%.2f",netSalary));

  }
}
