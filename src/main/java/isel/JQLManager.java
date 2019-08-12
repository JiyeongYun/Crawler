package isel;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class JQLManager {
	private String projectKey;

	private static final String JQL_FRAGMENT1 = "project = ";
	private static final String JQL_FRAGMENT1_2 = " AND issuetype = Bug AND resolution = fixed ";
	private static final String JQL_FRAGMENT2 = " AND issueKey < ";
	private static final String JQL_FRAGMENT3 = " AND issueKey <= '";
	private static final String JQL_FRAGMENT4 = "'";

	//constructor
	public JQLManager(String projectKey) {
		super();
		this.projectKey = projectKey;
	}

	//only project
	public String getJQL1() {
		return JQL_FRAGMENT1 + this.projectKey;
	}

	// project + option
	public String getJQL2(String issueKey) {
		return JQL_FRAGMENT1 + this.projectKey + JQL_FRAGMENT2 + issueKey;
	}
	
//	// project + period
//	public String getJQL3(String issueKey) {
//		return JQL_FRAGMENT1 + this.projectKey + JQL_FRAGMENT3 + start + JQL_FRAGMENT4;
//	}
	
	//project + option + period
	public String getJQL4(int start) {
		return JQL_FRAGMENT1 + this.projectKey + JQL_FRAGMENT2 + JQL_FRAGMENT3 + start + JQL_FRAGMENT4;
	}

	public String getEncodedJQL(String jql) throws UnsupportedEncodingException {
		return URLEncoder.encode(jql, "UTF-8");
	}
}
