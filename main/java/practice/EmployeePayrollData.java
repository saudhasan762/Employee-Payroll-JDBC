package practice;

import java.time.LocalDate;
import java.util.Objects;

public class EmployeePayrollData {
    String employeeName;
    int employeeID;
    double employeeSalary;
    public LocalDate startDate;

    public EmployeePayrollData(int employeeID,String employeeName,double employeeSalary) {
        this.employeeName = employeeName;
        this.employeeID = employeeID;
        this.employeeSalary = employeeSalary;
    }

    public EmployeePayrollData(int id, String name, double salary, LocalDate startDate) {
        this(id,name,salary);
        this.startDate = startDate;
    }


    @Override
    public String toString() {
        return "Employee Id: '" + employeeID + '\'' + ", Employee Name: " + employeeName + ", Employee Salary: " + employeeSalary;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmployeePayrollData that = (EmployeePayrollData) o;
        return employeeID == that.employeeID &&
                Double.compare(that.employeeSalary, employeeSalary) == 0 &&
                Objects.equals(employeeName, that.employeeName);
    }

}
