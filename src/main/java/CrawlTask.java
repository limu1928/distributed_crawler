import java.io.Serializable;
import java.util.UUID;

public class CrawlTask implements Serializable {
  UUID taskId;
  String URL;
  String keyword;
  int depth;

  public CrawlTask(UUID taskId, String URL, String keyword, int depth) {
    this.taskId = taskId;
    this.URL = URL;
    this.keyword = keyword;
    this.depth = depth;
  }
  
  public CrawlTask(UUID taskId, String URL, String keyword) {
	    this.taskId = taskId;
	    this.URL = URL;
	    this.keyword = keyword;
	  }

}
