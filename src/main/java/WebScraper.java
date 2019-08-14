import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class WebScraper  {
  private Map<UUID, Set<String>> visitedLinks;
  private final int maxDepth = 1;

  public WebScraper() {
    this.visitedLinks = new HashMap<>();
  }


  public Map<Integer, List<CrawlTask>> getPageLinks(UUID taskId, String URL, String keyword, int depth) {
    boolean keepCrawl = false;
    synchronized (visitedLinks) {
      Set<String> visited = visitedLinks.get(taskId);
      if (visited == null) visitedLinks.put(taskId, new HashSet<>());
      if (!visitedLinks.get(taskId).contains(URL) && depth <= this.maxDepth) {
        System.out.println(URL);
        keepCrawl = true;
        visitedLinks.get(taskId).add(URL);
      }
    }
    Map<Integer, List<CrawlTask>> result = new HashMap<>();
    if (keepCrawl) {
      try {
        Document doc = Jsoup.connect(URL).get();
        Elements childLinks = doc.select("a[href]");
        for (Element page : childLinks) {
          String link = page.attr("abs:href");
          result.computeIfAbsent(link.hashCode() % 5,
              k -> new ArrayList<CrawlTask>()).add(new CrawlTask(taskId, URL, keyword, depth+1));
        }
      } catch (Exception ex) {
        System.out.print(URL + ": " + ex.getMessage());
      }
    }
    return result;
  }


  public String getArticles(String URL, String pattern) {
    String result =  null;
    try {
      Document doc = Jsoup.connect(URL).get();
      if (doc.text().matches(pattern))
        result = doc.text().substring(0,Math.min(doc.text().length(), 50)) + "...";
    } catch (Exception ex) {
      System.out.println(URL + ": " + ex.getMessage());
    }
    return result;
  }

//  public static void main(String[] args) {
//    String keyword = "Sean Lew";
//    WebScraper scraper = new WebScraper();
//    scraper.singleURLCrawl("https://kiddancers.fandom.com/wiki/Sean_Lew");
//    System.out.println("###################################################");
//    scraper.getArticles(".*" + keyword + ".*");
//  }

}
