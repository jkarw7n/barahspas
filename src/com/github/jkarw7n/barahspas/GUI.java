package com.github.jkarw7n.barahspas;

import com.formdev.flatlaf.*;
import com.formdev.flatlaf.extras.*;
import com.formdev.flatlaf.icons.FlatSearchIcon;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.time.LocalDate;

import javax.swing.*;

class GUI {
    JFrame frame;
    JPanel mainPanel;

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

        patientsMenu.add(searchPatientsItem);
        patientsMenu.add(registerPatientItem);
        patientsMenu.add(updatePatientItem);
        patientsMenu.add(deletePatientItem);

        searchAppointmentsItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_PERIOD, InputEvent.CTRL_DOWN_MASK));
        scheduleAppointmentItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_DOWN_MASK));
        completeAppointmentItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, InputEvent.CTRL_DOWN_MASK));

        appointmentsMenu.add(searchAppointmentsItem);
        appointmentsMenu.add(scheduleAppointmentItem);
        appointmentsMenu.add(completeAppointmentItem);
        appointmentsMenu.add(updateAppointmentItem);
        appointmentsMenu.add(cancelAppointmentItem);
        appointmentsMenu.add(deleteAppointmentItem);

        searchMedicalStaffItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_SLASH, InputEvent.CTRL_DOWN_MASK));
        registerMedicalStaffItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M, InputEvent.CTRL_DOWN_MASK));

        medicalStaffMenu.add(searchMedicalStaffItem);
        medicalStaffMenu.add(registerMedicalStaffItem);
        medicalStaffMenu.add(updateMedicalStaffItem);
        medicalStaffMenu.add(deleteMedicalStaffItem);

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
        }

        menuBar.add(aboutMenu);
        menuBar.add(patientsMenu);
        menuBar.add(appointmentsMenu);
        menuBar.add(medicalStaffMenu);

        frame.setJMenuBar(menuBar);

        mainPanel = new JPanel(new CardLayout());

        addAboutPanel();
        addPatientsPanel();

        frame.add(mainPanel);
    }

    private void addAboutPanel() {
        JPanel aboutPanel = new JPanel(new BorderLayout());

        aboutPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        JPanel boxPanel = new JPanel();

        boxPanel.setLayout(new BoxLayout(boxPanel, BoxLayout.Y_AXIS));

        JLabel logoLabel = new JLabel(new FlatSVGIcon(GUI.class.getResource("/img/logo.svg")).derive(100, 100)),
            barahspasLabel = new JLabel("BaraHSPAS"),
            versionLabel = new JLabel("Version 1.0"),
            descriptionLabel = new JLabel("A barangay health station patient and appointment system");
        
        logoLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);

        barahspasLabel.putClientProperty(FlatClientProperties.STYLE_CLASS, "h1");
        barahspasLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);

        versionLabel.putClientProperty(FlatClientProperties.STYLE_CLASS, "h2");
        versionLabel.setForeground(Color.LIGHT_GRAY);
        versionLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);

        descriptionLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);

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
        JSpinner patientIdSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 1000, 1));

        patientIdLabel.setAlignmentX(JLabel.LEFT_ALIGNMENT);

        patientIdSpinner.setMaximumSize(patientIdSpinner.getPreferredSize());
        patientIdSpinner.setAlignmentX(JSpinner.LEFT_ALIGNMENT);

        patientIdPanel.add(patientIdLabel);
        patientIdPanel.add(patientIdSpinner);

        JLabel firstNameLabel = new JLabel("First Name");
        JTextField firstNameField = new JTextField(15);

        firstNameLabel.setAlignmentX(JLabel.LEFT_ALIGNMENT);

        firstNameField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Enter first name here");
        firstNameField.setMaximumSize(firstNameField.getPreferredSize());
        firstNameField.setAlignmentX(JTextField.LEFT_ALIGNMENT);

        firstNamePanel.add(firstNameLabel);
        firstNamePanel.add(firstNameField);

        JLabel lastNameLabel = new JLabel("Last Name");
        JTextField lastNameField = new JTextField(10);

        lastNameLabel.setAlignmentX(JLabel.LEFT_ALIGNMENT);

        lastNameField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Enter last name here");
        lastNameField.setMaximumSize(lastNameField.getPreferredSize());
        lastNameField.setAlignmentX(JTextField.LEFT_ALIGNMENT);

        lastNamePanel.add(lastNameLabel);
        lastNamePanel.add(lastNameField);

        JLabel mobileNumberLabel = new JLabel("Mobile Number");
        JTextField mobileNumberField = new JTextField(10);

        mobileNumberLabel.setAlignmentX(JLabel.LEFT_ALIGNMENT);

        mobileNumberField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Enter number here");
        mobileNumberField.setMaximumSize(mobileNumberField.getPreferredSize());
        mobileNumberField.setAlignmentX(JTextField.LEFT_ALIGNMENT);

        mobileNumberPanel.add(mobileNumberLabel);
        mobileNumberPanel.add(mobileNumberField);

        JButton quickSearchButton = new JButton("Quick Search");

        quickSearchPanel.add(quickSearchButton);

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

        JTable table = new JTable(new Object[][] {}, new String[] {"Patient ID", "First Name", "Middle Name", "Last Name", "Name Suffix", "Mobile Number", "Email Address", "Birth Date", "Sex", "Medical Records"});

        // double click a row to edit it

        table.setDefaultEditor(Object.class, null);

        JScrollPane scrollPane = new JScrollPane(table);

        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

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
}