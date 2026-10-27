package com.github.jkarw7n.barahspas;

import java.awt.Component;
import java.awt.GridLayout;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

class Dialog {
    private boolean notForSearching;
    private JPanel dialogPanel;
    private List<Integer> nonnullableIndices;

    public Dialog(boolean notForSearching, String... components) {
        this.notForSearching = notForSearching;
        dialogPanel = new JPanel(new GridLayout(0, 2, 5, 5));
        nonnullableIndices = new ArrayList<>();

        JLabel patientIdLabel = new JLabel("Patient ID"),
            firstNameLabel = new JLabel(markNonnullable("First Name")),
            middleNameLabel = new JLabel("Middle Name"),
            lastNameLabel = new JLabel(markNonnullable("Last Name")),
            nameSuffixLabel = new JLabel("Name Suffix"),
            mobileNumberLabel = new JLabel(markNonnullable("Mobile Number")),
            emailAddressLabel = new JLabel("Email Address"),
            birthDateLabel = new JLabel(markNonnullable("Birth Date (Month, Day, Year)")),
            patientSexLabel = new JLabel(markNonnullable("Patient Sex"));

        JSpinner patientIdSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 1000, 1));
        JTextField firstNameField = new JTextField(),
            middleNameField = new JTextField(),
            lastNameField = new JTextField();
        JComboBox<String> nameSuffixBox = new JComboBox<>(new String[] {"None", "Jr.", "Sr.", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X"});
        JTextField mobileNumberField = new JTextField(),
            emailAddressField = new JTextField();
        JPanel birthDatePanel = new JPanel(new GridLayout(1, 3, 5, 0));
        JSpinner monthSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 12, 1)),
            daySpinner = new JSpinner(new SpinnerNumberModel(1, 1, 31, 1)),
            yearSpinner = new JSpinner(new SpinnerNumberModel(1900, 1900, LocalDate.now().getYear(), 1));
        JComboBox<String> patientSexBox = new JComboBox<>(notForSearching ? new String[] {"Male", "Female"} : new String[] {"None", "Male", "Female"});

        yearSpinner.setEditor(new JSpinner.NumberEditor(yearSpinner, "#"));

        birthDatePanel.add(monthSpinner);
        birthDatePanel.add(daySpinner);
        birthDatePanel.add(yearSpinner);

        patientIdLabel.setToolTipText("Leave as 0 for a null value");
        nameSuffixBox.setEditable(false);
        patientSexBox.setEditable(false);

        if (!notForSearching)
            birthDateLabel.setToolTipText("Leave as January 1, 1900 for a null value");

        for (int i = 0; i < components.length; i++) {
            switch (components[i]) {
                case "Patient ID":
                    dialogPanel.add(patientIdLabel);
                    dialogPanel.add(patientIdSpinner);
                    break;
                case "First Name":
                    dialogPanel.add(firstNameLabel);
                    dialogPanel.add(firstNameField);

                    if (notForSearching) {
                        firstNameLabel.setToolTipText("Required field");
                        nonnullableIndices.add(i);
                    }

                    break;
                case "Middle Name":
                    dialogPanel.add(middleNameLabel);
                    dialogPanel.add(middleNameField);
                    break;
                case "Last Name":
                    dialogPanel.add(lastNameLabel);
                    dialogPanel.add(lastNameField);

                    if (notForSearching) {
                        nonnullableIndices.add(i);
                    }
                    
                    break;
                case "Name Suffix":
                    dialogPanel.add(nameSuffixLabel);
                    dialogPanel.add(nameSuffixBox);
                    break;
                case "Mobile Number":
                    dialogPanel.add(mobileNumberLabel);
                    dialogPanel.add(mobileNumberField);

                    if (notForSearching) {
                        mobileNumberLabel.setToolTipText("Required field");
                        nonnullableIndices.add(i);
                    }
                    
                    break;
                case "Email Address":
                    dialogPanel.add(emailAddressLabel);
                    dialogPanel.add(emailAddressField);
                    break;
                case "Birth Date":
                    dialogPanel.add(birthDateLabel);
                    dialogPanel.add(birthDatePanel);

                    if (notForSearching) {
                        birthDateLabel.setToolTipText("Required field");
                        nonnullableIndices.add(i);
                    }
                    
                    break;
                case "Patient Sex":
                    dialogPanel.add(patientSexLabel);
                    dialogPanel.add(patientSexBox);

                    if (notForSearching) {
                        patientIdLabel.setToolTipText("Required field");
                        nonnullableIndices.add(i);
                    }
                    
                    break;
            }
        }
    }

    private String markNonnullable(String text) {
        return notForSearching ? text + " (*)" : text;
    }

    public List<Object> showDialog(String message) {
        int result = JOptionPane.showConfirmDialog(null, dialogPanel, message, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            List<Object> list = new ArrayList<>();
            boolean allNull = true;

            for (int i = 1; i < dialogPanel.getComponents().length; i += 2) {
                Component component = dialogPanel.getComponent(i);

                if (component instanceof JSpinner spinner) {
                    if ((int) spinner.getValue() == 0) {
                        if (nonnullableIndices.contains((int) (i - 1) / 2))
                            showError("Non-nullable spinner value must not be 0.");
                        else
                            list.add(null);
                    } else {
                        list.add((int) spinner.getValue());
                        allNull = false;
                    }
                } else if (component instanceof JTextField textField) {
                    if (textField.getText() == null || textField.getText().isBlank()) {
                        if (nonnullableIndices.contains((int) (i - 1) / 2))
                            showError("Non-nullable text field value must not be null or blank.");
                        else
                            list.add(null);
                    } else {
                        list.add(textField.getText().strip());
                        allNull = false;
                    }
                } else if (component instanceof JComboBox comboBox) {
                    if (comboBox.getSelectedItem().equals("None"))
                        list.add(null);
                    else {
                        list.add(comboBox.getSelectedItem());
                        allNull = false;
                    }
                } else if (component instanceof JPanel panel) {
                    int month = (int) ((JSpinner) panel.getComponent(0)).getValue(),
                        day = (int) ((JSpinner) panel.getComponent(1)).getValue(),
                        year = (int) ((JSpinner) panel.getComponent(2)).getValue();

                    if (month == 1 && day == 1 && year == 1900 && !notForSearching)
                        list.add(null);
                    else {
                        list.add(LocalDate.of(year, month, day));
                        allNull = false;
                    }
                }
            }

            if (!allNull)
                return list;
        }

        return null;
    }

    public static void showError(String message) {
        JOptionPane.showMessageDialog(null, message, "An error occurred", JOptionPane.ERROR_MESSAGE);
    }
}
