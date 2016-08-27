package dr.games.mspacman.sc;

import java.util.List;
import java.util.ArrayList;
import java.awt.*;

public class ConnectedObject {

    private List<Point> points = new ArrayList<Point>();

    public void addPoint(Point point) {
        points.add(point);
    }

    public List<Point> getPoints() {
        return points;
    }

    public Point centerOfPoints() {
        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        int maxY = Integer.MIN_VALUE;
        for (Point point : points) {
            if (point.x < minX)
                minX = point.x;
            if (point.y < minY)
                minY = point.y;
            if (point.x > maxX)
                maxX = point.x;
            if (point.y > maxY)
                maxY = point.y;
        }
        return new Point(((minX + maxX) / 2), ((minY + maxY) / 2));
    }

}