package dr.games.mspacman.model;

public enum Direction
{
    UP {
        @Override
        public Direction opposite() {
            return DOWN;
        }
    },

    LEFT {
        @Override
        public Direction opposite() {
            return RIGHT;
        }
    },

    DOWN {
        @Override
        public Direction opposite() {
            return UP;
        }
    },

    RIGHT {
        @Override
        public Direction opposite() {
            return LEFT;
        }
    },

    NEUTRAL {
        @Override
        public Direction opposite() {
            return NEUTRAL;
        }
    };

    public abstract Direction opposite();
}