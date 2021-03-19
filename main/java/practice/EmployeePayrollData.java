package practice;

import java.time.LocalDate;
import java.util.Objects;

public class EmployeePayrollData {
    int id;
    String name;
    public String gender;
    double salary;
    public LocalDate startDate;

    public EmployeePayrollData(int id,String name,double salary) {
        this.name = name;
        this.id = id;
        this.salary = salary;
    }

    public EmployeePayrollData(int id, String name, double salary, LocalDate startDate) {
        this(id,name,salary);
        this.startDate = startDate;
    }

    public EmployeePayrollData(int id, String name, String gender, double salary, LocalDate startDate) {
        this(id,name,salary,startDate);
        this.gender = gender;
    }

    @Override
    public String toString() {
        return "Employee Id: '" + id + '\'' + ", Employee Name: " + name + ", Employee Salary: " + salary;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmployeePayrollData that = (EmployeePayrollData) o;
        return id == that.id &&
                Double.compare(that.salary, salary) == 0 &&
                Objects.equals(name, that.name);
    }


    @Override
    public int hashCode() {
        return Objects.hash(id, name, gender, salary, startDate);
    }


}
