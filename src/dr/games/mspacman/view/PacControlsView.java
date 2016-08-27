//package dr.games.mspacman.view;
//
//import dr.games.mspacman.model.PacGame;
//import dr.games.mspacman.model.PacGameObserver;
//
//import javax.swing.*;
//import javax.swing.event.ChangeEvent;
//import javax.swing.event.ChangeListener;
//import java.awt.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.util.Dictionary;
//import java.util.HashMap;
//import java.util.Hashtable;
//import java.util.Map;
//
//public class PacControlsView extends JPanel implements PacGameObserver {
//
//    private PacGameController controller;
//    private PacGame game;
//
//    // Buttons
//    private JButton startStop = new JButton("Start");
//    private JButton first = new JButton("|<");
//    private JButton fastRewind = new JButton("<<");
//    private JButton rewind = new JButton("<");
//    private JButton playPause = new JButton("Play");
//    private JButton forward = new JButton(">");
//    private JButton fastForward = new JButton(">>");
//    private JButton last = new JButton(">|");
//    private JSlider slider = new JSlider();
//
//    public PacControlsView(final PacGameController controller, PacGame game) {
//        this.controller = controller;
//        this.game = game;
//        game.registerObserver(this);
//        createControls();
//    }
//
//    public void createControls()
//    {
//        // Add buttons
//        add(startStop, "North");
//        add(first, "North");
//        add(fastRewind, "North");
//        add(rewind, "North");
//        add(playPause, "North");
//        add(forward, "North");
//        add(fastForward, "North");
//        add(last, "North");
//        add(slider, "South");
//
//        Font f = new Font("Arial", Font.PLAIN, 10);
//        startStop.setFont(f);
//        first.setFont(f);
//        fastRewind.setFont(f);
//        rewind.setFont(f);
//        rewind.setMaximumSize(new Dimension(20, 20));
//        playPause.setFont(f);
//        forward.setFont(f);
//        fastForward.setFont(f);
//        last.setFont(f);
//        slider.setFont(f);
//
//        // Set default values
//        startStop.setEnabled(true);
//        first.setEnabled(false);
//        fastRewind.setEnabled(false);
//        rewind.setEnabled(false);
//        playPause.setEnabled(false);
//        forward.setEnabled(false);
//        fastForward.setEnabled(false);
//        last.setEnabled(false);
//        slider.setEnabled(false);
//
//        // Add listeners
//
//        startStop.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                controller.startStop();
//            }
//        });
//
//        first.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                controller.first();
//            }
//        });
//
//        fastRewind.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                controller.fastRewind();
//            }
//        });
//
//        rewind.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                controller.rewind();
//            }
//        });
//
//        forward.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                controller.forward();
//            }
//        });
//
//        fastForward.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                controller.fastForward();
//            }
//        });
//
//        last.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                controller.last();
//            }
//        });
//
//        playPause.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                controller.playPause();
//            }
//        });
//
//        slider.addChangeListener(new ChangeListener() {
//            @Override
//            public void stateChanged(ChangeEvent e) {
//                JSlider source = (JSlider) e.getSource();
//                int fps = source.getValue();
////                System.out.println(source);
//                controller.changeIndex(fps);
//            }
//        });
//    }
//
//    @Override
//    public void update()
//    {
//        if (game.isRunning())
//        {
//            startStop.setText("Stop");
//            playPause.setEnabled(true);
//
//            //Create the label table
////            Dictionary<Integer, JLabel> labelTable = new Hashtable<Integer, JLabel>();
////            labelTable.put(0, new JLabel(String.valueOf(0)) );
////            labelTable.put(slider.getMaximum(), new JLabel(String.valueOf(slider.getMaximum())) );
////            slider.setLabelTable( labelTable );
////            slider.setPaintLabels(true);
//
//            if (game.isPaused()) {
//                playPause.setText("Play");
//                first.setEnabled(true);
//                fastRewind.setEnabled(true);
//                rewind.setEnabled(true);
//                forward.setEnabled(true);
//                fastForward.setEnabled(true);
//                last.setEnabled(true);
//                slider.setEnabled(true);
//                slider.setMinimum(0);
//                slider.setMaximum(game.getCycles() - 1);
//                slider.setValue(game.getCurrentCycle());
//                if (game.getCurrentCycle() == 0) {
//                    first.setEnabled(false);
//                    rewind.setEnabled(false);
//                    fastRewind.setEnabled(false);
//                } else if (game.getCurrentCycle() == game.getCycles() - 1) {
//                    forward.setEnabled(false);
//                    fastForward.setEnabled(false);
//                    last.setEnabled(false);
//                }
//            } else {
//                playPause.setText("Pause");
//                first.setEnabled(false);
//                fastRewind.setEnabled(false);
//                rewind.setEnabled(false);
//                forward.setEnabled(false);
//                fastForward.setEnabled(false);
//                last.setEnabled(false);
//                slider.setEnabled(false);
//                slider.setMaximum(game.getCycles() - 1);
//                slider.setValue(game.getCycles() - 1);
//            }
//        } else {
//            startStop.setText("Start");
//        }
//    }
//
//}
