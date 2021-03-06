
/* 
* Author: Austin Kibler, Samir Lamichhane, Christian Reynolds
* Purpose: This class is the player containing the board and tracking the ships on each, also responsible for playing sounds
* Date: 11/28/2018
*/
import java.util.*;
import java.io.*;

import javafx.embed.swing.JFXPanel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import javax.swing.*;

/**
 * BSPlayer
 */
public class BSPlayer {
    private String[][] board;
    private int[] ships;
    private int shipsDeployed = 0;
    private int shipHP = 2;
    private int shotsTaken = 1;

    public BSPlayer(int rows, int cols, int ships) {
        this.board = new String[rows][cols];
        this.ships = new int[ships];
        for (String[] row : board) {
            Arrays.fill(row, "o");
        }
    }

    public boolean deployShips(int row, int col, boolean vert) {
        int[][] tileSet = convShipTiles(row, col, shipHP, vert);
        if (!shipAlreadyThere(tileSet)) {
            for (int x = 0; x < tileSet.length; x++) {
                board[tileSet[x][0]][tileSet[x][1]] = Integer.toString(shipsDeployed);
            }
            ships[shipsDeployed] = shipHP;
            shipsDeployed++;
            if (shipsDeployed != 2) {
                shipHP++;
            }
            return true;
        } else {
            return false;
        }
    }

    public int[][] convShipTiles(int row, int col, int shipSize, boolean vert) {
        if (vert) {
            int[] tile1 = new int[] { row + 1, col };
            int[] tile2 = new int[] { row, col };
            if (shipSize >= 3) {
                int[] tile3 = new int[] { row - 1, col };
                if (shipSize >= 4) {
                    int[] tile4 = new int[] { row + 2, col };
                    if (shipSize >= 5) {
                        int[] tile5 = new int[] { row - 2, col };
                        int[][] tileSet = new int[][] { tile1, tile2, tile3, tile4, tile5 };
                        return tileSet;
                    } else {
                        int[][] tileSet = new int[][] { tile1, tile2, tile3, tile4 };
                        return tileSet;
                    }
                } else {
                    int[][] tileSet = new int[][] { tile1, tile2, tile3 };
                    return tileSet;
                }
            } else {
                int[][] tileSet = new int[][] { tile1, tile2 };
                return tileSet;
            }
        } else {
            int[] tile1 = new int[] { row, col + 1 };
            int[] tile2 = new int[] { row, col };
            if (shipSize >= 3) {
                int[] tile3 = new int[] { row, col - 1 };
                if (shipSize >= 4) {
                    int[] tile4 = new int[] { row, col + 2 };
                    if (shipSize >= 5) {
                        int[] tile5 = new int[] { row, col - 2 };
                        int[][] tileSet = new int[][] { tile1, tile2, tile3, tile4, tile5 };
                        return tileSet;
                    } else {
                        int[][] tileSet = new int[][] { tile1, tile2, tile3, tile4 };
                        return tileSet;
                    }
                } else {
                    int[][] tileSet = new int[][] { tile1, tile2, tile3 };
                    return tileSet;
                }
            } else {
                int[][] tileSet = new int[][] { tile1, tile2 };
                return tileSet;
            }
        }
    }

    public Boolean shipAlreadyThere(int[][] tileSet) {
        Boolean shipThere = false;
        for (int x = 0; x < tileSet.length; x++) {
            if (!board[tileSet[x][0]][tileSet[x][1]].equals("o")) {
                shipThere = true;
                return shipThere;
            }
        }
        return shipThere;
    }

    public String getTile(int row, int col) {
        return board[row][col];
    }

    public boolean checkHit(int row, int col) {
        String tile = board[row][col];
        if (tile.equals("o")) {
            board[row][col] = "m";
            JOptionPane.showMessageDialog(null, "No ship there!", "Battleship Fire Phase", 1);
            shotsTaken++;
            return true;
        } else if (tile.equals("x") || tile.equals("m")) {
            JOptionPane.showMessageDialog(null, "This tile has already been hit!", "Battleship Fire Phase", 1);
            return false;
        } else {
            int shipIndex = Integer.parseInt(tile);
            shipHit(shipIndex);
            board[row][col] = "x";
            shotsTaken++;
            return true;
        }
    }

    public void shipHit(int ship) {
        ships[ship] -= 1;
        if (ships[ship] == 0) {
            playSound();
            JOptionPane.showMessageDialog(null, "A ship has been sunk!", "Battleship Fire Phase", 1);
        } else {
            playSound();
            JOptionPane.showMessageDialog(null, "You hit something!", "Battleship Fire Phase", 1);
        }
    }

    public void playSound() {
        String os = System.getProperty("os.name");
        JFXPanel panel = new JFXPanel();
        try {
            if (os.toLowerCase().contains("Mac".toLowerCase())) {
                Random rand = new Random();
                int randNum = rand.nextInt(4) + 1;
                String bip = "Sounds/explosion" + randNum + ".mp3";
                Media hit = new Media(new File(bip).toURI().toString());
                MediaPlayer mediaPlayer = new MediaPlayer(hit);
                mediaPlayer.setStartTime(new Duration(0));
                mediaPlayer.play();
            } else {
                Random rand = new Random();
                int randNum = rand.nextInt(4) + 1;
                String bip = "Sounds\\explosion" + randNum + ".mp3";
                Media hit = new Media(new File(bip).toURI().toString());
                MediaPlayer mediaPlayer = new MediaPlayer(hit);
                mediaPlayer.setStartTime(new Duration(0));
                mediaPlayer.play();
            }
        } catch (Exception e) {
            System.err.println("Issue playing sound");
        }
    }

    public int countShips() {
        int shipsLeft = 0;
        for (int x = 0; x < ships.length; x++) {
            if (ships[x] != 0) {
                shipsLeft++;
            }
        }
        return shipsLeft;
    }

    public void setBoard(String saveBoard) {
        int x = 0;
        int ship0 = 0;
        int ship1 = 0;
        int ship2 = 0;
        int ship3 = 0;
        int ship4 = 0;
        for (int j = 0; j < board.length; j++) {
            for (int k = 0; k < 10; k++) {
                if (saveBoard.charAt(x) == '0') {
                    ship0 += 1;
                } else if (saveBoard.charAt(x) == '1') {
                    ship1 += 1;
                } else if (saveBoard.charAt(x) == '2') {
                    ship2 += 1;
                } else if (saveBoard.charAt(x) == '3') {
                    ship3 += 1;
                } else if (saveBoard.charAt(x) == '4') {
                    ship4 += 1;
                }
                board[j][k] = Character.toString(saveBoard.charAt(x));
                x++;
            }
        }
        loadShips(ship0, ship1, ship2, ship3, ship4);
    }

    public void loadShips(int ship0, int ship1, int ship2, int ship3, int ship4) {
        ships[0] = ship0;
        ships[1] = ship1;
        ships[2] = ship2;
        ships[3] = ship3;
        ships[4] = ship4;
    }

    public int getShipHP() {
        return shipHP;
    }

    public int getShotsTaken() {
        return shotsTaken;
    }

    public int getShipsDeployed() {
        return shipsDeployed;
    }

    public int getBoardLen() {
        return board.length;
    }

    public void setShotsTaken(int shotsTaken) {
        this.shotsTaken = shotsTaken;
    }

    public void setShipsDeployed(int shipsDeployed) {
        this.shipsDeployed = shipsDeployed;
    }

    @Override
    public String toString() {
        String string = "";
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < 10; j++) {
                string += board[i][j];
            }
        }
        string += "\n" + shotsTaken;
        return string;
    }
}