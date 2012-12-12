package ss.linearlogic.christmascrashers.world;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

import ss.linearlogic.christmascrashers.ChristmasCrashers;
import ss.linearlogic.christmascrashers.engine.GLGuru;
import ss.linearlogic.christmascrashers.engine.RenderMonkey;
import ss.linearlogic.christmascrashers.object.Object;
import ss.linearlogic.christmascrashers.object.ObjectType;

/**
 * Levels are what the player moves through and contain a 2D array of blocks and entities, with which the player
 * interacts while travelling around the level. All levels are fixed size, namely 640 by 320 tiles.
 * <p>Levels contain waypoints, including teleport checkpoint/respawn locations,
 * and are bundled together in groups called {@link World Worlds}.
 * 
 * @author LinearLogic
 * @since 0.2.1
 */
public class Level {

	/**
	 * The width, in tiles, of the level
	 */
	public static final int WIDTH = 64;

	/**
	 * The height, in tiles, of the level
	 */
	public static final int HEIGHT = 32;

	/**
	 * The level's unique integer ID value, which servers as a means of differentiating it from other levels in the
	 * same world. The level's ID is also featured in the name of the level's data file.
	 */
	private int ID;

	/**
	 * The ID of the world the level belongs to
	 */
	private int worldID;

	/**
	 * A 2D Array of all the in-game {@link Object objects}
	 */
	private Object[][] objects = new Object[WIDTH][HEIGHT];

	/**
	 * Constructor - creates a Level object with the given world name and ID. The level's contents are not loaded separately, by
	 * calling the {@link #load()} method (if the level is being made from scratch in the Level Editor, this call will not be made).
	 * 
	 * @param worldID The ID of the world the level will belong to
	 * @param ID The level's unique {@link #ID} value
	 */
	public Level(int worldID, int ID) {
		this.worldID = worldID;
		this.ID = ID;
		for (int i = 0; i < WIDTH; i++) {
			for (int j = 0; j < HEIGHT; j++) {
				objects[i][j] = new Object(this, i, j, ObjectType.AIR);
			}
		}
	}

	/**
	 * Renders the level, drawing all the object tiles within the current camera view
	 */
	public void draw() {
		int leftBound = (int) Math.floor(GLGuru.getXDisplacement() / Object.TILE_SIZE);
		int rightBound = (int) Math.ceil((GLGuru.getXDisplacement() + ChristmasCrashers.getWindowWidth()) / Object.TILE_SIZE);
		int bottomBound = (int) Math.floor(GLGuru.getYDisplacement() / Object.TILE_SIZE);
		int topBound = (int) Math.ceil((GLGuru.getYDisplacement() + ChristmasCrashers.getWindowHeight()) / Object.TILE_SIZE);
		
		// ArrayIndexOutOfBoundsException prevention
		if (leftBound < 0)
			leftBound = 0;
		if (rightBound >= WIDTH)
			rightBound = WIDTH;
		if (bottomBound < 0)
			bottomBound = 0;
		if (topBound >= HEIGHT)
			topBound = HEIGHT;
		for (int i = leftBound; i < rightBound; i++)
			for (int j = bottomBound; j < topBound; j++)
				if(objects[i][j].getType().getTexture() != null)
					RenderMonkey.renderTexturedRectangle(i * Object.TILE_SIZE, j * Object.TILE_SIZE, Object.TILE_SIZE, Object.TILE_SIZE, objects[i][j].getType().getTexture());
	}

	/**
	 * Loads the level file (creating it if it doesn't exist) and reads from it to load the level's 2D array of blocks and entities.
	 */
	public void load() {
		if (ChristmasCrashers.isDebugModeEnabled())
			System.out.println("Loading level " + ID + " in world " + worldID + ".");
		File dataFile = new File(getDiskLocation());
		if (!dataFile.exists()) {
			if (ChristmasCrashers.isDebugModeEnabled())
				System.out.println("Data file for level " + ID + " in world " + worldID + " does not exist - creating it now.");
			dataFile.mkdirs();
			try {
				dataFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
				System.err.println("Failed to save level " + ID + " in world" + worldID + " - could not create data file.");
				return;
			}
		}
		Scanner sc = null;
		try {
			sc = new Scanner(new File(getDiskLocation()));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		if (sc == null)
			return;
		if (!sc.hasNextLine()) {
			if (ChristmasCrashers.isDebugModeEnabled())
				System.out.println("[Warning] The data file for level " + ID + " in world + " + worldID + " is empty.");
			return;
		}
		for (int j = HEIGHT - 1; j >= 0; j--) { // i and j appear in an inverted order to ensure that the data file reflects the objects as they appear in the rendered level
			if (!sc.hasNextLine())
				break;
			String dataLine = sc.nextLine();
			for (int i = 0; i < WIDTH; i++) {
				if (i >= dataLine.length())
					break;
				ObjectType type = ObjectType.getTypeFromDataChar(dataLine.charAt(i));
				if (type.equals(ObjectType.AIR))
					continue;
				objects[i][j] = new Object(this, i, j, type);
			}
		}
	}

	/**
	 * Saves the level's 2D Array of {@link Object objects} and entities to the level's data file, creating it if it doesn't exist.
	 */
	public void save() {
		if (ChristmasCrashers.isDebugModeEnabled())
			System.out.println("Saving level " + ID + " in world " + worldID + ".");
		File dataFile = new File(getDiskLocation());
		if (!dataFile.exists()) {
			System.out.println("Data file for level " + ID + " in world " + worldID + " does not exist - creating it now.");
			dataFile.mkdirs();
			try {
				dataFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
				System.err.println("Failed to save level " + ID + " in world" + worldID + " - could not create data file.");
				return;
			}
		}
		FileWriter fw = null;
		try {
			fw = new FileWriter(getDiskLocation());
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (fw == null)
			return;
		PrintWriter pw = new PrintWriter(fw);
		for (int j = HEIGHT - 1; j >= 0; j--) { // See comment in load() method about the order of i and j being inverted
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < WIDTH; i++)
				sb.append(objects[i][j].getType().getDataChar());
			pw.println(sb.toString());
		}
		pw.close();
		try {
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @return The {@link ID} value of the level
	 */
	public int getID() {
		return ID;
	}

	/**
	 * @param ID The new {@link ID} value of the level
	 */
	public void setID(int ID) {
		this.ID = ID;
	}

	/**
	 * @return The ID of the world the level belongs to
	 */
	public int getWorldID() {
		return worldID;
	}

	/**
	 * @return The {@link #diskLocation} of the level's data file
	 */
	public String getDiskLocation() {
		return "files" + File.separator + "worlds" + File.separator + "world" + worldID + File.separator + "level" + ID + ".ll";
	}
}
