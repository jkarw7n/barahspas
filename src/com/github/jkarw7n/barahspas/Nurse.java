package com.github.jkarw7n.barahspas;

class Nurse extends MedicalStaff {
    private String assignedProgram;

    public Nurse() {
        super();
        setAssignedProgram("Maternal and Child Health");
    }

    public Nurse(
        String firstName,
        String middleName,
        String lastName,
        String nameSuffix,
        String mobileNumber,
        String emailAddress,
        String licenseNumber,
        String employmentType,
        String shiftSchedule,
        String assignedProgram
    ) {
        super(
            firstName,
            middleName,
            lastName,
            nameSuffix,
            mobileNumber,
            emailAddress,
            licenseNumber,
            employmentType,
            shiftSchedule
        );
        setAssignedProgram(assignedProgram);
    }

    public String getAssignedProgram() { return assignedProgram; }

    public void setAssignedProgram(String assignedProgram) {
        if (assignedProgram == null || assignedProgram.isBlank())
            throw new IllegalArgumentException("Error: Nurse assigned program cannot be null or blank.");

        // String[] assignedPrograms = {"Maternal and Child Health", "Immunization and Vaccines", "Family Planning", "Disease Control and Screening", "General Consultation and Vitals", "Environmental and Sanitary Health", "Mental Health and Substance Support"};

        this.assignedProgram = assignedProgram;
    }

    public boolean canDiagnoseAndPrescribe() { return false; }
}
