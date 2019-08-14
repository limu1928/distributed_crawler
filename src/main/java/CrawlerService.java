import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface CrawlerService extends Remote {
  void singleURLCrawl(CrawlTask task) throws RemoteException;
  void batchCrawl(List<CrawlTask> tasks) throws RemoteException;
  Map<String, String> retrieveResult(UUID taskId) throws RemoteException;
}
