package com.github.jkarw7n.barahspas;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

class Patient extends Person {
    private LocalDate birthDate;
    private String sex;
    private List<Object[]> medicalRecords;

    public Patient() {
        super();
        setBirthDate(LocalDate.now());
        setSex("Male");
        setMedicalRecords(null);
    }

    public Patient(
        String firstName,
        String middleName,
        String lastName,
        String nameSuffix,
        String mobileNumber,
        String emailAddress,
        LocalDate birthDate,
        String sex
    ) {
        super(
            firstName,
            middleName,
            lastName,
            nameSuffix,
            mobileNumber,
            emailAddress
        );
        setBirthDate(birthDate);
        setSex(sex);
    }

    public LocalDate getBirthDate() { return birthDate; }
    public String getSex() { return sex; }
    public List<Object[]> getMedicalRecords() { return medicalRecords; }

    public void setBirthDate(LocalDate birthDate) {
        if (birthDate == null)
            throw new IllegalArgumentException("Error: Patient birth date cannot be null.");

        if (birthDate.getYear() < 1900)
            throw new IllegalArgumentException("Error: Patient birth year cannot be earlier than 1900.");

        if (birthDate.isAfter(LocalDate.now()))
            throw new IllegalArgumentException("Error: Patient birth date cannot be later than " + LocalDate.now() + ".");

        this.birthDate = birthDate;
    }

    public void setSex(String sex) {
        if (sex == null || sex.isBlank())
            throw new IllegalArgumentException("Error: Patient sex cannot be null or blank.");

        this.sex = sex;
    }

    public void setMedicalRecords(List<Object[]> medicalRecords) {
        if (medicalRecords == null || medicalRecords.size() == 0) {
            this.medicalRecords = null;
            return;
        }

        for (Object[] record : medicalRecords) {
            if (record[0] == null)
                throw new IllegalArgumentException("Error: Patient medical record date cannot be null.");

            if (((LocalDate) record[0]).getYear() < 2000)
                throw new IllegalArgumentException("Error: Patient medical record date cannot be earlier than 2000.");

            if (((LocalDate) record[0]).isAfter(LocalDate.now()))
                throw new IllegalArgumentException("Error: Patient medical record date cannot be later than " + LocalDate.now() + ".");

            if (record[1] == null || ((String) record[1]).isBlank())
                throw new IllegalArgumentException("Error: Patient medical record cannot be null or blank.");
        }

        // check validated dates for appointment and birthdates

        this.medicalRecords = medicalRecords;
    }

    public long getAge() {
        return ChronoUnit.YEARS.between(birthDate, LocalDate.now());
    }
}
