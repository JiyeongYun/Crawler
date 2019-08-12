package isel;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class JQLManager {
	private String projectKey;
	private static final String JQL_FRAGMENT1 = "project = ";
	private static final String JQL_FRAGMENT2 = " AND issuetype = Bug AND resolution = fixed ";
	private static final String JQL_FRAGMENT3 = " AND issueKey < ";

	// constructor
	public JQLManager(String projectKey) {
		super();
		this.projectKey = projectKey;
	}

	// only project
	public String getJQL1() {
		return JQL_FRAGMENT1 + this.projectKey;
//		return JQL_FRAGMENT1 + this.projectKey + JQL_FRAGMENT2;
	}

	// project + option using issueKey
	public String getJQL2(String issueKey) {
		return JQL_FRAGMENT1 + this.projectKey + JQL_FRAGMENT3 + issueKey;
	}

	public String getEncodedJQL(String jql) throws UnsupportedEncodingException {
		return URLEncoder.encode(jql, "UTF-8");
	}
}
