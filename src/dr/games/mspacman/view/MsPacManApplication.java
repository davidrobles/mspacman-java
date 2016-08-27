//package dr.games.mspacman.view;
//
//import dr.games.mspacman.agents.KeyboardController;
//import dr.games.mspacman.model.PacAgent;
//import dr.games.mspacman.agents.PillEater;
//import dr.games.mspacman.model.PacGame;
//import dr.games.mspacman.model.PacModel;
//import dr.games.mspacman.view.GameStateView;
//
//import javax.swing.*;
//import java.awt.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.awt.event.KeyEvent;
//
//public class MsPacManApplication extends JFrame {
//
//    private PacGame game;
//    private GameStateView view;
//
//    private JMenuBar menuBar;
//    private JMenu fileMenu, viewMenu, helpMenu;
//    private JMenuItem menuItem;
//
//    private JPanel leftPanel = new JPanel();
//    private JPanel centerPanel = new JPanel();
//    private JPanel rightPanel = new JPanel();
//
//    private JButton startButton;
//
//    private JButton beginButton = new JButton("Beginning");
//    private JButton fastRewindButton = new JButton("<<");
//    private JButton rewindButton = new JButton("<");
//    private JButton playButton = new JButton("Play");
//    private JButton forwardButton = new JButton(">");
//    private JButton fastForwardButton = new JButton(">>");
//    private JButton currentButton = new JButton("Current");
//
//    private JLabel gameTickLabel = new JLabel();
//
//    String[] columnNames = {"PARAMETER", "VALUE"};
//
//    Object[][] data = {
//        {"Mary", "Campione"},
//        {"Alison", "Huml"},
//        {"Kathy", "Walrath"},
//        {"Sharon", "Zakhour"},
//        {"Philip", "Milne"}
//    };
//
//    Object[] agents = new Object[] {new PillEater()};
//
//    String[] petStrings = { "Controller 1", "Controller 2", "Controller 3", "Controller 4" };
//    JComboBox petList = new JComboBox(agents);
//
//    JTable table = new JTable(data, columnNames);
//    MsPacManApplication este;
//
//    int index = 0;
//
//    public MsPacManApplication() throws HeadlessException {
//        leftPanel.add(petList);
//        centerPanel.add(gameTickLabel);
//        este = this;
//        // Initialize buttons
//        beginButton = new JButton("|<");
//        beginButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                index = 0;
//                view.update(game.getGameStates().get(index));
//            }
//        });
//        fastRewindButton = new JButton("<<");
//        fastRewindButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                index -= 5;
//                view.update(game.getGameStates().get(index));
//            }
//        });
//        rewindButton = new JButton("<");
//        rewindButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                index--;
//                view.update(game.getGameStates().get(index));
//            }
//        });
//        playButton = new JButton("Play");
//        playButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                game.pause();
//                index = game.getGameModel().getTimeSteps();
//                playButton.setText("Resume");
////                Easy.save(game.getGameStates(), "/Users/drobles/Desktop/test.xml");
//            }
//        });
//        forwardButton = new JButton(">");
//        forwardButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                index++;
//                view.update(game.getGameStates().get(index));
//            }
//        });
//        fastForwardButton = new JButton(">>");
//        fastForwardButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                index += 5;
//                view.update(game.getGameStates().get(index));
//            }
//        });
//        currentButton = new JButton(">|");
//        currentButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                index = game.getGameModel().getTimeSteps();
//                view.update(game.getGameStates().get(index));
//            }
//        });
//        // Disable buttons
////        beginButton.setEnabled(false);
////        fastRewindButton.setEnabled(false);
////        rewindButton.setEnabled(false);
////        playButton.setEnabled(false);
////        forwardButton.setEnabled(false);
////        fastForwardButton.setEnabled(false);
////        currentButton.setEnabled(false);
//        // Add buttons
//        centerPanel.add(beginButton);
//        centerPanel.add(fastRewindButton);
//        centerPanel.add(rewindButton);
//        centerPanel.add(playButton);
//        centerPanel.add(forwardButton);
//        centerPanel.add(fastForwardButton);
//        centerPanel.add(currentButton);
//
//        menuBar = new JMenuBar();
//        fileMenu = new JMenu("File");
//        fileMenu.setMnemonic(KeyEvent.VK_F);
//        menuBar.add(fileMenu);
//        viewMenu = new JMenu("View");
//        viewMenu.setMnemonic(KeyEvent.VK_V);
//        menuBar.add(viewMenu);
//        helpMenu = new JMenu("Help");
//        helpMenu.setMnemonic(KeyEvent.VK_H);
//        menuBar.add(helpMenu);
//        setJMenuBar(menuBar);
//        startButton = new JButton("New Game");
//        leftPanel.add(startButton);
//        rightPanel.add(table);
////        setSize(600, 400);
//
////        setResizable(false);
//        ////////////////////////////////////////
//        leftPanel.setBackground(Color.GREEN);
//        centerPanel.setBackground(Color.YELLOW);
//        centerPanel.setBackground(Color.BLUE);
//        setLayout(new BorderLayout());
//        getContentPane().add(leftPanel, BorderLayout.WEST);
//        getContentPane().add(centerPanel, BorderLayout.CENTER);
//        getContentPane().add(rightPanel, BorderLayout.EAST);
//        startButton.addActionListener(new StartGameListener());
//        pack();
//        centerFrame();
//        setVisible(true);
//
//        KeyboardController agent = new KeyboardController();
//        PacModel pacModel = new PacModel(agent);
//        game = new PacGame(pacModel, 20);
//        game.registerStatusListener(este);
////        view = new PacView(game);
//
//        view = new GameStateView();
//        pacModel.registerObserver(view);
////        pacModel.registerEventsObserver(view);
//        view.addKeyListener(game);
//        view.addKeyListener(agent);
//        centerPanel.add(view);
//        game.start();
////        view.requestFocusInWindow();
//    }
//
//    class StartGameListener implements ActionListener {
//        @Override
//        public void actionPerformed(ActionEvent e) {
//            game.setPacmanAgent((PacAgent)petList.getSelectedObjects()[0]);
//            game.restart();
////            KeyboardController agent = new KeyboardController();
////            PacModel gameModel = new PacModel(agent);
////            PacGame game = new PacGame(gameModel, 20);
////            game.registerStatusListener(este);
////            PacView view = new PacView(game);
////            gameModel.registerEventsObserver(view);
////            view.addKeyListener(game);
////            view.addKeyListener(agent);
////            centerPanel.add(view);
////            game.start();
////            view.requestFocusInWindow();
//        }
//    }
//
//    private void centerFrame() {
//        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
//        int width = (int) screenSize.getWidth();
//        int height= (int) screenSize.getHeight();
//        int x = (width / 2) - (getWidth() / 2);
//        int y = (height / 2) - (getHeight() / 2);
//        setLocation(x, y);
//    }
//
//    @Override
//    public void update(PacGame pacGame) {
////        view.requestFocusInWindow();
//        if (pacGame.isPaused())
//            playButton.setText("Resume");
//        else
//            playButton.setText("Pause");
//
//        gameTickLabel.setText(String.valueOf(pacGame.getGameModel().getTimeSteps()));
////            playButton.setEnabled(true);
////        } else {
////            playButton.setEnabled(false);
////        }
//    }
//
//    public static void main(String[] args) {
//        new MsPacManApplication();
//    }
//
//}
