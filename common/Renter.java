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
        fName = first;
        lName = last;
        dLicense = "Not Provided";
        InsuranceProvider = "Not Provided";
        InsuranceAcct = "Not Provided";
        CreditCard = "Not Provided";    // can potentially be a class of its own
    }

    // METHODS  ```````````````````````````````````````````````````````````````
    // setters ----------------------------------------------------------------
    public void setName(String first, String last){
        fName = first;
        lName = last;
    }
    public void setInsurance (String provider, String acct) {
        InsuranceProvider = provider;
        InsuranceAcct = acct;
    }
    public void setDOB(Date dateOfBirth){ dob = dateOfBirth; }
    public void setLicense (String licenseNo) { dLicense = licenseNo; }
    public void setPayment (String cCard) { CreditCard = cCard; }

    // getters ----------------------------------------------------------------
    public String getName(){ return lName + ", " + fName; }
    public Date getDOB(){ return dob; }
    public String getLicense() {return dLicense; }
    public String getPayment () { return CreditCard; }
    public String getInsurance() {
        return InsuranceProvider  +": "+InsuranceAcct;
    }
}
