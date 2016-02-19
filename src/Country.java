import processing.core.PImage;
import processing.core.PVector;

public class Country {

	private String name;
	private PVector flag_position;
	private PImage flag_img;

	public Country() {

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public PVector getFlag_position() {
		return flag_position;
	}

	public void setFlag_position(PVector flag_position) {
		this.flag_position = flag_position;
	}

	public PImage getFlag_img() {
		return flag_img;
	}

	public void setFlag_img(PImage flag_img) {
		this.flag_img = flag_img;
	}

}
