package isel;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class JSONManager {
	private String json;
	private int issueTotalNum;

	public int getIssueTotalNum() {
		return issueTotalNum;
	}

	public void getIssueKey(String url) throws IOException {

		Document doc = Jsoup.connect(url).timeout(5000).get();

		// Crawling the what you want using Copy Selector
		this.json = doc.select("#content > div.navigator-container > div.navigator-body > div > div > div > div")
				.attr("data-issue-table-model-state");

		// Slice the json
		JsonParser jsonParser = new JsonParser();
		Object object = jsonParser.parse(this.json);
		JsonObject jsonObject = (JsonObject) object;
		JsonObject issueTable = (JsonObject) jsonObject.get("issueTable").getAsJsonObject();
		JsonArray issueKeyJson = (JsonArray) issueTable.get("issueKeys").getAsJsonArray();

		// set the issusTotalNum
		issueTotalNum = issueTable.get("total").getAsInt();

		// input the issueKey in issueKeyList
		for (int i = 0; i < issueKeyJson.size(); i++) {
			JiraBugIssueCrawler.issueKeyList.add(issueKeyJson.get(i).getAsString());
		}

	}

}
