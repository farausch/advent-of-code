use std::collections::HashMap;

const INPUT_STONES: &str = "1 23 456";

fn get_stones(stones: &str) -> Vec<u64> {
    stones
        .split_whitespace()
        .filter_map(|s| s.parse::<u64>().ok())
        .collect()
}

fn blink_at_stone(stone: u64) -> Vec<u64> {
    let mut new_stones = Vec::new();
    if stone == 0 {
        new_stones.push(1);
    } else if stone.to_string().len() % 2 == 0 {
        let stone_str = stone.to_string();
        let mid = stone_str.len() / 2;
        let first_half = &stone_str[..mid];
        let second_half = &stone_str[mid..];
        if let (Ok(first), Ok(second)) = (first_half.parse::<u64>(), second_half.parse::<u64>()) {
            new_stones.push(first);
            new_stones.push(second);
        }
    } else {
        new_stones.push(stone * 2024);
    }
    new_stones
}

fn compute_stones_count(
    stone: u64,
    iterations: usize,
    memory: &mut HashMap<(u64, usize), usize>,
) -> usize {
    if let Some(&count) = memory.get(&(stone, iterations)) {
        return count;
    }

    let count = if iterations == 0 {
        1
    } else {
        blink_at_stone(stone)
            .iter()
            .map(|&new_stone| compute_stones_count(new_stone, iterations - 1, memory))
            .sum()
    };

    memory.insert((stone, iterations), count);
    count
}

fn main() {
    let stones = get_stones(INPUT_STONES);
    let mut memory = HashMap::new();
    let mut total_count = 0;
    for stone in stones {
        total_count += compute_stones_count(stone, 75, &mut memory);
    }
    println!("Final amount of stones: {}", total_count);
}