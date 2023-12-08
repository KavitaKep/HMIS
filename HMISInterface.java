

import java.awt.*;       // Import all classes from java.awt, java.awt.event,
import java.awt.event.*; // javax.swing, and java.util packages.
import javax.swing.*;


public class HMISInterface extends JFrame {

   private Registry registry;
   private HomeQueue homeQueue;
   private CardLayout cardLayout;
   private JPanel cards;
   private Person user;
   private String userHash;

   
   HMISInterface(Registry registry, HomeQueue homeQueue) {
      super("SRHMIS Portal");
      this.registry = registry;
      this.homeQueue = homeQueue;
          
      JPanel firstScreenPanel = createFirstScreenPanel();
      JPanel surveyScreenPanel = createSurveyScreenPanel();
      JPanel homeQueuePanel = createHomeQueuePanel();

      this.cardLayout = new CardLayout();
      this.cards = new JPanel(cardLayout);

      cards.add(firstScreenPanel, "firstScreen");
      cards.add(surveyScreenPanel, "surveyScreen");
      cards.add(homeQueuePanel, "homeQueue");

      add(cards);
   
      // Configure size and visibility.
      setSize(600, 250);
      setVisible(true);  
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
   
   } // End of constructor.  

   private JPanel createFirstScreenPanel() {
      JLabel greeting = new JLabel("Welcome to the Sacramento Regional Homeless Management Information System");
      JLabel prompt1 = new JLabel("Enter your first name:");
      JLabel prompt2 = new JLabel("Enter your last name:");
      JLabel prompt3 = new JLabel("Enter the last four digits of your social security number:");
      JLabel Button = new JLabel("Invalid -- please enter your information into the appropriate fields.");

      JTextField firstName = new JTextField(15);
      JTextField lastName = new JTextField(15);
      JTextField ssn = new JTextField(15);

      JButton enterButton = new JButton("Enter");
      Button.setVisible(false);

      JPanel Greeting = new JPanel();
      Greeting.add(greeting);

      JPanel NameField = new JPanel();
      NameField.add(prompt1);
      NameField.add(firstName);

      JPanel LastNameField = new JPanel();
      LastNameField.add(prompt2);
      LastNameField.add(lastName);

      JPanel SsnField = new JPanel();
      SsnField.add(prompt3);
      SsnField.add(ssn);

      JPanel ButtonPanel = new JPanel();
      ButtonPanel.add(enterButton);
      ButtonPanel.add(Button);

      JPanel firstScreenPanel = new JPanel();
      firstScreenPanel.setLayout(new BoxLayout(firstScreenPanel, BoxLayout.Y_AXIS));
      firstScreenPanel.add(Greeting);
      firstScreenPanel.add(NameField);
      firstScreenPanel.add(LastNameField);
      firstScreenPanel.add(SsnField);
      firstScreenPanel.add(ButtonPanel);


      enterButton.addActionListener(e -> {
           String vFirstName = firstName.getText();
           String vLastName = lastName.getText();
           String vSsn = ssn.getText();
           // Ensure name and ssn information is entered into textfields.      
           if (vFirstName.isEmpty() || vLastName.isEmpty() || vSsn.isEmpty()) {
              Button.setVisible(true); // Prompt user to re-enter information.
              return;
           }       

           this.user = new Person(vFirstName, vLastName, vSsn);
           this.userHash = user.getHash();
           
           boolean isPersonInRegistry = registry.containsHash(user.getHash());
           System.out.println(user.getFirstName() + " " + user.getLastName() + " " + isPersonInRegistry + " " + user.getHash());

           // Add user to registry and housing queue if they are not in registry
           if (isPersonInRegistry == true) {
               cardLayout.show(cards, "homeQueue");
           } else {
               registry.addPerson(user);
               cardLayout.show(cards, "surveyScreen");
            
           }
       });

      return firstScreenPanel;
   }

   // Create the second Parent Panel with three (yes,no,decline to say) check boxes for if they are disabled, pregnant, or a veteran
   // Along with entry box for age
   private JPanel createSurveyScreenPanel(){
      JLabel survey_prompt = new JLabel("Please answer the following questions to help us better serve you.");
      JLabel age_prompt = new JLabel("Enter your age:");
      JLabel veteran_prompt = new JLabel("Are you a veteran?");
      JLabel pregnant_prompt = new JLabel("Are you pregnant?");
      JLabel disabled_prompt = new JLabel("Are you disabled?");
      JLabel Button = new JLabel("Invalid -- please enter your information into the appropriate fields.");

      JTextField age = new JTextField(15);
      JCheckBox veteran = new JCheckBox();
      JCheckBox pregnant = new JCheckBox();
      JCheckBox disabled = new JCheckBox();


      JButton enterButton = new JButton("Enter");
      Button.setVisible(false);

      JPanel SurveyPrompt = new JPanel();
      JPanel AgePrompt = new JPanel();
      JPanel VeteranPrompt = new JPanel();
      JPanel PregnantPrompt = new JPanel();
      JPanel DisabledPrompt = new JPanel();
      JPanel ButtonPanel = new JPanel();

      SurveyPrompt.add(survey_prompt);
      AgePrompt.add(age_prompt);
      AgePrompt.add(age);
      VeteranPrompt.add(veteran_prompt);
      VeteranPrompt.add(veteran);
      PregnantPrompt.add(pregnant_prompt);
      PregnantPrompt.add(pregnant);
      DisabledPrompt.add(disabled_prompt);
      DisabledPrompt.add(disabled);
      ButtonPanel.add(enterButton);
      ButtonPanel.add(Button);

      JPanel surveyScreenPanel = new JPanel();
      surveyScreenPanel.setLayout(new BoxLayout(surveyScreenPanel, BoxLayout.Y_AXIS));
      surveyScreenPanel.add(SurveyPrompt);
      surveyScreenPanel.add(AgePrompt);
      surveyScreenPanel.add(VeteranPrompt);
      surveyScreenPanel.add(PregnantPrompt);
      surveyScreenPanel.add(DisabledPrompt);
      surveyScreenPanel.add(ButtonPanel);

      enterButton.addActionListener(e -> {
           // All information that is added is stored in the registry, it's okay if anything is left blank, but the age must be numeric
           if(age.getText().matches("[0-9]+") == false){
              Button.setVisible(true); // Prompt user to re-enter information.
              return;
           }
           if(!age.getText().isEmpty()){
              String vAge = age.getText();
              user.setAge(vAge);
           }

            Boolean vVeteran = veteran.isSelected();
            Boolean vPregnant = pregnant.isSelected();
            Boolean vDisabled = disabled.isSelected();

            user.setIsVeteran(vVeteran);
            user.setIsPregnant(vPregnant);
            user.setIsDisabled(vDisabled);

            registry.updatePersonData(userHash, user.getData());

            cardLayout.show(cards, "homeQueue");
      });
      

      return surveyScreenPanel;
   }

   //Create the Homequeue panel, if someone is already in the queue it displays their position, along with the option to remove themselves or exit the program
   // If they are not in the queue it displays their position and the option to add themselves to the queue or exit the program
   private JPanel createHomeQueuePanel(){
      JLabel queue_prompt = new JLabel("You are currently number " + homeQueue.findPosition(userHash) + " in the waitlist.");
      JLabel notInQueue = new JLabel("You are not currently in the waitlist.");
      JLabel Button = new JLabel("Invalid -- please enter your information into the appropriate fields.");

      JButton joinButton = new JButton("Join Waitlist");
      JButton removeButton = new JButton("Leave Waitlist");
      JButton exitButton = new JButton("Exit");

      JPanel QueuePrompt = new JPanel();
      JPanel ButtonPanel = new JPanel();

      if (homeQueue.inQueue(userHash)) {
         QueuePrompt.add(queue_prompt);
      } else {
         QueuePrompt.add(notInQueue);
      }
   
      ButtonPanel.add(joinButton);
      ButtonPanel.add(removeButton);
      ButtonPanel.add(exitButton);
      ButtonPanel.add(Button);
      Button.setVisible(false);

      JPanel homeQueuePanel = new JPanel();
      homeQueuePanel.setLayout(new BoxLayout(homeQueuePanel, BoxLayout.Y_AXIS));
      homeQueuePanel.add(QueuePrompt);
      homeQueuePanel.add(ButtonPanel);

      joinButton.addActionListener(e -> {
           // Add user to housing queue
           homeQueue.add(userHash);
           cardLayout.show(cards, "homeQueue");
       });

      removeButton.addActionListener(e -> {
           // Remove user from housing queue
           homeQueue.remove(userHash);
           cardLayout.show(cards, "homeQueue");
       });

      exitButton.addActionListener(e -> {
           // Exit the application
           System.exit(0);
       });

      return homeQueuePanel;
   }

} // End of class HMISInterface.

class HMISWindowAdapter extends WindowAdapter {
    String housingQueuepath = "homeQueue.csv";
    String registryPath = "registry.csv";    
    JFrame window = null;
    Registry registry = null;
    HomeQueue homeQueue = null;
    
    HMISWindowAdapter(JFrame window, Registry registry, HomeQueue homeQueue) {
        this.window = window;
        this.registry = registry;
        this.homeQueue = homeQueue;
    }
            
    public void windowClosing(WindowEvent e) {
       // Write data to file when the window is closed
       registry.writeToFile(registryPath);
       homeQueue.writeToFile();
       
       // Exit the application
       System.exit(0);
   }
}
