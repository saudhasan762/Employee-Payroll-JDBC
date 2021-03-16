package practice;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

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
    public void givenDateRangeForEmployee_WhenRetrieved_ShouldMatchEmployeeCount(){
        EmployeePayrollService employeePayrollService = new EmployeePayrollService();
        String start_date = "2019-01-01";
        String end_date = "2020-10-05";
        List<EmployeePayrollData> employeePayrollData = employeePayrollService.readEmployeePayrollDataForDateRange(EmployeePayrollService.IOService.DB_IO,start_date,end_date);
        Assertions.assertEquals(2,employeePayrollData.size());
    }

    @Test
    public void givenEmployeeGenderAsMale_whenRetrievedMinimum_ShouldMatchSalary(){
        EmployeePayrollService employeePayrollService = new EmployeePayrollService();
        char gender = 'M';
        double min = employeePayrollService.minOfEmployeeSalary(EmployeePayrollService.IOService.DB_IO,gender);
        Assertions.assertEquals(1000000.00,min);
    }

    @Test
    public void givenEmployeeGenderAsMale_whenRetrievedMaximum_ShouldMatchSalary(){
        EmployeePayrollService employeePayrollService = new EmployeePayrollService();
        char gender = 'M';
        double min = employeePayrollService.maxOfEmployeeSalary(EmployeePayrollService.IOService.DB_IO,gender);
        Assertions.assertEquals(3000000.00,min);
    }

    @Test
    public void givenEmployeeGenderAsMale_whenRetrievedSum_ShouldMatchSumOfSalary(){
        EmployeePayrollService employeePayrollService = new EmployeePayrollService();
        char gender = 'M';
        double sum = employeePayrollService.sumOfEmployeeSalary(EmployeePayrollService.IOService.DB_IO,gender);
        Assertions.assertEquals(4000000.00,sum);
    }

    @Test
    public void givenEmployeeGenderAsFemale_whenRetrievedSum_ShouldMatchSumOfSalary(){
        EmployeePayrollService employeePayrollService = new EmployeePayrollService();
        char gender = 'F';
        double sum = employeePayrollService.sumOfEmployeeSalary(EmployeePayrollService.IOService.DB_IO,gender);
        Assertions.assertEquals(3000000.00,sum);
    }

    @Test
    public void givenEmployeeGenderAsMale_whenRetrievedAverage_ShouldMatchAverageOfSalary(){
        EmployeePayrollService employeePayrollService = new EmployeePayrollService();
        char gender = 'M';
        double average = employeePayrollService.averageOfEmployeeSalary(EmployeePayrollService.IOService.DB_IO,gender);
        Assertions.assertEquals(2000000.00,average);
    }

    @Test
    public void givenEmployeeGenderAsFemale_whenRetrievedAverage_ShouldMatchAverageOfSalary(){
        EmployeePayrollService employeePayrollService = new EmployeePayrollService();
        char gender = 'F';
        double average = employeePayrollService.averageOfEmployeeSalary(EmployeePayrollService.IOService.DB_IO,gender);
        Assertions.assertEquals(3000000.00,average);
    }

    @Test
    public void givenEmployeeGenderAsMale_whenRetrievedCount_ShouldMatchEmployeeCount(){
        EmployeePayrollService employeePayrollService = new EmployeePayrollService();
        char gender = 'M';
        int count = employeePayrollService.countOfEmployee(EmployeePayrollService.IOService.DB_IO,gender);
        Assertions.assertEquals(2,count);
    }

    @Test
    public void givenEmployeeGenderAsFemale_whenRetrievedCount_ShouldMatchEmployeeCount(){
        EmployeePayrollService employeePayrollService = new EmployeePayrollService();
        char gender = 'F';
        int count = employeePayrollService.countOfEmployee(EmployeePayrollService.IOService.DB_IO,gender);
        Assertions.assertEquals(1,count);
    }
}
