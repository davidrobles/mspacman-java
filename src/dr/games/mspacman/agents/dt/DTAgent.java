//package dr.games.mspacman.agents.dt;
//
//import dr.dt.Action;
//import dr.dt.FloatDecision;
//import dr.games.mspacman.model.Direction;
//import dr.games.mspacman.model.GameState;
//import dr.games.mspacman.model.Ghost;
//import dr.games.mspacman.model.PacAgent;
//
//public class DTAgent implements PacAgent {
//
//    GameState gameState;
//
//    // actions
//    Action up = new Action("Up");
//    Action right = new Action("Right");
//    Action down = new Action("Down");
//    Action left = new Action("Left");
//
//    // decisions
//    FloatDecision root = new FloatDecision("Under Attack?", down, up, 0, 10);
//
//    @Override
//    public Direction direction(GameState gameState) {
//        this.gameState = gameState;
//        updateValues();
//        Action selAct = (Action) root.makeDecision();
//        return actionToDir(selAct);
//    }
//
//    public Direction actionToDir(Action action)
//    {
//        if (action == up)
//            return Direction.UP;
//        else if (action == right)
//            return Direction.RIGHT;
//        else if (action == down)
//            return Direction.DOWN;
//        else if (action == left)
//            return Direction.LEFT;
//        else
//            return Direction.NEUTRAL;
//    }
//
//    public void updateValues()
//    {
//        int nearestDistance = Integer.MAX_VALUE;
//
//        for (Ghost ghost : gameState.getGhosts()) {
//            int ghostDist = gameState.getMazeState().getMaze().distance(ghost.getCurrentNode(),
//                    gameState.getPacman().getCurrentNode());
//            if (ghostDist < nearestDistance) {
//                nearestDistance = ghostDist;
//            }
//        }
//
//        root.setValue(nearestDistance);
//    }
//
//}
