package com.github.jkarw7n.barahspas;

import com.formdev.flatlaf.FlatClientProperties;
import java.awt.Component;
import java.awt.GridLayout;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

class Dialog {
    private boolean forSearching;
    private JPanel dialogPanel;
    private List<Integer> nonNullableIndices;

    public Dialog(boolean forSearching, String[] dialogComponents) {
        this.forSearching = forSearching;

        dialogPanel = new JPanel(new GridLayout(0, 2, 5, 5));
        nonNullableIndices = new ArrayList<>();

        // Log-in dialog labels
        JLabel usernameLabel = requiredLabel("Username"),
            passwordLabel = requiredLabel("Password");

        // General dialog labels
        JLabel firstNameLabel = requiredLabel("First Name"),
            middleNameLabel = new JLabel("Middle Name"),
            lastNameLabel = requiredLabel("Last Name"),
            nameSuffixLabel = new JLabel("Name Suffix"),
            mobileNumberLabel = requiredLabel("Mobile Number"),
            emailAddressLabel = new JLabel("Email Address");

        // Patient dialog labels
        JLabel patientIdLabel = new JLabel("Patient ID"),
            birthDateLabel = requiredLabel("Birth Date (Month, Day, Year)"),
            patientSexLabel = requiredLabel("Patient Sex");

        // Appointment dialog labels
        JLabel appointmentIdLabel = new JLabel("Appointment ID"),
            appointmentMedicalStaffIdLabel = requiredLabel("Medical Staff ID"),
            appointmentPatientIdLabel = requiredLabel("Patient ID"),
            appointmentDateLabel = requiredLabel("Appointment Date"),
            startTimeLabel = requiredLabel("Start Time"),
            endTimeLabel = requiredLabel("End Time"),
            appointmentDiagnosisLabel = requiredLabel("Appointment Diagnosis"),
            appointmentPrescriptionLabel = requiredLabel("Appointment Prescription"),
            appointmentNotesLabel = new JLabel("Appointment Notes");

        // Medical Staff dialog labels
        JLabel medicalStaffIdLabel = new JLabel("Medical Staff ID"),
            licenseNumberLabel = new JLabel("License Number"),
            employmentTypeLabel = requiredLabel("Employment Type"),
            shiftScheduleLabel = new JLabel("Shift Schedule"),
            medicalStaffTypeLabel = requiredLabel("Medical Staff Type"),
            doctorSpecializationLabel = new JLabel("Doctor Specialization"),
            assignedProgramLabel = new JLabel("Assigned Program");

        // Grid layouts
        GridLayout gridLayout = new GridLayout(1, 3, 5, 0);

        // Panels
        JPanel birthDatePanel = new JPanel(gridLayout),
            appointmentDatePanel = new JPanel(gridLayout),
            startTimePanel = new JPanel(gridLayout),
            endTimePanel = new JPanel(gridLayout);

        // Text fields
        JTextField usernameField = new JTextField(),
            firstNameField = new JTextField(),
            middleNameField = new JTextField(),
            lastNameField = new JTextField(),
            mobileNumberField = new JTextField(),
            emailAddressField = new JTextField(),
            shiftScheduleField = new JTextField(),
            doctorSpecializationField = new JTextField(),
            appointmentDiagnosisField = new JTextField(),
            appointmentPrescriptionField = new JTextField(),
            appointmentNotesField = new JTextField();
        
        // Log-in password field
        JPasswordField passwordField = new JPasswordField();

        // Combo boxes
        JComboBox<String> nameSuffixBox = new JComboBox<>(new String[] {"None", "Jr.", "Sr.", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X"}),
            patientSexBox = new JComboBox<>(forSearching ? new String[] {"None", "Male", "Female"} : new String[] {"Male", "Female"}),
            startTimeMeridianBox = new JComboBox<>(new String[] {"AM", "PM"}),
            endTimeMeridianBox = new JComboBox<>(new String[] {"AM", "PM"}),
            employmentTypeBox = new JComboBox<>(forSearching ? new String[] {"None", "Plantilla", "Doctors to the Barrio", "Contract of Service", "Volunteer"} : new String[] {"Plantilla", "Doctors to the Barrio", "Contract of Service", "Volunteer"}),
            medicalStaffTypeBox = new JComboBox<>(forSearching ? new String[] {"None", "Doctor", "Nurse"} : new String[] {"Doctor", "Nurse"}),
            assignedProgramBox = new JComboBox<>(forSearching ? new String[] {"None", "Maternal and Child Health", "Immunization and Vaccines", "Family Planning", "Disease Control and Screening", "General Consultation and Vitals", "Environmental and Sanitary Health", "Mental Health and Substance Support"} : new String[] {"Maternal and Child Health", "Immunization and Vaccines", "Family Planning", "Disease Control and Screening", "General Consultation and Vitals", "Environmental and Sanitary Health", "Mental Health and Substance Support"});

        // Spinners
        LocalDate dateToday = LocalDate.now();
        JSpinner patientIdSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 10000, 1)),
            birthDateMonthSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 12, 1)),
            birthDateDaySpinner = new JSpinner(new SpinnerNumberModel(1, 1, 31, 1)),
            birthDateYearSpinner = new JSpinner(new SpinnerNumberModel(1900, 1900, dateToday.getYear(), 1)),
            appointmentIdSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 10000, 1)),
            appointmentMedicalStaffIdSpinner = new JSpinner(forSearching ? new SpinnerNumberModel(0, 0, 10000, 1) : new SpinnerNumberModel(1, 1, 10000, 1)),
            appointmentPatientIdSpinner = new JSpinner(forSearching ? new SpinnerNumberModel(0, 0, 10000, 1) : new SpinnerNumberModel(1, 1, 10000, 1)),
            appointmentDateMonthSpinner = new JSpinner(forSearching ? new SpinnerNumberModel(1, 1, 12, 1) : new SpinnerNumberModel(dateToday.getMonthValue(), 1, 12, 1)),
            appointmentDateDaySpinner = new JSpinner(forSearching ? new SpinnerNumberModel(1, 1, 31, 1) : new SpinnerNumberModel(dateToday.getDayOfMonth(), 1, 31, 1)),
            appointmentDateYearSpinner = new JSpinner(new SpinnerNumberModel(dateToday.getYear(), dateToday.getYear(), 3000, 1)),
            startTimeHourSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 12, 1)),
            startTimeMinuteSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 59, 1)),
            endTimeHourSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 12, 1)),
            endTimeMinuteSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 59, 1)),
            medicalStaffIdSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 10000, 1)),
            licenseNumberSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 9999999, 1));

        // Remove comma from spinner values exceeding 1000
        for (JSpinner spinner : new JSpinner[] {patientIdSpinner, birthDateYearSpinner, appointmentIdSpinner, appointmentMedicalStaffIdSpinner, appointmentPatientIdSpinner, appointmentDateYearSpinner}) {
            spinner.setEditor(new JSpinner.NumberEditor(spinner, "#"));
        }

        // Force minute spinner values to always be two-digit
        startTimeMinuteSpinner.setEditor(new JSpinner.NumberEditor(startTimeMinuteSpinner, "00"));
        endTimeMinuteSpinner.setEditor(new JSpinner.NumberEditor(endTimeMinuteSpinner, "00"));

        // Force license number spinner values to always be seven-digit
        licenseNumberSpinner.putClientProperty("isLicenseNumber", "");
        licenseNumberSpinner.setEditor(new JSpinner.NumberEditor(licenseNumberSpinner, "0000000"));

        // Add components to panels
        birthDatePanel.add(birthDateMonthSpinner);
        birthDatePanel.add(birthDateDaySpinner);
        birthDatePanel.add(birthDateYearSpinner);

        appointmentDatePanel.add(appointmentDateMonthSpinner);
        appointmentDatePanel.add(appointmentDateDaySpinner);
        appointmentDatePanel.add(appointmentDateYearSpinner);

        startTimePanel.add(startTimeHourSpinner);
        startTimePanel.add(startTimeMinuteSpinner);
        startTimePanel.add(startTimeMeridianBox);

        endTimePanel.add(endTimeHourSpinner);
        endTimePanel.add(endTimeMinuteSpinner);
        endTimePanel.add(endTimeMeridianBox);

        // Add placeholders to text fields
        usernameField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Enter username here");
        passwordField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Enter password here");
        firstNameField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Enter first name here");
        middleNameField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Enter middle name here");
        lastNameField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Enter last name here");
        mobileNumberField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Enter mobile number here");
        emailAddressField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Enter email address here");
        shiftScheduleField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Enter shift schedule here");
        doctorSpecializationField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Enter doctor specialization here");
        appointmentDiagnosisField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Enter appointment diagnosis here");
        appointmentPrescriptionField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Enter appointment prescription here");
        appointmentNotesField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Enter appointment notes here");

        // Make combo boxes not editable
        nameSuffixBox.setEditable(false);
        patientSexBox.setEditable(false);
        startTimeMeridianBox.setEditable(false);
        endTimeMeridianBox.setEditable(false);
        employmentTypeBox.setEditable(false);
        medicalStaffTypeBox.setEditable(false);
        assignedProgramBox.setEditable(false);

        // Disable editing the doctor specialization field if the Dialog class' medicalStaffType field is not "Doctor", etc., as well as change their label's text and tooltip text
        if (medicalStaffTypeBox.getItemAt(0).equals("None")) {
            doctorSpecializationField.setEnabled(false);
            assignedProgramBox.setEnabled(false);

            doctorSpecializationLabel.setText("Doctor Specialization");
            doctorSpecializationLabel.setToolTipText(null);
            assignedProgramLabel.setText("Assigned Program");
            assignedProgramLabel.setToolTipText(null);
        } else {
            doctorSpecializationField.setEnabled(true);
            assignedProgramBox.setEnabled(false);

            if (!forSearching) {
                doctorSpecializationLabel.setText("Doctor Specialization (*)");
                doctorSpecializationLabel.setToolTipText("Required field");
            }

            assignedProgramLabel.setText("Assigned Program");
            assignedProgramLabel.setToolTipText(null);
        }

        medicalStaffTypeBox.addActionListener(e -> {
            switch ((String) medicalStaffTypeBox.getSelectedItem()) {
                case "Doctor":
                    doctorSpecializationField.setEnabled(true);
                    assignedProgramBox.setEnabled(false);

                    if (!forSearching) {
                        doctorSpecializationLabel.setText("Doctor Specialization (*)");
                        doctorSpecializationLabel.setToolTipText("Required field");
                    }

                    assignedProgramLabel.setText("Assigned Program");
                    assignedProgramLabel.setToolTipText(null);

                    break;
                case "Nurse":
                    doctorSpecializationField.setEnabled(false);
                    assignedProgramBox.setEnabled(true);

                    doctorSpecializationLabel.setText("Doctor Specialization");
                    doctorSpecializationLabel.setToolTipText(null);

                    if (!forSearching) {
                        assignedProgramLabel.setText("Assigned Program (*)");
                        assignedProgramLabel.setToolTipText("Required field");
                    }

                    break;
                default:
                    doctorSpecializationField.setEnabled(false);
                    assignedProgramBox.setEnabled(false);

                    doctorSpecializationLabel.setText("Doctor Specialization");
                    doctorSpecializationLabel.setToolTipText(null);
                    assignedProgramLabel.setText("Assigned Program");
                    assignedProgramLabel.setToolTipText(null);
            }
        });

        if (forSearching) {
            for (JLabel label : new JLabel[] {patientIdLabel, birthDateLabel, appointmentIdLabel, appointmentDateLabel, appointmentMedicalStaffIdLabel, appointmentPatientIdLabel, startTimeLabel, endTimeLabel, medicalStaffIdLabel, licenseNumberLabel}) {
                label.setToolTipText("Leave as is for a null value");
            }
        }

        // Add the components in the argument to dialogPanel
        for (int i = 0; i < dialogComponents.length; i++) {
            int nonNullableIndex = (i * 2) + 1;

            switch (dialogComponents[i]) {
                case "Username":
                    dialogPanel.add(usernameLabel);
                    dialogPanel.add(usernameField);
                    nonNullableIndices.add(nonNullableIndex);
                    break;
                case "Password":
                    dialogPanel.add(passwordLabel);
                    dialogPanel.add(passwordField);
                    nonNullableIndices.add(nonNullableIndex);
                    break;
                case "First Name":
                    dialogPanel.add(firstNameLabel);
                    dialogPanel.add(firstNameField);

                    if (!forSearching)
                        nonNullableIndices.add(nonNullableIndex);

                    break;
                case "Middle Name":
                    dialogPanel.add(middleNameLabel);
                    dialogPanel.add(middleNameField);
                    break;
                case "Last Name":
                    dialogPanel.add(lastNameLabel);
                    dialogPanel.add(lastNameField);

                    if (!forSearching)
                        nonNullableIndices.add(nonNullableIndex);

                    break;
                case "Name Suffix":
                    dialogPanel.add(nameSuffixLabel);
                    dialogPanel.add(nameSuffixBox);
                    break;
                case "Mobile Number":
                    dialogPanel.add(mobileNumberLabel);
                    dialogPanel.add(mobileNumberField);

                    if (!forSearching)
                        nonNullableIndices.add(nonNullableIndex);

                    break;
                case "Email Address":
                    dialogPanel.add(emailAddressLabel);
                    dialogPanel.add(emailAddressField);
                    break;
                case "Patient ID":
                    dialogPanel.add(patientIdLabel);
                    dialogPanel.add(patientIdSpinner);
                    break;
                case "Birth Date":
                    dialogPanel.add(birthDateLabel);
                    dialogPanel.add(birthDatePanel);

                    if (!forSearching)
                        nonNullableIndices.add(nonNullableIndex);

                    break;
                case "Patient Sex":
                    dialogPanel.add(patientSexLabel);
                    dialogPanel.add(patientSexBox);

                    if (!forSearching)
                        nonNullableIndices.add(nonNullableIndex);

                    break;
                case "Appointment ID":
                    dialogPanel.add(appointmentIdLabel);
                    dialogPanel.add(appointmentIdSpinner);
                    break;
                case "Medical Staff ID*":
                    dialogPanel.add(appointmentMedicalStaffIdLabel);
                    dialogPanel.add(appointmentMedicalStaffIdSpinner);

                    if (!forSearching)
                        nonNullableIndices.add(nonNullableIndex);

                    break;
                case "Patient ID*":
                    dialogPanel.add(appointmentPatientIdLabel);
                    dialogPanel.add(appointmentPatientIdSpinner);

                    if (!forSearching)
                        nonNullableIndices.add(nonNullableIndex);

                    break;
                case "Appointment Date":
                    dialogPanel.add(appointmentDateLabel);
                    dialogPanel.add(appointmentDatePanel);

                    if (!forSearching)
                        nonNullableIndices.add(nonNullableIndex);

                    break;
                case "Start Time":
                    dialogPanel.add(startTimeLabel);
                    dialogPanel.add(startTimePanel);

                    if (!forSearching)
                        nonNullableIndices.add(nonNullableIndex);

                    break;
                case "End Time":
                    dialogPanel.add(endTimeLabel);
                    dialogPanel.add(endTimePanel);

                    if (!forSearching)
                        nonNullableIndices.add(nonNullableIndex);

                    break;
                case "Appointment Diagnosis":
                    dialogPanel.add(appointmentDiagnosisLabel);
                    dialogPanel.add(appointmentDiagnosisField);
                    nonNullableIndices.add(nonNullableIndex);
                    break;
                case "Appointment Prescription":
                    dialogPanel.add(appointmentPrescriptionLabel);
                    dialogPanel.add(appointmentPrescriptionField);
                    nonNullableIndices.add(nonNullableIndex);
                    break;
                case "Appointment Notes":
                    dialogPanel.add(appointmentNotesLabel);
                    dialogPanel.add(appointmentNotesField);
                    break;
                case "Medical Staff ID":
                    dialogPanel.add(medicalStaffIdLabel);
                    dialogPanel.add(medicalStaffIdSpinner);
                    break;
                case "License Number":
                    dialogPanel.add(licenseNumberLabel);
                    dialogPanel.add(licenseNumberSpinner);
                    break;
                case "Employment Type":
                    dialogPanel.add(employmentTypeLabel);
                    dialogPanel.add(employmentTypeBox);

                    if (!forSearching)
                        nonNullableIndices.add(nonNullableIndex);

                    break;
                case "Shift Schedule":
                    dialogPanel.add(shiftScheduleLabel);
                    dialogPanel.add(shiftScheduleField);
                    break;
                case "Medical Staff Type":
                    dialogPanel.add(medicalStaffTypeLabel);
                    dialogPanel.add(medicalStaffTypeBox);

                    if (!forSearching)
                        nonNullableIndices.add(nonNullableIndex);

                    break;
                case "Doctor Specialization":
                    dialogPanel.add(doctorSpecializationLabel);
                    dialogPanel.add(doctorSpecializationField);
                    break;
                case "Assigned Program":
                    dialogPanel.add(assignedProgramLabel);
                    dialogPanel.add(assignedProgramBox);
            }

            if (!dialogPanel.isAncestorOf(assignedProgramLabel)) {
                doctorSpecializationLabel.setText("Doctor Specialization (*)");
                doctorSpecializationLabel.setToolTipText("Required field");
                doctorSpecializationField.setEnabled(true);
            } else if (!dialogPanel.isAncestorOf(doctorSpecializationLabel)) {
                assignedProgramLabel.setText("Assigned Program (*)");
                assignedProgramLabel.setToolTipText("Required field");
                assignedProgramBox.setEnabled(true);
            }
        }
    }

    public List<Object> showDialog(String dialogTitle) {
        int result = JOptionPane.showConfirmDialog(null, dialogPanel, dialogTitle, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION)
            return inputValues();

        return null;
    }

    // opens a dialog with pre-existing values already filled in
    public List<Object> showDialog(String dialogTitle, Object[] fillValues) {
        for (int i = 1; i < dialogPanel.getComponentCount(); i += 2) {
            Component component = dialogPanel.getComponent(i);
            Object fillValue = fillValues[(int) (i - 1) / 2];

            if (component instanceof JTextField textField)
                textField.setText((String) fillValue);
            else if (component instanceof JComboBox comboBox)
                comboBox.setSelectedItem(fillValue != null ? fillValue : comboBox.getSelectedItem());
            else if (component instanceof JSpinner spinner) {
                spinner.setValue(Integer.parseInt((String) fillValue));
            } else if (component instanceof JPanel panel) {
                if (panel.getComponent(2) instanceof JSpinner) {
                    LocalDate date = (LocalDate) fillValue;

                    ((JSpinner) panel.getComponent(0)).setValue(date.getMonthValue());
                    ((JSpinner) panel.getComponent(1)).setValue(date.getDayOfMonth());
                    ((JSpinner) panel.getComponent(2)).setValue(date.getYear());
                } else if (panel.getComponent(2) instanceof JComboBox) {
                    LocalTime time = (LocalTime) fillValue;
                    int hourValue = time.getHour();

                    if (hourValue > 11) {
                        if (hourValue > 12)
                            hourValue -= 12;

                        if (panel.getComponent(2) instanceof JComboBox comboBox)
                            comboBox.setSelectedItem("PM");
                    } else {
                        if (hourValue == 0)
                            hourValue = 12;

                        if (panel.getComponent(2) instanceof JComboBox comboBox)
                            comboBox.setSelectedItem("AM");
                    }

                    ((JSpinner) panel.getComponent(0)).setValue(hourValue);
                    ((JSpinner) panel.getComponent(1)).setValue(time.getMinute());
                }
            }
        }

        return showDialog(dialogTitle);
    }

    public static int showIdDialog(String dialogTitle, String labelText) {
        JPanel panel = new JPanel(new GridLayout(1, 2, 5, 0));
        JLabel label = new JLabel(labelText + " (*)");
        JSpinner spinner = new JSpinner(new SpinnerNumberModel(1, 1, 10000, 1));

        label.setToolTipText("Required field");

        panel.add(label);
        panel.add(spinner);

        int result = JOptionPane.showConfirmDialog(null, panel, dialogTitle, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION)
            return (int) spinner.getValue();
        else
            showErrorDialog("ID cannot be null or blank.");

        return 0;
    }

    public static void showErrorDialog(String errorMessage) {
        JOptionPane.showMessageDialog(null, errorMessage, "An error occurred", JOptionPane.ERROR_MESSAGE);
    }

    private List<Object> inputValues() {
        List<Object> inputValues = new ArrayList<>();

        // checks all of dialogPanel's odd-indexed components for input values
        for (int i = 1; i < dialogPanel.getComponentCount(); i += 2) {
            Component component = dialogPanel.getComponent(i);

            if (component instanceof JPasswordField passwordField) {
                if (passwordField.getPassword() == null || passwordField.getPassword().length == 0)
                    showErrorDialog("Required password field's value cannot be null or blank.");
                else
                    inputValues.add(String.valueOf(passwordField.getPassword()));
            } else if (component instanceof JTextField textField) {
                if (textField.getText() == null || textField.getText().isBlank()) {
                    if (nonNullableIndices.contains(i) && textField.isEnabled() && !forSearching)
                        showErrorDialog("Required text field's value cannot be null or blank.");
                    else
                        inputValues.add(null);
                } else
                    inputValues.add(textField.getText().strip());
            } else if (component instanceof JComboBox comboBox) {
                if (comboBox.getSelectedItem().equals("None")) {
                    if (nonNullableIndices.contains(i) && comboBox.isEnabled() && !forSearching) {
                        showErrorDialog("Required combo box's selected value cannot be \"None\".");
                    } else
                        inputValues.add(null);
                } else
                    inputValues.add(comboBox.getSelectedItem());
            } else if (component instanceof JSpinner spinner) {
                if (spinner.getClientProperty("isLicenseNumber") != null && (int) spinner.getValue() != 0)
                    inputValues.add(String.format("%07d", (int) spinner.getValue()));
                else if ((int) spinner.getValue() == 0)
                    if (nonNullableIndices.contains(i) && !forSearching)
                        showErrorDialog("Required spinner field's value cannot be 0.");
                    else
                        inputValues.add(null);
                else
                    inputValues.add((int) spinner.getValue());
            } else if (component instanceof JPanel panel) {
                // checks if the panel is for inputting dates or timestamps
                if (panel.getComponent(2) instanceof JSpinner yearSpinner) {
                    int monthValue = (int) ((JSpinner) panel.getComponent(0)).getValue(),
                        dayValue = (int) ((JSpinner) panel.getComponent(1)).getValue(),
                        yearValue = (int) yearSpinner.getValue();
                        
                    if (monthValue == 1 && dayValue == 1 && (yearValue == 1900 || yearValue == LocalDate.now().getYear()) && forSearching)
                        inputValues.add(null);
                    else
                        inputValues.add(LocalDate.of(yearValue, monthValue, dayValue));
                } else if (panel.getComponent(2) instanceof JComboBox comboBox) {
                    int hourValue = (int) ((JSpinner) panel.getComponent(0)).getValue(),
                        minuteValue = (int) ((JSpinner) panel.getComponent(1)).getValue();
                    String meridianValue = (String) comboBox.getSelectedItem();

                    if (meridianValue.equals("AM")) {
                        if (hourValue == 12)
                            hourValue = 0;
                    } else if (hourValue != 12)
                        hourValue += 12;

                    if (hourValue == 1 && minuteValue == 0 && meridianValue.equals("AM") && forSearching)
                        inputValues.add(null);
                    else
                        inputValues.add(LocalTime.of(hourValue, minuteValue));
                } else if (component instanceof JTextArea textArea)
                    inputValues.add(textArea.getText().strip());
            }
        }

        for (Object value : inputValues) {
            if (value != null)
                return inputValues;
        }

        return null;
    }

    private JLabel requiredLabel(String labelText) {
        JLabel label = new JLabel(forSearching ? labelText : labelText + " (*)");
        
        if (!forSearching)
            label.setToolTipText("Required field");

        return label;
    } 
}
