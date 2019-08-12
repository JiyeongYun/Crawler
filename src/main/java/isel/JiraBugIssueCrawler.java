package isel;

import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

public class JiraBugIssueCrawler {

	private String domain;
	private String projectKey;
	private String encodedJql;
	private String linkUrl;
	private boolean isSucceed;
	private Connection.Response response;

	public JQLManager jqlManager;
	public URLManager urlManager;
	public JSONManager jsonManager;
	public SeleniumManager seleniumManager;

	public static ArrayList<String> issueKeyList = new ArrayList<>();
	private static boolean invalidProjectKeyChecker = true;
	private static int disconnectionCausedByInvalidProjectKeyCount = 0;

//	public JiraBugIssueCrawler(String domain) throws InvalidDomainException {		
//		this.domain = validateDomain(domain);
//	}

	public JiraBugIssueCrawler(String domain, String projectKey) throws InvalidDomainException {
		this.domain = validateDomain(domain);
		this.projectKey = projectKey;
	}

	public void run() throws Exception {
		jsonManager = new JSONManager();
		seleniumManager = new SeleniumManager();
		seleniumManager.init();
		jqlManager = new JQLManager(this.projectKey);
		urlManager = new URLManager(this.domain);
		encodedJql = jqlManager.getEncodedJQL(jqlManager.getJQL1());
		linkUrl = urlManager.getURL(encodedJql);
		response = getResponse(linkUrl);
		isSucceed = requestSucceed(response.statusCode());

		issueKeyList.clear();

		while (isSucceed) {
			jsonManager.sliceJson(linkUrl);
			jsonManager.getIssueKey();

			if (jsonManager.getIssueTotalNum() <= 1000) {
				break;
			}

			// set the search date

//			encodedJql = jqlManager.getEncodedJQL(jqlManager.getJQL2(getPeriod()));
//			linkUrl = urlManager.getURL(encodedJql);
//			response = getResponse(linkUrl);
//			isSucceed = requestSucceed(response.statusCode());

			encodedJql = jqlManager.getEncodedJQL(jqlManager.getJQL2(issueKeyList.get(issueKeyList.size() - 1)));
			linkUrl = urlManager.getURL(encodedJql);
			response = getResponse(linkUrl);
			isSucceed = requestSucceed(response.statusCode());

		}

//		for (String str : issueKeyList) {
//			System.out.println("issueKey : " + str);
//		}
		System.out.println("Done !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");

	}

	private String getPeriod() throws Exception {

		String key = issueKeyList.get(issueKeyList.size() - 1);
		encodedJql = jqlManager.getEncodedJQL(jqlManager.getJQL2(key));
		linkUrl = urlManager.getURL(encodedJql);
		response = getResponse(linkUrl);
		isSucceed = requestSucceed(response.statusCode());

		if (isSucceed) {
			return seleniumManager.getDate(linkUrl);
		}

		return null;
	}

	private void offInvalidProjectKeyChecking() {
		invalidProjectKeyChecker = false;
	}

	private static String validateDomain(String domain) throws InvalidDomainException {
		String str = domain;
		String domainRegex = "(?:[a-z0-9](?:[a-z0-9-]{0,61}[a-z0-9])?\\.)+[a-z0-9][a-z0-9-]{0,61}[a-z0-9]";

		if (!str.matches(domainRegex)) {
			throw new InvalidDomainException();
		}

		if (str.equals("issues.apache.org")) {// Apache has /jira in the back.
			str = str.concat("/jira");
		}

		return str;
	}

	private static Connection.Response getResponse(String url) throws IOException {
		System.out.println("\nConnecting " + url + "...");
		return Jsoup.connect(url).maxBodySize(0).timeout(600000).ignoreHttpErrors(true).execute();
	}

	private static boolean requestSucceed(int statusCode) {
		return (statusCode / 100 == 2); // status code 2xx means that request has been succeeded.
	}

}
