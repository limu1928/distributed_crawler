import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ScraperAgent implements CrawlerService  {
  private int scraperId;
  private CrawlerService[] agents;
  private WebScraper scraper;


  private static final int[] PORTS = {4000,4001, 4002, 4003, 4004};
  private BlockingQueue<CrawlTask> queue;
//  TODO:
//  private Databse database;


  public ScraperAgent(int id) {
    this.scraperId = id;
    this.scraper = new WebScraper();
    this.queue = new ArrayBlockingQueue(1000);
    this.agents = new CrawlerService[PORTS.length];
    //TODO:
    //this.database = new Database;
  }

  public void configure() {
    System.out.println("Agent" + scraperId + " is launching...");
    try {
      CrawlerService stub = (CrawlerService) UnicastRemoteObject.exportObject(this, 0);
      Registry registry = LocateRegistry.createRegistry(PORTS[this.scraperId]);
      registry.rebind("scraperAgent" + scraperId, stub);
      int ind = 0;
      while(ind < PORTS.length) {
        try {
          Registry tempRegistry = LocateRegistry.getRegistry(PORTS[ind]);
          CrawlerService agent = (CrawlerService) tempRegistry.lookup("scraperAgent" + ind);
          this.agents[ind] = agent;
          ind ++;
        } catch (Exception ex) {
        }
      }

      /** TODO: find the databse for rmi
       * code here
       */


      for (int i = 0; i < 5; i++) {
        new Thread(new Runnable() {
          @Override
          public void run() {
            while(true) {
              try {
                CrawlTask task = queue.take();
                String text = scraper.getArticles(task.URL, task.keyword);
                if (text != null) {
                  //TODO: store in the databse
                  // database.KeyValueStore(task.taskId, task.URL, text);
                }
                Map<Integer, List<CrawlTask>> map =
                    scraper.getPageLinks(task.taskId, task.URL, task.keyword, task.depth);
                dispatch(map);
              } catch (InterruptedException ex) {
                break;
              }
            }
          }
        }).start();

      }
      System.out.println("Agent" + scraperId + " is running!");

    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  @Override
  public void singleURLCrawl(CrawlTask task) throws RemoteException {
    int targetId = task.URL.hashCode() % 5;
    if (targetId == this.scraperId) this.queue.add(task);
    else this.agents[targetId].singleURLCrawl(task);
  }

  @Override
  public void batchCrawl(List<CrawlTask> tasks) throws RemoteException {
    for (CrawlTask task : tasks) {
      this.queue.add(task);
    }
  }

  @Override
  public Map<String, String> retrieveResult(UUID taskId) throws RemoteException {
    //TODO: call the database to retrieve the result for request with given taskId
    return  null;
  }

  private void dispatch(Map<Integer, List<CrawlTask>> map) {
    try {
      for (int id : map.keySet()) {
        this.agents[id].batchCrawl(map.get(id));
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    }

  }

  public static void main(String[] args) {
    int ind = Integer.parseInt(args[0]);
    ScraperAgent agent = new ScraperAgent(ind);
    agent.configure();

  }
}
