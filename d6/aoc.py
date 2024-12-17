from enum import Enum

def get_input_grid(filename: str) -> list[list[str]]:
    with open(filename) as f:
        return [list(line.strip().replace("^", "u")) for line in f]

OBSTACLE_STRING = "#"
class Direction(Enum):
    UP = "u"
    DOWN = "d"
    LEFT = "l"
    RIGHT= "r"

def get_pointer_pos(grid: list[list[str]]) -> tuple[int, int]:
    for row_idx, row in enumerate(grid):
        for col_idx, col in enumerate(row):
            if grid[row_idx][col_idx] in [d.value for d in Direction]:
                return (row_idx, col_idx)

def is_obstacle_ahead(grid: list[list[str]], pointer_pos_row, pointer_pos_col, direction: Direction) -> bool:
    if direction == Direction.UP:
        return grid[pointer_pos_row - 1][pointer_pos_col] == OBSTACLE_STRING
    elif direction == Direction.DOWN:
        return grid[pointer_pos_row + 1][pointer_pos_col] == OBSTACLE_STRING
    elif direction == Direction.LEFT:
        return grid[pointer_pos_row][pointer_pos_col - 1] == OBSTACLE_STRING
    elif direction == Direction.RIGHT:
        return grid[pointer_pos_row][pointer_pos_col + 1] == OBSTACLE_STRING

def is_gridout_ahead(grid: list[list[str]], pointer_pos_row, pointer_pos_col, direction: Direction) -> bool:
    if direction == Direction.UP:
        return pointer_pos_row == 0
    elif direction == Direction.DOWN:
        return pointer_pos_row == len(grid) - 1
    elif direction == Direction.LEFT:
        return pointer_pos_col == 0
    elif direction == Direction.RIGHT:
        return pointer_pos_col == len(grid[0]) - 1

def turn_90_clockwise(grid: list[list[str]], pointer_pos_row, pointer_pos_col, direction: Direction) -> list[list[str]]:
    if direction == Direction.UP:
        grid[pointer_pos_row][pointer_pos_col] = Direction.RIGHT.value
    elif direction == Direction.DOWN:
        grid[pointer_pos_row][pointer_pos_col] = Direction.LEFT.value
    elif direction == Direction.LEFT:
        grid[pointer_pos_row][pointer_pos_col] = Direction.UP.value
    elif direction == Direction.RIGHT:
        grid[pointer_pos_row][pointer_pos_col] = Direction.DOWN.value
    return grid

def move_pointer(grid: list[list[str]], pointer_pos_row, pointer_pos_col, direction: Direction) -> list[list[str]]:
    grid[pointer_pos_row][pointer_pos_col] = VISITED_FIELD_STRING
    if direction == Direction.UP:
        grid[pointer_pos_row - 1][pointer_pos_col] = Direction.UP.value
    elif direction == Direction.DOWN:
        grid[pointer_pos_row + 1][pointer_pos_col] = Direction.DOWN.value
    elif direction == Direction.LEFT:
        grid[pointer_pos_row][pointer_pos_col - 1] = Direction.LEFT.value
    elif direction == Direction.RIGHT:
        grid[pointer_pos_row][pointer_pos_col + 1] = Direction.RIGHT.value
    return grid

def count_visited_pos(grid: list[list[str]]) -> int:
    return sum([row.count(VISITED_FIELD_STRING) for row in grid]) + 1

# Section A
VISITED_FIELD_STRING = "X"
input_grid = get_input_grid("input_test.txt")
pointer_pos_row, pointer_pos_col = get_pointer_pos(input_grid)
current_direction = Direction(input_grid[pointer_pos_row][pointer_pos_col])

while not is_gridout_ahead(input_grid, pointer_pos_row, pointer_pos_col, current_direction):
    if is_obstacle_ahead(input_grid, pointer_pos_row, pointer_pos_col, current_direction):
        input_grid = turn_90_clockwise(input_grid, pointer_pos_row, pointer_pos_col, current_direction)
        current_direction = Direction(input_grid[pointer_pos_row][pointer_pos_col])
    else:
        input_grid = move_pointer(input_grid, pointer_pos_row, pointer_pos_col, current_direction)
        pointer_pos_row, pointer_pos_col = get_pointer_pos(input_grid)
print("Section A number of visited fields:", count_visited_pos(input_grid))

# Section B
# Logic is ok, but Python efficiency is not enough. See aoc.rs for Rust implementation
loop_causing_obstacles = 0
VISITED_FIELD_STRING = "."

for row_idx in range(len(input_grid)):
    for col_idx in range(len(input_grid[0])):
        input_grid = get_input_grid("input_test.txt")
        visited_pos_log = []
        pointer_pos_row, pointer_pos_col = get_pointer_pos(input_grid)
        if pointer_pos_row == row_idx and pointer_pos_col == col_idx:
            continue
        current_direction = Direction(input_grid[pointer_pos_row][pointer_pos_col])
        input_grid[row_idx][col_idx] = OBSTACLE_STRING
        while not is_gridout_ahead(input_grid, pointer_pos_row, pointer_pos_col, current_direction):
            if (pointer_pos_row, pointer_pos_col, input_grid[pointer_pos_row][pointer_pos_col]) in visited_pos_log:
                loop_causing_obstacles += 1
                break
            visited_pos_log.append((pointer_pos_row, pointer_pos_col, input_grid[pointer_pos_row][pointer_pos_col]))
            if is_obstacle_ahead(input_grid, pointer_pos_row, pointer_pos_col, current_direction):
                input_grid = turn_90_clockwise(input_grid, pointer_pos_row, pointer_pos_col, current_direction)
                current_direction = Direction(input_grid[pointer_pos_row][pointer_pos_col])
            else:
                input_grid = move_pointer(input_grid, pointer_pos_row, pointer_pos_col, current_direction)
                pointer_pos_row, pointer_pos_col = get_pointer_pos(input_grid)
print("Section B number of possible loop causing obstacles:", loop_causing_obstacles)
