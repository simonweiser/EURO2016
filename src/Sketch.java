import java.util.ArrayList;
import java.util.List;

import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;
import processing.data.Table;
import processing.data.TableRow;

public class Sketch extends PApplet {

	PImage logo;
	PImage map;
	MercatorMap mercatorMap;
	float FLAG_SIZE;

	List<Country> countries;
	Country selectedCountry;

	boolean detailView;

	public void setup() {
		FLAG_SIZE = height / 30f;

		map = loadImage("res/img/map.png");
		image(map, 0, 0, width, height);
		mercatorMap = new MercatorMap(width, height, 67.25f, 33.1376f, -30.7617f, 59.9414f);

		logo = loadImage("res/img/em2016_logo.png");
		image(logo, 10, height - 160, 110, 146);

		detailView = false;
		selectedCountry = null;

		createCountries();
	}

	public void settings() {
		fullScreen();
	}

	public void draw() {
		if (detailView) {
			updateDetailView();
		} else {
			updateCountries(mouseX, mouseY);
		}
	}

	private void updateDetailView() {
		background(200);
		image(logo, 10, height - 160, 110, 146);

		float i = 0;
		for (Country country : countries) {
			if (!country.isMouseOver()) {
				country.displayDetailRadial(i, selectedCountry.getFlag_position(), selectedCountry.getFLAG_SIZE());
				i++;
			}
		}

		selectedCountry.displayDetailCenter();
	}

	public void mouseClicked() {
		if (mouseButton == LEFT) {
			if (selectedCountry != null) {
				detailView = true;
			}
		} else {
			setup();
		}
	}

	private void updateCountries(int x, int y) {

		boolean isMouseOverCountry = false;

		for (Country country : countries) {
			if (overCountry(country.getFlag_position().x, country.getFlag_position().y, FLAG_SIZE)) {
				country.setMouseOver(true);
				selectedCountry = country;
				isMouseOverCountry = true;
			} else {
				country.setMouseOver(false);
			}
			country.display();
		}

		if (isMouseOverCountry == false) {
			selectedCountry = null;
		}

	}

	private boolean overCountry(float x, float y, float diameter) {
		float disX = x - mouseX;
		float disY = y - mouseY;
		if (sqrt(sq(disX) + sq(disY)) < diameter / 2) {
			return true;
		} else {
			return false;
		}
	}

	private void createCountries() {
		countries = new ArrayList<Country>();

		createCountryAndAddtoList("Albania", 41.153332f, 20.168331f);
		createCountryAndAddtoList("Austria", 47.516231f, 14.550072f);
		createCountryAndAddtoList("Belgium", 50.760212f, 4.662849f);
		createCountryAndAddtoList("Croatia", 45.1f, 15.2f);
		createCountryAndAddtoList("Czech Republic", 49.817492f, 15.472962f);
		createCountryAndAddtoList("England", 53.253391f, -1.106438f);
		createCountryAndAddtoList("France", 47.345067f, 2.266766f);
		createCountryAndAddtoList("Germany", 51.165691f, 10.451526f);
		createCountryAndAddtoList("Hungary", 47.162494f, 19.503304f);
		createCountryAndAddtoList("Iceland", 64.842535f, -18.319874f);
		createCountryAndAddtoList("Italy", 43.003939f, 12.542688f);
		createCountryAndAddtoList("Northern Ireland", 54.747088f, -6.892629f);
		createCountryAndAddtoList("Poland", 51.919438f, 19.145136f);
		createCountryAndAddtoList("Portugal", 39.399872f, -8.224454f);
		createCountryAndAddtoList("Republic of Ireland", 53.216973f, -7.976798f);
		createCountryAndAddtoList("Romania", 45.943161f, 24.96676f);
		createCountryAndAddtoList("Russia", 56.569141f, 39.998701f);
		createCountryAndAddtoList("Slovakia", 48.881070f, 19.735445f);
		createCountryAndAddtoList("Spain", 40.463667f, -3.74922f);
		createCountryAndAddtoList("Sweden", 62.812541f, 14.823145f);
		createCountryAndAddtoList("Switzerland", 46.818188f, 8.227512f);
		createCountryAndAddtoList("Turkey", 38.963745f, 35.243322f);
		createCountryAndAddtoList("Ukraine", 49.789843f, 31.195906f);
		createCountryAndAddtoList("Wales", 52.144527f, -3.981143f);
	}

	private void createCountryAndAddtoList(String name, float flag_position_x, float flag_position_y) {
		PVector screenLoc = mercatorMap.getScreenLocation(new PVector(flag_position_x, flag_position_y));
		PImage flag_img = loadImage("res/img/nationalflaggen/" + name.toLowerCase() + ".png");
		PImage hover_img = loadImage("res/img/nationalflaggen/" + name.toLowerCase() + ".png");
		hover_img.filter(POSTERIZE, 2);

		// opponent, played, won, draw, lost, goals_for, goals_aggainst
		Table h2hData = loadTable("res/data/h2h_alltime/h2h_" + name.toLowerCase() + "_alltime.csv", "header");
		Country country = new Country(this, FLAG_SIZE, name, screenLoc, flag_img, hover_img, false, h2hData);
		countries.add(country);

		for (TableRow row : h2hData.rows()) {

			String opponent = row.getString("opponent");
			int played = row.getInt("played");
			int won = row.getInt("won");
			int draw = row.getInt("draw");
			int lost = row.getInt("lost");
			int goalsFor = row.getInt("goals_for");
			int goalsAggainst = row.getInt("goals_aggainst");

			println(name + " --> opponent: " + opponent + " // played: " + played + " // won: " + won + " // draw: "
					+ draw + " // lost: " + lost + " // goalsFor: " + goalsFor + " // goalsAggainst: " + goalsAggainst);
		}
	}

	public static void main(String args[]) {
		PApplet.main(new String[] { "Sketch" });
	}

}