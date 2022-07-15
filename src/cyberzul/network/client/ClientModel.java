package cyberzul.network.client;

import cyberzul.model.*;
import cyberzul.model.events.*;
import cyberzul.shared.JsonMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ClientModel extends CommonModel implements ModelStrategy {

    private static final Logger LOGGER = LogManager.getLogger(GameModel.class);
    private boolean loggedIn;
    private ClientNetworkConnection connection;
    private String thisPlayersName;

    public ClientModel() {
        super();
        loggedIn = false;
        this.connection = new ClientNetworkConnection(this);
        connection.start();
    }

    @Override
    public void startGame() {
        connection.send(JsonMessage.START_GAME);
    }

    @Override
    public void restartGame() {
        connection.send(JsonMessage.RESTART_GAME);
    }

    @Override
    public void cancelGame() {
        connection.send(JsonMessage.CANCEL_GAME);
    }

    @Override
    public void replacePlayerByAi(String playerName) {
        connection.send(JsonMessage.REPLACE_PLAYER_BY_AI);
    }

    @Override
    public void notifyTileChosen(String playerName, int indexOfTile, int offeringIndex) {
        connection.send(JsonMessage.notifyTileChosenMessage(indexOfTile, offeringIndex));
    }

    @Override
    public void makeActivePlayerPlaceTile(int rowOfPatternLine) {
        connection.send(JsonMessage.placeTileInPatternLine(rowOfPatternLine));
    }

    @Override
    public void tileFallsDown() {
        connection.send(JsonMessage.placeTileInFloorLine());
    }

    private synchronized ClientNetworkConnection getConnection() {
        return connection;
    }

    /**
     * Add a network connector to this model.
     *
     * @param connection The network connection to be added.
     */
    public synchronized void setConnection(ClientNetworkConnection connection) {
        this.connection = connection;
    }

    public synchronized void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    /**
     * Gets invoked if the client connects with the server. Even before he logs in with his name. He
     * then already gets the information about the names of the players who already joined and saves
     * them in his playerList
     *
     * @param playerNames the names of the players who already joined the server as JSONArray
     */
    public void connected(JSONArray playerNames) {
        try {
            setUpClientPlayersByName(playerNames);
            notifyListeners(new ConnectedWithServerEvent());
        } catch (JSONException jsonException) {
            jsonException.printStackTrace();
        }
    }

    /**
     * Send a login request to the server.
     *
     * @param nickname the chosen nickname of the chat participant.
     */
    public void loginWithName(final String nickname) {
        this.thisPlayersName = nickname;
        getConnection().sendLogin(nickname);
    }

    /**
     * Update the model accordingly when a login attempt is successful. This is afterwards published
     * to the subscribed listeners.
     */
    public void loggedIn() {
        setLoggedIn(true);
        playerList.add(new ClientPlayer(thisPlayersName));
        notifyListeners(new LoggedInEvent());
    }

    /**
     * Update the model accordingly when another user joined.
     *
     * @param nickname the player that joined the server.
     */
    public void userJoined(String nickname) {
        playerList.add(new ClientPlayer(nickname));
        notifyListeners(new UserJoinedEvent(nickname));
    }

    /**
     * Notify the subscribed observers that a login attempt has failed.
     */
    public void loginFailed(String message) {
        setLoggedIn(false);
        notifyListeners(new LoginFailedEvent(message));
    }

    /**
     * Update all information in the model that is needed by the view to start showing the game. Then
     * notify the listeners that the game has started.
     */
    public void handleGameStarted(JSONArray offerings, JSONArray playerNames) throws JSONException {
        updateOfferings(offerings);
        //player list gets updated. Just in case. We didn't run in a bug yet, but the integrity of the
        //order of players in the game is much more important than saving a few Strings in a JSON
        //message.
        setUpClientPlayersByName(playerNames);
        indexOfActivePlayer = 0;
        try {
            ClientPlayer startingPlayer = (ClientPlayer) playerList.get(0);
            startingPlayer.setHasStartingPlayerMarker(true);
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
        this.isGameStarted = true;
        notifyListeners(new GameStartedEvent());
    }

    /**
     * Intitializes the data structure that contains the information about each player.
     *
     * @param playerNames
     */
    private void setUpClientPlayersByName(JSONArray playerNames) throws JSONException {
        ArrayList<Player> playerList = new ArrayList<>();
        for (int i = 0; i < playerNames.length(); i++) {
            ClientPlayer clientPlayer = new ClientPlayer(playerNames.getString(i));
            playerList.add(clientPlayer);
        }
        this.playerList = playerList;
    }

    /**
     * Update the Offerings in the Model.
     *
     * @param offerings
     */
    private void updateOfferings(JSONArray offerings) throws JSONException {
        ArrayList<Offering> returnOfferingsList = new ArrayList<>();

        //update TableCenter
        ArrayList<ModelTile> content = new ArrayList<>();
        JSONArray tableCenterArray = offerings.getJSONArray(0);
        for (int i = 0; i < tableCenterArray.length(); i++) {
            content.add(ModelTile.toTile(tableCenterArray.getString(i)));
        }
        ClientTableCenter clientTableCenter = new ClientTableCenter();
        clientTableCenter.setContent(content);
        returnOfferingsList.add(clientTableCenter);

        //update FactoryDisplays
        for (int i = 1; i < offerings.length(); i++) {
            content = new ArrayList<>();
            JSONArray factoryDisplayArray = offerings.getJSONArray(i);
            for (int j = 0; j < factoryDisplayArray.length(); j++) {
                content.add(ModelTile.toTile(factoryDisplayArray.getString(j)));
            }
            ClientFactoryDisplay clientFactoryDisplay = new ClientFactoryDisplay();
            clientFactoryDisplay.setContent(content);
            returnOfferingsList.add(clientFactoryDisplay);
        }
        this.offerings = returnOfferingsList;
    }

    public void handleNextPlayersTurn(JSONObject object) throws JSONException {
        String nameOfActivePlayer = object.getString(JsonMessage.NAME_OF_ACTIVE_PLAYER_FIELD);
        List<String> playerNamesList = getPlayerNamesList();
        for (int i = 0; i < playerNamesList.size(); i++) {
            if (playerNamesList.get(i).equals(nameOfActivePlayer)) {
                this.indexOfActivePlayer = i;
                break;
            }
        }

        JSONArray offerings = object.getJSONArray(JsonMessage.OFFERINGS_FIELD);
        this.updateOfferings(offerings);

        String nameOfPlayerWhoEndedHisTurn =
                object.getString(JsonMessage.NAME_OF_PLAYER_WHO_ENDED_HIS_TURN_FIELD);

        ClientPlayer playerWhoEndedHisTurn =
                (ClientPlayer) getPlayerByName(nameOfPlayerWhoEndedHisTurn);

        JSONArray newPatternLinesOfPlayerWhoEndedHisTurn =
                object.getJSONArray(JsonMessage.PATTERN_LINES_FIELD);
        updatePatternLines(newPatternLinesOfPlayerWhoEndedHisTurn, playerWhoEndedHisTurn);

        JSONArray newFloorLineOfPlayerWhoEndedHisTurn =
                object.getJSONArray(JsonMessage.FLOOR_LINE_FIELD);
        updateFloorLine(newFloorLineOfPlayerWhoEndedHisTurn, playerWhoEndedHisTurn);

        notifyListeners(new NextPlayersTurnEvent(nameOfActivePlayer, nameOfPlayerWhoEndedHisTurn));
    }

    private void setPlayerWithSPM(int indexOfPlayerWhoShouldGetSPM) {
        try {
            for (Player p : playerList) {
                ClientPlayer clientPlayer = (ClientPlayer) p;
                clientPlayer.setHasStartingPlayerMarker(false);
            }
            ClientPlayer playerWhoShouldGetTheSPM =
                    (ClientPlayer) playerList.get(indexOfPlayerWhoShouldGetSPM);
            playerWhoShouldGetTheSPM.setHasStartingPlayerMarker(true);
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }

    private void updatePatternLines(JSONArray newPatternLines, ClientPlayer player) {
        try {
            ModelTile[][] patternLines =
                    new ModelTile[newPatternLines.length()][newPatternLines.length()];
            for (int row = 0; row < newPatternLines.length(); row++) {
                for (int col = 0; col < newPatternLines.getJSONArray(row).length(); col++) {
                    patternLines[row][col] =
                            ModelTile.toTile(newPatternLines.getJSONArray(row).getString(col));
                }
            }
            player.setPatternLines(patternLines);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void updateWall(JSONArray newWallMessage, ClientPlayer player) {
        try {
            boolean[][] wall =
                    new boolean[newWallMessage.length()][newWallMessage.length()];
            for (int row = 0; row < newWallMessage.length(); row++) {
                for (int col = 0; col < newWallMessage.getJSONArray(row).length(); col++) {
                    if (ModelTile.toTile(newWallMessage.getJSONArray(row).getString(col)) !=
                            ModelTile.EMPTY_TILE) {
                        wall[row][col] = true;
                    }
                }
            }
            player.setWall(wall);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void updatePoints(int newPoints, ClientPlayer player) {
        player.setPoints(newPoints);
    }

    private void updateFloorLine(JSONArray newFloorLine, ClientPlayer player) {
        try {
            ArrayList<ModelTile> floorLine = new ArrayList<>();
            for (int i = 0; i < newFloorLine.length(); i++) {
                floorLine.add(ModelTile.toTile(newFloorLine.getString(i)));
            }
            player.setFloorLine(floorLine);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void handleNotYourTurn() {
        notifyListeners(new NotYourTurnEvent());
    }

    public void handlePlayerHasChosenTile(JSONObject object) {
        try {
            notifyListeners(
                    new PlayerHasChosenTileEvent(object.getString(JsonMessage.NAME_OF_ACTIVE_PLAYER_FIELD)));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void handleNoValidTurnToMake() {
        notifyListeners(new NoValidTurnToMakeEvent());
    }

    public void handleGameNotStartable(String reason) {
        notifyListeners(new GameNotStartableEvent(reason));
    }

    public void handleRoundFinished(JSONObject message) {
        try {
            JSONArray players = message.getJSONArray(JsonMessage.PLAYER_FIELD);
            updatePlayers(players);

            setPlayerWithSPM(message.getInt(JsonMessage.INDEX_OF_PLAYER_WITH_SPM));
            notifyListeners(new RoundFinishedEvent());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void handleGameFinishedEvent(JSONObject message) {
        try {
            JSONArray players = message.getJSONArray(JsonMessage.PLAYER_FIELD);
            updatePlayers(players);
            this.isGameStarted = false;
            notifyListeners(new GameFinishedEvent(message.getString(JsonMessage.NICK_FIELD)));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void updatePlayers(JSONArray players) throws JSONException {
        for (int i = 0; i < players.length(); i++) {
            JSONObject playerObject = players.getJSONObject(i);
            String playerName = playerObject.getString(JsonMessage.NICK_FIELD);
            ClientPlayer clientPlayer = (ClientPlayer) getPlayerByName(playerName);

            updatePatternLines(playerObject.getJSONArray(JsonMessage.PATTERN_LINES_FIELD), clientPlayer);
            updateFloorLine(playerObject.getJSONArray(JsonMessage.FLOOR_LINE_FIELD), clientPlayer);
            updateWall(playerObject.getJSONArray(JsonMessage.WALL_FIELD), clientPlayer);
            updatePoints(playerObject.getInt(JsonMessage.POINTS_FIELD), clientPlayer);
        }
    }

    public String getPlayerName() {
        return this.thisPlayersName;
    }

    public void handleIllegalTurn() {
        notifyListeners(new IllegalTurnEvent());
    }

    public void handleGameCanceled(String playerWhoCanceledGame) {
        this.isGameStarted = false;
        notifyListeners(new GameCanceledEvent(playerWhoCanceledGame));
    }

    public void handleGameForfeited(String playerWhoForfeitedTheGame) {
        notifyListeners(new GameForfeitedEvent(playerWhoForfeitedTheGame));
    }
}


