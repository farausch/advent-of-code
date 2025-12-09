use std::fs;
use std::collections::HashSet;

#[derive(Debug, Clone, Copy, PartialEq, Eq, Hash)]
enum Direction {
    Up,
    Down,
    Left,
    Right,
}

impl Direction {
    fn from_char(c: char) -> Option<Direction> {
        match c {
            'u' => Some(Direction::Up),
            'd' => Some(Direction::Down),
            'l' => Some(Direction::Left),
            'r' => Some(Direction::Right),
            _ => None,
        }
    }

    fn to_char(&self) -> char {
        match self {
            Direction::Up => 'u',
            Direction::Down => 'd',
            Direction::Left => 'l',
            Direction::Right => 'r',
        }
    }

    fn turn_90_clockwise(&self) -> Direction {
        match self {
            Direction::Up => Direction::Right,
            Direction::Right => Direction::Down,
            Direction::Down => Direction::Left,
            Direction::Left => Direction::Up,
        }
    }
}

const OBSTACLE_STRING: char = '#';

fn get_input_grid(filename: &str) -> Vec<Vec<char>> {
    let contents = fs::read_to_string(filename).expect("Failed to read file");
    contents
        .lines()
        .map(|line| line.trim().replace("^", "u").chars().collect())
        .collect()
}

fn get_pointer_pos(grid: &[Vec<char>]) -> Option<(usize, usize, Direction)> {
    for (row_idx, row) in grid.iter().enumerate() {
        for (col_idx, &col) in row.iter().enumerate() {
            if let Some(direction) = Direction::from_char(col) {
                return Some((row_idx, col_idx, direction));
            }
        }
    }
    None
}

fn is_obstacle_ahead(
    grid: &[Vec<char>],
    pointer_pos: (usize, usize),
    direction: Direction,
) -> bool {
    let (row, col) = pointer_pos;
    match direction {
        Direction::Up if row > 0 => grid[row - 1][col] == OBSTACLE_STRING,
        Direction::Down if row + 1 < grid.len() => grid[row + 1][col] == OBSTACLE_STRING,
        Direction::Left if col > 0 => grid[row][col - 1] == OBSTACLE_STRING,
        Direction::Right if col + 1 < grid[row].len() => grid[row][col + 1] == OBSTACLE_STRING,
        _ => false,
    }
}

fn is_gridout_ahead(
    grid: &[Vec<char>],
    pointer_pos: (usize, usize),
    direction: Direction,
) -> bool {
    let (row, col) = pointer_pos;
    match direction {
        Direction::Up => row == 0,
        Direction::Down => row == grid.len() - 1,
        Direction::Left => col == 0,
        Direction::Right => col == grid[row].len() - 1,
    }
}

fn turn_90_clockwise(grid: &mut Vec<Vec<char>>, pointer_pos: (usize, usize), direction: Direction) {
    let (row, col) = pointer_pos;
    grid[row][col] = direction.turn_90_clockwise().to_char();
}

fn move_pointer(
    grid: &mut Vec<Vec<char>>,
    pointer_pos: (usize, usize),
    direction: Direction,
    visited_field_char: char,
) -> (usize, usize) {
    let (row, col) = pointer_pos;
    grid[row][col] = visited_field_char;
    match direction {
        Direction::Up => (row - 1, col),
        Direction::Down => (row + 1, col),
        Direction::Left => (row, col - 1),
        Direction::Right => (row, col + 1),
    }
}

fn main() {
    // Section B
    let input_grid = get_input_grid("input.txt");
    let mut loop_causing_obstacles = 0;
    let rows = input_grid.len();
    let cols = input_grid[0].len();

    for row in 0..rows {
        for col in 0..cols {
            let mut visited_positions: HashSet<(usize, usize, char)> = HashSet::new();
            let mut test_grid = get_input_grid("input.txt");
            let (mut test_row, mut test_col, mut test_direction) = get_pointer_pos(&test_grid).expect("No pointer found in test grid");
            if (row, col) == (test_row, test_col) {
                continue;
            }
            test_grid[row][col] = OBSTACLE_STRING;

            while !is_gridout_ahead(&test_grid, (test_row, test_col), test_direction) {
                if visited_positions.contains(&(test_row, test_col, test_grid[test_row][test_col]))
                {
                    loop_causing_obstacles += 1;
                    break;
                }
                // In Python we have visited_positions.append((pointer_pos_row, pointer_pos_col, input_grid[pointer_pos_row][pointer_pos_col])) reading the direction from the grid
                // Reason: move_pointer here returns the new position, but in Python it also inserts the direction in that position.
                // Therefore while in Python we see the pointer and direction in the grid, here we only see the pointer and the direction is stored in the variable test_direction with its latest state.
                visited_positions.insert((test_row, test_col, test_direction.to_char()));

                if is_obstacle_ahead(&test_grid, (test_row, test_col), test_direction) {
                    turn_90_clockwise(&mut test_grid, (test_row, test_col), test_direction);
                    test_direction = Direction::from_char(test_grid[test_row][test_col]).unwrap();
                } else {
                    let new_pos = move_pointer(&mut test_grid, (test_row, test_col), test_direction, '.');
                    test_row = new_pos.0;
                    test_col = new_pos.1;
                }
            }
        }
    }

    println!(
        "Section B number of possible loop causing obstacles: {}",
        loop_causing_obstacles
    );
}
