/**
 * * ****************************************
 * CSCI205 - Software Engineering and Design Fall 2015
 *
 * Name: Ryan Greene, Jack Napor, Danny Toback, & Richard Huffman Date: Nov 13,
 * 2015 Time: 7:52:40 AM
 *
 * Project: csci205FinalProject Package: Game File: ZombieController
 * Description: The controller for the whole game, works with the ZombieModel
 * and ZombieView
 *
 * ****************************************
 */
package Game;

import Piece.Player;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;

public class ZombieController implements ActionListener {

    private ZombieView theView;
    private Board board;
    private ShopView theStore;
    private DrawBoard drBoard;
    private DrawZombies drZombies;
    private ShopController shopController;
    private JLayeredPane layeredBoard;
    private Timer checkTimer;
    private TimerTask checker;
    private DrawBullet drBullet;
    private JLabel kcLabel;
    private boolean roundTransition = false;

    /**
     * This is the constructor for the Controller class for the ZombieView
     *
     * @param view
     * @throws IOException
     */
    public ZombieController(ZombieView view) throws IOException {
        this.theView = view;
        this.theView.setFocusable(false);
        this.board = new Board(1, 1);
        this.drBoard = new DrawBoard(board);
        this.drZombies = new DrawZombies(board);
        this.drBullet = new DrawBullet(board);
        this.theStore = new ShopView();
        this.shopController = new ShopController(this.theStore,
                                                 board.getPlayer());
        this.theStore.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                roundTransition = true;
            }
        });
        theView.getBoard().setLocation(350, 20);
        this.changeBTN();

        this.layeredBoard = new JLayeredPane();
        this.layeredBoard.setPreferredSize(new Dimension(500, 500));
        this.layeredBoard.setBounds(0, 0, 500, 500);

        theView.getLblRound().setText("Round " + board.getRoundNum());

        theView.getBoard().addKeyListener(this.drBoard);
        theView.getBoard().addKeyListener(this.drBullet);
        theView.getBoard().setFocusable(true);
        theView.getBtnStart().addActionListener(this);
        theView.getBtnExit().addActionListener(this);
        theView.getVitals().setPreferredSize(new Dimension(80, 80));
        theView.pack();

        //Sound.song.loop();
    }

    /**
     * Displays all of the vitals on the JLabel, vitalLabel. Updates frequently
     * to keep the player updated on their standing in the game (health, ammo,
     * ect)
     */
    public void displayVitals() {
        theView.getVitals().setForeground(Color.WHITE);
        String text = "<html>Kill Count: " + board.getPlayer().getKillCount() + "<br>"
                      + " Ammo Remaining: " + board.getPlayer().getAmmo() + "<br>" + " Armor: " + board.getPlayer().getArmor() + "<br>"
                      + " Health: " + board.getPlayer().getHealth() + "<br>"
                      + " Points: " + board.getPlayer().getPoints() + "</html>";
        theView.getVitals().setText(text);
    }

    /**
     * Starts the game when the button is pressed
     *
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == theView.getBtnStart()) {
            try {
                if (!board.getPlayer().isAlive()) {
                    resetBoard();
                }
                theView.getBoard().add(this.layeredBoard, BorderLayout.CENTER);
                board.addZombies();
                drBoard.setActive(true);
                resume();
            } catch (IOException ex) {
                Logger.getLogger(ZombieController.class.getName()).log(
                        Level.SEVERE,
                        null, ex);
            }
            this.layeredBoard.removeAll();
            this.layeredBoard.add(this.drBoard, JLayeredPane.DEFAULT_LAYER);
            this.layeredBoard.add(this.drZombies, JLayeredPane.PALETTE_LAYER);
            this.layeredBoard.add(this.drBullet, JLayeredPane.MODAL_LAYER);

            theView.getBoard().requestFocus();
            theView.getBtnStart().setEnabled(false);
            theView.getBtnExit().setEnabled(true);

        }

        if (e.getSource() == theView.getBtnExit()) {
            drZombies.stopZombies();
            drBoard.setActive(false);
            JOptionPane.showMessageDialog(null,
                                          "Thank you for playing Zombies!.",
                                          "Goodbye!", JOptionPane.PLAIN_MESSAGE);

            theView.getBtnExit().setEnabled(false);
            theView.getBtnStart().setEnabled(true);
        }
    }

    private void changeBTN() {
        //CHANGE BUTTON LOGO
        theView.getBtnStart().setIcon(new ImageIcon("images/start.png"));
        theView.getBtnStart().setOpaque(false);
        theView.getBtnStart().setContentAreaFilled(false);
        theView.getBtnStart().setBorderPainted(false);
        theView.getBtnStart().setText("");
        theView.getBtnExit().setIcon(new ImageIcon("images/exit.png"));
        theView.getBtnExit().setOpaque(false);
        theView.getBtnExit().setContentAreaFilled(false);
        theView.getBtnExit().setBorderPainted(false);
        theView.getBtnExit().setText("");
    }

    private void isPlayerAlive() {
        if (!board.getPlayer().isAlive()) {
            this.drBoard.repaint();
            theView.getBoard().remove(this.layeredBoard);
            theView.getBtnStart().setEnabled(true);
            theView.getBtnExit().setEnabled(false);
        }
    }

    public void winRound() throws IOException, InterruptedException {
        if (!board.areZombies()) {
            this.shopController.updateView(board.getPlayer());
            this.theStore.setVisible(true);
            drBoard.setActive(false);
            drZombies.stopZombies();
        }
        if (roundTransition) {
            this.theStore.setVisible(false);
            int roundNum = board.getRoundNum() + 1;
            theView.getLblRound().setText("Round " + roundNum);
            Player tempPlayer = board.getPlayer();
            board = new Board(1, roundNum);
            board.setPlayerLocation(tempPlayer);
            board.addZombies();
            this.drBoard.updateBoard(board);
            this.drZombies.updateBoard(board);
            this.drBullet.updateBoard(board);
            drBoard.setActive(true);
            roundTransition = false;
        }
    }

    private void pause() {
        this.checkTimer.cancel();
    }

    private void resume() {
        board.winRound();
        checkTimer = new Timer();
        checker = new TimerTask() {
            @Override
            public void run() {
                isPlayerAlive();
                displayVitals();
                try {
                    winRound();
                } catch (IOException ex) {
                    Logger.getLogger(ZombieController.class.getName()).log(
                            Level.SEVERE,
                            null,
                            ex);
                } catch (InterruptedException ex) {
                    Logger.getLogger(ZombieController.class.getName()).log(
                            Level.SEVERE,
                            null,
                            ex);
                }
            }
        };
        checkTimer.schedule(checker, 0, 1000);
    }

    private void resetBoard() throws IOException {
        this.board = new Board(1, 1);
        this.drBoard = new DrawBoard(board);
        this.drZombies = new DrawZombies(board);
        this.drBullet = new DrawBullet(board);
        theView.getBoard().addKeyListener(this.drBoard);
        theView.getBoard().addKeyListener(this.drBullet);
        this.board.getPlayer().setHealth(100);
        this.board.getPlayer().setAmmo(100);
        this.board.getPlayer().setGunLevel(1);
        this.board.getPlayer().setArmor(0);
        this.board.getPlayer().setKillCount(0);
    }
}
