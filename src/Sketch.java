import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;

public class Sketch extends PApplet {

	PImage map, de;
	float FLAG_SIZE;

	public void setup() {
		FLAG_SIZE = height / 30f;
		println("Window-Width: " + width + " // Window-Height: " + height + " // FLAG_SIZE: " + FLAG_SIZE);

		map = loadImage("res/img/map.png");
		de = loadImage("res/img/nationalflaggen/germany.png");

		image(map, 0, 0);

		MercatorMap mercatorMap = new MercatorMap(width, height, 67.2720f, 33.1376f, -30.7617f, 59.9414f);
		PVector pos_de = mercatorMap.getScreenLocation(new PVector(50.87065f, 10.559104f));
		image(de, pos_de.x - FLAG_SIZE / 2, pos_de.y - FLAG_SIZE / 2, FLAG_SIZE, FLAG_SIZE);
	}

	public void settings() {
		fullScreen();
	}

	public void draw() {

	}

	public static void main(String args[]) {
		PApplet.main(new String[] { "Sketch" });
	}

}