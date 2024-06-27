package lib;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

public class Employee {

    private static final int GRADE_1_SALARY = 3000000;
    private static final int GRADE_2_SALARY = 5000000;
    private static final int GRADE_3_SALARY = 7000000;
    private static final double FOREIGNER_SALARY_MULTIPLIER = 1.5;

    private String employeeId;
    private String firstName;
    private String lastName;
    private String idNumber;
    private String address;

    private int yearJoined;
    private int monthJoined;
    private int dayJoined;

    private boolean isForeigner;
    private Gender gender;

    private int monthlySalary;
    private int otherMonthlyIncome;
    private int annualDeductible;

    private Spouse spouse;
    private List<Child> children;

    private Employee(Builder builder) {
        this.employeeId = builder.employeeId;
        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
        this.idNumber = builder.idNumber;
        this.address = builder.address;
        this.yearJoined = builder.yearJoined;
        this.monthJoined = builder.monthJoined;
        this.dayJoined = builder.dayJoined;
        this.isForeigner = builder.isForeigner;
        this.gender = builder.gender;
        this.children = builder.children != null ? builder.children : new LinkedList<>();
    }

    public void setMonthlySalary(int grade) {
        switch (grade) {
            case 1:
                monthlySalary = GRADE_1_SALARY;
                break;
            case 2:
                monthlySalary = GRADE_2_SALARY;
                break;
            case 3:
                monthlySalary = GRADE_3_SALARY;
                break;
            default:
                throw new IllegalArgumentException("Invalid grade: " + grade);
        }
        if (isForeigner) {
            monthlySalary *= FOREIGNER_SALARY_MULTIPLIER;
        }
    }

    public void setAnnualDeductible(int deductible) {
        this.annualDeductible = deductible;
    }

    public void setAdditionalIncome(int income) {
        this.otherMonthlyIncome = income;
    }

    public void setSpouse(String spouseName, String spouseIdNumber) {
        this.spouse = new Spouse(spouseName, spouseIdNumber);
    }

    public void addChild(String childName, String childIdNumber) {
        children.add(new Child(childName, childIdNumber));
    }

    public int getAnnualIncomeTax() {
        int monthsWorkedInYear = calculateMonthsWorkedInYear();
        boolean hasSpouse = spouse != null;
        return TaxFunction.calculateTax(monthlySalary, otherMonthlyIncome, monthsWorkedInYear, annualDeductible, hasSpouse, children.size());
    }

    private int calculateMonthsWorkedInYear() {
        LocalDate currentDate = LocalDate.now();
        if (currentDate.getYear() == yearJoined) {
            return currentDate.getMonthValue() - monthJoined;
        } else {
            return 12;
        }
    }

    public enum Gender {
        MALE, FEMALE
    }

    public static class Spouse {
        private String name;
        private String idNumber;

        public Spouse(String name, String idNumber) {
            this.name = name;
            this.idNumber = idNumber;
        }
    }

    public static class Child {
        private String name;
        private String idNumber;

        public Child(String name, String idNumber) {
            this.name = name;
            this.idNumber = idNumber;
        }
    }

    public static class Builder {
        private String employeeId;
        private String firstName;
        private String lastName;
        private String idNumber;
        private String address;

        private int yearJoined;
        private int monthJoined;
        private int dayJoined;

        private boolean isForeigner;
        private Gender gender;

        private List<Child> children;

        public Builder(String employeeId, String firstName, String lastName, String idNumber, String address) {
            this.employeeId = employeeId;
            this.firstName = firstName;
            this.lastName = lastName;
            this.idNumber = idNumber;
            this.address = address;
        }

        public Builder yearJoined(int yearJoined) {
            this.yearJoined = yearJoined;
            return this;
        }

        public Builder monthJoined(int monthJoined) {
            this.monthJoined = monthJoined;
            return this;
        }

        public Builder dayJoined(int dayJoined) {
            this.dayJoined = dayJoined;
            return this;
        }

        public Builder isForeigner(boolean isForeigner) {
            this.isForeigner = isForeigner;
            return this;
        }

        public Builder gender(Gender gender) {
            this.gender = gender;
            return this;
        }

        public Builder children(List<Child> children) {
            this.children = children;
            return this;
        }

        public Employee build() {
            return new Employee(this);
        }
    }
}
