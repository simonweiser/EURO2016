import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;
import processing.core.PVector;
import processing.data.Table;

public class Sketch extends PApplet {

	PImage background;
	PImage logo;
	PImage map;
	MercatorMap mercatorMap;
	float FLAG_SIZE;

	ArrayList<Country> countries, countrySorted;
	Country selectedCountry, selectedCountryDetail;

	int drawSceneNum;

	public void setup() {

		PFont f = loadFont("res/font/BlairMdITCTT-Medium-48.vlw");
		textFont(f, 48);

		FLAG_SIZE = height / 30f;

		map = loadImage("res/img/map.png");
		image(map, 0, 0, width, height);
		mercatorMap = new MercatorMap(width, height, 67.25f, 33.1376f, -30.7617f, 59.9414f);

		logo = loadImage("res/img/em2016_logo.png");
		image(logo, 10, height - 160, 110, 146);

		background = loadImage("res/img/background_stadium.png");
		background.resize(width, height);
		background.filter(BLUR, 6);

		drawSceneNum = 1;
		selectedCountry = null;
		selectedCountryDetail = null;

		createCountries();

	}

	public void settings() {
		fullScreen();
	}

	public void draw() {
		switch (drawSceneNum) {
		case 1:
			updateScene1(mouseX, mouseY);
			break;

		case 2:
			updateScene2(mouseX, mouseY);
			break;

		case 3:
			updateScene3(mouseX, mouseY);
			break;

		default:
			println("ERROR: DRAW");
			updateScene1(mouseX, mouseY);
			break;
		}

	}

	private ArrayList<Country> sortCountriesByPlayed() {
		ArrayList<Country> countriesCopy = new ArrayList<Country>();
		countriesCopy.addAll(countries);
		countriesCopy.remove(selectedCountry);

		Collections.sort(countriesCopy, new Comparator<Country>() {

			@Override
			public int compare(Country c1, Country c2) {
				int rowIndex1 = c1.getH2hData().findRowIndex(selectedCountry.getName(), "opponent");
				int rowIndex2 = c2.getH2hData().findRowIndex(selectedCountry.getName(), "opponent");
				return c1.getH2hData().getInt(rowIndex1, "played") - c2.getH2hData().getInt(rowIndex2, "played");
			}

		});

		return countriesCopy;

	}

	public void mouseClicked() {
		if (mouseButton == LEFT) {
			if (selectedCountry != null) {
				countrySorted = sortCountriesByPlayed();
				drawSceneNum = 2;
			}
			if (selectedCountry != null && selectedCountryDetail != null) {
				drawSceneNum = 3;
			}
		} else {
			switch (drawSceneNum) {
			case 1:
				break;

			case 2:
				setup();
				break;

			case 3:
				drawSceneNum--;
				break;

			default:
				println("ERROR: MOUSECLICKED");
				break;
			}
		}
	}

	private void updateScene1(int x, int y) {

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

	private void updateScene2(int mouseX, int mouseY) {
		// background(200);
		image(background, 0, 0);
		image(logo, 10, height - 160, 110, 146);

		float i = 0;
		boolean isMouseOverCountry = false;

		for (Country country : countrySorted) {

			if (!country.isMouseOver()) {
				if (overCountry(country.getFlag_position().x, country.getFlag_position().y, FLAG_SIZE * 2)) {
					country.setMouseOverDetail(true);
					selectedCountryDetail = country;
					isMouseOverCountry = true;
				} else {
					country.setMouseOverDetail(false);
				}

				country.displayDetailRadial(i, selectedCountry, false);
				i++;
			}
		}

		if (isMouseOverCountry == false) {
			selectedCountryDetail = null;
		}

		selectedCountry.displayDetailCenter();
	}

	private void updateScene3(int mouseX, int mouseY) {
		// background(200);
		image(background, 0, 0);
		image(logo, 10, height - 160, 110, 146);

		selectedCountryDetail.displayDetailRadial(13, selectedCountry, true);
		selectedCountry.displayDetailCenter();
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
		Table h2hData = loadTable("res/data/h2h_alltime/h2h_" + name.toLowerCase() + "_alltime.csv", "header");
		Country country = new Country(this, FLAG_SIZE, name, screenLoc, flag_img, hover_img, false, h2hData);
		countries.add(country);
	}

	public static void main(String args[]) {
		PApplet.main(new String[] { "Sketch" });
	}

}