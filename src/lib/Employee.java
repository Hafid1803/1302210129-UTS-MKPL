package lib;

import java.time.LocalDate;
import java.time.Month;
import java.util.LinkedList;
import java.util.List;

public class Employee {

	private String employeeId;
	private String firstName;
	private String lastName;
	private String idNumber;
	private String address;
	
	private int yearJoined;
	private int monthJoined;
	private int dayJoined;
	private int monthWorkingInYear;
	
	private boolean isForeigner;
	private boolean gender; //true = Laki-laki, false = Perempuan
	
	private int monthlySalary;
	private int otherMonthlyIncome;
	private int annualDeductible;
	
	private String spouseName;
	private String spouseIdNumber;

	private List<String> childNames;
	private List<String> childIdNumbers;
	
	public Employee(String employeeId, String firstName, String lastName, String idNumber, String address, int yearJoined, int monthJoined, int dayJoined, boolean isForeigner, boolean gender) {
		this.employeeId = employeeId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.idNumber = idNumber;
		this.address = address;
		this.yearJoined = yearJoined;
		this.monthJoined = monthJoined;
		this.dayJoined = dayJoined;
		this.isForeigner = isForeigner;
		this.gender = gender;
		
		childNames = new LinkedList<String>();
		childIdNumbers = new LinkedList<String>();
	}
	
	/**
	 * Fungsi untuk menentukan gaji bulanan pegawai berdasarkan grade kepegawaiannya (grade 1: 3.000.000 per bulan, grade 2: 5.000.000 per bulan, grade 3: 7.000.000 per bulan)
	 * Jika pegawai adalah warga negara asing gaji bulanan diperbesar sebanyak 50%
	 */
	
	public void setMonthlySalary(int grade) {
	    switch (grade) {
	        case 1:
	            monthlySalary = 3000000;
	            break;
	        case 2:
	            monthlySalary = 5000000;
	            break;
	        case 3:
	            monthlySalary = 7000000;
	            break;
	        default:
	            throw new IllegalArgumentException("Invalid grade: " + grade);
	    }
	    if (isForeigner) {
	        monthlySalary *= 1.5;
	    }
	}

	private int calculateWorkingMonthsInYear() {
	    LocalDate date = LocalDate.now();
	    if (date.getYear() == yearJoined) {
	        return date.getMonthValue() - monthJoined;
	    } else {
	        return 12;
	    }
	}

	public int getAnnualIncomeTax() {
	    int workingMonths = calculateWorkingMonthsInYear();
	    return TaxFunction.calculateTax(monthlySalary, otherMonthlyIncome, workingMonths, annualDeductible, spouseIdNumber.equals(""), childIdNumbers.size());
	}
	
	public void setSpouse(String spouseName, String spouseIdNumber) {
	    this.spouseName = spouseName;
	    this.spouseIdNumber = spouseIdNumber;
	}
	
	public void addChild(String childName, String childIdNumber) {
		childNames.add(childName);
		childIdNumbers.add(childIdNumber);
	}
	
	public int getAnnualIncomeTax() {
	//LongMethod
	//Menghitung berapa lama pegawai bekerja dalam setahun ini, jika pegawai sudah bekerja dari tahun sebelumnya maka otomatis dianggap 12 bulan.
	    int monthsWorked = calculateMonthsWorked();
	    int annualIncome = calculateAnnualIncome(monthsWorked);
	    int tax = calculateTax(annualIncome);
	    tax = applyTaxReductions(tax);
	    return tax;
	}
	
	private int calculateMonthsWorked() {
	    LocalDate date = LocalDate.now();
	    if (date.getYear() == yearJoined) {
	        return date.getMonthValue() - monthJoined;
	    } else {
	        return 12;
	    }
	}
	
	private int calculateAnnualIncome(int monthsWorked) {
	    return (monthlySalary + otherMonthlyIncome) * monthsWorked - annualDeductible;
	}
	
	private int calculateTax(int annualIncome) {
	    if (annualIncome < 50000000) {
	        return (int) (0.05 * annualIncome);
	    } else if (annualIncome < 250000000) {
	        return (int) (0.15 * annualIncome);
	    } else {
	        return (int) (0.25 * annualIncome);
	    }
	}
	
	private int applyTaxReductions(int tax) {
	    if (!spouseIdNumber.equals("")) {
	        tax -= 5000000;
	    }
	    if (childIdNumbers.size() > 0) {
	        tax -= childIdNumbers.size() * 2000000;
	    }
	    return tax;
	}
