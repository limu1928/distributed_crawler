import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class WebScraper {
  private Set<String> visitedLinks;
  private List<String> articles;
  private int maxDepth;

  public WebScraper(int depth) {
    this.visitedLinks = new HashSet<>();
    articles = new ArrayList<>();
    this.maxDepth = depth;
  }

  public void crawl(String URL) {
    getPageLinks(URL, 0);
  }

  private void getPageLinks(String URL, int depth) {
    boolean keepCrawl = false;
    synchronized (visitedLinks) {
      if (!visitedLinks.contains(URL) && depth <= this.maxDepth) {
        System.out.println(URL);
        keepCrawl = true;
        visitedLinks.add(URL);
      }
    }
    if (keepCrawl) {
      try {
        Document doc = Jsoup.connect(URL).get();
        Elements childLinks = doc.select("a[href]");
        for (Element page : childLinks) {
          getPageLinks(page.attr("abs:href"), depth+1);
        }

      } catch (Exception ex) {
        System.out.print(URL + ": " + ex.getMessage());
      }
    }
  }


  public void getArticles(String pattern) {
    for (String link : visitedLinks) {
      try {
        Document doc = Jsoup.connect(link).get();
        if (doc.text().matches(pattern)) {
          this.articles.add(doc.text());
          System.out.println(doc.text().substring(0,Math.min(doc.text().length(), 50)) + "..." + "   " + link);
        }
      } catch (Exception ex) {
        System.out.println(link + ": " + ex.getMessage());
      }
    }
  }

  public static void main(String[] args) {
    String keyword = "Sean Lew";
    WebScraper scraper = new WebScraper(1);
    scraper.crawl("https://kiddancers.fandom.com/wiki/Sean_Lew");
    System.out.println("###################################################");
    scraper.getArticles(".*" + keyword + ".*");
  }

}
