/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package monopoly;

import java.io.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import objectmodel.Asset;
import objectmodel.CellBase;
import objectmodel.City;
import objectmodel.GameBoard;
import objectmodel.PlayerDetails;
/**
 *
 * @author Benda
 */
public class Utils
{
    public static final String MESSAGE_PLACEHOLDER = "%MESSAGE_PLACEHOLDER%";
    public static final String ERROR_PLACEHOLDER = "%ERROR_PLACEHOLDER%";
    public static final String WAITING_PLACEHOLDER = "%WAIT_PLACEHOLDER%";

    public static final String PLAYER_PLACEHOLDER = "%PLAYER#_PLACEHOLDER%";
    public static final String PLAYER_ID_PLACEHOLDER = "%PLAYER_ID_PLACEHOLDER%";
    public static final String PLAYER_NAME_PLACEHOLDER = "%PLAYER_NAME_PLACEHOLDER%";
    public static final String PLAYER_MONEY_PLACEHOLDER = "%PLAYER_MONEY_PLACEHOLDER%";
    public static final String PLAYER_IN_GAME_PLACEHOLDER = "%PLAYER_IN_GAME_PLACEHOLDER%";
    public static final String PLAYER_POSITION_PLACEHOLDER = "%PLAYER_POSITION_PLACEHOLDER%";
    public static final String PLAYER_IN_JAIL_PLACEHOLDER = "%PLAYER_IN_JAIL_PLACEHOLDER%";

    public static final String CELL_PLACEHOLDER = "%CELL#_PLACEHOLDER%";
    public static final String CELL_ID_PLACEHOLDER = "%CELL_ID_PLACEHOLDER%";
    public static final String CELL_TITLE_PLACEHOLDER = "%CELL_TITLE_PLACEHOLDER%";
    public static final String CELL_IMG_PLACEHOLDER = "%CELL_IMG_PLACEHOLDER%";
    public static final String CELL_HOUSES_PLACEHOLDER = "%CELL_HOUSES_PLACEHOLDER%";
    public static final String CELL_OWNER_PLACEHOLDER = "%CELL_OWNER_PLACEHOLDER%";

    public static final String WINNER_PLACEHOLDER = "%WINNER_PLACEHOLDER%";
    public static final String LOST_MESSAGE_PLACEHOLDER = "%LOST_MESSAGE_PLACEHOLDER%";
    public static final String DICE1_PLACEHOLDER = "%DICE1_PLACEHOLDER%";
    public static final String DICE2_PLACEHOLDER = "%DICE2_PLACEHOLDER%";
    public static final String FROM_CELL_PLACEHOLDER = "%FROM_CELL_PLACEHOLDER%";
    public static final String TO_CELL_PLACEHOLDER = "%TO_CELL_PLACEHOLDER%";

    public static final String ACTION_CREATE = "create";
    public static final String ACTION_JOIN = "join";
    public static final String ACTION_BOARD = "board";
    public static final String ACTION_DICES = "dices";
    public static final String ACTION_BUY = "buy";
    public static final String GET_STATE = "state";
    public static final String RESIGN = "resign";

    public static final int MAX_PLAYERS = 6;
    public static final int MIN_PLAYERS = 2;
    public static final int MAX_HUMANS = 6;
    public static final int MIN_HUMANS = 1;
    public static final int DICE_MAX = 6;
    public static final int DICE_MIN = 1;

    public static final String POSITIVE_BUY_REPLY = "yes";

    public static String convertStreamToString(InputStream is)
            throws IOException {
        /*
         * To convert the InputStream to String we use the
         * Reader.read(char[] buffer) method. We iterate until the
         * Reader return -1 which means there's no more data to
         * read. We use the StringWriter class to produce the string.
         */
        if (is != null) {
            Writer writer = new StringWriter();

            char[] buffer = new char[1024];
            try {
                Reader reader = new BufferedReader(
                        new InputStreamReader(is, "UTF-8"));
                int n;
                while ((n = reader.read(buffer)) != -1) {
                    writer.write(buffer, 0, n);
                }
            } finally {
                is.close();
            }
            return writer.toString();
        } else {
            return "";
        }
    }

    public static String GenerateBoardJSON(GameBoard board, ServletContext context)
    {
        String boardJSON = GetResourceAsString(context, "/json/game_board_init.json");
        String cellDesc = GetResourceAsString(context, "/json/cell_desc.json");
        
        for (int i=0; i < board.getCellBase().size(); i++)
        {
            CellBase cell = board.getCellBase().get(i);
            String currentCellDesc = cellDesc;
            currentCellDesc = currentCellDesc.replace(CELL_ID_PLACEHOLDER, Integer.toString(i + 1));
            currentCellDesc = currentCellDesc.replace(CELL_TITLE_PLACEHOLDER, cell.getName());
            currentCellDesc = currentCellDesc.replace(CELL_HOUSES_PLACEHOLDER, "0");
            currentCellDesc = currentCellDesc.replace(CELL_OWNER_PLACEHOLDER, "0");

            if (cell.getType().compareTo("City") == 0)
            {
                City city = (City) cell;
                currentCellDesc = currentCellDesc.replace(CELL_IMG_PLACEHOLDER, "css/images/" + city.getCountry().getName().replace(" ", "%20") + ".png");
                currentCellDesc = currentCellDesc.replace(CELL_HOUSES_PLACEHOLDER, Integer.toString(city.GetNumberOfHouses()));
            }
            else if (cell.getType().compareTo("Go") == 0)
            {
                currentCellDesc = currentCellDesc.replace(CELL_IMG_PLACEHOLDER, "css/images/start.png");
            }
            else if (cell.getType().compareTo("Service") == 0)
            {
                Asset asset = (Asset) cell;
                currentCellDesc = currentCellDesc.replace(CELL_IMG_PLACEHOLDER, "css/images/" + asset.getGroup().getName() + ".png");
            }
            else if (cell.getType().compareTo("Community Chest") == 0)
            {
                currentCellDesc = currentCellDesc.replace(CELL_IMG_PLACEHOLDER, "css/images/Community Chest.png".replace(" ", "%20"));
            }
            else if (cell.getType().compareTo("Chance") == 0)
            {
                currentCellDesc = currentCellDesc.replace(CELL_IMG_PLACEHOLDER, "css/images/Chance.png");
            }
            else if (cell.getType().compareTo("Jail") == 0)
            {
                currentCellDesc = currentCellDesc.replace(CELL_IMG_PLACEHOLDER, "css/images/Jail.gif");
            }
            else if (cell.getType().compareTo("Parking") == 0)
            {
                currentCellDesc = currentCellDesc.replace(CELL_IMG_PLACEHOLDER, "css/images/Parking.gif");
            }
            else if (cell.getType().compareTo("GotoJail") == 0)
            {
                currentCellDesc = currentCellDesc.replace(CELL_IMG_PLACEHOLDER, "css/images/GotoJail.gif");
            }

            boardJSON = boardJSON.replace(CELL_PLACEHOLDER.replace("#", Integer.toString(i +1)), currentCellDesc);
        }

        return boardJSON;
    }

    public static String GeneratePlayersJSON(List<PlayerDetails> players, ServletContext context)
    {
        String playersJSON = GetResourceAsString(context, "/json/players_init.json");
        String playerDesc = GetResourceAsString(context, "/json/player_desc.json");

        for (int i=0; i < MAX_PLAYERS; i++)
        {
            String currentPlayerDesc = playerDesc;
            currentPlayerDesc = currentPlayerDesc.replace(PLAYER_ID_PLACEHOLDER, Integer.toString(i + 1));
            currentPlayerDesc = currentPlayerDesc.replace(PLAYER_IN_JAIL_PLACEHOLDER, "no");
            currentPlayerDesc = currentPlayerDesc.replace(PLAYER_POSITION_PLACEHOLDER, "0");

            if (players.size() > i)
            {
                PlayerDetails player = players.get(i);

                currentPlayerDesc = currentPlayerDesc.replace(PLAYER_NAME_PLACEHOLDER, player.getName());
                currentPlayerDesc = currentPlayerDesc.replace(PLAYER_MONEY_PLACEHOLDER, Integer.toString(player.getAmount()));
                currentPlayerDesc = currentPlayerDesc.replace(PLAYER_IN_GAME_PLACEHOLDER, "yes");
            }
            else
            {
                currentPlayerDesc = currentPlayerDesc.replace(PLAYER_NAME_PLACEHOLDER, "dead_player");
                currentPlayerDesc = currentPlayerDesc.replace(PLAYER_MONEY_PLACEHOLDER, "0");
                currentPlayerDesc = currentPlayerDesc.replace(PLAYER_IN_GAME_PLACEHOLDER, "no");
            }

            playersJSON = playersJSON.replace(PLAYER_PLACEHOLDER.replace("#", Integer.toString(i +1)), currentPlayerDesc);
        }

        return playersJSON;
    }

    static String GenerateGameOverMessage(ServletContext context)
    {
        return GetResourceAsString(context, "/json/game_over.json");
    }

    static String GenerateGameWinnerMessage(ServletContext context, String winnerName) {
        String winnerMessage = GetResourceAsString(context, "/json/game_winner.json");
        winnerMessage = winnerMessage.replace(WINNER_PLACEHOLDER, winnerName);

        return winnerMessage;
    }

    static String GeneratePlayerLostMessage(ServletContext context, int playerID, String playerName, boolean resigned) {
        String playerLostMessage = GetResourceAsString(context, "/json/player_lost.json");
        playerLostMessage = playerLostMessage.replace(PLAYER_ID_PLACEHOLDER, Integer.toString(playerID));
        if (resigned)
            playerLostMessage = playerLostMessage.replace(LOST_MESSAGE_PLACEHOLDER, playerName + " has resgined from the game");
        else
            playerLostMessage = playerLostMessage.replace(LOST_MESSAGE_PLACEHOLDER, playerName + " has lost & leftthe game");

        return playerLostMessage;
    }

    static String GenerateSetPlayerTurnMessage(ServletContext context, int playerID) {
        String turnSetMessage = GetResourceAsString(context, "/json/set_turn.json");
        turnSetMessage = turnSetMessage.replace(PLAYER_ID_PLACEHOLDER, Integer.toString(playerID));
        return turnSetMessage;
    }

    static String GenerateThrowDiceRequest(ServletContext context, boolean displayError) {
        String rollRequest = GetResourceAsString(context, "/json/request_dice.json");
        if (displayError)
            rollRequest = rollRequest.replace(ERROR_PLACEHOLDER, "Dices values range between 1-6");
        else
            rollRequest = rollRequest.replace(ERROR_PLACEHOLDER, "");
        return rollRequest;
    }

    static String GenerateSendDiceValueMessage(ServletContext context, Integer firstDiceResult, Integer secondDiceResult) {
        String setDiceMessage = GetResourceAsString(context, "/json/set_dice.json");
        setDiceMessage = setDiceMessage.replace(DICE1_PLACEHOLDER, Integer.toString(firstDiceResult));
        setDiceMessage = setDiceMessage.replace(DICE2_PLACEHOLDER, Integer.toString(secondDiceResult));
        return setDiceMessage;
    }

    static String GenerateMovePlayerMessage(ServletContext context, int playerID, Integer fromCellID, Integer toCellID) {
        String playerMoveMessage = GetResourceAsString(context, "/json/move_player.json");
        playerMoveMessage = playerMoveMessage.replace(PLAYER_ID_PLACEHOLDER, Integer.toString(playerID));
        playerMoveMessage = playerMoveMessage.replace(FROM_CELL_PLACEHOLDER, Integer.toString(fromCellID));
        playerMoveMessage = playerMoveMessage.replace(TO_CELL_PLACEHOLDER, Integer.toString(toCellID));
        return playerMoveMessage;
    }

    static String GenerateBuyAssetRequest(ServletContext context, CellBase landCell) {
        String buyRequest = GetResourceAsString(context, "/json/request_buy.json");
        buyRequest = buyRequest.replace(CELL_TITLE_PLACEHOLDER, landCell.getName());
        return buyRequest;
    }

    static String GenerateBuildHouseRequest(ServletContext context, CellBase landCellToBuildHouse) {
        String buildRequest = GetResourceAsString(context, "/json/request_build.json");
        buildRequest = buildRequest.replace(CELL_TITLE_PLACEHOLDER, landCellToBuildHouse.getName());
        return buildRequest;
    }

    static String GenerateAssetBoughtMessage(ServletContext context, int playerID, Integer boughtCellID) {
        String boughtAssetMessage = GetResourceAsString(context, "/json/set_bought.json");
        boughtAssetMessage = boughtAssetMessage.replace(PLAYER_ID_PLACEHOLDER, Integer.toString(playerID));
        boughtAssetMessage = boughtAssetMessage.replace(CELL_ID_PLACEHOLDER, Integer.toString(boughtCellID));
        return boughtAssetMessage;
    }

    static String GenerateHouseBuiltMessage(ServletContext context, int playerID, Integer boardSquareID) {
        String addHouseMessage = GetResourceAsString(context, "/json/add_house.json");
        addHouseMessage = addHouseMessage.replace(PLAYER_ID_PLACEHOLDER, Integer.toString(playerID));
        addHouseMessage = addHouseMessage.replace(CELL_ID_PLACEHOLDER, Integer.toString(boardSquareID));
        return addHouseMessage;
    }

    static String GenerateGeneralMessage(ServletContext context, String messageType, String messageValue) {
        String generalMessage = GetResourceAsString(context, "/json/set_message.json");
        generalMessage = generalMessage.replace(MESSAGE_PLACEHOLDER, messageValue);
        return generalMessage;
    }

    static String GenerateUpdatePlayerBalanceMessage(ServletContext context, int playerID, int amount) {
        String updateBalanceMessage = GetResourceAsString(context, "/json/set_balance.json");
        updateBalanceMessage = updateBalanceMessage.replace(PLAYER_ID_PLACEHOLDER, Integer.toString(playerID));
        updateBalanceMessage = updateBalanceMessage.replace(PLAYER_MONEY_PLACEHOLDER, Integer.toString(amount));
        return updateBalanceMessage;
    }

    static String GetResourceAsString(ServletContext context, String resourcePath)
    {
        String resourceString = "";
        InputStream istream = context.getResourceAsStream(resourcePath);
        try {
            resourceString = convertStreamToString(istream);
            istream.close();
        } catch (IOException ex) {
            Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
        }

        return resourceString;

    }
}
