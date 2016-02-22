import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;
import processing.data.Table;
import processing.data.TableRow;

public class Country {

	private static final float SPEED = 0.03f;
	private PApplet parent;
	private float FLAG_SIZE;
	private String name;
	private PVector flag_position;
	private PImage flag_img, hover_img, team_logo;
	private boolean mouseOver;
	private boolean mouseOverDetail;
	private boolean mouseOverCenter = false;

	// opponent, played, won, draw, lost, goals_for, goals_aggainst
	private Table h2hData, countryInfo;

	float TARGET_FLAG_SIZE_CENTER;
	float TARGET_FLAG_SIZE_RADIAL;
	float TEAM_INFO_ELLIPSE_SIZE;

	PImage wmCupIcon;
	PImage emCupIcon;

	public Country(PApplet parent, float FLAG_SIZE, String name, PVector flag_position, PImage flag_img,
			PImage hover_img, PImage team_logo, boolean mouseOver, Table h2hData, Table countryInfo) {
		this.parent = parent;
		this.FLAG_SIZE = FLAG_SIZE;
		this.name = name;
		this.flag_position = flag_position;
		this.flag_img = flag_img;
		this.hover_img = hover_img;
		this.setTeam_logo(team_logo);
		this.mouseOver = mouseOver;
		this.h2hData = h2hData;
		this.countryInfo = countryInfo;

		this.setMouseOverDetail(false);

		TARGET_FLAG_SIZE_CENTER = FLAG_SIZE * 6f;
		TARGET_FLAG_SIZE_RADIAL = FLAG_SIZE;
		TEAM_INFO_ELLIPSE_SIZE = FLAG_SIZE * 20f;

		wmCupIcon = parent.loadImage("res/img/wm_cup.png");
		wmCupIcon.resize(50, 50);
		emCupIcon = parent.loadImage("res/img/em_cup.png");
		emCupIcon.resize(50, 50);
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

	public Table getH2hData() {
		return h2hData;
	}

	public void setH2hData(Table h2hData) {
		this.h2hData = h2hData;
	}

	public Table getCountryInfo() {
		return countryInfo;
	}

	public void setCountryInfo(Table countryInfo) {
		this.countryInfo = countryInfo;
	}

	public boolean isMouseOverDetail() {
		return mouseOverDetail;
	}

	public void setMouseOverDetail(boolean mouseOverDetail) {
		this.mouseOverDetail = mouseOverDetail;
	}

	public boolean isMouseOverCenter() {
		return mouseOverCenter;
	}

	public void setMouseOverCenter(boolean mouseOverCenter) {
		this.mouseOverCenter = mouseOverCenter;
	}

	public PImage getTeam_logo() {
		return team_logo;
	}

	public void setTeam_logo(PImage team_logo) {
		this.team_logo = team_logo;
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
		FLAG_SIZE += ds * SPEED;

		float targetX = parent.width / 2 - TARGET_FLAG_SIZE_CENTER / 2;
		float dx = targetX - flag_position.x;
		flag_position.x += dx * SPEED;

		float targetY = parent.height / 2 - TARGET_FLAG_SIZE_CENTER / 2;
		float dy = targetY - flag_position.y;
		flag_position.y += dy * SPEED;

		if (mouseOverCenter) {
			parent.image(hover_img, flag_position.x, flag_position.y, FLAG_SIZE, FLAG_SIZE);
		} else {
			parent.image(flag_img, flag_position.x, flag_position.y, FLAG_SIZE, FLAG_SIZE);
		}

	}

	public void displayDetailRadial(float i, Country selectedCountry, boolean showDetail) {
		PVector centerFlagPosition = selectedCountry.getFlag_position();
		float centerFlagSize = selectedCountry.getFLAG_SIZE();

		int played = 0;
		int won = 0;
		int draw = 0;
		int lost = 0;
		int goalsAggainst = 0;
		int goalsFor = 0;

		int minPlayed = 0;
		int maxPlayed = 0;

		for (TableRow row : h2hData.rows()) {
			String opponentName = row.getString("opponent");
			if (selectedCountry.getName().equals(opponentName)) {
				played = row.getInt("played");
				won = row.getInt("lost");
				draw = row.getInt("draw");
				lost = row.getInt("won");
				goalsAggainst = row.getInt("goals_for");
				goalsFor = row.getInt("goals_aggainst");

				for (TableRow selectedCountryRow : selectedCountry.getH2hData().rows()) {
					int currentPlayed = selectedCountryRow.getInt("played");
					if (currentPlayed > maxPlayed) {
						maxPlayed = currentPlayed;
					}
				}
			}
		}

		float red = 0f;
		float green = 0f;
		float blue = 0f;

		// COLOR
		if (won > lost) {
			red = 0f;
			green = 255f;
			blue = 0f;
		} else if (won < lost) {
			red = 255f;
			green = 0f;
			blue = 0f;
		} else {
			red = 0f;
			green = 0f;
			blue = 0f;
		}

		float x1 = flag_position.x;
		float y1 = flag_position.y;
		float x2 = centerFlagPosition.x + centerFlagSize / 2;
		float y2 = centerFlagPosition.y + centerFlagSize / 2;

		// DRAW LINE
		if (played != 0 && !showDetail) {
			float sw = PApplet.map(played, maxPlayed, 0, 45, 360);
			parent.strokeWeight(parent.height / sw);
			parent.stroke(red, green, blue);
			parent.line(x1, y1, x2, y2);
		}

		// DRAW DETAIL LINES AND TEXT
		if (played != 0 && showDetail) {
			showDetail(x1, y1, x2, y2, played, won, draw, lost, selectedCountry);
		} else if (played == 0 && showDetail) {
			showDetailNoMatchesPlayedYet(selectedCountry);
		}

		// RADIUS
		float cx = parent.width / 2;
		float cy = parent.height / 2;
		// float r = parent.height / 2.5f;
		float r = PApplet.map(played, maxPlayed, 0, parent.height / 4f, parent.height / 2.5f);

		float ds = TARGET_FLAG_SIZE_RADIAL - FLAG_SIZE;
		FLAG_SIZE += ds * SPEED;

		float targetX = cx + r * PApplet.cos(PApplet.radians((float) (i * (360f / 23f))));
		float dx = targetX - flag_position.x;
		flag_position.x += dx * SPEED;

		float targetY = cy + r * PApplet.sin(PApplet.radians((float) (i * (360f / 23f))));
		float dy = targetY - flag_position.y;
		flag_position.y += dy * SPEED;

		// DRAW RADIAL FLAG
		if (mouseOverDetail && !showDetail) {
			parent.image(hover_img, flag_position.x - FLAG_SIZE, flag_position.y - FLAG_SIZE, FLAG_SIZE * 2,
					FLAG_SIZE * 2);
		} else {
			parent.image(flag_img, flag_position.x - FLAG_SIZE, flag_position.y - FLAG_SIZE, FLAG_SIZE * 2,
					FLAG_SIZE * 2);
		}

	}

	private void showDetail(float x1, float y1, float x2, float y2, int played, int won, int draw, int lost,
			Country selectedCountry) {
		if (won > 0) {
			// win line
			parent.noFill();
			float sw = PApplet.map(won, 0, played, 0, 20);
			parent.strokeWeight(sw);
			parent.stroke(0, 255, 0);
			parent.bezier(x1, y1, x1, 0, x2, 0, x2, y2);
		}

		if (draw > 0) {
			// draw line
			parent.noFill();
			float sw = PApplet.map(draw, 0, played, 0, 20);
			parent.strokeWeight(sw);
			parent.stroke(0, 0, 0);
			parent.line(x1, y1, x2, y2);
		}

		if (lost > 0) {
			// lost line
			parent.noFill();
			float sw = PApplet.map(lost, 0, played, 0, 20);
			parent.strokeWeight(sw);
			parent.strokeWeight(sw);
			parent.stroke(255, 0, 0);
			parent.bezier(x1, y1, x1, parent.height, x2, parent.height, x2, y2);

		}

		// vs. text
		parent.fill(255);
		parent.textAlign(PApplet.CENTER);
		parent.textSize(32);
		parent.text(selectedCountry.getName(), parent.width / 4, 50);
		parent.textSize(48);
		parent.text("vs.", parent.width / 2, 50);
		parent.textSize(32);
		parent.text(name, (parent.width / 4) * 3, 50);

		// win text
		parent.textAlign(PApplet.LEFT);
		parent.textSize(32);
		parent.fill(0, 255, 0);
		parent.text("won: " + won, x2 + TARGET_FLAG_SIZE_CENTER, y2 - TARGET_FLAG_SIZE_CENTER * 1.5f);

		// draw text
		parent.textSize(32);
		parent.fill(0);
		parent.text("draw: " + draw, x2 + TARGET_FLAG_SIZE_CENTER, y2);

		// lost text
		parent.textSize(32);
		parent.fill(255, 0, 0);
		parent.text("lost: " + lost, x2 + TARGET_FLAG_SIZE_CENTER, y2 + TARGET_FLAG_SIZE_CENTER * 1.5f);

		// played text
		parent.textSize(48);
		parent.fill(255);
		parent.textAlign(PApplet.CENTER);
		parent.text("played: " + played, parent.width / 2, parent.height - 50);

	}

	private void showDetailNoMatchesPlayedYet(Country selectedCountry) {
		// vs. text
		parent.fill(255);
		parent.textAlign(PApplet.CENTER);
		parent.textSize(32);
		parent.text(selectedCountry.getName(), parent.width / 4, 50);
		parent.textSize(48);
		parent.text("vs.", parent.width / 2, 50);
		parent.textSize(32);
		parent.text(name, (parent.width / 4) * 3, 50);

		// played text
		parent.textSize(48);
		parent.fill(255);
		parent.textAlign(PApplet.CENTER);
		parent.text("no matches played yet", parent.width / 2, parent.height - 50);
	}

	public void displayDetailInfo() {

		float ds = TEAM_INFO_ELLIPSE_SIZE - FLAG_SIZE;
		FLAG_SIZE += ds * SPEED;

		float xMid = parent.width / 2;
		float yMid = parent.height / 2;

		parent.fill(200);
		parent.noStroke();
		parent.ellipse(xMid, yMid, FLAG_SIZE, FLAG_SIZE);

		// img-size: 100x130
		parent.image(team_logo, xMid - 50, (yMid / 2.7f) - ds);

		if (ds < 0.6) {

			parent.textAlign(PApplet.CENTER);
			parent.textSize(32);
			parent.fill(0);

			parent.text(name, xMid, yMid / 1.3f);

			String trainer = countryInfo.getString(0, "trainer");
			String rang = countryInfo.getString(0, "rang");
			String age = countryInfo.getString(0, "age");
			String marktwert = countryInfo.getString(0, "marktwert");
			String wm = countryInfo.getString(0, "wm");
			String em = countryInfo.getString(0, "em");

			parent.textSize(24);
			parent.text("Manager: " + trainer + "\n\nRank: " + rang + "\n\nAge (avg.): " + age + "\n\nMarketvalue: "
					+ marktwert, xMid, yMid);

			parent.text(wm, xMid - 40, yMid * 1.5f);
			parent.text(em, xMid + 40, yMid * 1.5f);

			parent.image(wmCupIcon, xMid - 100, yMid * 1.4f);
			parent.image(emCupIcon, xMid + 50, yMid * 1.4f);
		}
	}

}
