package practice;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class EmployeePayrollService {
    private List<EmployeePayrollData> employeePayrollList;
    private EmployeePayrollDBService employeePayrollDBService;

    public EmployeePayrollService() {
        employeePayrollDBService = EmployeePayrollDBService.getInstance();
    }

    public List<EmployeePayrollData> readEmployeePayrollDataForDateRange(IOService ioService, String start_date, String end_date) {
        if(ioService.equals(IOService.DB_IO))
            this.employeePayrollList = employeePayrollDBService.readDataForDateRange(start_date,end_date);
        return this.employeePayrollList;
    }

    public double sumOfEmployeeSalary(IOService ioService, char gender) {
        if(ioService.equals(IOService.DB_IO)){
            double sum = employeePayrollDBService.sumOfSalary(gender);
            return sum;
        }
        return 0.0;
    }

    public double averageOfEmployeeSalary(IOService ioService, char gender) {
        if(ioService.equals(IOService.DB_IO)){
            double average = employeePayrollDBService.averageOfSalary(gender);
            return average;
        }
        return 0.0;
    }

    public int countOfEmployee(IOService ioService, char gender) {
        if(ioService.equals(IOService.DB_IO)){
            int count = employeePayrollDBService.countEntries(gender);
            return count;
        }
        return 0;
    }

    public double minOfEmployeeSalary(IOService ioService, char gender) {
        if(ioService.equals(IOService.DB_IO)){
            double salary = employeePayrollDBService.getMinimumSalary(gender);
            return salary;
        }
        return 0.0;
    }

    public double maxOfEmployeeSalary(IOService ioService, char gender) {
        if(ioService.equals(IOService.DB_IO)){
            double salary = employeePayrollDBService.getMaximumSalary(gender);
            return salary;
        }
        return 0.0;
    }

    public enum IOService{CONSOLE_IO,FILE_IO,DB_IO,REST_IO}

    public EmployeePayrollService(List<EmployeePayrollData> employeePayrollList){
        this();
        this.employeePayrollList=employeePayrollList;
    }



    public void readData(IOService ioService){
        if (ioService.equals(IOService.CONSOLE_IO)) {
            Scanner scan = new Scanner(System.in);
            System.out.println("Enter Employee Name");
            String empName = scan.next();
            System.out.println("Enter Employee ID");
            int empID = scan.nextInt();
            System.out.println("Enter Employee Salary");
            int empSalary = scan.nextInt();

            EmployeePayrollData adder = new EmployeePayrollData(empID, empName, empSalary);
            employeePayrollList.add(adder);
        }else if (ioService.equals(IOService.FILE_IO)){
            new EmployeePayrollFileIOService().readData();
        }
    }

    public List<EmployeePayrollData> readEmployeePayrollData(IOService ioService){
        if(ioService.equals(IOService.DB_IO))
            this.employeePayrollList = employeePayrollDBService.readData();
        return this.employeePayrollList;
    }

    public boolean checkEmployeePayrollInSyncWithDB(String name) {
        List<EmployeePayrollData> employeePayrollDataList = employeePayrollDBService.getEmployeePayrollData(name);
        return employeePayrollDataList.get(0).equals(getEmployeePayrollData(name));
    }

    public void updateEmployeeSalary(String name, double salary) {
        int result = employeePayrollDBService.updateEmployeeData(name,salary);
        if (result == 0) return;
        EmployeePayrollData employeePayrollData = this.getEmployeePayrollData(name);
        if (employeePayrollData != null) employeePayrollData.employeeSalary = salary;
    }

    private EmployeePayrollData getEmployeePayrollData(String name) {
        return this.employeePayrollList.stream()
                .filter(employeePayrollDataItem -> employeePayrollDataItem.employeeName.equals(name) )
                .findFirst()
                .orElse(null);
    }

    public void writeEmployeePayrollData(IOService ioService){
        if (ioService.equals(IOService.CONSOLE_IO))
            System.out.println("OutPut\n"+employeePayrollList);
        else if (ioService.equals(IOService.FILE_IO))
            new EmployeePayrollFileIOService().writeData(employeePayrollList);
    }


    public void printEmployeePayrollData(IOService ioService) {
        if (ioService.equals(IOService.FILE_IO))
            new EmployeePayrollFileIOService().printData();
    }

    public long countEntries(IOService ioService) {
        if (ioService.equals(IOService.FILE_IO))
            return new EmployeePayrollFileIOService().countEntries();
        return 0;
    }

    public static void main(String[] args) {
        ArrayList<EmployeePayrollData> employeePayrollList = new ArrayList<>();
        EmployeePayrollService employeePayrollService = new EmployeePayrollService(employeePayrollList);
        employeePayrollService.readData(IOService.FILE_IO);
        employeePayrollService.writeEmployeePayrollData(IOService.FILE_IO);
    }
}
