package com.github.jkarw7n.barahspas;

import java.time.LocalDate;
import java.time.LocalTime;

class Appointment implements Identifiable {
    private int id;
    private MedicalStaff medicalStaff;
    private Patient patient;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private String status;
    private String diagnosis;
    private String prescription;
    private String notes;

    public Appointment(
        MedicalStaff medicalStaff,
        Patient patient,
        LocalDate date,
        LocalTime startTime,
        LocalTime endTime
    ) {
        setMedicalStaff(medicalStaff);
        setPatient(patient);
        setDate(date);
        setStartTime(startTime);
        setEndTime(endTime);
        setStatus("Scheduled");
    }

    public int getId() { return id; }
    public MedicalStaff getMedicalStaff() { return medicalStaff; }
    public Patient getPatient() { return patient; }
    public LocalDate getDate() { return date; }
    public LocalTime getStartTime() { return startTime; }
    public LocalTime getEndTime() { return endTime; }
    public String getStatus() { return status; }
    public String getDiagnosis() { return diagnosis; }
    public String getPrescription() { return prescription; }
    public String getNotes() { return notes; }

    public void setId(int id) {
        if (id <= 0)
            throw new IllegalArgumentException("Error: Appointment ID cannot be equal to or less than zero.");

        this.id = id;
    }

    public void setMedicalStaff(MedicalStaff medicalStaff) {
        if (medicalStaff == null)
            throw new IllegalArgumentException("Error: Appointment medical staff cannot be null.");

        if (medicalStaff.getId() == 0)
            throw new IllegalArgumentException("Error: Appointment medical staff must already have an ID.");

        this.medicalStaff = medicalStaff;
    }

    public void setPatient(Patient patient) {
        if (patient == null)
            throw new IllegalArgumentException("Error: Patient cannot be null.");

        if (patient.getId() == 0)
            throw new IllegalArgumentException("Error: Patient must already have an ID.");

        this.patient = patient;
    }

    public void setDate(LocalDate date) {
        if (date == null)
            throw new IllegalArgumentException("Error: Appointment date cannot be null.");

        if (date.getYear() < 2000)
            throw new IllegalArgumentException("Error: Appointment year cannot be earlier than 2000.");

        this.date = date;
    }

    public void setStartTime(LocalTime startTime) {
        if (startTime == null)
            throw new IllegalArgumentException("Error: Appointment start time cannot be null.");

        this.startTime = startTime;
    }

    public void setEndTime(LocalTime endTime) {
        if (endTime == null)
            throw new IllegalArgumentException("Error: Appointment end time cannot be null.");

        if (startTime != null && endTime.isBefore(startTime))
            throw new IllegalArgumentException("Error: Appointment end time cannot be earlier than the start time.");

        this.endTime = endTime;
    }

    public void setStatus(String status) {
        if (status == null || status.isBlank())
            throw new IllegalArgumentException("Error: Appointment status cannot be null or blank.");

        this.status = status;
    }

    public void setDiagnosis(String diagnosis) {
        if (diagnosis == null || diagnosis.isBlank()) {
            this.diagnosis = null;
            return;
        }

        if (!medicalStaff.canDiagnoseAndPrescribe())
            throw new IllegalArgumentException("Error: Appointment medical staff does not have the authority to diagnose patients.");

        this.diagnosis = diagnosis.replaceAll("\\s+", " ").strip();
    }

    public void setPrescription(String prescription) {
        if (prescription == null || prescription.isBlank()) {
            this.prescription = null;
            return;
        }

        if (!medicalStaff.canDiagnoseAndPrescribe())
            throw new IllegalArgumentException("Error: Appointment medical staff does not have the authority to prescribe to patients.");

        this.prescription = prescription.replaceAll("\\s+", " ").strip();
    }

    public void setNotes(String notes) {
        if (notes == null || notes.isBlank()) {
            this.notes = null;
            return;
        }

        this.notes = notes.replaceAll("\\s+", " ").strip();
    }
}
