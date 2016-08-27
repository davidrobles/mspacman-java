package dr.games.mspacman.model;

import java.util.Map;

public interface GhostsAgent {

    Map<Ghost, Direction> direction(GameState gs);
    
}
