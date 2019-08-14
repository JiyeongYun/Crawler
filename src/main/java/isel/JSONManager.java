package isel;

import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class JSONManager {
	private String json;
	private int issueTotalNum;
	private ArrayList<String> issueKeyList = new ArrayList<>();

	public int getIssueTotalNum() {
		return issueTotalNum;
	}

	public ArrayList<String> getIssueKey() {
		return issueKeyList;
	}

	public ArrayList<String> sliceJson(String url) {

		JsonParser jsonParser = new JsonParser();

		try {
			getJson(url);
			JsonObject object = (JsonObject) jsonParser.parse(json);
			JsonObject issueTable = (JsonObject) object.get("issueTable");
			JsonArray issueKeys = (JsonArray) issueTable.get("issueKeys");

			// set the issusTotalNum
			issueTotalNum = issueTable.get("total").getAsInt();

			// input the issueKey in issueKeyList
			for (int i = 0; i < issueKeys.size(); i++) {
				JiraBugIssueCrawler.issueKeyList.add(issueKeys.get(i).getAsString());
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return issueKeyList;
	}

	private void getJson(String url) throws IOException {

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

		issueKeyList.clear();
		
		// from JsonArray to ArrayList
		for (JsonElement temp : issueKeyJson) {
			issueKeyList.add(temp.getAsString());
		}

	}

}
