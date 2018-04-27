package SQL;
/*
  Class created by Alex Grech IV
  Class provides methods for connecting to and querying the database and other related things
 */


//need to add this as a dependency to make it work
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Set;

import javax.swing.JTable;

import Logic.Color;


public class ConnectDB {
    private static final String BASE_URL = "https://www.uvm.edu/~agrech/dbConnection/";
    private static final String READ_FILE = "Read.php";
    private static final String WRITE_FILE = "Write.php";

    /**
     * This method takes a query and parameters and creates an http request to a php script that will query the database
     * and return a Json encoded array
     * @param query         SQL query that is being sent to database
     * @param params        Parameters that replace '?' in the query
     * @param read          Boolean that tells whether the query will read (true) or write (false) to the database
     * @return              JsonArray that will hold the data return from the query
     * @throws IOException  Exception may occur if unable to open a connection to php page
     */
    private static JsonArray sendQuery(String query, String[] params, boolean read) throws IOException {
        String urlString = ConnectDB.BASE_URL + (read ? ConnectDB.READ_FILE : ConnectDB.WRITE_FILE);
        StringBuilder paramString = new StringBuilder("query=" + URLEncoder.encode(query, "UTF-8"));
        if (params != null) {
            for (String param : params) {
                paramString.append("&param").append(URLEncoder.encode("[]", "UTF-8")).append("=").append(URLEncoder.encode(param, "UTF-8"));
            }
        }
        //Do for Post
        byte[] postData = paramString.toString().getBytes(StandardCharsets.UTF_8);

        System.out.println("URL to test query:");
        System.out.println(urlString + "?" + paramString);
        // Connect to the URL
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        // More Post stuff
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty("Content-Length", String.valueOf(postData.length));
        conn.setRequestProperty("charset", "utf-8");
        conn.setDoOutput(true);
        conn.getOutputStream().write(postData);
        // Open connection
        conn.connect();

        // Convert to a JSON object array to access data
        JsonParser jp = new JsonParser(); //from gson
        JsonElement root = jp.parse(new InputStreamReader((InputStream) conn.getContent())); //Convert the input stream to a json element
        return root.getAsJsonArray();
    }

    /**
     * This method gets the save game data for a particular gameID
     * @param gameID    The ID of the game whose data you want to load
     * @return          A string that contains the data needed to reinitialize a game, returns an empty string if failed
     */
    public static String loadGameData(int gameID) {
        //define query and parameters
        String query = "SELECT fldSaveString FROM tblSaveGame WHERE pmkGameID = ?";
        String params[] = new String[1];
        params[0] = Integer.toString(gameID);

        //send query and get response
        JsonArray dataArr = null;
        try {
            dataArr = sendQuery(query, params, true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String saveString = "";
        if (dataArr != null) {
            saveString = dataArr.get(0).getAsJsonObject().get("0").getAsString();
        }
        return saveString;
    }

    /**
     * This method gets the save game data for a the most recent save
     * @return          A string that contains the data needed to reinitialize a game, returns an empty string if failed
     */
    public static String[] loadGameData() {
        //define query
        String query = "SELECT fldDate, fldSaveString FROM tblSaveGame WHERE fldDate = (SELECT MAX(fldDate) FROM tblSaveGame)";

        //send query and get response
        JsonArray dataArr = null;
        try {
            dataArr = sendQuery(query, null, true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String gameInfo[] = new String[2];
        if (dataArr != null) {
            gameInfo[0] = dataArr.get(0).getAsJsonObject().get("0").getAsString();
            gameInfo[1] = dataArr.get(0).getAsJsonObject().get("1").getAsString();
        }
        return gameInfo;
    }

    /**
     * This method gets the stats for a player from the database
     * @param player    The player that you want stats for
     * @return          A string array that contains the stats for the player. The info in the array follows this format:
     *                  Player name, wins, losses, times red, times green, times blue, times yellow, total bumps
     */
    public static String[][] getPlayerStats(String player) {
        //define query and parameters
        String query = "SELECT * FROM tblPlayer WHERE pmkPlayer = ?";
        String params[] = new String[1];
        params[0] = player;

        //send query and get response
        JsonArray dataArr = null;
        try {
            dataArr = sendQuery(query, params, true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String stats[][] = null;
        if (dataArr != null) {
            stats = new String[dataArr.size() + 1][dataArr.get(0).getAsJsonObject().size()/2];
            Set<String> keyset = dataArr.get(0).getAsJsonObject().keySet();
            int k = 0;
            for (String key : keyset) {
                if (key.length() > 2) {
                    stats[0][k] = key.substring(3);
                    k++;
                }
            }
            for (int i = 0; i < stats[1].length; i++) {
                stats[1][i] = dataArr.get(0).getAsJsonObject().get(Integer.toString(i)).getAsString();
            }
        }
        return stats;
    }

    /**
     * This method gets the stats for a player from the database
     * @return          A string array that contains the stats for all players. The info in the array follows this format:
     *                  Player name, wins, losses, times red, times green, times blue, times yellow, total bumps
     */
    public static String[][] getAllPlayerStats() {
        //define query
        String query = "SELECT * FROM tblPlayer";

        //send query and get response
        JsonArray dataArr = null;
        try {
            dataArr = sendQuery(query, null, true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String stats[][] = null;
        if (dataArr != null) {
            stats = new String[dataArr.size() + 1][dataArr.get(0).getAsJsonObject().size()/2];
            Set<String> keyset = dataArr.get(0).getAsJsonObject().keySet();
            int k = 0;
            for (String key : keyset) {
                if (key.length() > 2) {
                    stats[0][k] = key.substring(3);
                    k++;
                }
            }
            for (int i = 0; i < stats.length - 1; i++) {
                for (int j = 0; j < stats[i].length; j++) {
                    stats[i+1][j] = dataArr.get(i).getAsJsonObject().get(Integer.toString(j)).getAsString();
                }
            }
        }
        return stats;
    }

    /**
     * This method gets the info for a particular game by game ID in the database
     * @param gameID    The ID number for a game
     * @return          A string array that contains the stats for the game. The info in the array follows this format:
     *                  Game ID, player name, date, playtime, number of rounds, player color, winner color,
     *                  AI1 difficulty, AI2 difficulty, AI3 difficulty,
     *                  player number bumped, AI1 number bumped, AI2 number bumped, AI3 number bumped,
     *                  player number start, AI1 number start, AI2 number start, AI3 number start,
     *                  player number home, AI1 number home, AI2 number home, AI3 number home,
     */
    public static String[][] getGameInfo(int gameID) {
        //define query and parameters
        String query = "SELECT * FROM tblGames WHERE pmkGameID = ?";
        String params[] = new String[1];
        params[0] = Integer.toString(gameID);

        //send query and get response
        JsonArray dataArr = null;
        try {
            dataArr = sendQuery(query, params, true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String stats[][] = null;
        if (dataArr != null) {
            stats = new String[dataArr.size() + 1][dataArr.get(0).getAsJsonObject().size()/2];
            Set<String> keyset = dataArr.get(0).getAsJsonObject().keySet();
            int k = 0;
            for (String key : keyset) {
                if (key.length() > 2) {
                    stats[0][k] = key.substring(3);
                    k++;
                }
            }
            for (int i = 0; i < stats[1].length; i++) {
                stats[1][i] = dataArr.get(0).getAsJsonObject().get(Integer.toString(i)).getAsString();
            }
        }
        return stats;
    }

    /**
     * This method gets info for all games stored in the database (currently removed stats involving pawns in home and start
     * @return  An array that contains column headers and all of the info from the database
     */
    public static String[][] getAllGameInfo() {
        //define query
        String query = "SELECT * FROM tblGames";

        //send query and get response
        JsonArray dataArr = null;
        try {
            dataArr = sendQuery(query, null, true);
        } catch (IOException e) {
            e.printStackTrace();
        }


        String stats[][] = null;
        if (dataArr != null) {
            stats = new String[dataArr.size() + 1][dataArr.get(0).getAsJsonObject().size()/2 - 8];
            Set<String> keyset = dataArr.get(0).getAsJsonObject().keySet();
            int k = 0;
            for (String key : keyset) {
                if (key.length() > 2 && k < stats[0].length) {
                    stats[0][k] = key.substring(3);
                    k++;
                }
            }
            for (int i = 0; i < stats.length-1; i++) {
                for (int j = 0; j < stats[i].length; j++) {
                    stats[i+1][j] = dataArr.get(i).getAsJsonObject().get(Integer.toString(j)).getAsString();
                }
            }
        }
        return stats;
    }

    /**
     * This method saves game data into the database
     * @param gameID    The id of the game to be saved
     * @param saveData  The string containing all of the info being saved
     * @return          A boolean representing the success of the query
     */
    public static boolean saveGameData(int gameID, String saveData) {
        //define query and parameters
        String query;
        String params[] = new String[2];
        if (isSaved(gameID)) {
            query = "UPDATE tblSaveGame SET fldDate = NOW(), fldSaveString = ? WHERE pmkGameID = ?";
            params[0] = saveData;
            params[1] = Integer.toString(gameID);
        } else {
            query = "INSERT INTO tblSaveGame (pmkGameID, fldDate, fldSaveString) VALUES (?, NOW(), ?)";
            params[0] = Integer.toString(gameID);
            params[1] = saveData;
        }

        //send query and get response
        JsonArray dataArr = null;
        try {
            dataArr = sendQuery(query, params, false);
        } catch (IOException e) {
            e.printStackTrace();
        }

        boolean success = false;
        if (dataArr != null) {
            success = dataArr.get(0).getAsBoolean();
        }
        return success;
    }

    /**
     * This method inserts game data for a particular game into the database
     * @param player        player name
     * @param playtime      drawStep time
     * @param numRounds     number of rounds
     * @param playerColor   color of the player
     * @param winnerColor   color of the winner
     * @param AI1Diff       difficulty of first AI. Options: "Nice/Smart", "Nice/Dumb", "Cruel/Smart", "Cruel/Dumb"
     * @param AI2Diff       difficulty of second AI. Same as above plus "NULL"
     * @param AI3Diff       difficulty of third AI. Same as above
     * @param playerBumps   number of player bumps
     * @param AI1Bumps      number of bumps for first AI
     * @param AI2Bumps      number of bumps for second AI
     * @param AI3Bumps      number of bumps for third AI
     * @param playerStart   number of pawns in player start
     * @param AI1Start      number of pawns in start for first AI
     * @param AI2Start      number of pawns in start for second AI
     * @param AI3Start      number of pawns in start for third AI
     * @param playerHome    number of pawns in player start
     * @param AI1Home       number of pawns in home for first AI
     * @param AI2Home       number of pawns in home for second AI
     * @param AI3Home       number of pawns in home for third AI
     * @return              The ID of the entry in the database, needed for future updates and retrieval
     */
    public static int insertGameData(String player, int playtime, int numRounds, Color playerColor, Color winnerColor,
                                     String AI1Diff, String AI2Diff, String AI3Diff,
                                     int playerBumps, int AI1Bumps, int AI2Bumps, int AI3Bumps,
                                     int playerStart, int AI1Start, int AI2Start, int AI3Start,
                                     int playerHome, int AI1Home, int AI2Home, int AI3Home) {
        //define query and parameters
        String query = "INSERT INTO tblGames (fnkPlayer, fldDate, fldPlaytime, fldNumRounds, fldPlayerColor, fldWinner, "
                + "fldAI1Diff, fldAI2Diff, fldAI3Diff, "
                + "fldPlayerNumBump, fldAI1NumBump, fldAI2NumBump, fldAI3NumBump, "
                + "fldPlayerNumStart, fldAI1NumStart, fldAI2NumStart, fldAI3NumStart, "
                + "fldPlayerNumHome, fldAI1NumHome, fldAI2NumHome, fldAI3NumHome) "
                + "VALUES (?, DATE(NOW()), ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
        String params[] = new String[20];
        params[0] = player;
        params[1] = Integer.toString(playtime);
        params[2] = Integer.toString(numRounds);
        params[3] = playerColor.toString();
        params[4] = winnerColor.toString();
        params[5] = AI1Diff;
        params[6] = AI2Diff;
        params[7] = AI3Diff;
        params[8] = Integer.toString(playerBumps);
        params[9] = Integer.toString(AI1Bumps);
        params[10] = Integer.toString(AI2Bumps);
        params[11] = Integer.toString(AI3Bumps);
        params[12] = Integer.toString(playerStart);
        params[13] = Integer.toString(AI1Start);
        params[14] = Integer.toString(AI2Start);
        params[15] = Integer.toString(AI3Start);
        params[16] = Integer.toString(playerHome);
        params[17] = Integer.toString(AI1Home);
        params[18] = Integer.toString(AI2Home);
        params[19] = Integer.toString(AI3Home);

        //send query and get response
        JsonArray dataArr = null;
        try {
            dataArr = sendQuery(query, params, false);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //update Player stats
        updatePlayer(player, (playerColor == winnerColor), playerColor, playerBumps);

        //return game ID, -1 if failed
        if (dataArr != null) {
            if (dataArr.get(0).getAsBoolean()) {
                return getLastGameID();
            }
        }
        return -1;
    }

    /**
     * This method updates the entry for a game in the database
     * @param gameID        ID of the game
     * @param playtime      new drawStep time
     * @param numRounds     new number of rounds
     * @param winnerColor   color of the winner
     * @param playerBumps   new number of player bumps
     * @param AI1Bumps      new number of bumps for first AI
     * @param AI2Bumps      new number of bumps for second AI
     * @param AI3Bumps      new number of bumps for third AI
     * @param playerStart   new number of pawns in player start
     * @param AI1Start      new number of pawns in start for first AI
     * @param AI2Start      new number of pawns in start for second AI
     * @param AI3Start      new number of pawns in start for third AI
     * @param playerHome    new number of pawns in player start
     * @param AI1Home       new number of pawns in home for first AI
     * @param AI2Home       new number of pawns in home for second AI
     * @param AI3Home       new number of pawns in home for third AI
     * @return              A boolean representing whether the update succeeded
     */
    public static boolean updateGameData(int gameID, int playtime, int numRounds, Color winnerColor,
                                         int playerBumps, int AI1Bumps, int AI2Bumps, int AI3Bumps,
                                         int playerStart, int AI1Start, int AI2Start, int AI3Start,
                                         int playerHome, int AI1Home, int AI2Home, int AI3Home) {
        //update the player stats
        int origPlayerBump = getPlayerBumps(gameID);
        String player = getPlayerName(gameID);
        Color playerColor = getPlayerColor(gameID);
        boolean didUpdate = updatePlayer(player, (playerColor == winnerColor), Color.NULL, (playerBumps - origPlayerBump));
        if (!didUpdate) {
            System.out.println("Player stats update failed");
        }

        //define query and parameters
        String query = "UPDATE tblGames SET fldPlaytime = ?, fldNumRounds = ?, fldWinner = ?, "
                + "fldPlayerNumBump = ?, fldAI1NumBump = ?, fldAI2NumBump = ?, fldAI3NumBump = ?, "
                + "fldPlayerNumStart = ?, fldAI1NumStart = ?, fldAI2NumStart = ?, fldAI3NumStart = ?, "
                + "fldPlayerNumHome = ?, fldAI1NumHome = ?, fldAI2NumHome = ?, fldAI3NumHome = ? "
                + "WHERE pmkGameID = ?";
        String params[] = new String[16];
        params[0] = Integer.toString(playtime);
        params[1] = Integer.toString(numRounds);
        params[2] = winnerColor.toString();
        params[3] = Integer.toString(playerBumps);
        params[4] = Integer.toString(AI1Bumps);
        params[5] = Integer.toString(AI2Bumps);
        params[6] = Integer.toString(AI3Bumps);
        params[7] = Integer.toString(playerStart);
        params[8] = Integer.toString(AI1Start);
        params[9] = Integer.toString(AI2Start);
        params[10] = Integer.toString(AI3Start);
        params[11] = Integer.toString(playerHome);
        params[12] = Integer.toString(AI1Home);
        params[13] = Integer.toString(AI2Home);
        params[14] = Integer.toString(AI3Home);
        params[15] = Integer.toString(gameID);

        //send query and get response
        JsonArray dataArr = null;
        try {
            dataArr = sendQuery(query, params, false);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (dataArr != null) {
            return dataArr.get(0).getAsBoolean();
        } else {
            System.out.println("Game stat update failed");
            return false;
        }
    }

    /**
     * This method gets the current player bumps stat from the database
     * @param gameID    ID of the game
     * @return          the player bumps
     */
    private static int getPlayerBumps(int gameID) {
        //build query and parameters
        String query = "SELECT fldPlayerNumBump FROM tblGames WHERE pmkGameID = ?";
        String params[] = new String[1];
        params[0] = Integer.toString(gameID);

        //send query and get response
        JsonArray dataArr = null;
        try {
            dataArr = sendQuery(query, params, true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (dataArr != null) {

            return dataArr.get(0).getAsJsonObject().get("0").getAsInt();
        } else {
            return -1;
        }
    }

    /**
     * This method gets the player name for a game from the database
     * @param gameID    ID of the game
     * @return          name of the player for that game
     */
    private static String getPlayerName(int gameID) {
        //build query and parameters
        String query = "SELECT fnkPlayer FROM tblGames WHERE pmkGameID = ?";
        String params[] = new String[1];
        params[0] = Integer.toString(gameID);

        //send query and get response
        JsonArray dataArr = null;
        try {
            dataArr = sendQuery(query, params, true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (dataArr != null) {
            return dataArr.get(0).getAsJsonObject().get("0").getAsString();
        } else {
            return "";
        }
    }

    /**
     * This method gets the player color for a game from the database
     * @param gameID    ID of the game
     * @return          color of the player for that game
     */
    private static Color getPlayerColor(int gameID) {
        //build query and parameters
        String query = "SELECT fldPlayerColor FROM tblGames WHERE pmkGameID = ?";
        String params[] = new String[1];
        params[0] = Integer.toString(gameID);

        //send query and get response
        JsonArray dataArr = null;
        try {
            dataArr = sendQuery(query, params, true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (dataArr != null) {
            switch (dataArr.get(0).getAsJsonObject().get("0").getAsString()) {
                case "RED":
                    return Color.RED;
                case "GREEN":
                    return Color.GREEN;
                case "BLUE":
                    return Color.BLUE;
                case "YELLOW":
                    return Color.YELLOW;
                default:
                    return Color.NULL;
            }
        } else {
            return Color.NULL;
        }
    }

    /**
     * This method inserts/updates stats for a player in the database
     * @param player        name of plater
     * @param didWin        whether or not the player won
     * @param playerColor   the color of the player
     * @param bumps         the number of times the player bumped
     * @return              A boolean representing whether or not the query succeeded
     */
    private static boolean updatePlayer(String player, boolean didWin, Color playerColor, int bumps) {
        //define query and parameters
        String winField = didWin ? "fldWins" : "fldLosses";
        boolean updateColor = true;
        String colorField = "fldTimes";
        switch (playerColor) {
            case RED:
                colorField += "Red";
                break;
            case GREEN:
                colorField += "Green";
                break;
            case BLUE:
                colorField += "Blue";
                break;
            case YELLOW:
                colorField += "Yellow";
                break;
            case NULL:
                colorField = "";
                updateColor = false;
                break;
        }
        String colorUpdate = updateColor ? (colorField + " = " + colorField + " + 1, ") : "";
        String query;
        if (ConnectDB.hasEntry(player)) {
            query = "UPDATE tblPlayer SET " + winField + " = " + winField + " + 1, "
                    + colorUpdate
                    + "fldTotalBumps = fldTotalBumps + " + bumps + " "
                    + "WHERE pmkPlayer = ?";
        } else {
            colorField = updateColor ? (colorField + ", ") : "";
            String colorValue = updateColor ? "1, " : "";
            query = "INSERT INTO tblPlayer (pmkPlayer, " + winField + ", " + colorField + "fldTotalBumps) "
                    + "VALUES (?, 1, " + colorValue + bumps + ")";
        }
        String params[] = new String[1];
        params[0] = player;

        //send query and get response
        JsonArray dataArr = null;
        try {
            dataArr = sendQuery(query, params, false);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (dataArr != null) {
            return dataArr.get(0).getAsBoolean();
        }
        return false;
    }

    /**
     * This method checks to see if a player already has an entry for their stats in the database
     * @param player    name of player
     * @return          whether or not the player has an entry
     */
    private static boolean hasEntry(String player) {
        //define query and parameters
        String query = "SELECT COUNT(1) FROM tblPlayer WHERE pmkPlayer = ?";
        String params[] = new String[1];
        params[0] = player;

        //send query and get response
        JsonArray dataArr = null;
        try {
            dataArr = sendQuery(query, params, true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        boolean hasEntry = false;
        if (dataArr != null) {
            hasEntry = (dataArr.get(0).getAsJsonObject().get("0").getAsInt() == 1);
        }
        return hasEntry;
    }

    /**
     * This method gets the ID of the last entry entered into the table of game stats
     * @return  the ID of the most recent entry
     */
    private static int getLastGameID() {
        //define query
        String query = "SELECT MAX(pmkGameID) FROM tblGames";

        //send query and get response
        JsonArray dataArr = null;
        try {
            dataArr = sendQuery(query, null, true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (dataArr != null) {
            return dataArr.get(0).getAsJsonObject().get("0").getAsInt();
        }
        return -1;
    }

    /**
     * This method checks to see if a game has already been saved
     * @param gameID    ID of the game
     * @return          whether or not the game has been saved
     */
    private static boolean isSaved(int gameID) {
        //define query and parameters
        String query = "SELECT COUNT(1) FROM tblSaveGame WHERE pmkGameID = ?";
        String params[] = new String[1];
        params[0] = Integer.toString(gameID);

        //send query and get response
        JsonArray dataArr = null;
        try {
            dataArr = sendQuery(query, params, true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        boolean isSaved = false;
        if (dataArr != null) {
            isSaved = (dataArr.get(0).getAsJsonObject().get("0").getAsInt() == 1);
        }
        return isSaved;
    }

    /**
     * Converts the 2D array into JTable
     * @param data  2D string array where first row is the column headers
     * @return      JTable from array
     */
    public static JTable getAsJTable(String[][] data) {
        return new JTable(Arrays.copyOfRange(data, 1, data.length), data[0]);
    }
}
