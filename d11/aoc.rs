use std::collections::HashMap;

const INPUT_STONES: &str = "5910927 0 1 47 261223 94788 545 7771";

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
    } else {
        let num_digits = (stone as f64).log10().floor() as usize + 1;
        if num_digits % 2 == 0 {
            let mid = num_digits / 2;
            let divisor = 10_u64.pow(mid as u32);
            let first_half = stone / divisor;
            let second_half = stone % divisor;
            new_stones.push(first_half);
            new_stones.push(second_half);
        } else {
            new_stones.push(stone * 2024);
        }
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