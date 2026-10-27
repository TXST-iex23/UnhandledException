package common;

public class Renter{

    private String fName;
    private String lName;
    private String dLicense;
    private String InsuranceProvider;
    private String InsuranceAcct;
    private String CreditCard;          // can potentially be a class of its own
    private Date dob = new Date();


    public Renter() {
        fName = "Not Provided";
        lName = "Not Provided";
        dLicense = "Not Provided";
        InsuranceProvider = "Not Provided";
        InsuranceAcct = "Not Provided";
        CreditCard = "Not Provided";    // can potentially be a class of its own
    }

    public Renter(String first, String last) {
        setName(first, last);
        dLicense = "Not Provided";
        InsuranceProvider = "Not Provided";
        InsuranceAcct = "Not Provided";
        CreditCard = "Not Provided";    // can potentially be a class of its own
    }

    // METHODS  ```````````````````````````````````````````````````````````````
    // setters ----------------------------------------------------------------
    public void setName(String first, String last){
        String validFirstName = requireText(first, "First name");
        String validLastName = requireText(last, "Last name");
        fName = validFirstName;
        lName = validLastName;
    }
    public void setInsurance (String provider, String acct) {
        String validProvider = requireText(provider, "Insurance provider");
        String validAccount = requireText(acct, "Insurance account");
        InsuranceProvider = validProvider;
        InsuranceAcct = validAccount;
    }
    public void setDOB(Date dateOfBirth){
        if(dateOfBirth == null){ throw new IllegalArgumentException("Date of birth is required."); }
        dob = dateOfBirth;
    }
    public void setLicense (String licenseNo) { dLicense = requireText(licenseNo, "License number"); }
    public void setPayment (String cCard) { CreditCard = requireText(cCard, "Payment information"); }

    private String requireText(String value, String fieldName){
        if(value == null || value.trim().isEmpty()){
            throw new IllegalArgumentException(fieldName + " cannot be blank.");
        }
        return value.trim();
    }

    // getters ----------------------------------------------------------------
    public String getName(){ return lName + ", " + fName; }
    public Date getDOB(){ return dob; }
    public String getLicense() {return dLicense; }
    public String getPayment () { return CreditCard; }
    public String getInsurance() {
        return InsuranceProvider  +": "+InsuranceAcct;
    }
}
