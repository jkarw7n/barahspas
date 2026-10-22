package com.github.jkarw7n.barahspas;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.SortedMap;

class Patient extends Person {
    private LocalDate birthDate;
    private String sex;
    private SortedMap<LocalDate, String> medicalRecords;

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
    public SortedMap<LocalDate, String> getMedicalRecords() { return medicalRecords; }

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

    public void setMedicalRecords(SortedMap<LocalDate, String> medicalRecords) {
        if (medicalRecords == null || medicalRecords.size() == 0) {
            this.medicalRecords = null;
            return;
        }

        for (SortedMap.Entry<LocalDate, String> record : medicalRecords.entrySet()) {
            if (record.getKey() == null)
                throw new IllegalArgumentException("Error: Patient medical record date cannot be null.");

            if (record.getKey().getYear() < 1900)
                throw new IllegalArgumentException("Error: Patient medical record date cannot be earlier than 1900.");

            if (record.getKey().isAfter(LocalDate.now()))
                throw new IllegalArgumentException("Error: Patient medical record date cannot be later than " + LocalDate.now() + ".");

            if (record.getValue() == null || record.getValue().isBlank())
                throw new IllegalArgumentException("Error: Patient medical record cannot be null or blank.");
        }

        // check validated dates for appointment and birthdates

        this.medicalRecords = medicalRecords;
    }

    public long getAge() {
        return ChronoUnit.YEARS.between(birthDate, LocalDate.now());
    }
}
