package com.tse.www.game;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.io.IOException;


import static com.tse.www.game.Game.saveDataIn;

public class Menu{

    private JFrame frame;
    public Menu(String title){
        //builds frame
        frame = new JFrame();
        frame.setTitle(title);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLayout(new GridLayout(5,1));

        //textarea to input custom number of fields
        JTextArea dim = new JTextArea("3");
        dim.setBackground(Color.gray);
        dim.setForeground(Color.cyan);
        dim.setBorder(new LineBorder(Color.darkGray));
        dim.setFont(new Font("Arial", Font.PLAIN, 40));

        JTextArea setting = new JTextArea("1,2,3,4,5,6,7,0,8...");
        setting.setBackground(Color.gray);
        setting.setForeground(Color.cyan);
        setting.setBorder(new LineBorder(Color.darkGray));
        setting.setFont(new Font("Arial", Font.PLAIN, 40));

        //start custom game
        JButton customPlay = new JButton("Start Game");
        customPlay.setBackground(Color.darkGray);
        customPlay.setForeground(Color.cyan);
        customPlay.setFont(new Font("Arial", Font.PLAIN, 40));

        JButton customExit = new JButton("Exit");
        customExit.setBackground(Color.darkGray);
        customExit.setForeground(Color.cyan);
        customExit.setFont(new Font("Arial", Font.PLAIN, 40));

        //normal play button
        JButton play = new JButton("Play");
        play.setBackground(Color.darkGray);
        play.setForeground(Color.cyan);
        play.setFont(new Font("Arial", Font.PLAIN, 40));
        play.addActionListener(e -> {
            //start game with standard 3x3 field
            frame.setVisible(false);
            Game game = new Game(title, 3);
            game.show();
        });

        //opens dialog for custom dimension
        JButton customGrid = new JButton("Custom Grid");
        customGrid.setBackground(Color.darkGray);
        customGrid.setForeground(Color.cyan);
        customGrid.setFont(new Font("Arial", Font.PLAIN, 40));
        JLabel placeholder = new JLabel();
        customGrid.addActionListener(e -> {
            //start custom game
            JDialog gridDia = new JDialog();
            gridDia.setTitle("Custom Grid");
            gridDia.setBackground(Color.darkGray);
            gridDia.setLocationRelativeTo(null);
            gridDia.setLayout(new GridLayout(2,1));
            gridDia.add(dim);

            JPanel panel = new JPanel();
            panel.setLayout(new GridLayout(1,2));
            panel.add(customPlay);
            panel.add(customExit);

            gridDia.add(panel);
            gridDia.setSize(500, 300);
            gridDia.setVisible(true);

            customPlay.addActionListener(e1 -> {
                frame.setVisible(false);
                gridDia.setVisible(false);
                int num = Integer.parseInt(dim.getText());
                Game game = new Game(title, num);
                game.show();
            });
            customExit.addActionListener(e1 -> {System.exit(0);});
        });

        //opens dialog for custom dimension and field settings
        JButton custom = new JButton("Custom Grid & Fields");
        custom.setBackground(Color.darkGray);
        custom.setForeground(Color.cyan);
        custom.setFont(new Font("Arial", Font.PLAIN, 40));
        custom.addActionListener(e -> {
            //start custom game
            JDialog fieldDia = new JDialog();
            fieldDia.setTitle("Custom Grid and Fields");
            fieldDia.setBackground(Color.darkGray);
            fieldDia.setLocationRelativeTo(null);
            fieldDia.setLayout(new GridLayout(3,1));
            fieldDia.add(dim);
            fieldDia.add(setting);

            JPanel panel = new JPanel();
            panel.setLayout(new GridLayout(1,2));
            panel.add(customPlay);
            panel.add(customExit);

            fieldDia.add(panel);
            fieldDia.setSize(700, 300);
            fieldDia.setVisible(true);

            customPlay.addActionListener(e1 -> {
                frame.setVisible(false);
                fieldDia.setVisible(false);
                int num = Integer.parseInt(dim.getText());
                Game game = new Game(title, num, setting.getText());
                game.show();
            });
            customExit.addActionListener(e1 -> {System.exit(0);});
        });

        //load game
        JButton load = new JButton("Load Game");
        load.setBackground(Color.darkGray);
        load.setForeground(Color.cyan);
        load.setFont(new Font("Arial", Font.PLAIN, 40));
        load.addActionListener(e -> {
            //tries to load, opens a dialog if the load fails
            try {
                FileHandler fh = saveDataIn();
                Game game = new Game(title, fh);
                game.show();
                frame.setVisible(false);
            } catch (IOException ex) {
                int result = JOptionPane.showConfirmDialog(frame, "No save game yet. Do you want to close the game?", "Warning", JOptionPane.YES_NO_OPTION);
                if (result == JOptionPane.YES_OPTION){
                    System.exit(0);
                }
            } catch (ClassNotFoundException ex) {
                int result = JOptionPane.showConfirmDialog(frame, "No save game yet. Do you want to close the game?", "Warning", JOptionPane.YES_NO_OPTION);
                if (result == JOptionPane.YES_OPTION){
                    System.exit(0);
                }
            }
        });

        //simple exit button
        JButton exit = new JButton("Exit");
        exit.setBackground(Color.darkGray);
        exit.setForeground(Color.cyan);
        exit.setFont(new Font("Arial", Font.PLAIN, 40));
        exit.addActionListener(e -> {
            System.exit(0);
        });

        //add the buttons and sets size
        frame.add(play);
        frame.add(customGrid);
        frame.add(custom);
        frame.add(load);
        frame.add(exit);
        frame.setSize(600,600);
    }

    public void show(){
        frame.setVisible(true);
    }
}
