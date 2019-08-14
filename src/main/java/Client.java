import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Map;
import java.util.UUID;

public class Client {
  private static final int[] PORTS = {4000,4001, 4002, 4003, 4004};

  public static void main(String[] args) throws Exception{
    int id = Integer.parseInt(args[0]);
    Registry registry = LocateRegistry.getRegistry(PORTS[id]);
    CrawlerService agent = (CrawlerService) registry.lookup("scraperAgent"+id);

    BufferedReader nbr = new BufferedReader(new InputStreamReader(System.in));
    String line;

    while(true) {
      try {
        System.out.println("Please enter a link and keyword you want to search:");
        line = nbr.readLine();
        if (line.equals("exit")) break;
        String[] input = line.split(" ");
        UUID taskId = UUID.randomUUID();
        agent.singleURLCrawl(new CrawlTask(taskId, input[0], ".*" + input[1] + ".*", 0));
        Thread.sleep(1000);
        Map<String, String> result = agent.retrieveResult(taskId);
        if (result == null || result.size() == 0) {
          System.out.println("No result found");
        } else {
          for (String URL : result.keySet()) {
            System.out.println(URL);
            System.out.println(result.get(URL));
            System.out.println("\n");
          }
        }
      } catch (Exception ex) {
        ex.printStackTrace();
      }



    }
  }
}
