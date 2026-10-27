package com.github.jkarw7n.barahspas;

import com.formdev.flatlaf.*;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import java.awt.*;
import java.awt.event.*;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

class GUI implements ActionListener {
    JFrame frame;
    JPanel mainPanel;
    DefaultTableModel patientsTableModel, appointmentsTableModel, medicalStaffTableModel;

    public GUI() throws SQLException, UnsupportedLookAndFeelException {
        UIManager.setLookAndFeel(new FlatDarkLaf());

        frame = new JFrame("BaraHSPAS");

        addPanels();
        showFrame();
    }

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

        frame.add(mainPanel);
    }

    private void addAboutPanel() throws SQLException {
        JPanel aboutPanel = new JPanel(new BorderLayout());

        aboutPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        JPanel boxPanel = new JPanel();

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

    private void addPatientsPanel() throws SQLException {
        JPanel patientsPanel = new JPanel(new BorderLayout());
        JToolBar toolbar = new JToolBar();
        
        toolbar.setMargin(new Insets(3, 3, 3, 3));

        JButton searchButton = new JButton(new FlatSVGIcon(GUI.class.getResource("/img/search.svg")).derive(25, 25)),
            registerButton = new JButton(new FlatSVGIcon(GUI.class.getResource("/img/add.svg")).derive(25, 25)),
            updateButton = new JButton(new FlatSVGIcon(GUI.class.getResource("/img/update.svg")).derive(25, 25)),
            deleteButton = new JButton(new FlatSVGIcon(GUI.class.getResource("/img/delete.svg")).derive(25, 25));
        
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
        JSpinner patientIdSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 1000, 1));

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
        quickSearchButton.setToolTipText("Quick Search Patients");
        quickSearchButton.addActionListener(e -> {
            Map<String, Object> map = new HashMap<>();

            if ((int) patientIdSpinner.getValue() > 0)
                map.put("PatientID", (int) patientIdSpinner.getValue());

            if (firstNameField.getText() != null && !firstNameField.getText().isBlank())
                map.put("FirstName", firstNameField.getText());

            if (lastNameField.getText() != null && !lastNameField.getText().isBlank())
                map.put("LastName", lastNameField.getText());

            if (mobileNumberField.getText() != null && !mobileNumberField.getText().isBlank())
                map.put("MobileNumber", mobileNumberField.getText());

            if (map.size() > 0) {
                patientsTableModel.setRowCount(0);

                try {
                    new QueryManager().filterPatients(map).forEach((patient) -> {
                        patientsTableModel.addRow(new Object[] {patient.getId(), patient.getFirstName(), patient.getMiddleName(), patient.getLastName(), patient.getNameSuffix(), patient.getMobileNumber(), patient.getEmailAddress(), patient.getBirthDate(), patient.getAge(), patient.getSex(), "Open in dialog"});
                    });
                } catch (SQLException sqle) {
                    Dialog.showError(sqle.getMessage());
                }
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

        patientsTableModel = new DefaultTableModel(new Object[][] {}, new String[] {"Patient ID", "First Name", "Middle Name", "Last Name", "Name Suffix", "Mobile Number", "Email Address", "Birth Date", "Patient Age", "Patient Sex", "Medical Records"});

        JTable table = new JTable(patientsTableModel);

        cleanPatientsTableModel();

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
                        SortedMap<LocalDate, String> medicalRecords = new QueryManager().getPatient((int) table.getValueAt(row, 0)).getMedicalRecords();

                        if (medicalRecords == null)
                            medicalRecordPanel.add(new JLabel("No existing medical records were found for this patient."));
                        else {
                            medicalRecordPanel.setLayout(new GridLayout(0, 2, 5, 5));

                            medicalRecords.forEach((key, value) -> {
                                medicalRecordPanel.add(new JLabel(key.format(DateTimeFormatter.ofPattern("MMMM d, yyyy"))));
                                medicalRecordPanel.add(new JLabel(value));
                            });
                        }

                        JOptionPane.showMessageDialog(frame, medicalRecordPanel, "Medical Records", JOptionPane.PLAIN_MESSAGE);
                    } catch (SQLException sqle) {
                        Dialog.showError(sqle.getMessage());
                    }
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);

        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        patientsPanel.add(toolbar, BorderLayout.NORTH);
        patientsPanel.add(scrollPane, BorderLayout.CENTER);

        mainPanel.add(patientsPanel, "Patients");
    }

    private void showFrame() {
        frame.pack();
        frame.setIconImage(new FlatSVGIcon(GUI.class.getResource("/img/logo.svg")).derive(75, 100).getImage());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void cleanPatientsTableModel() throws SQLException {
        patientsTableModel.setRowCount(0);

        for (Patient patient : new QueryManager().getAllPatients()) {
            patientsTableModel.addRow(new Object[] {patient.getId(), patient.getFirstName(), patient.getMiddleName(), patient.getLastName(), patient.getNameSuffix(), patient.getMobileNumber(), patient.getEmailAddress(), patient.getBirthDate().format(DateTimeFormatter.ofPattern("MMMM d, yyyy")), patient.getAge(), patient.getSex(), "Open in dialog"});
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
            List<Object> list = new Dialog(false, components).showDialog(text);

            if (list == null) {
                try {
                    cleanPatientsTableModel();
                } catch (SQLException sqle) {
                    Dialog.showError(sqle.getMessage());
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
                    Dialog.showError(sqle.getMessage());
                }
            }
        } else if (text.equals("Register Patient")) {
            String[] components = {"First Name", "Middle Name", "Last Name", "Name Suffix", "Mobile Number", "Email Address", "Birth Date", "Patient Sex"};
            List<Object> list = new Dialog(true, components).showDialog(text);

            if (list != null && list.get(0) != null) {
                try {
                    new QueryManager().registerPatient(new Patient((String) list.get(0), (String) list.get(1), (String) list.get(2), (String) list.get(3), (String) list.get(4), (String) list.get(5), (LocalDate) list.get(6), (String) list.get(7)));

                    cleanPatientsTableModel();
                } catch (SQLException sqle) {
                    Dialog.showError(sqle.getMessage());
                }
            }
        } else if (text.equals("Update Patient")) {
            int patientId = Dialog.showIdDialog(text, "Patient ID");

            if (patientId > 0) {
                try {
                    String[] components = {"First Name", "Middle Name", "Last Name", "Name Suffix", "Mobile Number", "Email Address", "Birth Date", "Patient Sex"};
                    Patient patient = new QueryManager().getPatient(patientId);
                    List<Object> list = new Dialog(true, components).showFilledDialog(text, new Object[] {patient.getFirstName(), patient.getMiddleName(), patient.getLastName(), patient.getNameSuffix(), patient.getMobileNumber(), patient.getEmailAddress(), patient.getBirthDate(), patient.getSex()});

                    if (list != null && list.get(0) != null) {
                        patient = new Patient((String) list.get(0), (String) list.get(1), (String) list.get(2), (String) list.get(3), (String) list.get(4), (String) list.get(5), (LocalDate) list.get(6), (String) list.get(7));

                        patient.setId(patientId);

                        new QueryManager().updatePatient(patient);

                        cleanPatientsTableModel();
                    }
                } catch (SQLException sqle) {
                    Dialog.showError(sqle.getMessage());
                }
            }
        } else if (text.equals("Delete Patient")) {
            int patientId = Dialog.showIdDialog(text, "Patient ID");

            if (patientId > 0) {
                try {
                    int result = JOptionPane.showConfirmDialog(frame, "Are you sure you want to delete patient " + new QueryManager().getPatient(patientId).getFullName() + "?", text, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

                    if (result == JOptionPane.YES_OPTION) {
                        new QueryManager().deletePatient(new QueryManager().getPatient(patientId));

                        cleanPatientsTableModel();
                    }
                } catch (SQLException sqle) {
                    Dialog.showError(sqle.getMessage());
                }
            }
        } else if (text.equals("Quick Search Patients")) {

        }
    }
}