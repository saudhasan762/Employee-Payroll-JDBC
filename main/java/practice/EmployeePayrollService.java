package practice;

import java.time.LocalDate;
import java.util.*;

public class EmployeePayrollService {
    private List<EmployeePayrollData> employeePayrollList;
    private final EmployeePayrollDBService employeePayrollDBService;
    public enum IOService{CONSOLE_IO,FILE_IO,DB_IO}

    public EmployeePayrollService(List<EmployeePayrollData> employeePayrollList){
        this();
        this.employeePayrollList=employeePayrollList;
    }

    public EmployeePayrollService() {
        employeePayrollDBService = EmployeePayrollDBService.getInstance();
    }

    public List<EmployeePayrollData> readEmployeePayrollDataForDateRange(IOService ioService, LocalDate start_date, LocalDate end_date) {
        if(ioService.equals(IOService.DB_IO))
            this.employeePayrollList = employeePayrollDBService.getEmployeeForDateRange(start_date,end_date);
        return this.employeePayrollList;
    }

    public Map<String, Double> readAverageSalaryByGender(IOService ioService) {
        if(ioService.equals(IOService.DB_IO))
            return employeePayrollDBService.getAverageSalaryByGender();
        return null;
    }

    public Map<String, Double> readSumOfSalaryByGender(IOService ioService) {
        if(ioService.equals(IOService.DB_IO)){
            return employeePayrollDBService.getSumOfSalaryByGender();
        }
        return null;
    }

    public Map<String, Integer> readCountByGender(IOService ioService) {
        if(ioService.equals(IOService.DB_IO)){
            return employeePayrollDBService.getCountByGender();
        }
        return null;
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

    public void addEmployeeToPayroll(List<EmployeePayrollData> employeePayrollDataList) {
        employeePayrollDataList.forEach(employeePayrollData -> {
            System.out.println("Employee Being Added: "+employeePayrollData.employeeName);
            this.addEmployeeToPayroll(employeePayrollData.employeeName, employeePayrollData.employeeSalary,
                    employeePayrollData.startDate, employeePayrollData.gender);
            System.out.println("Employee Added: "+employeePayrollData.employeeName);
        });
        System.out.println(this.employeePayrollList);
    }

    public void addEmployeeToPayrollWithThreads(List<EmployeePayrollData> employeePayrollDataList){
        Map<Integer, Boolean> integerBooleanMap = new HashMap<>();
        employeePayrollDataList.forEach(employeePayrollData -> {
            Runnable task = () -> {
                integerBooleanMap.put(employeePayrollData.hashCode(), false);
                System.out.println("Employee Being Added: "+Thread.currentThread().getName());
                this.addEmployeeToPayroll(employeePayrollData.employeeName, employeePayrollData.employeeSalary,
                        employeePayrollData.startDate, employeePayrollData.gender);
                integerBooleanMap.put(employeePayrollData.hashCode(), true);
                System.out.println("Employee Added: "+Thread.currentThread().getName());
            };
            Thread thread = new Thread(task, employeePayrollData.employeeName);
            thread.start();
        });
        while (integerBooleanMap.containsValue(false)){
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {}
        }
        System.out.println(employeePayrollDataList);
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

    public void addEmployeeToPayroll(String name, double salary, LocalDate startDate, String gender) {
        employeePayrollList.add(employeePayrollDBService.addEmployeeToPayroll(name, salary,startDate,gender));
    }

    public void printEmployeePayrollData(IOService ioService) {
        if (ioService.equals(IOService.FILE_IO))
            new EmployeePayrollFileIOService().printData();
    }

    public long countEntries(IOService ioService) {
        if (ioService.equals(IOService.FILE_IO))
            return new EmployeePayrollFileIOService().countEntries();
        return employeePayrollList.size();
    }

    public static void main(String[] args) {
        ArrayList<EmployeePayrollData> employeePayrollList = new ArrayList<>();
        EmployeePayrollService employeePayrollService = new EmployeePayrollService(employeePayrollList);
        employeePayrollService.readData(IOService.FILE_IO);
        employeePayrollService.writeEmployeePayrollData(IOService.FILE_IO);
    }
}
