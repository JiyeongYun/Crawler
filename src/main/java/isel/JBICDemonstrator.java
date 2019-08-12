package isel;

import java.io.IOException;

import org.apache.commons.cli.ParseException;

public class JBICDemonstrator {
	private String domain;
	private String projectKey;

	public static void main(String[] args) {
		JBICDemonstrator jbicDemonstrator = new JBICDemonstrator();

		try {
			jbicDemonstrator.run(args);
		} catch (ParseException e) {
			System.err.println("\nParsing failed.\n\tReason - " + e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidDomainException e) {
			e.printStackTrace();
		} catch (InvalidProjectKeyException e) {
			e.printStackTrace();
		}
	}

	private void run(String[] args)
			throws ParseException, IOException, InvalidDomainException, InvalidProjectKeyException {

		// args[0] : domain
		// args[1] : projectKey
		this.domain = args[0];
		this.projectKey = args[1];

		JiraBugIssueCrawler jiraBugIssueCrawler = new JiraBugIssueCrawler(this.domain, this.projectKey);
		try {
			jiraBugIssueCrawler.run();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
