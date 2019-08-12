package isel;

import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

public class JiraBugIssueCrawler {

	private String domain;
	private String projectKey;
	private String path;
	
	private String encodedJql;
	private String linkUrl;
	private boolean isSucceed;
	private Connection.Response response;

	public static ArrayList<String> issueKeyList = new ArrayList<>();
//	private static boolean invalidProjectKeyChecker = true;
//	private static int disconnectionCausedByInvalidProjectKeyCount = 0;

//	private static final int MAX_DISCONNECTION = 50; //TODO using mathematical methods to improve
	private static final String DEFAULT_PATH = System.getProperty("user.dir"); //current working directory
	
	public JiraBugIssueCrawler(String domain, String projectKey) throws InvalidDomainException{
		this(domain, projectKey, DEFAULT_PATH);
	}
	
	public JiraBugIssueCrawler(String domain, String projectKey, String path) throws InvalidDomainException {
		this.domain = validateDomain(domain);
		this.projectKey = projectKey;
		this.path = path;
	}

	public void run() throws IOException, InvalidProjectKeyException {
		JQLManager jqlManager = new JQLManager(this.projectKey);
		URLManager urlManager = new URLManager(this.domain);
		
		encodedJql = jqlManager.getEncodedJQL(jqlManager.getJQL1());
		linkUrl = urlManager.getURL(encodedJql);
		response = getResponse(linkUrl);
		isSucceed = requestSucceed(response.statusCode());

		issueKeyList.clear();
		
		while (isSucceed) {
			JSONManager jsonManager = new JSONManager();
			jsonManager.sliceJson(linkUrl);
			jsonManager.getIssueKey();
			
			if (jsonManager.getIssueTotalNum() <= 1000) {
				break;
			}

			// set the search option using issueKey
			encodedJql = jqlManager.getEncodedJQL(jqlManager.getJQL2(issueKeyList.get(issueKeyList.size() - 1)));
			linkUrl = urlManager.getURL(encodedJql);
			response = getResponse(linkUrl);
			isSucceed = requestSucceed(response.statusCode());

		}
		
		FileManager fileManager = new FileManager(this.path, this.domain, this.projectKey, issueKeyList);
		fileManager.storeCSVFile();		
		
	}

//	private void offInvalidProjectKeyChecking() {
//		invalidProjectKeyChecker = false;
//	}

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
