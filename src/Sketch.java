import java.util.ArrayList;
import java.util.List;

import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;

public class Sketch extends PApplet {

<<<<<<< Updated upstream
	PImage map;
	MercatorMap mercatorMap;
=======
	PImage map, de, en;
>>>>>>> Stashed changes
	float FLAG_SIZE;
	PVector pos_de;
	boolean countryOver = false;

	List<Country> countries;

	public void setup() {
		FLAG_SIZE = height / 30f;
		println("Window-Width: " + width + " // Window-Height: " + height + " // FLAG_SIZE: " + FLAG_SIZE);

		map = loadImage("res/img/map.png");
		image(map, 0, 0, width, height);

<<<<<<< Updated upstream
		mercatorMap = new MercatorMap(width, height, 67.25f, 33.1376f, -30.7617f, 59.9414f);
		createCountries();
		drawFlags();
=======
		de = loadImage("res/img/nationalflaggen/germany.png");
		en = loadImage("res/img/nationalflaggen/england.png");
		MercatorMap mercatorMap = new MercatorMap(width, height, 67.2720f, 33.1376f, -30.7617f, 59.9414f);
		pos_de = mercatorMap.getScreenLocation(new PVector(50.87065f, 10.559104f));
		image(de, pos_de.x - FLAG_SIZE / 2, pos_de.y - FLAG_SIZE / 2, FLAG_SIZE, FLAG_SIZE);
>>>>>>> Stashed changes
	}

	public void settings() {
		fullScreen();
	}

	public void draw() {
		update(mouseX, mouseY);

		if (countryOver) {
			PImage hover = de.copy();
			hover.filter(POSTERIZE, 2);
			image(hover, pos_de.x - FLAG_SIZE / 2, pos_de.y - FLAG_SIZE / 2, FLAG_SIZE, FLAG_SIZE);
		} else {
			image(de, pos_de.x - FLAG_SIZE / 2, pos_de.y - FLAG_SIZE / 2, FLAG_SIZE, FLAG_SIZE);
		}

	}

	public void mouseClicked() {

		if (countryOver) {
			println("TRUE");
		} else {
			println("FALSE");
		}

	}

	void update(int x, int y) {
		if (overCountry(pos_de.x, pos_de.y, FLAG_SIZE)) {
			countryOver = true;
		} else {
			countryOver = false;
		}
	}

	boolean overCountry(float x, float y, float diameter) {
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
		Country country = new Country();
		country.setName(name);
		country.setFlag_position(mercatorMap.getScreenLocation(new PVector(flag_position_x, flag_position_y)));
		country.setFlag_img(loadImage("res/img/nationalflaggen/" + name.toLowerCase() + ".png"));
		countries.add(country);
	}

	private void drawFlags() {
		for (Country country : countries) {
			image(country.getFlag_img(), country.getFlag_position().x - FLAG_SIZE / 2,
					country.getFlag_position().y - FLAG_SIZE / 2, FLAG_SIZE, FLAG_SIZE);
		}

	}

	public static void main(String args[]) {
		PApplet.main(new String[] { "Sketch" });
	}

}