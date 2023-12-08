public class HMIS {
    public static void main(String[] args) throws Exception {
        String housingQueuepath = "homeQueue.csv";
        String registryPath = "registry.csv";
        Registry registry = new Registry(registryPath); 
        HomeQueue housingQueue = new HomeQueue(housingQueuepath);
       
        HMISInterface h1 = new HMISInterface(registry, housingQueue);
        h1.addWindowListener(new HMISWindowAdapter(h1, registry, housingQueue));
    }
}
