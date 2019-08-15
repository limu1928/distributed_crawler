import java.io.Serializable;

public class CrawlResult implements Serializable{
	CrawlTask crawlTask;
	String text;
	/**public CrawlTask getCrawlTask() {
		return crawlTask;
	}
	public void setCrawlTask(CrawlTask crawlTask) {
		this.crawlTask = crawlTask;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}**/
	public CrawlResult(CrawlTask crawlTask, String text) {
		super();
		this.crawlTask = crawlTask;
		this.text = text;
	}
	public CrawlResult() {
		super();
	}
	public CrawlResult(CrawlTask crawlTask) {
		super();
		this.crawlTask = crawlTask;
	}
	public CrawlResult(String text) {
		super();
		this.text = text;
	}
	
}