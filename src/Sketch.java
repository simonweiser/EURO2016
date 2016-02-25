import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;
import processing.core.PVector;
import processing.data.Table;
import processing.video.Movie;

public class Sketch extends PApplet {

	private PImage background, logo, map, onButton, offButton;
	private MercatorMap mercatorMap;
	float FLAG_SIZE;
	private ArrayList<Country> countries, countrySorted;
	private Country selectedCountry, selectedCountryDetail;
	private int drawSceneNum, currentFrameCount;
	private boolean groupFilterActive = false;

	private Movie splashMovie;
	private float movieDuration;
	private double startTime, endTime;

	private float fs16;
	private float fs20;
	private float fs24;
	private float fs32;
	private float fs48;

	public void setup() {

		smooth();

		PFont f = loadFont("res/font/BlairMdITCTT-Medium-48.vlw");
		textFont(f, 48);
		fs16 = height / 50f;
		fs20 = height / 40f;
		fs24 = height / 33.33f;
		fs32 = height / 25f;
		fs48 = height / 16.66f;

		FLAG_SIZE = height / 30f;

		map = loadImage("res/img/map.png");
		map.resize(width, height);
		mercatorMap = new MercatorMap(width, height, 67.25f, 33.1376f, -30.7617f, 59.9414f);

		logo = loadImage("res/img/em2016_logo.png");
		logo.resize(width / 13, 0);

		onButton = loadImage("res/img/onButton.png");
		offButton = loadImage("res/img/offButton.png");
		onButton.resize(logo.width - 10, 0);
		offButton.resize(logo.width - 10, 0);

		background = loadImage("res/img/background_stadium.png");
		background.resize(width, height);
		background.filter(BLUR, 6);

		drawSceneNum = 1;
		selectedCountry = null;
		selectedCountryDetail = null;
		currentFrameCount = 0;

		createCountries();

		// splashMovie = new Movie(this, "res/data/splashVideo.mp4");
		// splashMovie.play();
		// movieDuration = splashMovie.duration() + 2f;
		startTime = System.currentTimeMillis() / 1000.0;

	}

	public void settings() {
		fullScreen();
	}

	public void draw() {
		switch (drawSceneNum) {
		case 0:
			updateScene0(mouseX, mouseY);
			break;

		case 1:
			updateScene1(mouseX, mouseY);
			break;

		case 2:
			updateScene2(mouseX, mouseY);
			break;

		case 3:
			updateScene3(mouseX, mouseY);
			break;

		case 4:
			updateScene4(mouseX, mouseY);
			break;

		default:
			println("ERROR: DRAW");
			updateScene1(mouseX, mouseY);
			break;
		}
	}

	private void reset() {

		FLAG_SIZE = height / 30f;

		image(map, 0, 0);
		image(logo, 10, height - logo.height - 15);

		drawSceneNum = 1;
		selectedCountry = null;
		selectedCountryDetail = null;

		// reset countries
		for (Country country : countries) {
			country.setFLAG_SIZE(FLAG_SIZE);
			country.setFlag_position(country.getInitialFlagPos().copy());
			country.setMouseOver(false);
			country.setMouseOverDetail(false);
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

		float xRect = width - (logo.width + 15f);
		float yRect = 15;
		float xButton = (logo.width / 2 + xRect) - onButton.width / 2;
		float yButton = logo.height * 0.4f + yRect;

		if (overButton((int) xButton, (int) yButton, onButton.width, onButton.height)) {
			groupFilterActive = !groupFilterActive;
		}

		if (mouseButton == LEFT && selectedCountry != null) {
			switch (drawSceneNum) {
			case 1:
				countrySorted = sortCountriesByPlayed();
				drawSceneNum = 2;
				break;

			case 2:
				if (selectedCountryDetail != null) {
					selectedCountryDetail.setGoalsForCounter(0);
					selectedCountryDetail.setGoalsAggainstCounter(0);
					currentFrameCount = frameCount;
					drawSceneNum = 3;
				}
				if (selectedCountry.isMouseOverCenter()) {
					drawSceneNum = 4;
				}
				break;

			case 3:
				// no click interaction
				break;

			case 4:
				// no click interaction
				break;

			default:
				println("ERROR: MOUSECLICKED");
				break;
			}

		} else {
			switch (drawSceneNum) {
			case 1:
				break;

			case 2:
				reset();
				break;

			case 3:
				drawSceneNum = 2;
				break;

			case 4:
				drawSceneNum = 2;
				break;

			default:
				println("ERROR: MOUSECLICKED");
				break;
			}
		}
	}

	/**
	 * SplashScreen
	 */
	private void updateScene0(int mouseX, int mouseY) {
		endTime = System.currentTimeMillis() / 1000.0;

		if (endTime - startTime > movieDuration) {
			drawSceneNum = 1;
		} else {
			background(0);
			image(splashMovie, 0, 0);
			// image(splashMovie, width / 2 - 400, height / 2 - 300, 800, 600);
		}
	}

	/**
	 * Called every time a new frame is available to read
	 */
	public void movieEvent(Movie m) {
		m.read();
	}

	/**
	 * Karte
	 */
	private void updateScene1(int x, int y) {
		boolean isMouseOverCountry = false;
		image(map, 0, 0);

		float xRect = width - (logo.width + 15f);
		float yRect = 15;
		float xButton = (logo.width / 2 + xRect) - onButton.width / 2;
		float yButton = logo.height * 0.4f + yRect;
		float xText = logo.width / 2 + xRect;
		float yText = logo.height * 0.2f + yRect;

		noStroke();
		fill(200, 200, 200, 100);
		rect(xRect, yRect, logo.width, logo.height, 7);
		textSize(fs16);
		textAlign(CENTER);
		fill(0);
		text("group\nfilter", xText, yText);

		image(logo, 15, height - (logo.height + 15));

		if (groupFilterActive) {
			image(onButton, xButton, yButton);
			for (Country country : countries) {
				if (overCountry(country.getFlag_position().x, country.getFlag_position().y, FLAG_SIZE)) {
					country.setMouseOver(true);
					selectedCountry = country;
					isMouseOverCountry = true;
					text("Group\n" + selectedCountry.getGroup(), xText, yText + logo.height * 0.6f);
				} else {
					country.setMouseOver(false);
				}

				if (selectedCountry == null) {
					country.display();
				} else {
					for (Country opponent : countries) {
						if (opponent.getGroup().equals(selectedCountry.getGroup())) {
							opponent.display();
						}
					}
				}
			}
		} else {
			text("All\nGroups", xText, yText + (logo.height * 0.6f));
			image(offButton, xButton, yButton);
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
		}

		if (isMouseOverCountry == false) {
			selectedCountry = null;
		}

	}

	/**
	 * Head2Head
	 */
	private void updateScene2(int mouseX, int mouseY) {
		image(background, 0, 0);

		boolean isMouseOverCountry = false;

		if (groupFilterActive) {

			textSize(fs48);
			fill(255);
			textAlign(CENTER);
			text("Group " + selectedCountry.getGroup(), width / 2, 50);

			float i = 0;

			for (Country opponent : countrySorted) {

				if (opponent.getGroup().equals(selectedCountry.getGroup())) {

					if (!opponent.isMouseOver()) {
						if (overCountry(opponent.getFlag_position().x, opponent.getFlag_position().y, FLAG_SIZE * 2)) {
							opponent.setMouseOverDetail(true);
							selectedCountryDetail = opponent;
							isMouseOverCountry = true;
						} else {
							opponent.setMouseOverDetail(false);
						}

						opponent.displayDetailRadial(i, selectedCountry, false, 0);
						i++;
					}
				}
			}
		} else {

			float i = 0;
			for (Country country : countrySorted) {

				if (!country.isMouseOver()) {
					if (overCountry(country.getFlag_position().x, country.getFlag_position().y, FLAG_SIZE * 2)) {
						country.setMouseOverDetail(true);
						selectedCountryDetail = country;
						isMouseOverCountry = true;
					} else {
						country.setMouseOverDetail(false);
					}

					country.displayDetailRadial(i, selectedCountry, false, 0);
					i++;
				}
			}

		}

		if (isMouseOverCountry == false) {
			selectedCountryDetail = null;
		}

		if (overCountry(selectedCountry.getFlag_position().x + selectedCountry.getFLAG_SIZE() / 2,
				selectedCountry.getFlag_position().y + selectedCountry.getFLAG_SIZE() / 2,
				selectedCountry.getFLAG_SIZE())) {
			selectedCountry.setMouseOverCenter(true);
		} else {
			selectedCountry.setMouseOverCenter(false);
		}

		selectedCountry.displayDetailCenter();
	}

	/**
	 * VS
	 */
	private void updateScene3(int mouseX, int mouseY) {
		image(background, 0, 0);

		selectedCountryDetail.displayDetailRadial(13, selectedCountry, true, currentFrameCount);
		selectedCountry.displayDetailCenter();
	}

	/**
	 * Team Info
	 */
	private void updateScene4(int mouseX, int mouseY) {
		image(background, 0, 0);

		selectedCountry.displayDetailInfo();
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

	boolean overButton(int x, int y, int width, int height) {
		if (mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height) {
			return true;
		} else {
			return false;
		}
	}

	private void createCountries() {
		countries = new ArrayList<Country>();

		createCountryAndAddtoList("Albania", 41.153332f, 20.168331f, "A");
		createCountryAndAddtoList("Austria", 47.516231f, 14.550072f, "F");
		createCountryAndAddtoList("Belgium", 50.760212f, 4.662849f, "E");
		createCountryAndAddtoList("Croatia", 45.1f, 15.2f, "D");
		createCountryAndAddtoList("Czech Republic", 49.817492f, 15.472962f, "D");
		createCountryAndAddtoList("England", 53.253391f, -1.106438f, "B");
		createCountryAndAddtoList("France", 47.345067f, 2.266766f, "A");
		createCountryAndAddtoList("Germany", 51.165691f, 10.451526f, "C");
		createCountryAndAddtoList("Hungary", 47.162494f, 19.503304f, "F");
		createCountryAndAddtoList("Iceland", 64.842535f, -18.319874f, "F");
		createCountryAndAddtoList("Italy", 43.003939f, 12.542688f, "E");
		createCountryAndAddtoList("Northern Ireland", 54.747088f, -6.892629f, "C");
		createCountryAndAddtoList("Poland", 51.919438f, 19.145136f, "C");
		createCountryAndAddtoList("Portugal", 39.399872f, -8.224454f, "F");
		createCountryAndAddtoList("Republic of Ireland", 53.216973f, -7.976798f, "E");
		createCountryAndAddtoList("Romania", 45.943161f, 24.96676f, "A");
		createCountryAndAddtoList("Russia", 56.569141f, 39.998701f, "B");
		createCountryAndAddtoList("Slovakia", 48.881070f, 19.735445f, "B");
		createCountryAndAddtoList("Spain", 40.463667f, -3.74922f, "D");
		createCountryAndAddtoList("Sweden", 62.812541f, 14.823145f, "E");
		createCountryAndAddtoList("Switzerland", 46.818188f, 8.227512f, "A");
		createCountryAndAddtoList("Turkey", 38.963745f, 35.243322f, "D");
		createCountryAndAddtoList("Ukraine", 49.789843f, 31.195906f, "C");
		createCountryAndAddtoList("Wales", 52.144527f, -3.981143f, "B");
	}

	private void createCountryAndAddtoList(String name, float flag_position_x, float flag_position_y, String group) {
		PVector screenLoc = mercatorMap.getScreenLocation(new PVector(flag_position_x, flag_position_y));
		PImage flag_img = loadImage("res/img/nationalflaggen/" + name.toLowerCase() + ".png");
		PImage hover_img = loadImage("res/img/nationalflaggen/" + name.toLowerCase() + ".png");
		hover_img.filter(POSTERIZE, 2);
		PImage team_logo = loadImage("res/img/teamlogos/" + name.toLowerCase() + ".png");
		Table h2hData = loadTable("res/data/h2h_alltime/h2h_" + name.toLowerCase() + "_alltime.csv", "header");
		Table countryInfo = loadTable("res/data/team_info/team_info_" + name.toLowerCase() + ".csv", "header");
		Table players = loadTable("res/data/players/playersCSV/players_" + name.toLowerCase() + ".csv", "header");
		Country country = new Country(this, FLAG_SIZE, name, screenLoc, flag_img, hover_img, team_logo, false, h2hData,
				countryInfo, players, group);
		countries.add(country);
	}

	public static void main(String args[]) {
		PApplet.main(new String[] { "Sketch" });
	}

}