package com.github.jkarw7n.barahspas;

class Doctor extends MedicalStaff {
    private String specialization;

    public Doctor() {
        super();
        setSpecialization("Specialization");
    }

    public Doctor(
        String firstName,
        String middleName,
        String lastName,
        String nameSuffix,
        String mobileNumber,
        String emailAddress,
        String licenseNumber,
        String employmentType,
        String shiftSchedule,
        String specialization
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
        setSpecialization(specialization);
    }

    public String getSpecialization() { return specialization; }

    public void setSpecialization(String specialization) {
        if (specialization == null || specialization.isBlank())
            throw new IllegalArgumentException("Error: Doctor specialization cannot be null or blank.");

        this.specialization = specialization.replaceAll("\\s+", " ").strip();
    }

    @Override
    public boolean canDiagnoseAndPrescribe() {
        return super.getLicenseNumber() != null;
    }
}
