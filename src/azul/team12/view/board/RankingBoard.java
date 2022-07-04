package azul.team12.view.board;

import azul.team12.controller.Controller;
import azul.team12.model.GameModel;
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

    private final List<Player> playersList;


    /**
     * The constructor to create the ranking board showing the points of the players.
     */
    public RankingBoard(Controller controller) {
        this.controller = controller;
        playersList = controller.rankingPlayerWithPoints();

        createRankingArea();

    }

    /**
     * create the ranking area with the players' name and their points.
     */
    private void createRankingArea() {
        rankingBoardPanel = new JPanel();
        rankingBoardPanel.setMaximumSize(new Dimension(300, 260));
        rankingBoardPanel.setLayout(new FlowLayout());

        add(rankingBoardPanel, BorderLayout.CENTER);

        for (Player player : playersList) {
            rankingBoardPanel.add(new JLabel(player.getName() + player.getPoints()));
        }
    }


}
