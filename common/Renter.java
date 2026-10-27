package common;

/**
 * Stores the contact and account information for a vehicle rental customer.
 * Private fields keep the data encapsulated; callers use methods to read or update it.
 */
public class Renter{

    /** The renter's first name. */
    private String fName;
    /** The renter's last name. */
    private String lName;
    /** The renter's driver's license number. */
    private String dLicense;
    /** The renter's insurance provider. */
    private String InsuranceProvider;
    /** The renter's account with the insurance provider. */
    private String InsuranceAcct;
    /** Payment information supplied for the rental. */
    private String CreditCard;
    /** The renter's date of birth; initialized to the default date. */
    private Date dob = new Date();


    /** Creates a renter whose personal and account fields have placeholder values. */
    public Renter() {
        fName = "Not Provided";
        lName = "Not Provided";
        dLicense = "Not Provided";
        InsuranceProvider = "Not Provided";
        InsuranceAcct = "Not Provided";
        CreditCard = "Not Provided";    // can potentially be a class of its own
    }

    /**
     * Creates a renter with a required first and last name.
     *
     * @param first the renter's first name
     * @param last the renter's last name
     * @throws IllegalArgumentException if either name is null or blank
     */
    public Renter(String first, String last) {
        setName(first, last);
        dLicense = "Not Provided";
        InsuranceProvider = "Not Provided";
        InsuranceAcct = "Not Provided";
        CreditCard = "Not Provided";    // can potentially be a class of its own
    }

    /**
     * Updates both required name fields after checking them.
     *
     * @param first the renter's new first name
     * @param last the renter's new last name
     * @throws IllegalArgumentException if either name is null or blank
     */
    public void setName(String first, String last){
        // Validate both names first so a bad last name cannot leave a half-updated renter.
        String validFirstName = requireText(first, "First name");
        String validLastName = requireText(last, "Last name");
        fName = validFirstName;
        lName = validLastName;
    }
    /**
     * Stores the renter's insurance provider and account.
     * Both values are checked before either field is changed.
     *
     * @param provider the insurance company name
     * @param acct the account identifier with that provider
     * @throws IllegalArgumentException if either value is null or blank
     */
    public void setInsurance (String provider, String acct) {
        // As with names, only update the object after both pieces pass validation.
        String validProvider = requireText(provider, "Insurance provider");
        String validAccount = requireText(acct, "Insurance account");
        InsuranceProvider = validProvider;
        InsuranceAcct = validAccount;
    }
    /**
     * Sets the renter's date of birth.
     *
     * @param dateOfBirth the validated date of birth
     * @throws IllegalArgumentException if the date is {@code null}
     */
    public void setDOB(Date dateOfBirth){
        if(dateOfBirth == null){ throw new IllegalArgumentException("Date of birth is required."); }
        dob = dateOfBirth;
    }
    /**
     * Sets the renter's driver's license number.
     *
     * @param licenseNo the nonblank license number
     * @throws IllegalArgumentException if the value is null or blank
     */
    public void setLicense (String licenseNo) { dLicense = requireText(licenseNo, "License number"); }

    /**
     * Sets the renter's payment information.
     *
     * @param cCard the nonblank payment information
     * @throws IllegalArgumentException if the value is null or blank
     */
    public void setPayment (String cCard) { CreditCard = requireText(cCard, "Payment information"); }

    /**
     * Trims a required text field and rejects missing values.
     *
     * @param value the text to validate
     * @param fieldName the field label used in the error message
     * @return the trimmed text
     * @throws IllegalArgumentException if the value is null or blank
     */
    private String requireText(String value, String fieldName){
        if(value == null || value.trim().isEmpty()){
            throw new IllegalArgumentException(fieldName + " cannot be blank.");
        }
        return value.trim();
    }

    /**
     * Returns the renter's name as last name followed by first name.
     *
     * @return the formatted renter name
     */
    public String getName(){ return lName + ", " + fName; }

    /**
     * Returns the renter's date of birth.
     *
     * @return the date of birth
     */
    public Date getDOB(){ return dob; }

    /**
     * Returns the driver's license number.
     *
     * @return the license number or its placeholder value
     */
    public String getLicense() {return dLicense; }

    /**
     * Returns the payment information.
     *
     * @return the payment value or its placeholder
     */
    public String getPayment () { return CreditCard; }

    /**
     * Returns the insurance provider and account in one readable string.
     *
     * @return the insurance details
     */
    public String getInsurance() {
        return InsuranceProvider  +": "+InsuranceAcct;
    }
}
