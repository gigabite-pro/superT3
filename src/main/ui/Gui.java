package ui;

import model.GuiGame;
import model.SmallGrid;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.ColorUIResource;
import java.awt.*;

import java.util.ArrayList;


import static java.awt.Cursor.HAND_CURSOR;

public class Gui {
    ArrayList<JPanel> panels = new ArrayList<>(); //largeGrid
    JFrame mainFrame;
    JLayeredPane layeredPane;
    JTable table;
    GuiGame game;

    public Gui() {
        UIManager.put("Button.disabledText", new ColorUIResource(Color.white));
        game = new GuiGame();
        showSplashScreen();
        init();
        showMainMenu();
    }

    private void init() {
        mainFrame = new JFrame();
        mainFrame.setTitle("superT3 - The ultimate tic-tac-toe");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setResizable(false);
        mainFrame.setSize(new Dimension(540,600));
        mainFrame.setLocationRelativeTo(null); // open window in the center
        mainFrame.setLayout(null);
        layeredPane = new JLayeredPane();
        layeredPane.setBounds(0,0,540,540);
        mainFrame.add(layeredPane);

        ImageIcon icon = new ImageIcon("src/main/assets/icon.png");
        mainFrame.setIconImage(icon.getImage());
        mainFrame.getContentPane().setBackground(new Color(0x9146FF));
        mainFrame.setVisible(true);
        handleWindowClose();
    }

    private void handleWindowClose() {
        mainFrame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                if (game.isHasGameStarted()) {
                    showQuitMenu();
                } else {
                    game.play();
                    showQuitMenu();
                }
                System.exit(0);
            }
        });
    }

    private void showSplashScreen() {
        JFrame frame = new JFrame();
        frame.setSize(180,180);
        frame.setLocationRelativeTo(null);
        frame.setUndecorated(true);
        ImageIcon splashIcon = new ImageIcon("src/main/assets/icon.png");
        ImageIcon newImage = new ImageIcon(splashIcon.getImage()
                .getScaledInstance(220,180,Image.SCALE_SMOOTH));
        JLabel splashLabel = new JLabel(newImage);
        frame.add(splashLabel);
        frame.setVisible(true);

        try {
            Thread.sleep(3000); // Replace with your desired delay time
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        frame.setVisible(false);
        frame.dispose();
    }

    private void showGrid() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
        mainPanel.setBackground(new Color(0x9146FF));
        mainPanel.setBounds(0,0,530,540);

        mainPanel.add(Box.createVerticalStrut(50));
        mainPanel.add(createNavBar());

        JPanel gridPanel = new JPanel();
        gridPanel.setBounds(0,0,510,540);
        gridPanel.setBackground(new Color(0x9146FF));
        gridPanel.setLayout(new GridLayout(3,3,10,10));

        addPanels(gridPanel);
        mainPanel.add(Box.createVerticalStrut(70));
        mainPanel.add(gridPanel);
        layeredPane.add(mainPanel, Integer.valueOf(0));
        layeredPane.repaint();
        layeredPane.revalidate();
        checkForActiveGrid();
    }

    public JPanel createNavBar() {
        JPanel navBar = new JPanel();
        navBar.setPreferredSize(new Dimension(530,5));
        navBar.setBackground(Color.BLACK);
        navBar.setLayout(new BoxLayout(navBar, BoxLayout.X_AXIS));
        navBar.setOpaque(false);

        JLabel activeGrid = createLabelForNav("Active grid: " + game.getActiveGrid());
        JLabel activePlayer = createLabelForNav("Active player: " + game.getActivePlayer().getSymbol());

        JButton exitButton = new JButton("Exit");
        exitButton.setCursor(Cursor.getPredefinedCursor(HAND_CURSOR));
        exitButton.setBackground(Color.BLACK);
        exitButton.setForeground(Color.white);
        exitButton.setFont(new Font("Verdana", Font.BOLD,10));
        exitButton.setFocusable(false);
        exitButton.addActionListener(e -> {
            layeredPane.removeAll();
            showMainMenu();
        });

        navBar.add(activeGrid);
        navBar.add(Box.createHorizontalStrut(80));
        navBar.add(activePlayer);
        navBar.add(Box.createHorizontalStrut(110));
        navBar.add(exitButton);

        return navBar;
    }

    private JLabel createLabelForNav(String text) {
        JLabel label = new JLabel(text);
        label.setBounds(0,0,20,5);
        label.setFont(new Font("Verdana", Font.PLAIN,15));
        label.setForeground(Color.white);
        return label;
    }

    public void addPanels(JPanel gridPanel) {
        for (int i = 0; i < 9; i++) {
            JPanel panel = new JPanel();
            panel.setBackground(Color.BLACK);
            panel.setBorder(new EmptyBorder(10,10,10,10));

            showWinTieGrid(panel, i);

            panels.add(panel); // smallGrid to largeGrid
            gridPanel.add(panel);
        }
    }

    private void showWinTieGrid(JPanel panel, int i) {
        SmallGrid smallGrid = game.getGrid().getSmallGrid(i + 1);
        if (smallGrid.hasSmallWon()) {
            panel.setLayout(new GridBagLayout());
            JLabel label = new JLabel(smallGrid.getWinnerSymbol());
            label.setFont(new Font("Verdana", Font.BOLD, 40));
            label.setForeground(Color.white);
            panel.add(label);
        } else if (smallGrid.hasTied()) {
            panel.setLayout(new GridBagLayout());
            JLabel label = new JLabel("TIED");
            label.setFont(new Font("Verdana", Font.BOLD, 40));
            label.setForeground(Color.white);
            panel.add(label);
        } else {
            for (int j = 0; j < 9; j++) {
                panel.setLayout(new GridLayout(3,3,10,10));
                JButton button = createButton(i,j);
                panel.add(button);
            }
        }
    }

    private JButton createButton(int i, int j) {
        JButton button = customizeButton(i,j);
        button.setFocusable(false);
        button.setMargin(new Insets(0, 0, 0, 0));
        if (game.getActiveGrid() == (i + 1)) {
            button.addActionListener(e -> handleButtonClick(button));
        } else {
            if (!game.getSmallPlayedPositionKeys(i + 1).contains(j + 1)) {
                button.setBackground(new Color(0xdc3545));
            }
            button.setEnabled(false);
            button.setForeground(Color.white);
        }
        button.setCursor(Cursor.getPredefinedCursor(HAND_CURSOR));
        return button;
    }

    private JButton customizeButton(int i, int j) {
        JButton button;
        if (game.getSmallPlayedPositionKeys(i + 1).contains(j + 1)) {
            String value = game.getGrid().getSmallGrid(i + 1).getSmallGrid().get(j + 1);
            button = new JButton(value);
            if (value.equals("X")) {
                button.setBackground(new Color(0x007bff));
            } else {
                button.setBackground(new Color(0xffc107));
            }
            button.setForeground(Color.black);
            button.setEnabled(false);
        } else {
            button = new JButton("" + (j + 1) + "");
            button.setBackground(new Color(0x28a745));
            button.setForeground(Color.white);
        }
        return button;
    }

    private void handleButtonClick(JButton button) {
        int position = Integer.parseInt(button.getText());
        int response = game.addPosition(position);
        handleWin(response);
    }

    private void handleWin(int response) {
        if (response == 3) {
            layeredPane.removeAll();
            showGrid();
        } else if (response == 2) {
            layeredPane.removeAll();
            showGrid();
            showInfoBox("The game has tied!");
        } else if (response == 1) {
            layeredPane.removeAll();
            showGrid();
            showInfoBox("Congratulations! " + game.getActivePlayer().getSymbol() + " has won the game!");
        }
    }

    private void showInfoBox(String text) {
        JOptionPane.showMessageDialog(null, text,"Game over", JOptionPane.PLAIN_MESSAGE);
    }

    private void checkForActiveGrid() {
        if (game.getWonGrids().contains(game.getActiveGrid()) || game.getActiveGrid() == 0) {
            game.setActiveGrid(showActiveGridPrompt() + 1);
            layeredPane.removeAll();
            showGrid();
        }
    }

    private int showActiveGridPrompt() {
        Integer[] options = {1,2,3,4,5,6,7,8,9};
        int activeGrid = JOptionPane.showOptionDialog(
                null,
                "Which grid do you want to play in?",
                "Select active grid",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                0);
        if (activeGrid == -1) {
            activeGrid = showActiveGridPrompt();
        }
        return activeGrid;
    }

    private void showMainMenu() {
        JPanel panel = new JPanel();
        panel.setBounds(0,0,540,540);
        panel.setLayout(new GridBagLayout());
        panel.setBackground(new Color(0x9146FF));
        JLabel heading = new JLabel("Main Menu");
        heading.setLocation(170,100);
        heading.setFont(new Font("Verdana", Font.BOLD, 25));
        JPanel menuPanel = createMainMenuButtons();
        panel.add(heading, setGBC(0,0,0,0.0f,0.0f,100,0));
        panel.add(menuPanel, setGBC(1,0,0,1.0f,1.0f,0,100));
        layeredPane.add(panel, Integer.valueOf(0));
    }

    private JPanel createMainMenuButtons() {
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new GridBagLayout());
        menuPanel.setSize(new Dimension(300,100));
        menuPanel.setOpaque(false);
        JButton b1 = new JButton("Play");
        JButton b2 = new JButton("Quit");
        setButtonProperties(b1);
        setButtonProperties(b2);
        b1.addActionListener(e -> {
            layeredPane.removeAll();
            showInnerMenu();
        });
        b2.addActionListener(e -> showQuitMenu());
        menuPanel.add(b1, setGBC(0,100,20,0.0f,0.0f,0,0));
        menuPanel.add(Box.createVerticalStrut(30), setGBC(1,0,0,0.0f,0.0f,0,0));
        menuPanel.add(b2, setGBC(2,100,20,0.0f,0.0f,0,0));

        return menuPanel;
    }

    private void setButtonProperties(JButton b) {
        b.setFont(new Font("Verdana", Font.BOLD, 20));
        b.setCursor(Cursor.getPredefinedCursor(HAND_CURSOR));
        b.setBackground(Color.BLACK);
        b.setForeground(Color.white);
        b.setFocusable(false);
    }

    private GridBagConstraints setGBC(int gy, int ipadx, int ipady, float gwx, float gwy, int pt, int pb) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = gy;
        gbc.ipadx = ipadx;
        gbc.ipady = ipady;
        gbc.weightx = gwx;
        gbc.weighty = gwy;
        gbc.insets = new Insets(pt,0,pb,0);
        return gbc;
    }

    private void showInnerMenu() {
        JPanel panel = new JPanel();
        panel.setBounds(0,0,540,540);
        panel.setLayout(new GridBagLayout());
        panel.setBackground(new Color(0x9146FF));
        JLabel heading = new JLabel("Game Menu");
        heading.setLocation(170,100);
        heading.setFont(new Font("Verdana", Font.BOLD, 25));
        JPanel menuPanel = createInnerMenuButtons();
        panel.add(heading, setGBC(0,0,0,0.0f,0.0f,100,0));
        panel.add(menuPanel, setGBC(1,0,0,1.0f,1.0f,0,100));
        layeredPane.add(panel, Integer.valueOf(0));
        layeredPane.repaint();
        layeredPane.revalidate();
    }

    private JPanel createInnerMenuButtons() {
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new GridBagLayout());
        menuPanel.setSize(new Dimension(300,100));
        menuPanel.setOpaque(false);
        JButton b1 = new JButton("New Game");
        JButton b2 = new JButton("Load Game");
        setButtonProperties(b1);
        setButtonProperties(b2);
        b1.addActionListener(e -> {
            layeredPane.removeAll();
            game.play();
            showGrid();
        });
        b2.addActionListener(e -> showLoadMenu());
        menuPanel.add(b1, setGBC(0,100,20,0.0f,0.0f,0,0));
        menuPanel.add(Box.createVerticalStrut(30), setGBC(1,0,0,0.0f,0.0f,0,0));
        menuPanel.add(b2, setGBC(2,100,20,0.0f,0.0f,0,0));

        return menuPanel;
    }

    private void showLoadMenu() {
        JFrame frame = new JFrame();
        frame.setTitle("Select saved game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setSize(new Dimension(540,200));
        frame.setLocationRelativeTo(null);
        createLoadPanel(frame);
    }

    private void createLoadPanel(JFrame frame) {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        if (createTable().getRowCount() < 1) {
            JOptionPane.showMessageDialog(
                    null,
                    "No saved games exist.",
                    "No Game",
                    JOptionPane.WARNING_MESSAGE);
            frame.dispose();
        } else {
            mainPanel.add(new JScrollPane(createTable()));
            mainPanel.add(Box.createVerticalStrut(10));
            JPanel panel = new JPanel();
            panel.setBounds(0,0,500,50);
            panel.setLayout(new FlowLayout(FlowLayout.CENTER));

            JButton button = new JButton("Select");
            handleLoadButtonClick(button, frame);

            panel.add(button);
            mainPanel.add(panel);
            mainPanel.add(Box.createVerticalStrut(10));
            frame.setContentPane(mainPanel);
            frame.setVisible(true);
        }
    }

    private void handleLoadButtonClick(JButton button, JFrame frame) {
        button.addActionListener(e -> {
            if (!table.getSelectionModel().isSelectionEmpty()) {
                String selectedGame = (String)table.getValueAt(table.getSelectedRow(), 0);
                frame.dispose();
                if (game.load(selectedGame)) {
                    layeredPane.removeAll();
                    showGrid();
                }
            }
        });
    }

    private JTable createTable() {
        String[] columnNames = {"Saved Name",
                "Current Player",
                "Active Grid",
                "Saved Date & Time"};

        ArrayList<Object[]> data = game.loadDataFromFile();

        table = new JTable(data.toArray(new Object[0][]),columnNames);
        table.setPreferredScrollableViewportSize(new Dimension(500, 70));
        table.setFillsViewportHeight(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        return table;

    }

    private void showQuitMenu() {
        int option = JOptionPane.showConfirmDialog(
                null,
                "Do you want to save before quitting?",
                "Save", JOptionPane.YES_NO_CANCEL_OPTION);
        if (option == -1 || option == 2) { // cancel or close
            JOptionPane.getRootFrame().dispose();
        } else if (option == 0) { // yes
            showSavePrompt();
        } else { // no
            quit();
        }
    }

    private void showSavePrompt() {
        String input = JOptionPane.showInputDialog(null,"Save as:", "Save as",JOptionPane.PLAIN_MESSAGE);
        if (game.save(input)) {
            showSuccessfullySavedMessage();
        } else {
            showConfirmBox(input);
        }
    }

    private void showSuccessfullySavedMessage() {
        JOptionPane.showMessageDialog(null, "Saved successfully!");
        quit();
    }

    private void showConfirmBox(String input) {
        String[] options = {"Change name", "Update game"};
        int confirm = JOptionPane.showOptionDialog(
                null,
                "A game with this name already exists. What do you want to do?",
                "Conflicting Name",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.WARNING_MESSAGE,
                null,
                options,
                0);
        if (confirm == 0) {
            showSavePrompt();
        } else {
            game.update(input);
            showSuccessfullySavedMessage();
        }
    }

    private void quit() {
        game.printLogs();
        mainFrame.dispose();
    }
}
