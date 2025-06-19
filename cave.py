import sys
import random

EMPTY = '.'
WALL = 'W'
START = '>'
FINISH = '<'
COLLAPSE = 'C'

class Cave:
    def __init__(self, width, height, wall_count, collapse_count):
        self.width = width
        self.height = height
        self.wall_count = wall_count
        self.collapse_count = collapse_count
        self.grid = [[EMPTY for _ in range(width)] for _ in range(height)]

    def generate(self):
        self.place_start_finish()
        self.place_walls()
        self.place_collapse_spots()

    def place_start_finish(self):
        start_y = random.randint(0, self.height - 1)
        finish_y = random.randint(0, self.height - 1)
        self.grid[start_y][0] = START
        self.grid[finish_y][self.width - 1] = FINISH

    def place_walls(self):
        for _ in range(self.wall_count):
            for _ in range(100):
                horizontal = random.choice([True, False])
                length = random.randint(1, 5)
                x = random.randint(0, self.width - 1)
                y = random.randint(0, self.height - 1)

                if self.can_place_wall(x, y, length, horizontal):
                    for i in range(length):
                        nx = x + i if horizontal else x
                        ny = y if horizontal else y + i
                        self.grid[ny][nx] = WALL
                    break

    def can_place_wall(self, x, y, length, horizontal):
        for i in range(length):
            nx = x + i if horizontal else x
            ny = y if horizontal else y + i
            if nx >= self.width or ny >= self.height or self.grid[ny][nx] != EMPTY:
                return False
        return True

    def place_collapse_spots(self):
        placed = 0
        for _ in range(10000):
            if placed >= self.collapse_count:
                break
            x = random.randint(0, self.width - 1)
            y = random.randint(0, self.height - 1)
            if self.grid[y][x] == EMPTY:
                self.grid[y][x] = COLLAPSE
                self.apply_hollow_sound(x, y)
                placed += 1
        if placed < self.collapse_count:
            raise RuntimeError("Not enough space to place all collapse spots")

    def apply_hollow_sound(self, cx, cy):
        for dy in range(-1, 2):
            for dx in range(-1, 2):
                if dx == 0 and dy == 0:
                    continue
                nx, ny = cx + dx, cy + dy
                if 0 <= nx < self.width and 0 <= ny < self.height:
                    current = self.grid[ny][nx]
                    if current == EMPTY:
                        self.grid[ny][nx] = '1'
                    elif current.isdigit():
                        if current < '9':
                            self.grid[ny][nx] = chr(ord(current) + 1)

    def print_grid(self):
        for row in self.grid:
            print(' '.join(row))


def main():
    if len(sys.argv) != 5:
        print("Usage: python cave.py <width> <height> <wallCount> <collapseCount>")
        return

    width = int(sys.argv[1])
    height = int(sys.argv[2])
    wall_count = int(sys.argv[3])
    collapse_count = int(sys.argv[4])

    print(f"Creating a play field of size {width}x{height} with {wall_count} walls and {collapse_count} collapsing spots")

    cave = Cave(width, height, wall_count, collapse_count)
    cave.generate()
    cave.print_grid()


if __name__ == "__main__":
    main()
