package com.tse.www.game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Game {

    private final int dim;
    private int size;
    private int height;
    private int width;
    private final String[] win;
    private JFrame frame;
    private final JButton[][] board;
    private int empty;
    private JPanel panel = new JPanel();
    private int count = 0;
    private String gameTitle = "";
    ArrayList<Integer> initList = new ArrayList<Integer>(size);
    ArrayList<Integer> tempList = new ArrayList<Integer>();
    FileHandler fh = new FileHandler();
    ArrayList<Integer> customList = new ArrayList<>();

    //building the gui without save game
    public Game(String title, int dim) {
        this.dim = dim;
        this.height = this.dim*100;
        this.width = this.dim*100;
        this.size = this.dim * this.dim;
        this.empty = this.dim * this.dim;
        this.gameTitle = title;
        this.win = new String[size - 1];
        this.board = new JButton[this.dim][this.dim];

        for (int i = 1; i < size; i++) {
            win[i - 1] = Integer.toString(i);
        }

        buildBoard(false);
    }

    public Game(String title, int dim, String text) {
        this.dim = dim;
        this.height = this.dim*100;
        this.width = this.dim*100;
        this.size = this.dim * this.dim;
        this.empty = this.dim * this.dim;
        this.gameTitle = title;
        this.win = new String[size - 1];
        this.board = new JButton[this.dim][this.dim];

        for (int i = 1; i < size; i++) {
            win[i - 1] = Integer.toString(i);
        }

        String[] str = text.split(",");
        if (str.length==size){
            for (String cha : str) {
                customList.add(Integer.parseInt(cha));
            }
            buildBoard(false);
        } else {
            JOptionPane.showMessageDialog(null,"The custom fields didn't fit the board. Please try again.");
            Menu menu = new Menu(title);
            menu.show();
        }
    }

    //builds the game with save file
    public Game (String title, FileHandler fh){
        this.dim = fh.getDim();
        this.height = this.dim*100;
        this.width = this.dim*100;
        this.size = this.dim * this.dim;
        this.empty = this.dim * this.dim;
        this.gameTitle = title;
        this.win = new String[size - 1];
        this.board = new JButton[this.dim][this.dim];
        this.initList = fh.getList();

        for (int i = 1; i < size; i++) {
            win[i - 1] = Integer.toString(i);
        }
        this.fh = fh;
        this.count = fh.getCount();

        buildBoard(false);
    }

    public int getIndex(int i, int j) {
        return ((i * dim) + j);
    }

    public void buildBoard(boolean newBoard) {

        //builds the board with data from save game
        if (!fh.getList().isEmpty()) {
            initList = fh.getList();
            frame = new JFrame();
            frame.setTitle(gameTitle+" "+fh.getCount());

            buildFields();
        } else if (newBoard){
            //build new board after starting another round
            frame = new JFrame();
            frame.setTitle(gameTitle);

            initList = new ArrayList<Integer>();
            panel = new JPanel();
            //Build game tiles
            for (int i = 0; i < size; i++) {
                initList.add(i, i);
            }

            Collections.shuffle(initList);

            buildFields();
        } else {
            //builds a new board, since no save game is present
            frame = new JFrame();
            frame.setTitle(gameTitle);

            //Build game tiles
            if (customList.isEmpty()){
                for (int i = 0; i < size; i++) {
                    initList.add(i, i);
                }

                Collections.shuffle(initList);
            } else {
                initList = new ArrayList<Integer>(customList);
            }

            buildFields();
        }
    }

    public void buildFields(){

        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int result = JOptionPane.showConfirmDialog(frame, "Do you want to save before exit?", "Exit Confirmation : ", JOptionPane.YES_NO_OPTION);
                if (result == JOptionPane.YES_OPTION) {
                    FileHandler fh = new FileHandler();
                    fh.setCount(count);
                    fh.setDim(dim);
                    fh.setList(initList);
                    try {
                        saveDataOut(fh);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                } else if (result == JOptionPane.NO_OPTION)
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            }
        });
        frame.setResizable(false);
        frame.setSize(width, height);
        panel.setLayout(new GridLayout(this.dim, this.dim));

        panel.setLayout(new GridLayout(dim, dim));

        for (int i = 0; i < size; i++) {
            int row = i / dim;
            int col = i % dim;
            board[row][col] = new JButton(String.valueOf(initList.get(i)));

            if (initList.get(i) == 0) {
                empty = i;
                board[row][col].setVisible(false);
            }

            board[row][col].setBackground(Color.darkGray);
            board[row][col].setForeground(Color.cyan);
            board[row][col].setFont(new Font("Arial", Font.PLAIN, 40));
            board[row][col].addActionListener(e -> {
                JButton btn = (JButton) e.getSource();
                int index = indexOf(btn.getText());
                if (index == -1) {
                    System.out.println("Error. Index out of Bounds");
                    System.exit(0);
                }
                int row2 = index / dim;
                int col2 = index % dim;

                turn(row2, col2);
                if (isFinished()) {
                    finish();
                }
            });
            panel.add(board[row][col]);

            frame.setBackground(Color.gray);
            frame.add(panel);
        }
    }

    public void finish(){
        int result = JOptionPane.showConfirmDialog(null, "You won with "+count+" turns! Another round?", "Game Over", JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION){
            frame.setVisible(false);
            buildBoard(true);
            frame.setVisible(true);
        } else {
            System.exit(0);
        }
    }

    //switches tiles when it's a valid move
    public boolean turn(int row, int col) {
        frame.setTitle(gameTitle);
        int emptyRow = empty / dim;
        int emptyCol = empty % dim;
        int rowDiff = emptyRow - row;
        int colDiff = emptyCol - col;
        boolean isInRow = (row == emptyRow);
        boolean isInCol = (col == emptyCol);
        boolean notDiagonal = (isInRow || isInCol);

        if (notDiagonal) {
            int diff = Math.abs(colDiff);
            if (colDiff < 0 & isInRow) {
                for (int i = 0; i < diff; i++) {
                    board[emptyRow][emptyCol + i].setText(board[emptyRow][emptyCol + (i + 1)].getText());
                }
            } else if (colDiff > 0 & isInRow) {
                for (int i = 0; i < diff; i++) {
                    board[emptyRow][emptyCol - i].setText(board[emptyRow][emptyCol - (i + 1)].getText());
                }
            }

            diff = Math.abs(rowDiff);

            if (rowDiff < 0 & isInCol) {
                for (int i = 0; i < diff; i++) {
                    board[emptyRow + i][emptyCol].setText(board[emptyRow + (i + 1)][emptyCol].getText());
                }
            } else if (rowDiff > 0 & isInCol) {
                for (int i = 0; i < diff; i++) {
                    board[emptyRow - i][emptyCol].setText(board[emptyRow - (i + 1)][emptyCol].getText());
                }
            }

            //swap empty with given square
            board[emptyRow][emptyCol].setVisible(true);
            board[row][col].setText(Integer.toString(0));
            board[row][col].setVisible(false);
            empty = getIndex(row, col);
        }
        tempList.removeAll(tempList);
        initList.remove(initList);

        for (int i = 0; i<dim; i++){
            for (int j = 0; j<dim; j++){
                tempList.add(Integer.parseInt(board[i][j].getText()));
            }
        }
        initList = tempList;
        count++;
        frame.setTitle(frame.getTitle()+" Turns: "+count);
        return true;
    }

    public int indexOf(String cell) {
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[row].length; col++) {
                if (board[row][col].getText().equals(cell)) {
                    return (getIndex(row, col));
                }
            }
        }
        return -1;
    }

    private boolean isFinished() {
        for (int i = win.length - 1; i >= 0; i--) {
            String num = board[i / dim][i % dim].getText();
            if (!num.equals(win[i])) {
                return false;
            }
        }
        return true;
    }

    //methods to save and load the game
    public static void saveDataOut(FileHandler fh) throws IOException {
        //saves current state of the game
        String filename = "save.txt";
        FileOutputStream fos = new FileOutputStream(filename);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(fh);
        oos.close();
    }

    public  static FileHandler saveDataIn() throws IOException, ClassNotFoundException {
        String filename = "save.txt";
        FileInputStream fin = new FileInputStream(filename);
        ObjectInputStream ois = new ObjectInputStream(fin);
        FileHandler fh = (FileHandler) ois.readObject();
        ois.close();
        return fh;
    }

    public void show() {
        frame.setVisible(true);
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }
}
