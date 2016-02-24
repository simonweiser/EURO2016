import java.util.LinkedList;

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
	private PVector flag_position, initialFlagPos;
	private PImage flag_img, hover_img, team_logo, field;;
	private boolean mouseOver;
	private boolean mouseOverDetail;
	private boolean mouseOverCenter = false;

	// opponent, played, won, draw, lost, goals_for, goals_aggainst
	private Table h2hData, countryInfo, players;

	float TARGET_FLAG_SIZE_CENTER;
	float TARGET_FLAG_SIZE_RADIAL;
	float TEAM_INFO_ELLIPSE_SIZE;
	float PLAYER_SIZE;

	float fs24;
	float fs32;
	float fs48;

	PImage wmCupIcon;
	PImage emCupIcon;

	LinkedList<Player> playerList;

	PImage football;

	float ballPosX;
	float ballPosY;

	private int goalsForCounter, goalsAggainstCounter;
	private Player selectedPlayer;

	public Country(PApplet parent, float FLAG_SIZE, String name, PVector flag_position, PImage flag_img, PImage hover_img, PImage team_logo, boolean mouseOver, Table h2hData, Table countryInfo, Table players) {
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
		this.players = players;

		this.setInitialFlagPos(flag_position.copy());

		this.setMouseOverDetail(false);

		TARGET_FLAG_SIZE_CENTER = FLAG_SIZE * (parent.height / 133.33f);
		TARGET_FLAG_SIZE_RADIAL = FLAG_SIZE;
		TEAM_INFO_ELLIPSE_SIZE = FLAG_SIZE * (parent.height / 40f);
		PLAYER_SIZE = FLAG_SIZE;

		wmCupIcon = parent.loadImage("res/img/wm_cup.png");
		wmCupIcon.resize(50, 50);
		emCupIcon = parent.loadImage("res/img/em_cup.png");
		emCupIcon.resize(50, 50);

		football = parent.loadImage("res/img/football.png");
		football.resize(20, 20);

		field = parent.loadImage("res/img/field.png");

		fs24 = parent.height / 33.33f;
		fs32 = parent.height / 25f;
		fs48 = parent.height / 16.66f;

		playerList = new LinkedList<Player>();

		for (TableRow row : players.rows()) {
			String playerName = row.getString("name");
			String birthday = row.getString("birthday");
			String teamName = row.getString("team/_alt");
			String position = row.getString("position");
			String value = row.getString("value");
			int number = row.getInt("number");

			PImage playerImg = parent.loadImage("res/data/players/images/player_" + playerName + "_pic.png");
			PImage teamImg = parent.loadImage("res/data/players/images/player_" + playerName + "_team.png");

			Player p = new Player(this.parent, playerName, birthday, teamName, position, value, number, playerImg, teamImg);

			playerList.add(p);
		}

		goalsForCounter = 0;
		goalsAggainstCounter = 0;

		ballPosX = parent.width / 2 - 10;
		ballPosY = parent.height / 2 - 10;
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

	public Table getPlayers() {
		return players;
	}

	public void setPlayers(Table players) {
		this.players = players;
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

	public int getGoalsForCounter() {
		return goalsForCounter;
	}

	public void setGoalsForCounter(int goalsForCounter) {
		this.goalsForCounter = goalsForCounter;
	}

	public int getGoalsAggainstCounter() {
		return goalsAggainstCounter;
	}

	public void setGoalsAggainstCounter(int goalsAggainstCounter) {
		this.goalsAggainstCounter = goalsAggainstCounter;
	}

	public void display() {
		if (mouseOver) {
			parent.image(hover_img, flag_position.x - FLAG_SIZE / 2, flag_position.y - FLAG_SIZE / 2, FLAG_SIZE, FLAG_SIZE);
		} else {
			parent.image(flag_img, flag_position.x - FLAG_SIZE / 2, flag_position.y - FLAG_SIZE / 2, FLAG_SIZE, FLAG_SIZE);
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

	public void displayDetailRadial(float i, Country selectedCountry, boolean showDetail, int currentFrameCount) {
		PVector centerFlagPosition = selectedCountry.getFlag_position();
		float centerFlagSize = selectedCountry.getFLAG_SIZE();

		int played = 0;
		int won = 0;
		int draw = 0;
		int lost = 0;
		int goalsAggainst = 0;
		int goalsFor = 0;

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

			float sw = 0;

			// STROKE WEIGHT
			if (won > lost) {
				if (won == 1)
					sw = 360;
				else
					sw = PApplet.map(won - lost, 1, won, 360, 40);
			} else if (won < lost) {
				if (lost == 1)
					sw = 360;
				else
					sw = PApplet.map(lost - won, 1, lost, 360, 40);
			} else {
				sw = 200;
			}

			parent.strokeWeight(parent.height / sw);
			parent.stroke(red, green, blue);
			parent.line(x1, y1, x2, y2);
		}

		// DRAW DETAIL LINES AND TEXT
		if (played != 0 && showDetail) {
			showDetail(x1, y1, x2, y2, played, won, draw, lost, goalsFor, goalsAggainst, selectedCountry, currentFrameCount);
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
			parent.image(hover_img, flag_position.x - FLAG_SIZE, flag_position.y - FLAG_SIZE, FLAG_SIZE * 2, FLAG_SIZE * 2);
		} else {
			parent.image(flag_img, flag_position.x - FLAG_SIZE, flag_position.y - FLAG_SIZE, FLAG_SIZE * 2, FLAG_SIZE * 2);
		}

	}

	private void showDetail(float x1, float y1, float x2, float y2, int played, int won, int draw, int lost, int goalsFor, int goalsAggainst, Country selectedCountry, int currentFrameCount) {
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
		parent.textSize(fs32);
		parent.text(selectedCountry.getName(), parent.width / 4, 50);
		parent.textSize(fs48);
		parent.text("vs.", parent.width / 2, 50);
		parent.textSize(fs32);
		parent.text(name, (parent.width / 4) * 3, 50);

		// win text
		parent.textAlign(PApplet.LEFT);
		parent.textSize(fs24);
		parent.fill(0, 255, 0);
		parent.text("won: " + won, x2 + TARGET_FLAG_SIZE_CENTER, y2 - TARGET_FLAG_SIZE_CENTER * 1.5f);

		// draw text
		parent.textSize(fs24);
		parent.fill(0);
		parent.text("draw: " + draw, x2 + TARGET_FLAG_SIZE_CENTER, y2);

		// lost text
		parent.textSize(fs24);
		parent.fill(255, 0, 0);
		parent.text("lost: " + lost, x2 + TARGET_FLAG_SIZE_CENTER, y2 + TARGET_FLAG_SIZE_CENTER * 1.3f);

		// goalsFor text
		parent.textAlign(PApplet.LEFT);
		parent.textSize(fs24);
		parent.fill(0, 255, 0);
		parent.text("goals for: " + goalsForCounter, 50, y2 - TARGET_FLAG_SIZE_CENTER * 1.5f);

		// goalsAggainst text
		parent.textSize(fs24);
		parent.fill(255, 0, 0);
		parent.text("goals aggainst: " + goalsAggainstCounter, 50, y2 + TARGET_FLAG_SIZE_CENTER * 1.3f);

		// played text
		parent.textSize(fs48);
		parent.fill(255);
		parent.textAlign(PApplet.CENTER);
		parent.text("played: " + played, parent.width / 2, parent.height - 50);

		// anim goals for
		if ((parent.frameCount - currentFrameCount) > 100) {
			if (goalsForCounter < goalsFor) {
				// if (parent.frameCount % 10 == 0) {
				goalsForCounter++;
				// ballPosX = parent.width / 2 - TARGET_FLAG_SIZE_CENTER - 10;
				// ballPosY = parent.height / 2 - 10;
				// }
				//
				//
				// float targetX = 250;
				// float dx = targetX - ballPosX;
				// ballPosX += dx * 0.1f;
				//
				// float targetY = parent.height / 2 - 50;
				// float dy = targetY - ballPosY;
				// ballPosY += dy * 0.1f;
				//
				// parent.image(football, ballPosX, ballPosY);
				pieChart(1, goalsFor + goalsAggainst);
			} else if (goalsAggainstCounter < goalsAggainst) {
				// anim goals aggainst
				// if (parent.frameCount % 10 == 0) {
				goalsAggainstCounter++;
				// ballPosX = flag_position.x - FLAG_SIZE - 10;
				// ballPosY = flag_position.y - 10;
				// }
				// float targetX = 250;
				// float dx = targetX - ballPosX;
				// ballPosX += dx * 0.1f;
				//
				// float targetY = parent.height / 2;
				// float dy = targetY - ballPosY;
				// ballPosY += dy * 0.1f;
				//
				// parent.image(football, ballPosX, ballPosY);
				pieChart(2, goalsFor + goalsAggainst);
			} else {
				pieChart(2, goalsFor + goalsAggainst);
			}
		}

	}

	void pieChart(int drawChart, float sumOfGoals) {
		int xPos = 200;
		int chartSize = 200;

		float start1 = PApplet.PI;
		float stop1 = ((PApplet.TWO_PI / sumOfGoals) * goalsForCounter) + start1;

		float start2 = stop1;
		float stop2 = ((PApplet.TWO_PI / sumOfGoals) * goalsAggainstCounter) + start2;

		switch (drawChart) {
		// goalsFor
		case 1:
			parent.noStroke();
			parent.fill(0, 255, 0);
			parent.arc(xPos, parent.height / 2, chartSize, chartSize, start1, stop1, PApplet.PIE);
			break;

		// goalsFor and goalsAggainst
		case 2:
			parent.noStroke();
			parent.fill(0, 255, 0);
			parent.arc(xPos, parent.height / 2, chartSize, chartSize, start1, stop1, PApplet.PIE);
			parent.noStroke();
			parent.fill(255, 0, 0);
			parent.arc(xPos, parent.height / 2, chartSize, chartSize, start2, stop2, PApplet.PIE);
			break;

		default:
			System.err.println("ERROR PIECHART");
			break;
		}
	}

	private void showDetailNoMatchesPlayedYet(Country selectedCountry) {
		// vs. text
		parent.fill(255);
		parent.textAlign(PApplet.CENTER);
		parent.textSize(fs32);
		parent.text(selectedCountry.getName(), parent.width / 4, 50);
		parent.textSize(fs48);
		parent.text("vs.", parent.width / 2, 50);
		parent.textSize(fs32);
		parent.text(name, (parent.width / 4) * 3, 50);

		// played text
		parent.textSize(fs48);
		parent.fill(255);
		parent.textAlign(PApplet.CENTER);
		parent.text("no matches played yet", parent.width / 2, parent.height - 50);
	}

	public PVector getInitialFlagPos() {
		return initialFlagPos;
	}

	public void setInitialFlagPos(PVector initialFlagPos) {
		this.initialFlagPos = initialFlagPos;
	}

	public void displayDetailInfo() {

		// Außen

		float xMid = parent.width / 2;
		float yMid = parent.height / 2;

		// Mitte
		float ds = TEAM_INFO_ELLIPSE_SIZE - FLAG_SIZE;
		FLAG_SIZE += ds * SPEED;

		parent.fill(255);
		parent.noStroke();
		parent.ellipse(xMid, yMid, FLAG_SIZE - 1, FLAG_SIZE - 1);

		parent.image(field, xMid - FLAG_SIZE / 2, yMid - FLAG_SIZE / 2, FLAG_SIZE, FLAG_SIZE);

		if (selectedPlayer != null) {
			parent.image(selectedPlayer.getTeamImg(), xMid - selectedPlayer.getTeamImg().width / 2, (yMid / 2.7f) - ds);
		} else {
			// img-size: 100x130
			parent.image(team_logo, xMid - 50, (yMid / 2.7f) - ds);
		}

		if (ds < 0.6) {

			if (selectedPlayer != null) {

				parent.textAlign(PApplet.CENTER);
				parent.textSize(fs32);
				parent.fill(0);
				parent.text(selectedPlayer.getPlayerName(), xMid, yMid / 1.3f);
				parent.textSize(fs24);
				parent.text(selectedPlayer.getTeamName() + "\n\n" + selectedPlayer.getBirthday() + "\n\n" + selectedPlayer.getPosition() + "\n\n" + selectedPlayer.getValue() + "\n\n#" + selectedPlayer.getNumber(), xMid, yMid);

			} else {

				parent.textAlign(PApplet.CENTER);
				parent.textSize(fs32);
				parent.fill(0);
				parent.text(name, xMid, yMid / 1.3f);

				String trainer = countryInfo.getString(0, "trainer");
				String rang = countryInfo.getString(0, "rang");
				String age = countryInfo.getString(0, "age");
				String marktwert = countryInfo.getString(0, "marktwert");
				String wm = countryInfo.getString(0, "wm");
				String em = countryInfo.getString(0, "em");

				parent.textSize(fs24);
				parent.text("Manager: " + trainer + "\n\nFIFA-Rank: " + rang + "\n\nAge (ø): " + age + "\n\nValue (€): " + marktwert + " Mio.", xMid, yMid);

				parent.text(wm, xMid - 40, yMid * 1.5f);
				parent.text(em, xMid + 75, yMid * 1.5f);

				parent.image(wmCupIcon, xMid - 100, yMid * 1.4f);
				parent.image(emCupIcon, xMid + 10, yMid * 1.4f);
			}
		}

		float r = TEAM_INFO_ELLIPSE_SIZE / 2.0f;

		float i = 0;
		boolean showPlayerDetail = false;
		for (Player player : playerList) {

			float pWidth = parent.height / 16;
			float pHeight = parent.height / 12;

			float targetX = xMid + r * PApplet.cos(PApplet.radians((float) (i * (360f / (float) playerList.size()))));
			// float dx = targetX - flag_position.x;
			// flag_position.x += dx * SPEED;

			float targetY = yMid + r * PApplet.sin(PApplet.radians((float) (i * (360f / (float) playerList.size()))));
			// float dy = targetY - flag_position.y;
			// flag_position.y += dy * SPEED;

			parent.image(player.getPlayerImg(), targetX - pWidth / 2, targetY - pHeight / 2, pWidth, pHeight);

			if (overPlayer(targetX, targetY, pWidth)) {
				player.setMouseOverPlayer(true);
				showPlayerDetail = true;
				selectedPlayer = player;
			} else {
				player.setMouseOverPlayer(false);
			}

			if (player.isMouseOverPlayer()) {
				parent.image(player.getHover_img(), targetX - pWidth / 2, targetY - pHeight / 2, pWidth, pHeight);
			}

			i++;
		}

		if (showPlayerDetail == false) {
			selectedPlayer = null;
		}

	}

	private boolean overPlayer(float x, float y, float diameter) {
		float disX = x - parent.mouseX;
		float disY = y - parent.mouseY;
		if (PApplet.sqrt(PApplet.sq(disX) + PApplet.sq(disY)) < diameter / 2) {
			return true;
		} else {
			return false;
		}
	}

}
