package practice;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class EmployeePayrollServiceTest {
    @Test
    void given3EmployeeDataWhenWrittenToFilesShouldMatchEmployeeEntries() {
        EmployeePayrollData[] arrayOfEmp={
                new EmployeePayrollData(1,"Jeff Bezos",10000.0),
                new EmployeePayrollData(2,"Bill Gates",20000.0),
                new EmployeePayrollData(3,"Mark",150000.0),
        };
        EmployeePayrollService employeePayrollService = new EmployeePayrollService(Arrays.asList(arrayOfEmp));
        employeePayrollService.writeEmployeePayrollData(EmployeePayrollService.IOService.FILE_IO);
        employeePayrollService.printEmployeePayrollData(EmployeePayrollService.IOService.FILE_IO);
        long entries = employeePayrollService.countEntries(EmployeePayrollService.IOService.FILE_IO);
        System.out.println(entries);
        Assertions.assertEquals(3,entries);
    }

    @Test
    public void givenEmployeePayrollInDB_WhenRetrieved_ShouldMatchEmployeeCount(){
        EmployeePayrollService employeePayrollService = new EmployeePayrollService();
        List<EmployeePayrollData> employeePayrollData = employeePayrollService.readEmployeePayrollData(EmployeePayrollService.IOService.DB_IO);
        Assertions.assertEquals(3,employeePayrollData.size());
    }

    @Test
    public void givenNewSalaryForEmployee_WhenUpdated_ShouldSyncWithDB(){
        EmployeePayrollService employeePayrollService = new EmployeePayrollService();
        List<EmployeePayrollData> employeePayrollData = employeePayrollService.readEmployeePayrollData(EmployeePayrollService.IOService.DB_IO);
        employeePayrollService.updateEmployeeSalary("Terisa",3000000.00);
        boolean result = employeePayrollService.checkEmployeePayrollInSyncWithDB("Terisa");
        Assertions.assertTrue(result);
    }

    @Test
    public void givenDateRange_WhenRetrieved_ShouldMatchEmployeeCount(){
        EmployeePayrollService employeePayrollService = new EmployeePayrollService();
        employeePayrollService.readEmployeePayrollData(EmployeePayrollService.IOService.DB_IO);
        LocalDate startDate = LocalDate.of(2018,1,1);
        LocalDate endDate = LocalDate.now();
        List<EmployeePayrollData> employeePayrollData = employeePayrollService.readEmployeePayrollDataForDateRange(EmployeePayrollService.IOService.DB_IO,startDate,endDate);
        Assertions.assertEquals(3,employeePayrollData.size());
    }

    @Test
    public void givenEmployeePayrollData_whenRetrievedSumByGender_ShouldMatchSumOfSalary(){
        EmployeePayrollService employeePayrollService = new EmployeePayrollService();
        employeePayrollService.readEmployeePayrollData(EmployeePayrollService.IOService.DB_IO);
        Map<String, Double> sumOfSalaryByGender = employeePayrollService.readSumOfSalaryByGender(EmployeePayrollService.IOService.DB_IO);
        Assertions.assertTrue(sumOfSalaryByGender.get("M").equals(4000000.00) &
                sumOfSalaryByGender.get("F").equals(3000000.00));
    }

    @Test
    public void givenPayrollData_whenRetrievedAverageByGender_ShouldMatchAverageOfSalary(){
        EmployeePayrollService employeePayrollService = new EmployeePayrollService();
        employeePayrollService.readEmployeePayrollData(EmployeePayrollService.IOService.DB_IO);
        Map<String, Double> averageSalaryByGender = employeePayrollService.readAverageSalaryByGender(EmployeePayrollService.IOService.DB_IO);
        Assertions.assertTrue(averageSalaryByGender.get("M").equals(2000000.00) &
                averageSalaryByGender.get("F").equals(3000000.00));
    }

    @Test
    public void givenPayrollData_whenRetrievedCountByGender_ShouldMatchEmployeeCount(){
        EmployeePayrollService employeePayrollService = new EmployeePayrollService();
        employeePayrollService.readEmployeePayrollData(EmployeePayrollService.IOService.DB_IO);
        Map<String, Integer> countByGender= employeePayrollService.readCountByGender(EmployeePayrollService.IOService.DB_IO);
        Assertions.assertTrue(countByGender.get("M").equals(2) &
                countByGender.get("F").equals(1));
    }

    @Test
    public void givenNewEmployee_WhenAdded_ShouldSyncWithDB(){
        EmployeePayrollService employeePayrollService = new EmployeePayrollService();
        employeePayrollService.readEmployeePayrollData(EmployeePayrollService.IOService.DB_IO);
        employeePayrollService.addEmployeeToPayroll("Mark", 5000000.00, LocalDate.now(), "M");
        boolean result = employeePayrollService.checkEmployeePayrollInSyncWithDB("Mark");
        Assertions.assertTrue(result);
    }

    @Test
    public void givenEmployees_WhenAdded_ShouldMatchEmployeeEntries(){
        EmployeePayrollData[] arrayOfEmp = {
                new EmployeePayrollData(0,"Jeff Bezos","M",1000000.0,LocalDate.now()),
                new EmployeePayrollData(0,"Bill Gates","M",2000000.0,LocalDate.now()),
                new EmployeePayrollData(0,"Mark Zuckerberg","M",3000000.0, LocalDate.now()),
                new EmployeePayrollData(0,"Sunder","M",6000000.0, LocalDate.now()),
                new EmployeePayrollData(0,"Mukesh","M",10000000.0, LocalDate.now()),
                new EmployeePayrollData(0,"Anil","M",2000000.0, LocalDate.now()),
        };
        EmployeePayrollService employeePayrollService = new EmployeePayrollService();
        employeePayrollService.readEmployeePayrollData(EmployeePayrollService.IOService.DB_IO);
        Instant start = Instant.now();
        employeePayrollService.addEmployeeToPayroll(Arrays.asList(arrayOfEmp));
        Instant end = Instant.now();
        System.out.println("Duration without Thread: "+ Duration.between(start,end));
        Assertions.assertEquals(7,employeePayrollService.countEntries(EmployeePayrollService.IOService.DB_IO));
    }
}
