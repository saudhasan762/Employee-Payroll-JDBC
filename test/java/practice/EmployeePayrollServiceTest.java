package practice;

import com.google.gson.Gson;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class EmployeePayrollServiceTest {
    @Test
    public void given3EmployeeDataWhenWrittenToFilesShouldMatchEmployeeEntries() {
        EmployeePayrollData[] arrayOfEmp = {
                new EmployeePayrollData(1, "Jeff Bezos", 10000.0),
                new EmployeePayrollData(2, "Bill Gates", 20000.0),
                new EmployeePayrollData(3, "Mark", 150000.0),
        };
        EmployeePayrollService employeePayrollService = new EmployeePayrollService(Arrays.asList(arrayOfEmp));
        employeePayrollService.writeEmployeePayrollData(EmployeePayrollService.IOService.FILE_IO);
        employeePayrollService.printEmployeePayrollData(EmployeePayrollService.IOService.FILE_IO);
        long entries = employeePayrollService.countEntries(EmployeePayrollService.IOService.FILE_IO);
        System.out.println(entries);
        Assert.assertEquals(3, entries);
    }

    @Test
    public void givenEmployeePayrollInDB_WhenRetrieved_ShouldMatchEmployeeCount() {
        EmployeePayrollService employeePayrollService = new EmployeePayrollService();
        List<EmployeePayrollData> employeePayrollData = employeePayrollService.readEmployeePayrollData(EmployeePayrollService.IOService.DB_IO);
        Assert.assertEquals(3, employeePayrollData.size());
    }

    @Test
    public void givenNewSalaryForEmployee_WhenUpdated_ShouldSyncWithDB() {
        EmployeePayrollService employeePayrollService = new EmployeePayrollService();
        List<EmployeePayrollData> employeePayrollData = employeePayrollService.readEmployeePayrollData(EmployeePayrollService.IOService.DB_IO);
        employeePayrollService.updateEmployeeSalary("Terisa", 3000000.00);
        boolean result = employeePayrollService.checkEmployeePayrollInSyncWithDB("Terisa");
        Assert.assertTrue(result);
    }

    @Test
    public void givenDateRange_WhenRetrieved_ShouldMatchEmployeeCount() {
        EmployeePayrollService employeePayrollService = new EmployeePayrollService();
        employeePayrollService.readEmployeePayrollData(EmployeePayrollService.IOService.DB_IO);
        LocalDate startDate = LocalDate.of(2018, 1, 1);
        LocalDate endDate = LocalDate.now();
        List<EmployeePayrollData> employeePayrollData = employeePayrollService.readEmployeePayrollDataForDateRange(EmployeePayrollService.IOService.DB_IO, startDate, endDate);
        Assert.assertEquals(3, employeePayrollData.size());
    }

    @Test
    public void givenEmployeePayrollData_whenRetrievedSumByGender_ShouldMatchSumOfSalary() {
        EmployeePayrollService employeePayrollService = new EmployeePayrollService();
        employeePayrollService.readEmployeePayrollData(EmployeePayrollService.IOService.DB_IO);
        Map<String, Double> sumOfSalaryByGender = employeePayrollService.readSumOfSalaryByGender(EmployeePayrollService.IOService.DB_IO);
        Assert.assertTrue(sumOfSalaryByGender.get("M").equals(4000000.00) &
                sumOfSalaryByGender.get("F").equals(3000000.00));
    }

    @Test
    public void givenPayrollData_whenRetrievedAverageByGender_ShouldMatchAverageOfSalary() {
        EmployeePayrollService employeePayrollService = new EmployeePayrollService();
        employeePayrollService.readEmployeePayrollData(EmployeePayrollService.IOService.DB_IO);
        Map<String, Double> averageSalaryByGender = employeePayrollService.readAverageSalaryByGender(EmployeePayrollService.IOService.DB_IO);
        Assert.assertTrue(averageSalaryByGender.get("M").equals(2000000.00) &
                averageSalaryByGender.get("F").equals(3000000.00));
    }

    @Test
    public void givenPayrollData_whenRetrievedCountByGender_ShouldMatchEmployeeCount() {
        EmployeePayrollService employeePayrollService = new EmployeePayrollService();
        employeePayrollService.readEmployeePayrollData(EmployeePayrollService.IOService.DB_IO);
        Map<String, Integer> countByGender = employeePayrollService.readCountByGender(EmployeePayrollService.IOService.DB_IO);
        Assert.assertTrue(countByGender.get("M").equals(2) &
                countByGender.get("F").equals(1));
    }

    @Test
    public void givenNewEmployee_WhenAdded_ShouldSyncWithDB() {
        EmployeePayrollService employeePayrollService = new EmployeePayrollService();
        employeePayrollService.readEmployeePayrollData(EmployeePayrollService.IOService.DB_IO);
        employeePayrollService.addEmployeeToPayroll("Mark", 5000000.00, LocalDate.now(), "M");
        boolean result = employeePayrollService.checkEmployeePayrollInSyncWithDB("Mark");
        Assert.assertTrue(result);
    }

    @Test
    public void givenEmployees_WhenAdded_ShouldMatchEmployeeEntries() {
        EmployeePayrollData[] arrayOfEmp = {
                new EmployeePayrollData(0, "Jeff Bezos", "M", 1000000.0, LocalDate.now()),
                new EmployeePayrollData(0, "Bill Gates", "M", 2000000.0, LocalDate.now()),
                new EmployeePayrollData(0, "Mark Zuckerberg", "M", 3000000.0, LocalDate.now()),
                new EmployeePayrollData(0, "Sunder", "M", 6000000.0, LocalDate.now()),
                new EmployeePayrollData(0, "Mukesh", "M", 10000000.0, LocalDate.now()),
                new EmployeePayrollData(0, "Anil", "M", 2000000.0, LocalDate.now()),
        };
        EmployeePayrollService employeePayrollService = new EmployeePayrollService();
        employeePayrollService.readEmployeePayrollData(EmployeePayrollService.IOService.DB_IO);
        Instant start = Instant.now();
        employeePayrollService.addEmployeeToPayroll(Arrays.asList(arrayOfEmp));
        Instant end = Instant.now();
        System.out.println("Duration without Thread: " + Duration.between(start, end));
        Instant threadStart = Instant.now();
        employeePayrollService.addEmployeeToPayrollWithThreads(Arrays.asList(arrayOfEmp));
        Instant threadEnd = Instant.now();
        System.out.println("Duration with Thread: " + Duration.between(threadStart, threadEnd));
        Assert.assertEquals(13, employeePayrollService.countEntries(EmployeePayrollService.IOService.DB_IO));
    }

    @Before
    public void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 3000;
    }

    public EmployeePayrollData[] getEmployeeList() {
        Response response = RestAssured.get("/employee_payroll");
        System.out.println("EMPLOYEE PAYROLL ENTRIES IN JSONServer:\n" + response.asString());
        EmployeePayrollData[] arrayOfEmp = new Gson().fromJson(response.asString(), EmployeePayrollData[].class);
        return arrayOfEmp;
    }

    public Response addEmployeeToJsonServer(EmployeePayrollData employeePayrollData) {
        String empJson = new Gson().toJson(employeePayrollData);
        RequestSpecification request = RestAssured.given();
        request.header("Content-Type", "application/json");
        request.body(empJson);
        return request.post("/employee_payroll");
    }

    @Test
    public void givenEmployeeDataInJsonServer_WhenRetrieved_ShouldMatchTheCount() {
        EmployeePayrollData[] arrayOfEmp = getEmployeeList();
        EmployeePayrollService employeePayrollService;
        employeePayrollService = new EmployeePayrollService(Arrays.asList(arrayOfEmp));
        long entries = employeePayrollService.countEntries(EmployeePayrollService.IOService.REST_IO);
        Assert.assertEquals(3, entries);
    }

    @Test
    public void givenNewEmployee_WhenAdded_ShouldMatch210ResponseAndCount() {
        EmployeePayrollService employeePayrollService;
        EmployeePayrollData[] arrayOfEmp = getEmployeeList();
        employeePayrollService = new EmployeePayrollService(Arrays.asList(arrayOfEmp));

        EmployeePayrollData employeePayrollData = null;
        employeePayrollData = new EmployeePayrollData(0, "Mark Zuckerberg", "M", 500000.0, LocalDate.now());
        Response response = addEmployeeToJsonServer(employeePayrollData);
        int statusCode = response.getStatusCode();
        Assert.assertEquals(201, statusCode);

        employeePayrollData = new Gson().fromJson(response.asString(), EmployeePayrollData.class);
        employeePayrollService.addEmployeeToPayroll(employeePayrollData, EmployeePayrollService.IOService.REST_IO);
        long entries = employeePayrollService.countEntries(EmployeePayrollService.IOService.REST_IO);
        Assert.assertEquals(3,entries);
    }

    @Test
    public void givenListOfEmployee_WhenAdded_ShouldMatch201ResponseAndCount(){
        EmployeePayrollService employeePayrollService;
        EmployeePayrollData[] arrayOfEmp = getEmployeeList();
        employeePayrollService = new EmployeePayrollService(Arrays.asList(arrayOfEmp));

        EmployeePayrollData[] arrayOfEmpPayroll = {
                new EmployeePayrollData(0,"Sunder","M",600000.0, LocalDate.now()),
                new EmployeePayrollData(0, "Mukesh", "M",100000.0, LocalDate.now()),
                new EmployeePayrollData(0,"Anil","M",200000.0, LocalDate.now())
        };
        for (EmployeePayrollData employeePayrollData : arrayOfEmpPayroll){
            Response response = addEmployeeToJsonServer(employeePayrollData);
            int statusCode = response.getStatusCode();
            Assert.assertEquals(201, statusCode);

            employeePayrollData = new Gson().fromJson(response.asString(), EmployeePayrollData.class);
            employeePayrollService.addEmployeeToPayroll(employeePayrollData, EmployeePayrollService.IOService.REST_IO);
        }

        long entries = employeePayrollService.countEntries(EmployeePayrollService.IOService.REST_IO);
        Assert.assertEquals(6, entries);
    }

    @Test
    public void givenNewSalary_WhenUpdated_ShouldMatch200Response(){
        EmployeePayrollService employeePayrollService;
        EmployeePayrollData[] arrayOfEmp = getEmployeeList();
        employeePayrollService = new EmployeePayrollService(Arrays.asList(arrayOfEmp));

        employeePayrollService.updateEmployeeSalary("Anil",3000000.00, EmployeePayrollService.IOService.REST_IO);
        EmployeePayrollData employeePayrollData = employeePayrollService.getEmployeePayrollData("Anil");

        String empJson = new Gson().toJson(employeePayrollData);
        RequestSpecification request = RestAssured.given();
        request.header("Content-Type","application/json");
        request.body(empJson);
        Response response = request.put("/employee_payroll/"+employeePayrollData.id);
        int statusCode = response.getStatusCode();
        Assert.assertEquals(200, statusCode);
    }

    @Test
    public void givenEmployeeToDelete_WhenDeleted_ShouldMatch200ResponseAndCount(){
        EmployeePayrollService employeePayrollService;
        EmployeePayrollData[] arrayOfEmp = getEmployeeList();
        employeePayrollService = new EmployeePayrollService(Arrays.asList(arrayOfEmp));

        EmployeePayrollData employeePayrollData = employeePayrollService.getEmployeePayrollData("Anil");
        RequestSpecification request = RestAssured.given();
        request.header("Content-Type", "application/json");
        Response response = request.delete("/employee_payroll/"+employeePayrollData.id);
        int statusCode = response.getStatusCode();
        Assert.assertEquals(200, statusCode);

        employeePayrollService.deleteEmployeePayroll(employeePayrollData.name, EmployeePayrollService.IOService.REST_IO);
        long entries = employeePayrollService.countEntries(EmployeePayrollService.IOService.REST_IO);
        Assert.assertEquals(5, entries);
    }
}
