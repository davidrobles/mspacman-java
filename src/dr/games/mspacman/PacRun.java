package dr.games.mspacman;

import dr.games.mspacman.agents.KeyboardController;
import dr.games.mspacman.agents.PillEater;
import dr.games.mspacman.agents.ProGhostsController;
import dr.games.mspacman.agents.RealGhosts;
import dr.games.mspacman.agents.mc.NewPureMC;
import dr.games.mspacman.agents.mc.PureMC;
import dr.games.mspacman.agents.mc.PureMCWithDiscount;
import dr.games.mspacman.agents.mcpaths.PathMCAgent;
import dr.games.mspacman.agents.uct.PacUCT;
import dr.games.mspacman.agents.uct.PacUCTWithGhostsMinimaxing;
import dr.games.mspacman.model.*;
import dr.games.mspacman.util.PacStats;
import dr.games.mspacman.view.AgentView;
import dr.games.mspacman.view.GameStateView;
import dr.games.mspacman.view.PacFrame;
import dr.util.ElapsedTimer;

import java.util.Map;

public class     PacRun {

        // ONLY GAME STATE

        static void runGameManually()
        {
            PacAgent pacAgent = new PathMCAgent();
            GhostsAgent ghostsAgent = new RealGhosts();

            GameState gameState = GameState.newDefaultGameState();
            GameStateView view = new GameStateView();
            gameState.registerGameStateObserver(view);
            new PacFrame(view);

            while (!gameState.isGameOver())
            {
                // Get PacMan move
                Direction pacDesDir = pacAgent.direction(gameState);
                // Get Ghosts moves
                Map<Ghost, Direction> ghostsDesDirs = ghostsAgent.direction(gameState);
                // Make moves
                gameState.nextState(pacDesDir, ghostsDesDirs);
                // Delay game
                try { Thread.sleep(0); } catch (InterruptedException ignored) { }
            }
        }

    static void runSimulations(PacAgent agent, GhostsAgent ghostsAgent, int nSims)
    {
        PacStats stats = new PacStats();
        ElapsedTimer timer = new ElapsedTimer();

        System.out.printf("Running %d simulations...%n%n", nSims);

        PacGame game = new PacGame(agent, ghostsAgent, 0);

        // Run simulations
        for (int i = 1; i <= nSims; i++) {
            timer.reset();
            game.reset();
            System.out.print("Simulation " + i + "... ");
            game.runSimulation();
            System.out.println(game.getGameState().getScore());
            stats.addResult(
                    game.getGameState().getScore(),
                    game.getGameState().getTimeSteps(),
                    game.getGameState().getMazeIndex(),
                    timer.elapsedSeconds());
        }

        stats.printSummary();
    }

    static void runGameWithKeyboard(GhostsAgent ghostsAgent, int delay)
    {
        KeyboardController agent = new KeyboardController();
        PacGame game = new PacGame(agent, ghostsAgent, delay);
        game.getGameState().print = true; 
        GameStateView view = GameStateView.newGameStateViewFramed();
        game.registerGameStateObserver(view);
        view.addKeyListener(agent);
        game.start();
    }

    static void runGameWithAgent(PacAgent agent, GhostsAgent ghostsAgent, int delay)
    {
        PacGame game = new PacGame(agent, ghostsAgent, delay);
        GameStateView view = GameStateView.newGameStateViewFramed();
        view.addExtras((AgentView) agent);
//        view.addExtras((AgentView) ghostsAgent);
        game.registerGameStateObserver(view);
        view.addKeyListener(game);
        game.run();
    }

    public static void main(String[] args)
    {
//        runGameWithKeyboard(new RealGhosts(), 2000);
        runGameWithAgent(new PacUCTWithGhostsMinimaxing(), new ProGhostsController(0.80, 0.60, 0.40, 0.20), 0);
//        runGameWithAgent(new PathMCAgent(), new ProGhostsController(0.80, 0.60, 0.40, 0.20), 15);
//        runSimulations(new PathMCAgent(), new ProGhostsController(0.80, 0.60, 0.40, 0.20), 10);
    }

}
