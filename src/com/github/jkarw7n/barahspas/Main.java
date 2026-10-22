package com.github.jkarw7n.barahspas;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;

public class Main {
    public static void main(String[] args) throws SQLException {
        Patient patient = new Patient("Joao Naicolau", null, "Quintelaza", null, "09185982633", null, LocalDate.of(2002, 9, 29), "Male");
        MedicalStaff doctor = new Doctor("Marius", "Bjornasonurza", "Ziska", null, "09028710225", "mariusaziska@gmail.com", "7817822", "Doctors to the Barrio", "T 9 AM - 5 PM", "Radiology");
        QueryManager manager = new QueryManager();

        patient = manager.registerPatient(patient);
        doctor = manager.registerMedicalStaff(doctor);

        Appointment appointment = new Appointment(doctor, patient, LocalDate.now(), LocalTime.now(), LocalTime.of(21, 30));

        appointment = manager.scheduleAppointment(appointment);
        appointment.setEndTime(LocalTime.of(22, 1));
        manager.updateAppointment(appointment);
        appointment.setDiagnosis("Asthma");
        appointment.setPrescription("Oxytocin 400mg");
        manager.completeAppointment(appointment);
        manager.deleteAppointment(appointment);
    }
}
