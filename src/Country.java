import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;

public class Country {

	private PApplet parent;
	private float FLAG_SIZE;
	private String name;
	private PVector flag_position;
	private PImage flag_img, hover_img;
	private boolean mouseOver;

	float TARGET_FLAG_SIZE_CENTER;
	float TARGET_FLAG_SIZE_RADIAL;

	public Country(PApplet parent, float FLAG_SIZE, String name, PVector flag_position, PImage flag_img,
			PImage hover_img, boolean mouseOver) {
		this.parent = parent;
		this.FLAG_SIZE = FLAG_SIZE;
		this.name = name;
		this.flag_position = flag_position;
		this.flag_img = flag_img;
		this.hover_img = hover_img;
		this.mouseOver = mouseOver;

		TARGET_FLAG_SIZE_CENTER = FLAG_SIZE * 6f;
		TARGET_FLAG_SIZE_RADIAL = FLAG_SIZE * 1.5f;
	}

	public PApplet getParent() {
		return parent;
	}

	public void setParent(PApplet parent) {
		this.parent = parent;
	}

	public float getFLAG_SIZE() {
		return FLAG_SIZE;
	}

	public void setFLAG_SIZE(float FLAG_SIZE) {
		this.FLAG_SIZE = FLAG_SIZE;
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

	public PImage getHover_img() {
		return hover_img;
	}

	public void setHover_img(PImage hover_img) {
		this.hover_img = hover_img;
	}

	public boolean isMouseOver() {
		return mouseOver;
	}

	public void setMouseOver(boolean mouseOver) {
		this.mouseOver = mouseOver;
	}

	public void display() {
		if (mouseOver) {
			parent.image(hover_img, flag_position.x - FLAG_SIZE / 2, flag_position.y - FLAG_SIZE / 2, FLAG_SIZE,
					FLAG_SIZE);
		} else {
			parent.image(flag_img, flag_position.x - FLAG_SIZE / 2, flag_position.y - FLAG_SIZE / 2, FLAG_SIZE,
					FLAG_SIZE);
		}
	}

	public void displayDetailCenter() {
		float ds = TARGET_FLAG_SIZE_CENTER - FLAG_SIZE;
		FLAG_SIZE += ds * 0.05;

		float targetX = parent.width / 2 - TARGET_FLAG_SIZE_CENTER / 2;
		float dx = targetX - flag_position.x;
		flag_position.x += dx * 0.05;

		float targetY = parent.height / 2 - TARGET_FLAG_SIZE_CENTER / 2;
		float dy = targetY - flag_position.y;
		flag_position.y += dy * 0.05;

		parent.image(flag_img, flag_position.x, flag_position.y, FLAG_SIZE, FLAG_SIZE);
	}

	public void displayDetailRadial(float i, PVector centerFlagPosition) {
		float ds = TARGET_FLAG_SIZE_RADIAL - FLAG_SIZE;
		FLAG_SIZE += ds * 0.05;

		float cx = parent.width / 2;
		float cy = parent.height / 2;
		float r = parent.height / 2.5f;

		float targetX = cx + r * PApplet.cos(PApplet.radians((float) (i * (360f / 23f))));
		float dx = targetX - flag_position.x;
		flag_position.x += dx * 0.05;

		float targetY = cy + r * PApplet.sin(PApplet.radians((float) (i * (360f / 23f))));
		float dy = targetY - flag_position.y;
		flag_position.y += dy * 0.05;

		parent.stroke(255, 0, 0);
		parent.line(centerFlagPosition.x + TARGET_FLAG_SIZE_CENTER / 2,
				centerFlagPosition.y + TARGET_FLAG_SIZE_CENTER / 2, flag_position.x, flag_position.y);
		parent.image(flag_img, flag_position.x - FLAG_SIZE, flag_position.y - FLAG_SIZE, FLAG_SIZE * 2, FLAG_SIZE * 2);

	}
}
