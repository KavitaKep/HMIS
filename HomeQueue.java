import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.LinkedList;

public class HomeQueue {
    private LinkedList<String> housingQueue;
    private String filepath;

    public HomeQueue(String filepath) {
        this.housingQueue = new LinkedList<>();
        this.filepath = filepath;
        loadFromFile();
    }

    public void add(String hash) {
        housingQueue.add(hash);
    }

    public void remove(String hash) {
        housingQueue.remove(hash);
    }


    private void loadFromFile(){
        try (BufferedReader reader = new BufferedReader(new FileReader(this.filepath))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                for (String value : data) {
                    housingQueue.add(value.trim());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(this.filepath))) {
            while (!housingQueue.isEmpty()) {
                String nextHash = housingQueue.pop();
                if (nextHash != null) {
                    writer.write(nextHash);
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addQueue(String hash) {
        housingQueue.add(hash);
    }

    public String removalQueue() {
        if (!housingQueue.isEmpty()) {
            return housingQueue.pop();
        } else {
            return null;
        }
    }

    public int findPosition(String targetHash) {
        return housingQueue.indexOf(targetHash) + 1;
    }

    public boolean inQueue(String targetHash) {
        return housingQueue.contains(targetHash);
    }
}

