import java.util.*;

public class Cave {

    private static final char EMPTY = '.';
    private static final char WALL = 'W';
    private static final char START = '>';
    private static final char FINISH = '<';
    private static final char COLLAPSE = 'C';

    private final int width;
    private final int height;
    private final int wallCount;
    private final int collapseCount;
    private final char[][] grid;
    private final Random random = new Random();

    public Cave(int width, int height, int wallCount, int collapseCount) {
        this.width = width;
        this.height = height;
        this.wallCount = wallCount;
        this.collapseCount = collapseCount;
        this.grid = new char[height][width];
        for (char[] row : grid) {
            Arrays.fill(row, EMPTY);
        }
    }

    public void generate() {
        placeStartFinish();
        placeWalls();
        placeCollapseSpots();
    }

    private void placeStartFinish() {
        int startY = random.nextInt(height);
        int finishY = random.nextInt(height);
        grid[startY][0] = START;
        grid[finishY][width - 1] = FINISH;
    }

    private void placeWalls() {
        for (int i = 0; i < wallCount; i++) {
            boolean horizontal = random.nextBoolean();
            int length = 1 + random.nextInt(5);
            boolean placed = false;

            for (int attempt = 0; attempt < 100 && !placed; attempt++) {
                int x = random.nextInt(width);
                int y = random.nextInt(height);
                if (canPlaceWall(x, y, length, horizontal)) {
                    for (int j = 0; j < length; j++) {
                        int nx = horizontal ? x + j : x;
                        int ny = horizontal ? y : y + j;
                        grid[ny][nx] = WALL;
                    }
                    placed = true;
                }
            }
        }
    }

    private boolean canPlaceWall(int x, int y, int length, boolean horizontal) {
        for (int i = 0; i < length; i++) {
            int nx = horizontal ? x + i : x;
            int ny = horizontal ? y : y + i;
            if (nx >= width || ny >= height || grid[ny][nx] != EMPTY) {
                return false;
            }
        }
        return true;
    }

    private void placeCollapseSpots() {
        int placed = 0;
        for (int attempts = 0; attempts < 10000 && placed < collapseCount; attempts++) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            if (grid[y][x] == EMPTY) {
                grid[y][x] = COLLAPSE;
                applyHollowSound(x, y);
                placed++;
            }
        }
        if (placed < collapseCount) {
            throw new IllegalStateException("Not enough space to place all collapse spots");
        }
    }

    private void applyHollowSound(int cx, int cy) {
        for (int dy = -1; dy <= 1; dy++) {
            for (int dx = -1; dx <= 1; dx++) {
                int nx = cx + dx;
                int ny = cy + dy;
                if (dx == 0 && dy == 0) continue;
                if (nx >= 0 && ny >= 0 && nx < width && ny < height) {
                    char c = grid[ny][nx];
                    if (c == EMPTY) {
                        grid[ny][nx] = '1';
                    } else if (Character.isDigit(c)) {
                        if (c < '9') {
                            grid[ny][nx] = (char) (c + 1);
                        }
                    }
                }
            }
        }
    }

    public void print() {
        for (char[] row : grid) {
            for (char c : row) {
                System.out.print(c + " ");
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        if (args.length != 4) {
            System.out.println("Usage: java Cave <width> <height> <wallCount> <collapseCount>");
            return;
        }
        int width = Integer.parseInt(args[0]);
        int height = Integer.parseInt(args[1]);
        int walls = Integer.parseInt(args[2]);
        int collapses = Integer.parseInt(args[3]);

        System.out.printf("Creating a play field of size %dx%d with %d walls and %d collapsing spots\n", width, height, walls, collapses);

        Cave cave = new Cave(width, height, walls, collapses);
        cave.generate();
        cave.print();
    }
}
