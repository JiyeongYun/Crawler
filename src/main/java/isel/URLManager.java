package isel;

public class URLManager {
	private String domain;
	
	private static final String URL_FRAGMENT1 = "https://";
	private static final String URL_FRAGMENT2 = "/issues/?jql=";
//	private static final String URL_FRAGMENT3 = "/browse/";
	
	public URLManager(String domain) {
		super();
		this.domain = domain;
	}
	
	public String getURL(String encodedJql) {
		return URL_FRAGMENT1 + this.domain + URL_FRAGMENT2 + encodedJql;
	}

}
