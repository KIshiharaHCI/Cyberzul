package azul.team12.view.board;

import azul.team12.controller.Controller;
import azul.team12.model.Player;


import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * The Ranking board which shows the name of all players and their points in descending order.
 */

public class RankingBoard extends JPanel {

    private final Controller controller;
    private JPanel rankingBoardPanel;

    private List<Player> playersList;


    /**
     * The constructor to create the ranking board showing the points of the players.
     * @param controller
     * @param playersList
     */
    public RankingBoard(Controller controller, List<Player> playersList) {
        this.controller = controller;
        this.playersList = controller.rankingPlayerWithPoints();

        createRankingArea();

    }

    /**
     * create the ranking area with the players' name and their points.
     */
    private void createRankingArea() {
        rankingBoardPanel = new JPanel();
        setBackground(Color.GREEN);
        rankingBoardPanel.setMaximumSize(new Dimension(300, 260));
        rankingBoardPanel.setLayout(new GridLayout(playersList.size(), 1));
        add(rankingBoardPanel, BorderLayout.CENTER);
        for (Player player : playersList) {
            rankingBoardPanel.add(new JLabel(player.getName() + ": " + controller.getPoints(player.getName())));
        }
    }

    /**
     * Updates the current points of all players.
     */
    public void updateRankingBoard() {
        remove(rankingBoardPanel);
        playersList = controller.rankingPlayerWithPoints();
        createRankingArea();
    }


}
