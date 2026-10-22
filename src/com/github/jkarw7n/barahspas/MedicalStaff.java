package com.github.jkarw7n.barahspas;

abstract class MedicalStaff extends Person {
    private String licenseNumber;
    private String employmentType;
    private String shiftSchedule;

    protected MedicalStaff() {
        super();
        setLicenseNumber(null);
        setEmploymentType("Plantilla");
        setShiftSchedule(null);
    }

    protected MedicalStaff(
        String firstName,
        String middleName,
        String lastName,
        String nameSuffix,
        String mobileNumber,
        String emailAddress,
        String licenseNumber,
        String employmentType,
        String shiftSchedule
    ) {
        super(
            firstName,
            middleName,
            lastName,
            nameSuffix,
            mobileNumber,
            emailAddress
        );
        setLicenseNumber(licenseNumber);
        setEmploymentType(employmentType);
        setShiftSchedule(shiftSchedule);
    }

    public String getLicenseNumber() { return licenseNumber; }
    public String getEmploymentType() { return employmentType; }
    public String getShiftSchedule() { return shiftSchedule; }

    public void setLicenseNumber(String licenseNumber) {
        if (licenseNumber == null || licenseNumber.isBlank()) {
            this.licenseNumber = null;
            return;
        }

        if (licenseNumber.replaceAll("\\s+", " ").strip().length() != 7)
            throw new IllegalArgumentException("Error: Medical staff license number must have a length of exactly seven characters.");

        if (!licenseNumber.replaceAll("\\s+", " ").strip().matches("^\\d+$"))
            throw new IllegalArgumentException("Error: Medical staff license number must be composed of only numerical digits.");

        this.licenseNumber = licenseNumber.replaceAll("\\s+", " ").strip();
    }

    public void setEmploymentType(String employmentType) {
        if (employmentType == null || employmentType.isBlank())
            throw new IllegalArgumentException("Error: Medical staff employment type cannot be null or blank.");

        this.employmentType = employmentType;
    }

    public void setShiftSchedule(String shiftSchedule) {
        if (shiftSchedule == null || shiftSchedule.isBlank()) {
            this.shiftSchedule = null;
            return;
        }

        this.shiftSchedule = shiftSchedule.replaceAll("\\s+", " ").strip();
    }

    public abstract boolean canDiagnoseAndPrescribe();
}
