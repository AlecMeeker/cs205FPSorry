package Main.sql;

//need to add this as a dependency to make it work
import com.google.gson.*;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import Main.src.*;


public class ConnectDB {
    private static final String BASE_URL = "https://www.uvm.edu/~agrech/dbConnection/";
    private static final String READ_FILE = "Read.php";
    private static final String WRITE_FILE =  "Write.php";


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

        System.out.println("URL for the request:");
//        System.out.println(urlString + "?" + paramString); //Get
        System.out.println(urlString); //Post
        // Connect to the URL
//        URL url = new URL(urlString + "?" + paramString); //Get
        URL url = new URL(urlString); //Post
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        // More Post stuff
        conn.setRequestMethod("POST");
        conn.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty("Content-Length", String.valueOf(postData.length));
        conn.setRequestProperty( "charset", "utf-8");
        conn.setDoOutput(true);
        conn.getOutputStream().write(postData);
        // Open connection
        conn.connect();

        // Convert to a JSON object array to access data
        JsonParser jp = new JsonParser(); //from gson
        JsonElement root = jp.parse(new InputStreamReader((InputStream) conn.getContent())); //Convert the input stream to a json element
        return root.getAsJsonArray();
    }


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
        params[0] = player; params[1] = Integer.toString(playtime); params[2] = Integer.toString(numRounds);
        params[3] = playerColor.toString(); params[4] = winnerColor.toString();
        params[5] = AI1Diff; params[6] = AI2Diff; params[7] = AI3Diff;
        params[8] = Integer.toString(playerBumps); params[9] = Integer.toString(AI1Bumps);
        params[10] = Integer.toString(AI2Bumps); params[11] = Integer.toString(AI3Bumps);
        params[12] = Integer.toString(playerStart); params[13] = Integer.toString(AI1Start);
        params[14] = Integer.toString(AI2Start); params[15] = Integer.toString(AI3Start);
        params[16] = Integer.toString(playerHome); params[17] = Integer.toString(AI1Home);
        params[18] = Integer.toString(AI2Home); params[19] = Integer.toString(AI3Home);

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
            if(dataArr.get(0).getAsBoolean()) {
                return getLastGameID();
            }
        }
        return -1;
    }


    public static boolean updateGame(int gameID, int playtime, int numRounds, Color winnerColor,
                                     int playerBumps, int AI1Bumps, int AI2Bumps, int AI3Bumps,
                                     int playerStart, int AI1Start, int AI2Start, int AI3Start,
                                     int playerHome, int AI1Home, int AI2Home, int AI3Home) {
        int origPlayerBump = getPlayerBumps(gameID);
        String player = getPlayerName(gameID);
        Color playerColor = getPlayerColor(gameID);
        boolean didUpdate = updatePlayer(player, (playerColor == winnerColor), Color.NULL,(playerBumps - origPlayerBump));
        if (!didUpdate){
            System.out.println("Player stats update failed");
        }

        //define query and parameters
        String query = "UPDATE tblGames SET fldPlaytime = ?, fldNumRounds = ?, fldWinner = ?, "
                        + "fldPlayerNumBump = ?, fldAI1NumBump = ?, fldAI2NumBump = ?, fldAI3NumBump = ?, "
                        + "fldPlayerNumStart = ?, fldAI1NumStart = ?, fldAI2NumStart = ?, fldAI3NumStart = ?, "
                        + "fldPlayerNumHome = ?, fldAI1NumHome = ?, fldAI2NumHome = ?, fldAI3NumHome = ? "
                        + "WHERE pmkGameID = ?";
        String params[] = new String[16];
        params[0] = Integer.toString(playtime); params[1] = Integer.toString(numRounds); params[2] = winnerColor.toString();
        params[3] = Integer.toString(playerBumps); params[4] = Integer.toString(AI1Bumps);
        params[5] = Integer.toString(AI2Bumps); params[6] = Integer.toString(AI3Bumps);
        params[7] = Integer.toString(playerStart); params[8] = Integer.toString(AI1Start);
        params[9] = Integer.toString(AI2Start); params[10] = Integer.toString(AI3Start);
        params[11] = Integer.toString(playerHome); params[12] = Integer.toString(AI1Home);
        params[13] = Integer.toString(AI2Home); params[14] = Integer.toString(AI3Home);
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
        }
        else {
            System.out.println("Game stat update failed");
            return false;
        }
    }


    public static int getPlayerBumps(int gameID) {
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
        }
        else {
            return -1;
        }
    }

    public static String getPlayerName(int gameID) {
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
        }
        else {
            return "";
        }
    }


    public static Color getPlayerColor(int gameID) {
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
            switch(dataArr.get(0).getAsJsonObject().get("0").getAsString()) {
                case "RED": return Color.RED;
                case "GREEN": return Color.GREEN;
                case "BLUE": return Color.BLUE;
                case "YELLOW": return Color.YELLOW;
                default: return Color.NULL;
            }
        }
        else {
            return Color.NULL;
        }
    }

    public static boolean updatePlayer(String player, boolean didWin, Color playerColor, int bumps) {
        String winField = didWin?"fldWins":"fldLosses";
        boolean updateColor = true;
        String colorField = "fldTimes";
        switch (playerColor){
            case RED: colorField += "Red"; break;
            case GREEN: colorField += "Green"; break;
            case BLUE: colorField += "Blue"; break;
            case YELLOW: colorField += "Yellow"; break;
            case NULL: colorField = ""; updateColor = false; break;
        }
        String colorUpdate = updateColor?(colorField + " = " + colorField + " + 1, "):"";
        String query;
        if (ConnectDB.hasEntry(player)) {
            query = "UPDATE tblPlayer SET " + winField + " = " + winField + " + 1, "
                    + colorUpdate
                    + "fldTotalBumps = fldTotalBumps + " + bumps + " "
                    + "WHERE pmkPlayer = ?";
        } else {
            colorField = updateColor?(colorField + ", "):"";
            String colorValue = updateColor?"1, ":"";
            query = "INSERT INTO tblPlayer (pmkPlayer, " + winField + ", " + colorField + "fldTotalBumps) "
                    + "VALUES (?, 1, " + colorValue + bumps + ")";
        }
        String params[] = new String[1];
        params[0] = player;

        JsonArray dataArr = null;
        try {
            dataArr = sendQuery(query,params,false);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (dataArr != null) {
            return dataArr.get(0).getAsBoolean();
        }
        return false;
    }


    public static boolean hasEntry(String player) {
        //define query and parameters
        String query = "SELECT COUNT(1) FROM tblPlayer WHERE pmkPlayer = ?";
        String params[] = new String[1];
        params[0] = player;

        JsonArray dataArr = null;
        try {
            dataArr = sendQuery(query,params,true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        int hasArray = 0;
        if (dataArr != null) {
            hasArray = dataArr.get(0).getAsJsonObject().get("0").getAsInt();
        }
        return (hasArray == 1);
    }


    public static int getPlayerWins(String player){
        //define query and parameters
        String query = "SELECT tblPlayer.fldWins FROM tblPlayer WHERE tblPlayer.pmkPlayer = ?";
        String params[] = new String[1];
        params[0] = player;

        JsonArray dataArr = null;
        try {
            dataArr = sendQuery(query,params,true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (dataArr != null) {
            return dataArr.get(0).getAsJsonObject().get("fldWins").getAsInt();
        }
        return -1;
    }


    public static int getNumGames(String player) {
        String query = "SELECT fldWins, fldLosses FROM tblPlayer WHERE pmkPlayer = ?";
        String params[] = new String[1];
        params[0] = player;

        JsonArray dataArr = null;
        try {
            dataArr = sendQuery(query, params, true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        int wins = 0;
        int losses = 0;
        if (dataArr != null) {
            wins = dataArr.get(0).getAsJsonObject().get("fldWins").getAsInt();
            losses = dataArr.get(0).getAsJsonObject().get("fldLosses").getAsInt();
        }
        return wins + losses;
    }


    private static int getLastGameID() {
        String query = "SELECT MAX(pmkGameID) FROM tblGames";

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
}
