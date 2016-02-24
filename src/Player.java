import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;

public class Player {

	private PApplet parent;

	private String playerName;
	private String birthday;
	private String teamName;
	private String position;
	private String value;
	private int number;
	private PImage playerImg;
	private PImage teamImg;

	public Player(PApplet parent, String playerName, String birthday, String teamName, String position, String value,
			int number, PImage playerImg, PImage teamImg) {
		this.parent = parent;
		this.playerName = playerName;
		this.birthday = birthday;
		this.teamName = teamName;
		this.position = position;
		this.value = value;
		this.number = number;
		this.playerImg = playerImg;
		this.teamImg = teamImg;

		this.playerImg.resize(58, 76);

		PGraphics pgraph = this.parent.createGraphics(this.playerImg.width, this.playerImg.height);
		pgraph.beginDraw();
		pgraph.background(0);
		pgraph.fill(255);
		pgraph.noStroke();
		pgraph.smooth();
		pgraph.ellipseMode(PApplet.CENTER);
		pgraph.ellipse(pgraph.width / 2, pgraph.height / 2, this.playerImg.width, this.playerImg.height);
		pgraph.endDraw();
		this.playerImg.mask(pgraph);

	}

	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public PImage getPlayerImg() {
		return playerImg;
	}

	public void setPlayerImg(PImage playerImg) {
		this.playerImg = playerImg;
	}

	public PImage getTeamImg() {
		return teamImg;
	}

	public void setTeamImg(PImage teamImg) {
		this.teamImg = teamImg;
	}

}
