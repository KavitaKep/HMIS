import java.util.HashMap;
import java.util.Map;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

// Class where I will store data pertaining to a person based on their hash, which is generated from their name and SSN
public class Registry{
    private HashMap<String, Object[]> registryMap;

    public Registry(String filepath){
        this.registryMap = new HashMap<String, Object[]>();
        loadFromFile(filepath);
    }

    public void loadFromFile(String filepath){
        try (BufferedReader reader = new BufferedReader(new FileReader(filepath))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                String hash = data[0];
                Object[] personData = new Object[data.length - 1];
                for (int i = 1; i < data.length; i++) {
                    personData[i - 1] = data[i].trim();
                }
                registryMap.put(hash, personData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

public void writeToFile(String filePath) {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
        for (Map.Entry<String, Object[]> entry : registryMap.entrySet()) {
            writer.write(entry.getKey());
            for (Object data : entry.getValue()) {
                // if data is null, continue
                if (data == null) {
                    writer.write(",");
                    continue;
                }
                writer.write("," + data.toString());
            }
            writer.newLine();
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
}

    public void addPerson(Person person){
        if (registryMap.containsKey(person.getHash())){
            // check the checksum of the person to be added "person" and the checksum corresponding to the keyhash already in the map
            if (registryMap.get(person.getHash())[0].equals(person.getCheckSum())){
                System.out.println("Person already exists in registry.");
            }
            else {
                System.out.println("Person already exists in registry, but the data does not match.");
                person.rehash();
                registryMap.put(person.getHash(), person.getData());
            }
        }
        else { 
        registryMap.put(person.getHash(), person.getData());
        }
    }

    public Object[] getData(String hash){
        return registryMap.get(hash);
    }

    public void removePerson(String hash){
        registryMap.remove(hash);
    }

    public boolean containsPerson(String firstname, String lastname, String ssn){
        Person person = new Person(firstname, lastname, ssn);
        return registryMap.containsKey(person.getHash());
    }

    public boolean containsHash(String hash){
        return registryMap.containsKey(hash);
    }

    public void updatePersonData(String hash, Object[] newData) {
        if (registryMap.containsKey(hash)) {
            registryMap.put(hash, newData);
        } else {
            System.out.println("Person not found in registry.");
        }
    }

    public int countPeople() {
        return registryMap.size();
    }
}
