package com.github.jkarw7n.barahspas;

import com.formdev.flatlaf.*;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

class GUI implements ActionListener {
    private JFrame frame;
    private JPanel mainPanel;
    private DefaultTableModel patientsTableModel, appointmentsTableModel, medicalStaffTableModel;
    private static DateTimeFormatter dateFormatter, timeFormatter;

    public GUI() throws SQLException, UnsupportedLookAndFeelException {
        UIManager.setLookAndFeel(new FlatDarkLaf());

        frame = new JFrame("BaraHSPAS");
        
        patientsTableModel = new DefaultTableModel(new Object[][] {}, new String[] {"Patient ID", "First Name", "Middle Name", "Last Name", "Name Suffix", "Mobile Number", "Email Address", "Birth Date", "Patient Age", "Patient Sex", "Medical Records"});
        appointmentsTableModel = new DefaultTableModel(new Object[][] {}, new String[] {"Appointment ID", "Medical Staff ID", "Patient ID", "Appointment Date", "Start Time", "End Time", "Appointment Status", "Appointment Diagnosis", "Appointment Prescription", "Appointment Notes"});
        medicalStaffTableModel = new DefaultTableModel(new Object[][] {}, new String[] {"Medical Staff ID", "First Name", "Middle Name", "Last Name", "Name Suffix", "Mobile Number", "Email Address", "License Number", "Employment Type", "Shift Schedule", "Medical Staff Type", "Doctor Specialization", "Assigned Programs"});

        dateFormatter = DateTimeFormatter.ofPattern("MMMM d, yyyy");
        timeFormatter = DateTimeFormatter.ofPattern("h:mm a");

        addPanels();
        showFrame();
    }

    // Creates the menu bar before initializing the main panel and adding it to the frame
    private void addPanels() throws SQLException {
        JMenuBar menuBar = new JMenuBar();
        JMenu aboutMenu = new JMenu("About"),
            patientsMenu = new JMenu("Patients"),
            appointmentsMenu = new JMenu("Appointments"),
            medicalStaffMenu = new JMenu("Medical Staff");
        JMenuItem searchPatientsItem = new JMenuItem("Search Patients"),
            registerPatientItem = new JMenuItem("Register Patient"),
            updatePatientItem = new JMenuItem("Update Patient"),
            deletePatientItem = new JMenuItem("Delete Patient"),
            searchAppointmentsItem = new JMenuItem("Search Appointments"),
            scheduleAppointmentItem = new JMenuItem("Schedule Appointment"),
            completeAppointmentItem = new JMenuItem("Complete Appointment"),
            updateAppointmentItem = new JMenuItem("Update Appointment"),
            cancelAppointmentItem = new JMenuItem("Cancel Appointment"),
            deleteAppointmentItem = new JMenuItem("Delete Appointment"),
            searchMedicalStaffItem = new JMenuItem("Search Medical Staff"),
            registerMedicalStaffItem = new JMenuItem("Register Medical Staff"),
            updateMedicalStaffItem = new JMenuItem("Update Medical Staff"),
            deleteMedicalStaffItem = new JMenuItem("Delete Medical Staff");

        searchPatientsItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_COMMA, InputEvent.CTRL_DOWN_MASK));
        registerPatientItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, InputEvent.CTRL_DOWN_MASK));

        for (JMenuItem menuItem : new JMenuItem[] {searchPatientsItem, registerPatientItem, updatePatientItem, deletePatientItem}) {
            patientsMenu.add(menuItem);
            menuItem.addActionListener(this);
        }

        searchAppointmentsItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_PERIOD, InputEvent.CTRL_DOWN_MASK));
        scheduleAppointmentItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_DOWN_MASK));
        completeAppointmentItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, InputEvent.CTRL_DOWN_MASK));

        for (JMenuItem menuItem : new JMenuItem[] {searchAppointmentsItem, scheduleAppointmentItem, completeAppointmentItem, updateAppointmentItem, cancelAppointmentItem, deleteAppointmentItem}) {
            appointmentsMenu.add(menuItem);
            menuItem.addActionListener(this);
        }

        searchMedicalStaffItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_SLASH, InputEvent.CTRL_DOWN_MASK));
        registerMedicalStaffItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M, InputEvent.CTRL_DOWN_MASK));

        for (JMenuItem menuItem : new JMenuItem[] {searchMedicalStaffItem, registerMedicalStaffItem, updateMedicalStaffItem, deleteMedicalStaffItem}) {
            medicalStaffMenu.add(menuItem);
            menuItem.addActionListener(this);
        }

        aboutMenu.setMnemonic('b');
        patientsMenu.setMnemonic('p');
        appointmentsMenu.setMnemonic('a');
        medicalStaffMenu.setMnemonic('m');

        for (JMenu menu : new JMenu[] {aboutMenu, patientsMenu, appointmentsMenu, medicalStaffMenu}) {
            menu.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    CardLayout cardLayout = (CardLayout) mainPanel.getLayout();
                    cardLayout.show(mainPanel, ((JMenu) e.getSource()).getText());
                }
            });

            menuBar.add(menu);
        }

        frame.setJMenuBar(menuBar);

        mainPanel = new JPanel(new CardLayout());

        addAboutPanel();
        addPatientsPanel();
        addAppointmentsPanel();
        addMedicalStaffPanel();

        frame.add(mainPanel);
    }

    // Creates the about panel and adds it to the main panel
    private void addAboutPanel() {
        JPanel aboutPanel = new JPanel(new BorderLayout()),
            boxPanel = new JPanel();

        aboutPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        boxPanel.setLayout(new BoxLayout(boxPanel, BoxLayout.Y_AXIS));

        JLabel logoLabel = new JLabel(new FlatSVGIcon(GUI.class.getResource("/img/logo.svg")).derive(100, 100)),
            barahspasLabel = new JLabel("BaraHSPAS"),
            versionLabel = new JLabel("Version 1.0"),
            descriptionLabel = new JLabel("A barangay health station patient and appointment system");
        
        for (JLabel label : new JLabel[] {logoLabel, barahspasLabel, versionLabel, descriptionLabel}) {
            label.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        }

        barahspasLabel.putClientProperty(FlatClientProperties.STYLE_CLASS, "h1");

        versionLabel.putClientProperty(FlatClientProperties.STYLE_CLASS, "h2");
        versionLabel.setForeground(Color.LIGHT_GRAY);

        boxPanel.add(Box.createVerticalGlue());
        boxPanel.add(logoLabel);
        boxPanel.add(Box.createVerticalStrut(25));
        boxPanel.add(barahspasLabel);
        boxPanel.add(versionLabel);
        boxPanel.add(Box.createVerticalStrut(10));
        boxPanel.add(descriptionLabel);
        boxPanel.add(Box.createVerticalGlue());

        aboutPanel.add(boxPanel, BorderLayout.CENTER);

        mainPanel.add(aboutPanel, "About");
    }

    // Creates the patients panel and adds it to the main panel
    private void addPatientsPanel() throws SQLException {
        JPanel patientsPanel = new JPanel(new BorderLayout());
        JToolBar toolbar = new JToolBar();

        toolbar.setMargin(new Insets(3, 3, 3, 3));

        JButton searchButton = new JButton(svgIconOf("search.svg")),
            registerButton = new JButton(svgIconOf("add.svg")),
            updateButton = new JButton(svgIconOf("update.svg")),
            deleteButton = new JButton(svgIconOf("delete.svg"));
        
        searchButton.setToolTipText("Search Patients");
        registerButton.setToolTipText("Register Patient");
        updateButton.setToolTipText("Update Patient");
        deleteButton.setToolTipText("Delete Patient");

        JPanel patientIdPanel = new JPanel(),
            firstNamePanel = new JPanel(),
            lastNamePanel = new JPanel(),
            mobileNumberPanel = new JPanel(),
            quickSearchPanel = new JPanel();
        
        for (JPanel panel : new JPanel[] {patientIdPanel, firstNamePanel, lastNamePanel, mobileNumberPanel, quickSearchPanel}) {
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            panel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        }

        JLabel patientIdLabel = new JLabel("Patient ID");
        JSpinner patientIdSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 10000, 1));

        patientIdSpinner.setEditor(new JSpinner.NumberEditor(patientIdSpinner, "#"));
        patientIdSpinner.setMaximumSize(patientIdSpinner.getPreferredSize());

        patientIdPanel.add(patientIdLabel);
        patientIdPanel.add(patientIdSpinner);

        JLabel firstNameLabel = new JLabel("First Name");
        JTextField firstNameField = new JTextField(15);

        firstNameField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Enter first name here");
        firstNameField.setMaximumSize(firstNameField.getPreferredSize());

        firstNamePanel.add(firstNameLabel);
        firstNamePanel.add(firstNameField);

        JLabel lastNameLabel = new JLabel("Last Name");
        JTextField lastNameField = new JTextField(10);

        lastNameField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Enter last name here");
        lastNameField.setMaximumSize(lastNameField.getPreferredSize());

        lastNamePanel.add(lastNameLabel);
        lastNamePanel.add(lastNameField);

        JLabel mobileNumberLabel = new JLabel("Mobile Number");
        JTextField mobileNumberField = new JTextField(10);

        mobileNumberField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Enter number here");
        mobileNumberField.setMaximumSize(mobileNumberField.getPreferredSize());

        mobileNumberPanel.add(mobileNumberLabel);
        mobileNumberPanel.add(mobileNumberField);

        JButton quickSearchButton = new JButton("Quick Search");

        quickSearchButton.setMnemonic('q');
        quickSearchButton.addActionListener(e -> {
            Map<String, Object> map = new HashMap<>();

            if ((int) patientIdSpinner.getValue() > 0)
                map.put("PatientID", (int) patientIdSpinner.getValue());

            if (firstNameField.getText() != null && !firstNameField.getText().isBlank())
                map.put("FirstName", firstNameField.getText().strip());

            if (lastNameField.getText() != null && !lastNameField.getText().isBlank())
                map.put("LastName", lastNameField.getText().strip());

            if (mobileNumberField.getText() != null && !mobileNumberField.getText().isBlank())
                map.put("MobileNumber", mobileNumberField.getText().strip());

            try {
                if (map.size() > 0) {
                    patientsTableModel.setRowCount(0);

                    new QueryManager().filterPatients(map).forEach((patient) -> {
                        patientsTableModel.addRow(new Object[] {patient.getId(), patient.getFirstName(), patient.getMiddleName(), patient.getLastName(), patient.getNameSuffix(), patient.getMobileNumber(), patient.getEmailAddress(), patient.getBirthDate(), patient.getAge(), patient.getSex(), "Open in dialog"});
                    });
                } else
                    buildPatientsTableModel();
            } catch (SQLException sqle) {
                Dialog.showErrorDialog(sqle.getMessage());
            }
        });

        quickSearchPanel.add(quickSearchButton);

        for (JComponent component : new JComponent[] {patientIdLabel, patientIdSpinner, firstNameLabel, firstNameField, lastNameLabel, lastNameField, mobileNumberLabel, mobileNumberField}) {
            component.setAlignmentX(JComponent.LEFT_ALIGNMENT);
        }

        for (JButton button : new JButton[] {searchButton, registerButton, updateButton, deleteButton}) {
            button.addActionListener(this);
        }

        toolbar.add(searchButton);
        toolbar.add(registerButton);
        toolbar.add(updateButton);
        toolbar.add(deleteButton);
        toolbar.add(new JToolBar.Separator());
        toolbar.add(patientIdPanel);
        toolbar.add(firstNamePanel);
        toolbar.add(lastNamePanel);
        toolbar.add(mobileNumberPanel);
        toolbar.add(quickSearchPanel);

        JTable table = new JTable(patientsTableModel);

        buildPatientsTableModel();

        JTextField editor = new JTextField();

        editor.setEditable(false);

        table.setDefaultEditor(Object.class, new DefaultCellEditor(editor));
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint()),
                    column = table.columnAtPoint(e.getPoint());

                if (column == 10) {
                    try {
                        JPanel medicalRecordPanel = new JPanel();
                        List<Object[]> medicalRecords = new QueryManager().getPatient((int) table.getValueAt(row, 0)).getMedicalRecords();

                        if (medicalRecords == null)
                            medicalRecordPanel.add(new JLabel("No existing medical records were found for this patient."));
                        else {
                            medicalRecordPanel.setLayout(new GridLayout(0, 2, 5, 5));

                            medicalRecords.forEach((medicalRecord) -> {
                                medicalRecordPanel.add(new JLabel(((LocalDate) medicalRecord[0]).format(dateFormatter)));
                                medicalRecordPanel.add(new JLabel((String) medicalRecord[1]));
                            });
                        }

                        JOptionPane.showMessageDialog(frame, medicalRecordPanel, "Medical Records", JOptionPane.PLAIN_MESSAGE);
                    } catch (SQLException sqle) {
                        Dialog.showErrorDialog(sqle.getMessage());
                    }
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);

        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        patientsPanel.add(toolbar, BorderLayout.NORTH);
        patientsPanel.add(scrollPane, BorderLayout.CENTER);

        mainPanel.add(patientsPanel, "Patients");
    }

    private void addAppointmentsPanel() throws SQLException {
        JPanel appointmentsPanel = new JPanel(new BorderLayout());
        JToolBar toolbar = new JToolBar();

        toolbar.setMargin(new Insets(3, 3, 3, 3));

        JButton searchButton = new JButton(svgIconOf("search.svg")),
            scheduleButton = new JButton(svgIconOf("add.svg")),
            completeButton = new JButton(svgIconOf("complete.svg")),
            updateButton = new JButton(svgIconOf("update.svg")),
            cancelButton = new JButton(svgIconOf("cancel.svg")),
            deleteButton = new JButton(svgIconOf("delete.svg"));
        
        searchButton.setToolTipText("Search Appointments");
        scheduleButton.setToolTipText("Schedule Appointment");
        completeButton.setToolTipText("Complete Appointment");
        updateButton.setToolTipText("Update Appointment");
        cancelButton.setToolTipText("Cancel Appointment");
        deleteButton.setToolTipText("Delete Appointment");

        JPanel appointmentIdPanel = new JPanel(),
            medicalStaffIdPanel = new JPanel(),
            patientIdPanel = new JPanel(),
            appointmentDatePanel = new JPanel(),
            appointmentDateNestedPanel = new JPanel(),
            quickSearchPanel = new JPanel();

        appointmentDateNestedPanel.setLayout(new BoxLayout(appointmentDateNestedPanel, BoxLayout.X_AXIS));
        
        for (JPanel panel : new JPanel[] {appointmentIdPanel, patientIdPanel, medicalStaffIdPanel, appointmentDatePanel, quickSearchPanel}) {
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            panel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        }

        JLabel appointmentIdLabel = new JLabel("Appointment ID");
        JSpinner appointmentIdSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 10000, 1));

        appointmentIdPanel.add(appointmentIdLabel);
        appointmentIdPanel.add(appointmentIdSpinner);

        JLabel medicalStaffIdLabel = new JLabel("Medical Staff ID");
        JSpinner medicalStaffIdSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 10000, 1));

        medicalStaffIdPanel.add(medicalStaffIdLabel);
        medicalStaffIdPanel.add(medicalStaffIdSpinner);

        JLabel patientIdLabel = new JLabel("Patient ID");
        JSpinner patientIdSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 10000, 1));

        patientIdPanel.add(patientIdLabel);
        patientIdPanel.add(patientIdSpinner);

        JLabel appointmentDateLabel = new JLabel("Appointment Date (Month, Day, Year)");
        JSpinner monthSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 12, 1)),
            daySpinner = new JSpinner(new SpinnerNumberModel(1, 1, 31, 1)),
            yearSpinner = new JSpinner(new SpinnerNumberModel(1900, 1900, LocalDate.now().getYear(), 1));

        appointmentDateNestedPanel.add(monthSpinner);
        appointmentDateNestedPanel.add(Box.createHorizontalStrut(10));
        appointmentDateNestedPanel.add(daySpinner);
        appointmentDateNestedPanel.add(Box.createHorizontalStrut(10));
        appointmentDateNestedPanel.add(yearSpinner);

        appointmentDatePanel.add(appointmentDateLabel);
        appointmentDatePanel.add(appointmentDateNestedPanel);

        JButton quickSearchButton = new JButton("Quick Search");

        quickSearchButton.setMnemonic('q');
        quickSearchButton.addActionListener(e -> {
            Map<String, Object> map = new HashMap<>();

            if ((int) appointmentIdSpinner.getValue() > 0)
                map.put("AppointmentID", (int) appointmentIdSpinner.getValue());

            if ((int) medicalStaffIdSpinner.getValue() > 0)
                map.put("MedicalStaffID", (int) medicalStaffIdSpinner.getValue());

            if ((int) patientIdSpinner.getValue() > 0)
                map.put("PatientID", (int) patientIdSpinner.getValue());

            if ((int) monthSpinner.getValue() != 1 || (int) daySpinner.getValue() != 1 || (int) yearSpinner.getValue() != 1900)
                map.put("AppointmentDate", LocalDate.of((int) yearSpinner.getValue(), (int) monthSpinner.getValue(), (int) daySpinner.getValue()).toString());

            try {
                if (map.size() > 0) {
                    appointmentsTableModel.setRowCount(0);

                    new QueryManager().filterAppointments(map).forEach((appointment) -> {
                        appointmentsTableModel.addRow(new Object[] {appointment.getId(), appointment.getMedicalStaff().getId(), appointment.getPatient().getId(), appointment.getDate().format(dateFormatter), appointment.getStartTime().format(timeFormatter), appointment.getEndTime().format(timeFormatter), appointment.getStatus(), appointment.getDiagnosis(), appointment.getPrescription(), appointment.getNotes()});
                    });
                } else
                    buildAppointmentsTableModel();
            } catch (SQLException sqle) {
                Dialog.showErrorDialog(sqle.getMessage());
            }
        });

        quickSearchPanel.add(quickSearchButton);

        for (JComponent component : new JComponent[] {appointmentIdLabel, appointmentIdSpinner, medicalStaffIdLabel, medicalStaffIdSpinner, patientIdLabel, patientIdSpinner, appointmentDateLabel, appointmentDateNestedPanel}) {
            component.setAlignmentX(JComponent.LEFT_ALIGNMENT);
        }

        for (JSpinner spinner : new JSpinner[] {appointmentIdSpinner, medicalStaffIdSpinner, patientIdSpinner, monthSpinner, daySpinner, yearSpinner}) {
            spinner.setEditor(new JSpinner.NumberEditor(spinner, "#"));
            spinner.setMaximumSize(spinner.getPreferredSize());
        }

        for (JButton button : new JButton[] {searchButton, scheduleButton, completeButton, updateButton, cancelButton, deleteButton}) {
            button.addActionListener(this);
        }

        toolbar.add(searchButton);
        toolbar.add(scheduleButton);
        toolbar.add(completeButton);
        toolbar.add(updateButton);
        toolbar.add(cancelButton);
        toolbar.add(deleteButton);
        toolbar.add(new JToolBar.Separator());
        toolbar.add(appointmentIdPanel);
        toolbar.add(medicalStaffIdPanel);
        toolbar.add(patientIdPanel);
        toolbar.add(appointmentDatePanel);
        toolbar.add(quickSearchPanel);

        JTable table = new JTable(appointmentsTableModel);

        buildAppointmentsTableModel();

        JTextField editor = new JTextField();

        editor.setEditable(false);

        table.setDefaultEditor(Object.class, new DefaultCellEditor(editor));

        JScrollPane scrollPane = new JScrollPane(table);

        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        appointmentsPanel.add(toolbar, BorderLayout.NORTH);
        appointmentsPanel.add(scrollPane, BorderLayout.CENTER);

        mainPanel.add(appointmentsPanel, "Appointments");
    }

    private void addMedicalStaffPanel() throws SQLException {
        JPanel medicalStaffPanel = new JPanel(new BorderLayout());
        JToolBar toolbar = new JToolBar();
        
        toolbar.setMargin(new Insets(3, 3, 3, 3));

        JButton searchButton = new JButton(svgIconOf("search.svg")),
            registerButton = new JButton(svgIconOf("add.svg")),
            updateButton = new JButton(svgIconOf("update.svg")),
            deleteButton = new JButton(svgIconOf("delete.svg"));
        
        searchButton.setToolTipText("Search Medical Staff");
        registerButton.setToolTipText("Register Medical Staff");
        updateButton.setToolTipText("Update Medical Staff");
        deleteButton.setToolTipText("Delete Medical Staff");

        JPanel medicalStaffIdPanel = new JPanel(),
            firstNamePanel = new JPanel(),
            lastNamePanel = new JPanel(),
            medicalStaffTypePanel = new JPanel(),
            licenseNumberPanel = new JPanel(),
            employmentTypePanel = new JPanel(),
            quickSearchPanel = new JPanel();

        for (JPanel panel : new JPanel[] {medicalStaffIdPanel, firstNamePanel, lastNamePanel, medicalStaffTypePanel, licenseNumberPanel, employmentTypePanel, quickSearchPanel}) {
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            panel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        }

        JLabel medicalStaffIdLabel = new JLabel("Medical Staff ID");
        JSpinner medicalStaffIdSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 10000, 1));

        medicalStaffIdSpinner.setEditor(new JSpinner.NumberEditor(medicalStaffIdSpinner, "#"));

        medicalStaffIdPanel.add(medicalStaffIdLabel);
        medicalStaffIdPanel.add(medicalStaffIdSpinner);

        JLabel firstNameLabel = new JLabel("First Name");
        JTextField firstNameField = new JTextField(15);

        firstNameField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Enter first name here");

        firstNamePanel.add(firstNameLabel);
        firstNamePanel.add(firstNameField);

        JLabel lastNameLabel = new JLabel("Last Name");
        JTextField lastNameField = new JTextField(10);

        lastNameField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Enter last name here");

        lastNamePanel.add(lastNameLabel);
        lastNamePanel.add(lastNameField);

        JLabel medicalStaffTypeLabel = new JLabel("Medical Staff Type");
        JComboBox<String> medicalStaffTypeBox = new JComboBox<>(new String[] {"None", "Doctor", "Nurse"});

        medicalStaffTypeBox.setEditable(false);

        medicalStaffTypePanel.add(medicalStaffTypeLabel);
        medicalStaffTypePanel.add(medicalStaffTypeBox);

        JLabel licenseNumberLabel = new JLabel("License Number");
        JSpinner licenseNumberSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 9999999, 1));

        licenseNumberLabel.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Enter license number here");

        licenseNumberSpinner.setEditor(new JSpinner.NumberEditor(licenseNumberSpinner, "0000000"));

        licenseNumberPanel.add(licenseNumberLabel);
        licenseNumberPanel.add(licenseNumberSpinner);

        JLabel employmentTypeLabel = new JLabel("Employment Type");
        JComboBox<String> employmentTypeBox = new JComboBox<>(new String[] {"None", "Plantilla", "Doctors to the Barrio", "Contract of Service", "Volunteer"});

        employmentTypeBox.setEditable(false);

        employmentTypePanel.add(employmentTypeLabel);
        employmentTypePanel.add(employmentTypeBox);

        JButton quickSearchButton = new JButton("Quick Search");

        quickSearchButton.setMnemonic('q');
        quickSearchButton.addActionListener(e -> {
            Map<String, Object> map = new HashMap<>();

            if ((int) medicalStaffIdSpinner.getValue() > 0)
                map.put("MedicalStaffID", (int) medicalStaffIdSpinner.getValue());

            if (firstNameField.getText() != null && !firstNameField.getText().isBlank())
                map.put("FirstName", firstNameField.getText().strip());

            if (lastNameField.getText() != null && !lastNameField.getText().isBlank())
                map.put("LastName", lastNameField.getText().strip());

            if (!medicalStaffTypeBox.getSelectedItem().equals("None"))
                map.put("MedicalStaffType", medicalStaffTypeBox.getSelectedItem());

            if ((int) licenseNumberSpinner.getValue() != 0)
                map.put("LicenseNumber", String.format("%07d", (int) licenseNumberSpinner.getValue()));

            if (!employmentTypeBox.getSelectedItem().equals("None"))
                map.put("EmploymentType", employmentTypeBox.getSelectedItem());

            try {
                if (map.size() > 0) {
                    medicalStaffTableModel.setRowCount(0);

                    new QueryManager().filterMedicalStaff(map).forEach((medicalStaff) -> {
                        medicalStaffTableModel.addRow(new Object[] {medicalStaff.getId(), medicalStaff.getFirstName(), medicalStaff.getMiddleName(), medicalStaff.getLastName(), medicalStaff.getNameSuffix(), medicalStaff.getMobileNumber(), medicalStaff.getEmailAddress(), medicalStaff.getLicenseNumber(), medicalStaff.getEmploymentType(), medicalStaff.getShiftSchedule(), medicalStaff.getClass().getSimpleName(), medicalStaff instanceof Doctor doctor ? doctor.getSpecialization() : null, medicalStaff instanceof Nurse nurse ? nurse.getAssignedProgram() : null});
                    });
                } else
                    buildMedicalStaffTableModel();
            } catch (SQLException sqle) {
                Dialog.showErrorDialog(sqle.getMessage());
            }
        });

        quickSearchPanel.add(quickSearchButton);

        for (JComponent component : new JComponent[] {medicalStaffIdLabel, medicalStaffIdSpinner, firstNameLabel, firstNameField, lastNameLabel, lastNameField, medicalStaffTypeLabel, medicalStaffTypeBox, licenseNumberLabel, licenseNumberSpinner, employmentTypeLabel, employmentTypeBox}) {
            component.setMaximumSize(component.getPreferredSize());
            component.setAlignmentX(JComponent.LEFT_ALIGNMENT);
        }

        for (JButton button : new JButton[] {searchButton, registerButton, updateButton, deleteButton}) {
            button.addActionListener(this);
        }

        toolbar.add(searchButton);
        toolbar.add(registerButton);
        toolbar.add(updateButton);
        toolbar.add(deleteButton);
        toolbar.add(new JToolBar.Separator());
        toolbar.add(medicalStaffIdPanel);
        toolbar.add(firstNamePanel);
        toolbar.add(lastNamePanel);
        toolbar.add(medicalStaffTypePanel);
        toolbar.add(licenseNumberPanel);
        toolbar.add(employmentTypePanel);
        toolbar.add(quickSearchPanel);

        JTable table = new JTable(medicalStaffTableModel);

        buildMedicalStaffTableModel();

        JTextField editor = new JTextField();

        editor.setEditable(false);

        table.setDefaultEditor(Object.class, new DefaultCellEditor(editor));

        JScrollPane scrollPane = new JScrollPane(table);

        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        medicalStaffPanel.add(toolbar, BorderLayout.NORTH);
        medicalStaffPanel.add(scrollPane, BorderLayout.CENTER);

        mainPanel.add(medicalStaffPanel, "Medical Staff");
    }

    private void showFrame() {
        frame.pack();
        frame.setIconImage(new FlatSVGIcon(GUI.class.getResource("/img/logo.svg")).derive(75, 100).getImage());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private FlatSVGIcon svgIconOf(String name) {
        return new FlatSVGIcon(GUI.class.getResource("/img/" + name)).derive(25, 25);
    }

    private void buildPatientsTableModel() throws SQLException {
        patientsTableModel.setRowCount(0);

        for (Patient patient : new QueryManager().getAllPatients()) {
            patientsTableModel.addRow(new Object[] {patient.getId(), patient.getFirstName(), patient.getMiddleName(), patient.getLastName(), patient.getNameSuffix(), patient.getMobileNumber(), patient.getEmailAddress(), patient.getBirthDate().format(dateFormatter), patient.getAge(), patient.getSex(), "Open in dialog"});
        }
    }

    private void buildAppointmentsTableModel() throws SQLException {
        appointmentsTableModel.setRowCount(0);

        for (Appointment appointment : new QueryManager().getAllAppointments()) {
            appointmentsTableModel.addRow(new Object[] {appointment.getId(), appointment.getMedicalStaff().getId(), appointment.getPatient().getId(), appointment.getDate().format(dateFormatter), appointment.getStartTime().format(timeFormatter), appointment.getEndTime().format(timeFormatter), appointment.getStatus(), appointment.getDiagnosis(), appointment.getPrescription(), appointment.getNotes()});
        }
    }

    private void buildMedicalStaffTableModel() throws SQLException {
        medicalStaffTableModel.setRowCount(0);

        for (MedicalStaff medicalStaff : new QueryManager().getAllMedicalStaff()) {
            medicalStaffTableModel.addRow(new Object[] {medicalStaff.getId(), medicalStaff.getFirstName(), medicalStaff.getMiddleName(), medicalStaff.getLastName(), medicalStaff.getNameSuffix(), medicalStaff.getMobileNumber(), medicalStaff.getEmailAddress(), medicalStaff.getLicenseNumber(), medicalStaff.getEmploymentType(), medicalStaff.getShiftSchedule(), medicalStaff.getClass().getSimpleName(), medicalStaff instanceof Doctor doctor ? doctor.getSpecialization() : null, medicalStaff instanceof Nurse nurse ? nurse.getAssignedProgram() : null});
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String text = null;

        if (e.getSource() instanceof JMenuItem menuItem)
            text = menuItem.getText();
        else if (e.getSource() instanceof JButton button)
            text = button.getToolTipText();

        if (text.equals("Search Patients")) {
            String[] components = {"Patient ID", "First Name", "Middle Name", "Last Name", "Name Suffix", "Mobile Number", "Email Address", "Birth Date", "Patient Sex"};
            List<Object> list = new Dialog(true, components).showDialog(text);

            if (list == null) {
                try {
                    buildPatientsTableModel();
                } catch (SQLException sqle) {
                    Dialog.showErrorDialog(sqle.getMessage());
                }
            } else {
                try {
                    Map<String, Object> map = new HashMap<>();

                    for (int i = 0; i < components.length; i++) {
                        if (list.get(i) != null) {
                            if (list.get(i) instanceof LocalDate date)
                                map.put(components[i].replaceAll(" ", ""), date.toString());
                            else
                                map.put(components[i].replaceAll(" ", ""), list.get(i));
                        }
                    }

                    patientsTableModel.setRowCount(0);

                    new QueryManager().filterPatients(map).forEach((patient) -> {
                        patientsTableModel.addRow(new Object[] {patient.getId(), patient.getFirstName(), patient.getMiddleName(), patient.getLastName(), patient.getNameSuffix(), patient.getMobileNumber(), patient.getEmailAddress(), patient.getBirthDate(), patient.getAge(), patient.getSex(), "Open in dialog"});
                    });
                } catch (SQLException sqle) {
                    Dialog.showErrorDialog(sqle.getMessage());
                }
            }
        } else if (text.equals("Register Patient")) {
            String[] components = {"First Name", "Middle Name", "Last Name", "Name Suffix", "Mobile Number", "Email Address", "Birth Date", "Patient Sex"};
            List<Object> list = new Dialog(false, components).showDialog(text);

            if (list != null) {
                try {
                    new QueryManager().registerPatient(new Patient((String) list.get(0), (String) list.get(1), (String) list.get(2), (String) list.get(3), (String) list.get(4), (String) list.get(5), (LocalDate) list.get(6), (String) list.get(7)));

                    buildPatientsTableModel();
                } catch (SQLException | IllegalArgumentException sqliae) {
                    Dialog.showErrorDialog(sqliae.getMessage());
                }
            }
        } else if (text.equals("Update Patient")) {
            int patientId = Dialog.showIdDialog(text, "Patient ID");

            if (patientId > 0) {
                try {
                    new QueryManager().getPatient(patientId);

                    String[] components = {"First Name", "Middle Name", "Last Name", "Name Suffix", "Mobile Number", "Email Address", "Birth Date", "Patient Sex"};
                    Patient patient = new QueryManager().getPatient(patientId);
                    List<Object> list = new Dialog(false, components).showDialog(text, new Object[] {patient.getFirstName(), patient.getMiddleName(), patient.getLastName(), patient.getNameSuffix(), patient.getMobileNumber(), patient.getEmailAddress(), patient.getBirthDate(), patient.getSex()});

                    if (list != null) {
                        patient.setFirstName((String) list.get(0));
                        patient.setMiddleName((String) list.get(1));
                        patient.setLastName((String) list.get(2));
                        patient.setNameSuffix((String) list.get(3));
                        patient.setMobileNumber((String) list.get(4));
                        patient.setEmailAddress((String) list.get(5));
                        patient.setBirthDate((LocalDate) list.get(6));
                        patient.setSex((String) list.get(7));

                        new QueryManager().updatePatient(patient);

                        buildPatientsTableModel();
                    }
                } catch (SQLException | IllegalArgumentException | NullPointerException sqlianpe) {
                    Dialog.showErrorDialog(sqlianpe.getMessage());
                }
            }
        } else if (text.equals("Delete Patient")) {
            int patientId = Dialog.showIdDialog(text, "Patient ID");

            if (patientId > 0) {
                try {
                    Patient patient = new QueryManager().getPatient(patientId);

                    int result = JOptionPane.showConfirmDialog(frame, "Are you sure you want to delete patient " + patient.getFullName() + "?", text, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

                    if (result == JOptionPane.YES_OPTION) {
                        new QueryManager().deletePatient(patient);

                        buildPatientsTableModel();
                    }
                } catch (SQLException | IllegalArgumentException | NullPointerException sqlianpe) {
                    Dialog.showErrorDialog(sqlianpe.getMessage());
                }
            }
        } else if (text.equals("Search Appointments")) {
            String[] components = {"Appointment ID", "Medical Staff ID*", "Patient ID*", "Appointment Date", "Start Time", "End Time", "Appointment Status"};
            List<Object> list = new Dialog(true, components).showDialog(text);

            if (list == null) {
                try {
                    buildAppointmentsTableModel();
                } catch (SQLException sqle) {
                    Dialog.showErrorDialog(sqle.getMessage());
                }
            } else {
                try {
                    Map<String, Object> map = new HashMap<>();

                    for (int i = 0; i < components.length; i++) {
                        if (list.get(i) != null) {
                            if (list.get(i) instanceof LocalDate date)
                                map.put(components[i].replaceAll(" ", ""), date.toString());
                            else if (list.get(i) instanceof LocalTime time)
                                map.put(components[i].replaceAll(" ", ""), time.toString());
                            else
                                map.put(components[i].replaceAll("[\\s*]", ""), list.get(i));
                        }
                    }

                    appointmentsTableModel.setRowCount(0);

                    new QueryManager().filterAppointments(map).forEach((appointment) -> {
                        appointmentsTableModel.addRow(new Object[] {appointment.getId(), appointment.getMedicalStaff().getId(), appointment.getPatient().getId(), appointment.getDate().format(dateFormatter), appointment.getStartTime().format(timeFormatter), appointment.getEndTime().format(timeFormatter), appointment.getStatus(), appointment.getDiagnosis(), appointment.getPrescription(), appointment.getNotes()});
                    });
                } catch (SQLException sqle) {
                    Dialog.showErrorDialog(sqle.getMessage());
                }
            }
        } else if (text.equals("Schedule Appointment")) {
            String[] components = {"Medical Staff ID*", "Patient ID*", "Appointment Date", "Start Time", "End Time"};
            List<Object> list = new Dialog(false, components).showDialog(text);

            if (list != null) {
                try {
                    new QueryManager().scheduleAppointment(new Appointment(new QueryManager().getMedicalStaff((int) list.get(0)), new QueryManager().getPatient((int) list.get(1)), (LocalDate) list.get(2), (LocalTime) list.get(3), (LocalTime) list.get(4)));

                    buildAppointmentsTableModel();
                } catch (SQLException | IllegalArgumentException sqliae) {
                    Dialog.showErrorDialog(sqliae.getMessage());
                }
            }
        } else if (text.equals("Complete Appointment")) {
            int appointmentId = Dialog.showIdDialog(text, "Appointment ID");

            if (appointmentId > 0) {
                try {
                    Appointment appointment = new QueryManager().getAppointment(appointmentId);

                    if (appointment.getMedicalStaff().canDiagnoseAndPrescribe()) {
                        String[] components = {"Appointment Diagnosis", "Appointment Prescription", "Appointment Notes"};
                        List<Object> list = new Dialog(false, components).showDialog(text);

                        if (list != null) {
                            appointment.setDiagnosis((String) list.get(0));
                            appointment.setPrescription((String) list.get(1));
                            appointment.setNotes((String) list.get(2));
                        }
                    } else {
                        List<Object> list = new Dialog(false, new String[] {"Appointment Notes"}).showDialog(text);

                        if (list != null)
                            appointment.setNotes((String) list.get(0));
                    }

                    new QueryManager().completeAppointment(appointment);

                    buildAppointmentsTableModel();
                    buildMedicalStaffTableModel();
                } catch (SQLException | IllegalArgumentException | NullPointerException sqlianpe) {
                    Dialog.showErrorDialog(sqlianpe.getMessage());
                }
            }
        } else if (text.equals("Update Appointment")) {
            int appointmentId = Dialog.showIdDialog(text, "Appointment ID");

            if (appointmentId > 0) {
                try {
                    new QueryManager().getAppointment(appointmentId);

                    String[] components = {"Medical Staff ID*", "Patient ID*", "Appointment Date", "Start Time", "End Time"};
                    Appointment appointment = new QueryManager().getAppointment(appointmentId);
                    List<Object> list = new Dialog(false, components).showDialog(text, new Object[] {appointment.getMedicalStaff().getId(), appointment.getPatient().getId(), appointment.getDate(), appointment.getStartTime(), appointment.getEndTime()});

                    if (list != null) {
                        appointment.setMedicalStaff(new QueryManager().getMedicalStaff((int) list.get(0)));
                        appointment.setPatient(new QueryManager().getPatient((int) list.get(1)));
                        appointment.setDate((LocalDate) list.get(2));
                        appointment.setStartTime((LocalTime) list.get(3));
                        appointment.setEndTime((LocalTime) list.get(4));

                        new QueryManager().updateAppointment(appointment);

                        buildAppointmentsTableModel();
                    }
                } catch (SQLException | IllegalArgumentException | NullPointerException sqlianpe) {
                    Dialog.showErrorDialog(sqlianpe.getMessage());
                }
            }
        } else if (text.equals("Cancel Appointment")) {
            int appointmentId = Dialog.showIdDialog(text, "Appointment ID");

            if (appointmentId > 0) {
                try {
                    Appointment appointment = new QueryManager().getAppointment(appointmentId);

                    if (appointment != null) {
                        int result = JOptionPane.showConfirmDialog(frame, "Are you sure you want to cancel appointment " + appointmentId + "?", text, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

                        if (result == JOptionPane.YES_OPTION) {
                            new QueryManager().cancelAppointment(appointment);

                            buildAppointmentsTableModel();
                        }
                    }
                } catch (SQLException | IllegalArgumentException | NullPointerException sqlianpe) {
                    Dialog.showErrorDialog(sqlianpe.getMessage());
                }
            }
        } else if (text.equals("Delete Appointment")) {
            int appointmentId = Dialog.showIdDialog(text, "Appointment ID");

            if (appointmentId > 0) {
                try {
                    Appointment appointment = new QueryManager().getAppointment(appointmentId);

                    if (appointment != null) {
                        int result = JOptionPane.showConfirmDialog(frame, "Are you sure you want to delete appointment " + appointmentId + "?", text, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

                        if (result == JOptionPane.YES_OPTION) {
                            new QueryManager().deleteAppointment(appointment);

                            buildAppointmentsTableModel();
                        }
                    }
                } catch (SQLException | IllegalArgumentException | NullPointerException sqlianpe) {
                    Dialog.showErrorDialog(sqlianpe.getMessage());
                }
            }
        } else if (text.equals("Search Medical Staff")) {
            String[] components = {"Medical Staff ID", "First Name", "Middle Name", "Last Name", "Name Suffix", "Mobile Number", "Email Address", "License Number", "Employment Type", "Shift Schedule", "Medical Staff Type", "Doctor Specialization", "Assigned Program"};
            List<Object> list = new Dialog(true, components).showDialog(text);

            if (list == null) {
                try {
                    buildMedicalStaffTableModel();
                } catch (SQLException sqle) {
                    Dialog.showErrorDialog(sqle.getMessage());
                }
            } else {
                try {
                    Map<String, Object> map = new HashMap<>();

                    for (int i = 0; i < components.length; i++) {
                        if (list.get(i) != null)
                            map.put(components[i].replaceAll("[\\s*]", ""), list.get(i));
                    }

                    medicalStaffTableModel.setRowCount(0);

                    new QueryManager().filterMedicalStaff(map).forEach((medicalStaff) -> {
                        medicalStaffTableModel.addRow(new Object[] {medicalStaff.getId(), medicalStaff.getFirstName(), medicalStaff.getMiddleName(), medicalStaff.getLastName(), medicalStaff.getNameSuffix(), medicalStaff.getMobileNumber(), medicalStaff.getEmailAddress(), medicalStaff.getLicenseNumber(), medicalStaff.getEmploymentType(), medicalStaff.getShiftSchedule(), medicalStaff.getClass().getSimpleName(), medicalStaff instanceof Doctor doctor ? doctor.getSpecialization() : null, medicalStaff instanceof Nurse nurse ? nurse.getAssignedProgram() : null});
                    });
                } catch (SQLException sqle) {
                    Dialog.showErrorDialog(sqle.getMessage());
                }
            }
        } else if (text.equals("Register Medical Staff")) {
            String[] components = {"First Name", "Middle Name", "Last Name", "Name Suffix", "Mobile Number", "Email Address", "License Number", "Employment Type", "Shift Schedule", "Medical Staff Type", "Doctor Specialization", "Assigned Program"};
            List<Object> list = new Dialog(false, components).showDialog(text);

            if (list != null) {
                try {
                    if (((String) list.get(9)).equals("Doctor")) {
                        new QueryManager().registerMedicalStaff(new Doctor((String) list.get(0), (String) list.get(1), (String) list.get(2), (String) list.get(3), (String) list.get(4), (String) list.get(5), String.valueOf(list.get(6)), (String) list.get(7), (String) list.get(8), (String) list.get(10)));
                    } else {
                        new QueryManager().registerMedicalStaff(new Nurse((String) list.get(0), (String) list.get(1), (String) list.get(2), (String) list.get(3), (String) list.get(4), (String) list.get(5), String.valueOf(list.get(6)), (String) list.get(7), (String) list.get(8), (String) list.get(11)));
                    }

                    buildMedicalStaffTableModel();
                } catch (SQLException | IllegalArgumentException sqliae) {
                    Dialog.showErrorDialog(sqliae.getMessage());
                }
            }
        } else if (text.equals("Update Medical Staff")) {
            int medicalStaffId = Dialog.showIdDialog(text, "Medical Staff ID");

            if (medicalStaffId > 0) {
                try {
                    MedicalStaff medicalStaff = new QueryManager().getMedicalStaff(medicalStaffId);
                    String[] components = null;

                    if (medicalStaff instanceof Doctor)
                        components = new String[] {"First Name", "Middle Name", "Last Name", "Name Suffix", "Mobile Number", "Email Address", "License Number", "Employment Type", "Shift Schedule", "Doctor Specialization"};
                    else
                        components = new String[] {"First Name", "Middle Name", "Last Name", "Name Suffix", "Mobile Number", "Email Address", "License Number", "Employment Type", "Shift Schedule", "Assigned Program"};

                    List<Object> list = new Dialog(false, components).showDialog(text, new Object[] {medicalStaff.getFirstName(), medicalStaff.getMiddleName(), medicalStaff.getLastName(), medicalStaff.getNameSuffix(), medicalStaff.getMobileNumber(), medicalStaff.getEmailAddress(), medicalStaff.getLicenseNumber(), medicalStaff.getEmploymentType(), medicalStaff.getShiftSchedule(), medicalStaff instanceof Doctor doctor ? doctor.getSpecialization() : ((Nurse) medicalStaff).getAssignedProgram()});

                    if (list != null) {
                        medicalStaff.setFirstName((String) list.get(0));
                        medicalStaff.setMiddleName((String) list.get(1));
                        medicalStaff.setLastName((String) list.get(2));
                        medicalStaff.setNameSuffix((String) list.get(3));
                        medicalStaff.setMobileNumber((String) list.get(4));
                        medicalStaff.setEmailAddress((String) list.get(5));
                        medicalStaff.setLicenseNumber(String.valueOf(list.get(6)));
                        medicalStaff.setEmploymentType((String) list.get(7));
                        medicalStaff.setShiftSchedule((String) list.get(8));
                        
                        if (medicalStaff instanceof Doctor doctor)
                            doctor.setSpecialization((String) list.get(9));
                        else if (medicalStaff instanceof Nurse nurse)
                            nurse.setAssignedProgram((String) list.get(9));

                        new QueryManager().updateMedicalStaff(medicalStaff);

                        buildMedicalStaffTableModel();
                    }
                } catch (SQLException | IllegalArgumentException | NullPointerException sqlianpe) {
                    Dialog.showErrorDialog(sqlianpe.getMessage());
                }
            }
        } else if (text.equals("Delete Medical Staff")) {
            int medicalStaffId = Dialog.showIdDialog(text, "Medical Staff ID");

            if (medicalStaffId > 0) {
                try {
                    MedicalStaff medicalStaff = new QueryManager().getMedicalStaff(medicalStaffId);

                    int result = JOptionPane.showConfirmDialog(frame, "Are you sure you want to delete medical staff " + medicalStaff.getFullName() + "?", text, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

                    if (result == JOptionPane.YES_OPTION) {
                        new QueryManager().deleteMedicalStaff(medicalStaff);

                        buildMedicalStaffTableModel();
                    }
                } catch (SQLException | IllegalArgumentException | NullPointerException sqlianpe) {
                    Dialog.showErrorDialog(sqlianpe.getMessage());
                }
            }
        }
    }
}