
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest; 
import java.security.NoSuchAlgorithmException;

public class Person {
    private String firstName;
    private String lastName;
    private String ssn;
    private String age;
    private boolean isVeteran;
    private boolean isPregnant;
    private boolean isDisabled;
    private String keyhash;
    private String checksum;

    public Person(String keyhash){
        this.keyhash = keyhash;
    }

    // Logging in or registering for the first time, the user will enter their first name, last name, and SSN.
    public Person(String firstName, String lastName, String ssn){
        this.firstName = firstName;
        this.lastName = lastName;
        this.ssn = ssn;
        this.keyhash = makekeyHash();
        this.checksum = makeCheckSum();
    }

    // Data pulled from the database will be anonymous, but the data and keyhash are there. 
    public Person(String keyhash, String age, boolean isVeteran, boolean isPregnant, boolean isDisabled){
        this.keyhash = keyhash;
        this.age = age;

        this.isVeteran = isVeteran;
        this.isPregnant = isPregnant;
        this.isDisabled = isDisabled;
    }

    // This method is where the person's personal SHA-256 keyhash is generated, which is their unique and anonymous identifier. 
    public String makekeyHash(){
        String key = generateKey();
        try{
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(key.getBytes(StandardCharsets.UTF_8));
            byte[] digest = md.digest();
            StringBuffer hexString = new StringBuffer();
            for(int i = 0; i < digest.length; i++){
                hexString.append(Integer.toHexString(0xFF & digest[i]));
            }
            keyhash = hexString.toString();
            return keyhash;
        } catch (NoSuchAlgorithmException e){
            e.printStackTrace();
            return null;
        }
    }

    // Create a second type of hash for checksuming the person's input in order to detect collisions. The hashing algorithm needs to be different than
    // the keyhash algorithm. (So not sha-256)

    public  String makeCheckSum() {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            String input = firstName + lastName + ssn;
            byte[] hash = md.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    // Make a new keyhash for the person that uses 2 less digits of their SSN
    public void rehash(){
        String newSSN = ssn.substring(0, ssn.length() - 2);
        this.ssn = newSSN;
        String newKeyHash = makekeyHash();
        this.keyhash = newKeyHash;
    }


    public String getFirstName(){
        return firstName;
    }

    public String getLastName(){
        return lastName;
    }

    public String setFirstName(String firstName){
        this.firstName = firstName;
        return firstName;
    }

    public String setLastName(String lastName){
        this.lastName = lastName;
        return lastName;
    }

    public String getSSN(){
        return ssn;
    }

    public void setSSN(String ssn){
        this.ssn = ssn;
    }
    public String generateKey(){
        String key = firstName + lastName + ssn;
        return key;
    }

    public void setAge(String age){
        this.age = age;
    }

    public String getAge(){
        return age;
    }



    public boolean getIsVeteran(){
        return isVeteran;
    }

    public void setIsVeteran(boolean isVeteran){
        this.isVeteran = isVeteran;
    }

    public boolean getIsPregnant(){
        return isPregnant;
    }

    public void setIsPregnant(boolean isPregnant){
        this.isPregnant = isPregnant;
    }

    public boolean getIsDisabled(){
        return isDisabled;
    }

    public void setIsDisabled(boolean isDisabled){
        this.isDisabled = isDisabled;
    }

    public String getHash(){
        return keyhash;
    }

    public void setkeyHash(String keyhash){
        this.keyhash = keyhash;
    }

    public String getCheckSum(){
        return checksum;
    }

    public void setCheckSum(String checksum){
        this.checksum = checksum;
    }

    public Object[] getData(){
        Object[] data = {checksum, age, isVeteran, isPregnant, isDisabled};
        return data;
    }


}