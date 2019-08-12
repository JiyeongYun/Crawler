package isel;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

import org.apache.commons.io.FileUtils;

public class FileManager {
	private String path;
	private String domain;
	private String projectKey;
	private ArrayList<String> issueKeyList = new ArrayList<>();

	public FileManager(String path, String domain, String projectKey, ArrayList<String> issueKeyList) {
		super();
		this.path = path;
		this.domain = domain;
		this.projectKey = projectKey;
		this.issueKeyList = issueKeyList;
	}

	private static String validateTeamName(String domain) {
		String[] elements = domain.split("\\.");
		return (elements.length == 3) ? domain.substring(domain.indexOf('.') + 1, domain.lastIndexOf('.')) : domain; // TeamName is between . marks in domain.
	}

	public void storeCSVFile() throws IOException {

		String issueKeysWithComma = String.join("\n", issueKeyList);

		// Set File Name
		Date date = new Date();
		Timestamp timeStamp = new Timestamp(date.getTime());
		String teamName = validateTeamName(this.domain);
		String directory = this.path + File.separator + teamName + this.projectKey + File.separator;
		String savedFileName = directory + teamName + this.projectKey + "IssueKeys" + timeStamp + ".csv";

		System.out.println("\n\tCollecting Issue keys into " + savedFileName);

		// Make file
		File savedFile = new File(savedFileName);
		savedFile.getParentFile().mkdirs();
		FileUtils.write(savedFile, issueKeysWithComma, "UTF-8");

		System.out.println("\tCollecting completed.");
	}

}
