package com.github.jkarw7n.barahspas;

abstract class Person implements Identifiable {
    private int id;
    private String firstName;
    private String middleName;
    private String lastName;
    private String nameSuffix;
    private String mobileNumber;
    private String emailAddress;

    protected Person() {
        this(
            "First Name",
            null,
            "Last Name",
            null,
            "09123456789",
            null
        );
    }

    protected Person(
        String firstName,
        String middleName,
        String lastName,
        String nameSuffix,
        String mobileNumber,
        String emailAddress
    ) {
        setFirstName(firstName);
        setMiddleName(middleName);
        setLastName(lastName);
        setNameSuffix(nameSuffix);
        setMobileNumber(mobileNumber);
        setEmailAddress(emailAddress);
    }

    public int getId() { return id; }
    public String getFirstName() { return firstName; }
    public String getMiddleName() { return middleName; }
    public String getLastName() { return lastName; }
    public String getNameSuffix() { return nameSuffix; }
    public String getMobileNumber() { return mobileNumber; }
    public String getEmailAddress() { return emailAddress; }

    public void setId(int id) {
        if (id <= 0)
            throw new IllegalArgumentException("Error: Person ID cannot be equal to or less than zero.");

        this.id = id;
    }

    public void setFirstName(String firstName) {
        if (firstName == null || firstName.isBlank())
            throw new IllegalArgumentException("Error: Person first name cannot be null or blank.");

        this.firstName = capitalizeName(firstName);
    }
    
    public void setMiddleName(String middleName) {
        if (middleName == null || middleName.isBlank()) {
            this.middleName = null;
            return;
        }

        this.middleName = capitalizeName(middleName);
    }

    public void setLastName(String lastName) {
        if (lastName == null || lastName.isBlank())
            throw new IllegalArgumentException("Error: Person last name cannot be null or blank.");

        this.lastName = capitalizeName(lastName);
    }

    public void setNameSuffix(String nameSuffix) {
        if (nameSuffix == null || nameSuffix.isBlank() || nameSuffix.equals("N/A")) { // balikan to
            this.nameSuffix = null;
            return;
        }

        this.nameSuffix = nameSuffix;
    }

    public void setMobileNumber(String mobileNumber) {
        if (mobileNumber == null || mobileNumber.isBlank())
            throw new IllegalArgumentException("Error: Person mobile number cannot be null or blank.");

        if (mobileNumber.replaceAll("\\s+", " ").strip().length() != 11)
            throw new IllegalArgumentException("Error: Person mobile number must have a length of exactly 11 characters (09XXXXXXXXX).");

        if (!mobileNumber.replaceAll("\\s+", " ").strip().matches("^\\d+$"))
            throw new IllegalArgumentException("Error: Person mobile number must be composed of only numerical digits.");

        this.mobileNumber = mobileNumber.replaceAll("\\s+", " ").strip();
    }

    public void setEmailAddress(String emailAddress) {
        if (emailAddress == null || emailAddress.isBlank()) {
            this.emailAddress = null;
            return;
        }

        if (!emailAddress.replaceAll("\\s+", " ").strip().matches("^[a-z0-9]{1}[a-z0-9\\._\\-]+[a-z0-9]{1}@[a-z0-9]{1}[a-z0-9\\.\\-]*[a-z0-9]{1}\\.[a-z0-9]+$"))
            throw new IllegalArgumentException("Error: Person email address invalid.");

        this.emailAddress = emailAddress.replaceAll("\\s+", " ").strip();
    }

    public String getFullName() {
        return firstName + (middleName != null ? " " + middleName.charAt(0) + "." : "") + " " + lastName + (nameSuffix != null ? " " + nameSuffix : "");
    }

    private String capitalizeName(String name) {
        String[] split = name.replaceAll("\\s+", " ").strip().split(" ");

        for (int i = 0; i < split.length; i++) {
            split[i] = split[i].replaceAll("\\s+", " ").strip().toUpperCase().charAt(0) + split[i].replaceAll("\\s+", " ").strip().toLowerCase().substring(1);
        }

        return String.join(" ", split);
    }
}
