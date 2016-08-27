package dr.games.mspacman.view;

import java.awt.*;

public interface AgentView {

    void drawUnderPills(Graphics2D dbg2D, int startX, int startY, int nodeSize);
    
    void drawAbovePills(Graphics2D dbg2D);

    void drawAboveEverything(Graphics2D dbg2D);

}
