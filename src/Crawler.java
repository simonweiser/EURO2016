import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Crawler {

	public static void crawl() {

		try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
			String base1 = "https://api.import.io/store/connector/a6416af3-7d55-4bd1-9a99-910524a8a7d2/_query?";
			String url1 = "input=webpage/url:http://www.11v11.com/teams/";
			String url2 = "/tab/stats/season/";
			String base2 = "/&&_apikey=73e4c58d37b7446a98c6a2e1d7fc0834897107cfb748bf64f5e6eda3278655ea018869a951e7ccace6d0093e9a3965fc988966c3c493e6e6328e10db6601af3e933fce15fe717ba3c7791109c186f1a7";

			String country = "germany";
			String year = "1986";

			String request = base1 + url1 + country + url2 + year + base2;

			HttpGet getRequest = new HttpGet(request);
			getRequest.addHeader("accept", "application/json");

			HttpResponse response = httpClient.execute(getRequest);

			if (response.getStatusLine().getStatusCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + response.getStatusLine().getStatusCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));

			StringBuffer result = new StringBuffer();
			String output;
			while ((output = br.readLine()) != null) {
				result.append(output);
			}

			try {
				JSONObject obj = new JSONObject(result.toString());
				// String pageName = obj.getJSONObject("results").getString("pageName");

				JSONArray arr = obj.getJSONArray("results");
				for (int i = 0; i < arr.length(); i++) {
					String opponent = arr.getJSONObject(i).getString("opponent");
					String played = arr.getJSONObject(i).getString("played");
					String won = arr.getJSONObject(i).getString("won");
					String draw = arr.getJSONObject(i).getString("draw");
					String lost = arr.getJSONObject(i).getString("lost");
					String goals_for = arr.getJSONObject(i).getString("goals_for");
					String goals_aggainst = arr.getJSONObject(i).getString("goals_aggainst");
					System.out.println(opponent + "played: " + played + "won: " + won + "draw: " + draw + "lost: " + lost + "goalsfor: " + goals_for + "goalsagainst: " + goals_aggainst);
				}
			} catch (JSONException e1) {
				e1.printStackTrace();
			}

		} catch (ClientProtocolException e) {

			e.printStackTrace();

		} catch (IOException e) {

			e.printStackTrace();
		}

	}

}