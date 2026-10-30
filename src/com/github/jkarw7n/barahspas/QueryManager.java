package com.github.jkarw7n.barahspas;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

class QueryManager {
    private static final String DATABASE_URL = "jdbc:sqlite:barahspas.db";
    private static Connection connection;

    public QueryManager() throws SQLException {
        connection = DriverManager.getConnection(DATABASE_URL);

        String pragma = "PRAGMA foreign_keys = ON;",
            medicalStaff = """
                CREATE TABLE IF NOT EXISTS MedicalStaff (
                    MedicalStaffID INTEGER PRIMARY KEY,
                    FirstName TEXT NOT NULL,
                    MiddleName TEXT,
                    LastName TEXT NOT NULL,
                    NameSuffix TEXT,
                    MobileNumber TEXT NOT NULL UNIQUE,
                    EmailAddress TEXT UNIQUE,
                    LicenseNumber TEXT UNIQUE,
                    EmploymentType TEXT NOT NULL,
                    ShiftSchedule TEXT,
                    MedicalStaffType TEXT NOT NULL,
                    DoctorSpecialization TEXT,
                    AssignedProgram TEXT,
                    IsDeleted BOOLEAN DEFAULT 0,
                    CONSTRAINT UniqueStaffIdentity UNIQUE (FirstName, MiddleName, LastName, NameSuffix, MobileNumber)
                );
            """,
            patient = """
                CREATE TABLE IF NOT EXISTS Patient (
                    PatientID INTEGER PRIMARY KEY,
                    FirstName TEXT NOT NULL,
                    MiddleName TEXT,
                    LastName TEXT NOT NULL,
                    NameSuffix TEXT,
                    MobileNumber TEXT NOT NULL UNIQUE,
                    EmailAddress TEXT UNIQUE,
                    BirthDate TEXT NOT NULL,
                    PatientSex TEXT NOT NULL,
                    IsDeleted BOOLEAN DEFAULT 0,
                    CONSTRAINT UniquePatientIdentity UNIQUE (FirstName, MiddleName, LastName, NameSuffix, MobileNumber)
                );
            """,
            medicalRecord = """
                CREATE TABLE IF NOT EXISTS MedicalRecord (
                    PatientID INTEGER,
                    MedicalRecord TEXT NOT NULL,
                    DateRecorded TEXT DEFAULT (DATE('now')),
                    IsDeleted BOOLEAN DEFAULT 0,
                    PRIMARY KEY (PatientID, MedicalRecord, DateRecorded),
                    FOREIGN KEY (PatientID) REFERENCES Patient (PatientID)
                );
            """,
            appointment = """
                CREATE TABLE IF NOT EXISTS Appointment (
                    AppointmentID INTEGER PRIMARY KEY,
                    MedicalStaffID INTEGER,
                    PatientID INTEGER,
                    AppointmentDate TEXT NOT NULL,
                    StartTime TEXT NOT NULL,
                    EndTime TEXT NOT NULL,
                    AppointmentStatus TEXT DEFAULT 'Scheduled',
                    AppointmentDiagnosis TEXT,
                    AppointmentPrescription TEXT,
                    AppointmentNotes TEXT,
                    IsDeleted BOOLEAN DEFAULT 0,
                    FOREIGN KEY (MedicalStaffID) REFERENCES MedicalStaff (MedicalStaffID),
                    FOREIGN KEY (PatientID) REFERENCES Patient (PatientID)
                );
            """;

        Statement statement = connection.createStatement();
        
        for (String query : new String[] {pragma, medicalStaff, patient, medicalRecord, appointment}) {
            statement.executeUpdate(query);
        }
    }

    public MedicalStaff getMedicalStaff(int medicalStaffId) throws SQLException {
        if (medicalStaffId <= 0)
            throw new IllegalArgumentException("Error: Medical staff ID cannot be equal to or less than zero.");

        MedicalStaff medicalStaff = null;

        String query = """
            SELECT *
            FROM MedicalStaff
            WHERE
                MedicalStaffID = ?
                AND
                IsDeleted = 0;
        """;

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, medicalStaffId);

        try (ResultSet set = preparedStatement.executeQuery()) {
            while (set.next()) {
                if (set.getString("MedicalStaffType").equals("Doctor")) {
                    medicalStaff = new Doctor(
                        set.getString("FirstName"),
                        set.getString("MiddleName"),
                        set.getString("LastName"),
                        set.getString("NameSuffix"),
                        set.getString("MobileNumber"),
                        set.getString("EmailAddress"),
                        set.getString("LicenseNumber"),
                        set.getString("EmploymentType"),
                        set.getString("ShiftSchedule"),
                        set.getString("DoctorSpecialization")
                    );
                } else {
                    medicalStaff = new Nurse(
                        set.getString("FirstName"),
                        set.getString("MiddleName"),
                        set.getString("LastName"),
                        set.getString("NameSuffix"),
                        set.getString("MobileNumber"),
                        set.getString("EmailAddress"),
                        set.getString("LicenseNumber"),
                        set.getString("EmploymentType"),
                        set.getString("ShiftSchedule"),
                        set.getString("AssignedProgram")
                    );
                }

                medicalStaff.setId(set.getInt("MedicalStaffID"));
            }
        }

        return medicalStaff;
    }

    public List<MedicalStaff> getAllMedicalStaff() throws SQLException {
        List<MedicalStaff> list = new ArrayList<>();

        String query = """
            SELECT MedicalStaffID
            FROM MedicalStaff
            WHERE IsDeleted = 0;
        """;

        Statement statement = connection.createStatement();

        try (ResultSet set = statement.executeQuery(query)) {
            while (set.next()) {
                list.add(getMedicalStaff(set.getInt("MedicalStaffID")));
            }
        }

        return list;
    }

    public List<MedicalStaff> filterMedicalStaff(Map<String, Object> filters) throws SQLException {
        if (filters == null || filters.size() == 0)
            throw new IllegalArgumentException("Error: Medical staff filter cannot be null or empty.");

        List<MedicalStaff> list = new ArrayList<>();
        String query = "SELECT MedicalStaffID FROM MedicalStaff WHERE IsDeleted = 0";

        for (Map.Entry<String, Object> entry : filters.entrySet()) {
            String stringApostrophe = entry.getValue() instanceof String ? "'" : "";
            query += " AND " + entry.getKey() + " = " + stringApostrophe + entry.getValue() + stringApostrophe;
        }

        Statement statement = connection.createStatement();

        try (ResultSet set = statement.executeQuery(query)) {
            while (set.next()) {
                list.add(getMedicalStaff(set.getInt(1)));
            }
        }

        return list;
    }

    public MedicalStaff registerMedicalStaff(MedicalStaff medicalStaff) throws SQLException {
        if (medicalStaff == null)
            throw new IllegalArgumentException("Error: Medical staff cannot be null.");

        MedicalStaff registeredMedicalStaff = null;

        String query = """
            INSERT INTO MedicalStaff (
                FirstName,
                MiddleName,
                LastName,
                NameSuffix,
                MobileNumber,
                EmailAddress,
                LicenseNumber,
                EmploymentType,
                ShiftSchedule,
                MedicalStaffType,
                DoctorSpecialization,
                AssignedProgram
            ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);
        """;

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, medicalStaff.getFirstName());
        preparedStatement.setString(2, medicalStaff.getMiddleName());
        preparedStatement.setString(3, medicalStaff.getLastName());
        preparedStatement.setString(4, medicalStaff.getNameSuffix());
        preparedStatement.setString(5, medicalStaff.getMobileNumber());
        preparedStatement.setString(6, medicalStaff.getEmailAddress());
        preparedStatement.setString(7, medicalStaff.getLicenseNumber());
        preparedStatement.setString(8, medicalStaff.getEmploymentType());
        preparedStatement.setString(9, medicalStaff.getShiftSchedule());

        if (medicalStaff instanceof Doctor doctor) {
            preparedStatement.setString(10, "Doctor");
            preparedStatement.setString(11, doctor.getSpecialization());
            preparedStatement.setString(12, null);
        } else {
            preparedStatement.setString(10, "Nurse");
            preparedStatement.setString(11, null);
            preparedStatement.setString(12, ((Nurse) medicalStaff).getAssignedProgram());
        }

        preparedStatement.executeUpdate();

        try (ResultSet set = preparedStatement.getGeneratedKeys()) {
            while (set.next()) {
                registeredMedicalStaff = getMedicalStaff((int) set.getLong(1));
            }
        }

        return registeredMedicalStaff;
    }

    public boolean updateMedicalStaff(MedicalStaff medicalStaff) throws SQLException {
        if (medicalStaff.getId() <= 0)
            throw new IllegalArgumentException("Error: Medical staff ID cannot be equal to or less than zero.");

        String query = """
            INSERT INTO MedicalStaff (FirstName, MiddleName, LastName, NameSuffix, MobileNumber, EmailAddress, LicenseNumber, EmploymentType, ShiftSchedule, MedicalStaffType, DoctorSpecialization, AssignedProgram, MedicalStaffID)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            ON CONFLICT (MedicalStaffID)
            DO UPDATE SET
                FirstName = EXCLUDED.FirstName,
                MiddleName = EXCLUDED.MiddleName,
                LastName = EXCLUDED.LastName,
                NameSuffix = EXCLUDED.NameSuffix,
                MobileNumber = EXCLUDED.MobileNumber,
                EmailAddress = EXCLUDED.EmailAddress,
                LicenseNumber = EXCLUDED.LicenseNumber,
                EmploymentType = EXCLUDED.EmploymentType,
                ShiftSchedule = EXCLUDED.ShiftSchedule,
                MedicalStaffType = EXCLUDED.MedicalStaffType,
                DoctorSpecialization = EXCLUDED.DoctorSpecialization,
                AssignedProgram = EXCLUDED.AssignedProgram;
        """;

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, medicalStaff.getFirstName());
        preparedStatement.setString(2, medicalStaff.getMiddleName());
        preparedStatement.setString(3, medicalStaff.getLastName());
        preparedStatement.setString(4, medicalStaff.getNameSuffix());
        preparedStatement.setString(5, medicalStaff.getMobileNumber());
        preparedStatement.setString(6, medicalStaff.getEmailAddress());
        preparedStatement.setString(7, medicalStaff.getLicenseNumber());
        preparedStatement.setString(8, medicalStaff.getEmploymentType());
        preparedStatement.setString(9, medicalStaff.getShiftSchedule());

        if (medicalStaff instanceof Doctor doctor) {
            preparedStatement.setString(10, "Doctor");
            preparedStatement.setString(11, doctor.getSpecialization());
            preparedStatement.setString(12, null);
        } else {
            preparedStatement.setString(10, "Nurse");
            preparedStatement.setString(11, null);
            preparedStatement.setString(12, ((Nurse) medicalStaff).getAssignedProgram());
        }

        preparedStatement.setInt(13, medicalStaff.getId());
        preparedStatement.executeUpdate();
        return true;
    }

    public boolean deleteMedicalStaff(MedicalStaff medicalStaff) throws SQLException {
        if (medicalStaff.getId() <= 0)
            throw new IllegalArgumentException("Error: Medical staff ID cannot be equal to or less than zero.");

        String query = """
            UPDATE MedicalStaff
            SET IsDeleted = 1
            WHERE MedicalStaffID = ?;
        """;

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, medicalStaff.getId());
        preparedStatement.executeUpdate();
        return true;
    }

    public Patient getPatient(int patientId) throws SQLException {
        if (patientId <= 0)
            throw new IllegalArgumentException("Error: Patient ID cannot be equal to or less than zero.");

        Patient patient = null;

        String patientQuery = """
            SELECT *
            FROM Patient
            WHERE
                PatientID = ?
                AND
                IsDeleted = 0;
        """;

        PreparedStatement patientStatement = connection.prepareStatement(patientQuery);
        patientStatement.setInt(1, patientId);

        try (ResultSet set = patientStatement.executeQuery()) {
            while (set.next()) {
                patient = new Patient(
                    set.getString("FirstName"),
                    set.getString("MiddleName"),
                    set.getString("LastName"),
                    set.getString("NameSuffix"),
                    set.getString("MobileNumber"),
                    set.getString("EmailAddress"),
                    LocalDate.parse(set.getString("BirthDate")),
                    set.getString("PatientSex")
                );

                patient.setId(set.getInt(1));
            }
        }

        List<Object[]> medicalRecords = new ArrayList<>();

        String medicalRecordQuery = """
            SELECT *
            FROM MedicalRecord
            WHERE
                PatientID = ?
                AND
                IsDeleted = 0;
        """;

        PreparedStatement medicalRecordStatement = connection.prepareStatement(medicalRecordQuery);
        medicalRecordStatement.setInt(1, patientId);

        try (ResultSet set = medicalRecordStatement.executeQuery()) {
            while (set.next()) {
                medicalRecords.add(new Object[] {
                    LocalDate.parse(set.getString("DateRecorded")),
                    set.getString("MedicalRecord")
                });
            }
        }

        medicalRecords.sort(Comparator.comparing(medicalRecord -> (LocalDate) medicalRecord[0]));
        patient.setMedicalRecords(medicalRecords);
        return patient;
    }

    public List<Patient> getAllPatients() throws SQLException {
        List<Patient> list = new ArrayList<>();

        String query = """
            SELECT PatientID
            FROM Patient
            WHERE IsDeleted = 0;
        """;

        Statement statement = connection.createStatement();

        try (ResultSet set = statement.executeQuery(query)) {
            while (set.next()) {
                list.add(getPatient(set.getInt("PatientID")));
            }
        }

        return list;
    }

    public List<Patient> filterPatients(Map<String, Object> filters) throws SQLException {
        if (filters == null || filters.size() == 0)
            throw new IllegalArgumentException("Error: Patients filter cannot be null or empty.");

        List<Patient> list = new ArrayList<>();
        String query = "SELECT PatientID FROM Patient WHERE IsDeleted = 0";

        for (Map.Entry<String, Object> entry : filters.entrySet()) {
            String stringApostrophe = entry.getValue() instanceof String ? "'" : "";
            query += " AND " + entry.getKey() + " = " + stringApostrophe + entry.getValue() + stringApostrophe;
        }

        Statement statement = connection.createStatement();

        try (ResultSet set = statement.executeQuery(query)) {
            while (set.next()) {
                list.add(getPatient(set.getInt("PatientID")));
            }
        }

        return list;
    }

    public Patient registerPatient(Patient patient) throws SQLException {
        if (patient == null)
            throw new IllegalArgumentException("Error: Patient cannot be null.");

        Patient registeredPatient = null;

        String query = """
            INSERT INTO Patient (
                FirstName,
                MiddleName,
                LastName,
                NameSuffix,
                MobileNumber,
                EmailAddress,
                BirthDate,
                PatientSex
            ) VALUES (?, ?, ?, ?, ?, ?, ?, ?);
        """;

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, patient.getFirstName());
        preparedStatement.setString(2, patient.getMiddleName());
        preparedStatement.setString(3, patient.getLastName());
        preparedStatement.setString(4, patient.getNameSuffix());
        preparedStatement.setString(5, patient.getMobileNumber());
        preparedStatement.setString(6, patient.getEmailAddress());
        preparedStatement.setString(7, patient.getBirthDate().toString());
        preparedStatement.setString(8, patient.getSex());
        preparedStatement.executeUpdate();

        try (ResultSet set = preparedStatement.getGeneratedKeys()) {
            while (set.next()) {
                registeredPatient = getPatient((int) set.getLong(1));
            }
        }

        return registeredPatient;
    }

    public boolean updatePatient(Patient patient) throws SQLException {
        if (patient.getId() <= 0)
            throw new IllegalArgumentException("Error: Patient ID cannot be equal to or less than zero.");

        String query = """
            INSERT INTO Patient (FirstName, MiddleName, LastName, NameSuffix, MobileNumber, EmailAddress, BirthDate, PatientSex, PatientID)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
            ON CONFLICT (PatientID)
            DO UPDATE SET
                FirstName = EXCLUDED.FirstName,
                MiddleName = EXCLUDED.MiddleName,
                LastName = EXCLUDED.LastName,
                NameSuffix = EXCLUDED.NameSuffix,
                MobileNumber = EXCLUDED.MobileNumber,
                EmailAddress = EXCLUDED.EmailAddress,
                BirthDate = EXCLUDED.BirthDate,
                PatientSex = EXCLUDED.PatientSex;
        """;

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, patient.getFirstName());
        preparedStatement.setString(2, patient.getMiddleName());
        preparedStatement.setString(3, patient.getLastName());
        preparedStatement.setString(4, patient.getNameSuffix());
        preparedStatement.setString(5, patient.getMobileNumber());
        preparedStatement.setString(6, patient.getEmailAddress());
        preparedStatement.setString(7, patient.getBirthDate().toString());
        preparedStatement.setString(8, patient.getSex());
        preparedStatement.setInt(9, patient.getId());
        preparedStatement.executeUpdate();
        return true;
    }

    public boolean deletePatient(Patient patient) throws SQLException {
        if (patient.getId() <= 0)
            throw new IllegalArgumentException("Error: Patient ID cannot be equal to or less than zero.");

        String query = """
            UPDATE Patient
            SET IsDeleted = 1
            WHERE PatientID = ?;
        """;

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, patient.getId());
        preparedStatement.executeUpdate();
        return true;
    }

    public Appointment getAppointment(int appointmentId) throws SQLException {
        if (appointmentId <= 0)
            throw new IllegalArgumentException("Error: Appointment ID cannot be equal to or less than zero.");

        Appointment appointment = null;

        String query = """
            SELECT *
            FROM Appointment
            WHERE
                AppointmentID = ?
                AND
                IsDeleted = 0;
        """;

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, appointmentId);

        try (ResultSet set = preparedStatement.executeQuery()) {
            while (set.next()) {
                appointment = new Appointment(
                    getMedicalStaff(set.getInt("MedicalStaffID")),
                    getPatient(set.getInt("PatientID")),
                    LocalDate.parse(set.getString("AppointmentDate")),
                    LocalTime.parse(set.getString("StartTime")),
                    LocalTime.parse(set.getString("EndTime"))
                );

                appointment.setId(set.getInt("AppointmentID"));
                appointment.setStatus(set.getString("AppointmentStatus"));
                appointment.setDiagnosis(set.getString("AppointmentDiagnosis"));
                appointment.setPrescription(set.getString("AppointmentPrescription"));
                appointment.setNotes(set.getString("AppointmentNotes"));
            }
        }

        return appointment;
    }

    public List<Appointment> getAllAppointments() throws SQLException {
        List<Appointment> list = new ArrayList<>();

        String query = """
            SELECT AppointmentID
            FROM Appointment
            WHERE IsDeleted = 0;
        """;

        Statement statement = connection.createStatement();

        try (ResultSet set = statement.executeQuery(query)) {
            while (set.next()) {
                list.add(getAppointment(set.getInt("AppointmentID")));
            }
        }

        return list;
    }

    public List<Appointment> filterAppointments(Map<String, Object> filters) throws SQLException {
        if (filters == null || filters.size() == 0)
            throw new IllegalArgumentException("Error: Appointments filter cannot be null or empty.");

        List<Appointment> list = new ArrayList<>();
        String query = "SELECT AppointmentID FROM Appointment WHERE IsDeleted = 0";

        for (Map.Entry<String, Object> entry : filters.entrySet()) {
            String stringApostrophe = entry.getValue() instanceof String ? "'" : "";
            query += " AND " + entry.getKey() + " = " + stringApostrophe + entry.getValue() + stringApostrophe;
        }

        Statement statement = connection.createStatement();

        try (ResultSet set = statement.executeQuery(query)) {
            while (set.next()) {
                list.add(getAppointment(set.getInt("AppointmentID")));
            }
        }

        return list;
    }

    public Appointment scheduleAppointment(Appointment appointment) throws SQLException {
        if (appointment == null)
            throw new IllegalArgumentException("Error: Appointment cannot be null.");

        Appointment registeredAppointment = null;

        String query = """
            INSERT INTO Appointment (
                MedicalStaffID,
                PatientID,
                AppointmentDate,
                StartTime,
                EndTime
            ) VALUES (?, ?, ?, ?, ?);
        """;

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, appointment.getMedicalStaff().getId());
        preparedStatement.setInt(2, appointment.getPatient().getId());
        preparedStatement.setString(3, appointment.getDate().toString());
        preparedStatement.setString(4, appointment.getStartTime().toString());
        preparedStatement.setString(5, appointment.getEndTime().toString());
        preparedStatement.executeUpdate();

        try (ResultSet set = preparedStatement.getGeneratedKeys()) {
            while (set.next()) {
                registeredAppointment = getAppointment((int) set.getLong(1));
            }
        }

        return registeredAppointment;
    }

    public boolean updateAppointment(Appointment appointment) throws SQLException {
        if (appointment.getId() <= 0)
            throw new IllegalArgumentException("Error: Appointment ID cannot be equal to or less than zero.");

        String query = """
            INSERT INTO Appointment (MedicalStaffID, PatientID, AppointmentDate, StartTime, EndTime, AppointmentID)
            VALUES (?, ?, ?, ?, ?, ?)
            ON CONFLICT (AppointmentID)
            DO UPDATE SET
                MedicalStaffID = EXCLUDED.MedicalStaffID,
                PatientID = EXCLUDED.PatientID,
                AppointmentDate = EXCLUDED.AppointmentDate,
                StartTime = EXCLUDED.StartTime,
                EndTime = EXCLUDED.EndTime,
                AppointmentID = EXCLUDED.AppointmentID;
        """;

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, appointment.getMedicalStaff().getId());
        preparedStatement.setInt(2, appointment.getPatient().getId());
        preparedStatement.setString(3, appointment.getDate().toString());
        preparedStatement.setString(4, appointment.getStartTime().toString());
        preparedStatement.setString(5, appointment.getEndTime().toString());
        preparedStatement.setInt(6, appointment.getId());
        preparedStatement.executeUpdate();
        return true;
    }

    public boolean completeAppointment(Appointment appointment) throws SQLException {
        if (appointment.getId() <= 0)
            throw new IllegalArgumentException("Error: Appointment ID cannot be equal to or less than zero.");

        String query = null;
        PreparedStatement preparedStatement = null;

        if (appointment.getMedicalStaff() instanceof Doctor && ((Doctor) appointment.getMedicalStaff()).canDiagnoseAndPrescribe()) {
            query = """
                UPDATE Appointment
                SET
                    AppointmentStatus = 'Completed',
                    AppointmentDiagnosis = ?,
                    AppointmentPrescription = ?,
                    AppointmentNotes = ?
                WHERE AppointmentID = ?;
            """;

            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, appointment.getDiagnosis());
            preparedStatement.setString(2, appointment.getPrescription());
            preparedStatement.setString(3, appointment.getNotes());
            preparedStatement.setInt(4, appointment.getId());

            if (appointment.getDiagnosis() != null && !appointment.getDiagnosis().isBlank())
                addMedicalRecord(appointment.getPatient().getId(), "Diagnosis: " + appointment.getDiagnosis());

            if (appointment.getPrescription() != null && !appointment.getPrescription().isBlank())
                addMedicalRecord(appointment.getPatient().getId(), "Prescription: " + appointment.getPrescription());
        } else {
            query = """
                UPDATE Appointment
                SET
                    AppointmentStatus = 'Completed',
                    AppointmentNotes = ?
                WHERE AppointmentID = ?;
            """;

            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, appointment.getNotes());
            preparedStatement.setInt(2, appointment.getId());
        }

        preparedStatement.executeUpdate();
        return true;
    }

    public boolean cancelAppointment(Appointment appointment) throws SQLException {
        if (appointment.getId() <= 0)
            throw new IllegalArgumentException("Error: Appointment ID cannot be equal to or less than zero.");

        String query = """
            UPDATE Appointment
            SET AppointmentStatus = 'Cancelled'
            WHERE AppointmentID = ?;
        """;

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, appointment.getId());
        preparedStatement.executeUpdate();
        return true;
    }

    public boolean deleteAppointment(Appointment appointment) throws SQLException {
        if (appointment.getId() <= 0)
            throw new IllegalArgumentException("Error: Appointment ID cannot be equal to or less than zero.");

        String query = """
            UPDATE Appointment
            SET IsDeleted = 1
            WHERE AppointmentID = ?;
        """;

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, appointment.getId());
        preparedStatement.executeUpdate();
        return true;
    }

    public boolean addMedicalRecord(
        int patientId,
        String medicalRecord
    ) throws SQLException {
        if (patientId <= 0)
            throw new IllegalArgumentException("Error: Patient ID cannot be equal to or less than zero.");

        if (medicalRecord == null || medicalRecord.isBlank())
            throw new IllegalArgumentException("Error: Medical record cannot be null or blank.");

        String query = """
            INSERT INTO MedicalRecord (
                PatientID,
                MedicalRecord,
                DateRecorded
            ) VALUES (?, ?, ?);
        """;

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, patientId);
        preparedStatement.setString(2, medicalRecord);
        preparedStatement.setString(3, LocalDate.now().toString());
        preparedStatement.executeUpdate();
        return true;
    }

    public boolean removeMedicalRecord(
        int patientId,
        String medicalRecord
    ) throws SQLException {
        if (patientId <= 0)
            throw new IllegalArgumentException("Error: Patient ID cannot be equal to or less than zero.");

        if (medicalRecord == null || medicalRecord.isBlank())
            throw new IllegalArgumentException("Error: Medical record cannot be null or blank.");

        String query = """
            UPDATE MedicalRecord
            SET IsDeleted = 1
            WHERE
                PatientID = ?
                AND
                MedicalRecord = ?;
        """;

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, patientId);
        preparedStatement.setString(2, medicalRecord);
        preparedStatement.executeUpdate();
        return true;
    }
}