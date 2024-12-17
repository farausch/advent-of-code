use std::fs::File;
use std::io::{self, BufRead};
use std::num::ParseIntError;
  
fn read_file_to_arrays(file_path: &str) -> Result<(Vec<i32>, Vec<i32>), io::Error> {  
    let mut a1 = Vec::new();  
    let mut a2 = Vec::new();  
  
    let file = File::open(file_path)?;  
    let reader = io::BufReader::new(file);  
  
    for line in reader.lines() {  
        let line = line?;
        if let Some((left, right)) = line.split_once(';') {  
            let num1 = left.trim().parse::<i32>().map_err(|e: ParseIntError| {  
                io::Error::new(io::ErrorKind::InvalidData, e.to_string())  
            })?;  
            let num2 = right.trim().parse::<i32>().map_err(|e: ParseIntError| {  
                io::Error::new(io::ErrorKind::InvalidData, e.to_string())  
            })?;  
            a1.push(num1);  
            a2.push(num2);  
        }  
    }  
  
    Ok((a1, a2))  
}  

fn sort<A, T>(mut array: A) -> A
where
    A: AsMut<[T]>,
    T: Ord,
{
    let slice = array.as_mut();
    slice.sort();

    array
}

fn main() {
    // Section A
    let file_path = "input.txt";

    let (a1, a2) = read_file_to_arrays(file_path).unwrap();
    let a1_sorted = sort(a1);
    let a2_sorted = sort(a2);
    let mut pos_dist_sum: i32 = 0;

    for n in 0..a1_sorted.len() {
        pos_dist_sum += (a1_sorted[n] - a2_sorted[n]).abs();
    }
    println!("Section A sum of absolute differences: {}", pos_dist_sum);

    // Section B
    let mut sim_score: i32 = 0;
    for n in 0..a1_sorted.len() {
        let mut left_occurences_right = 0;
        for m in 0..a2_sorted.len() {
            if a1_sorted[n] == a2_sorted[m] {
                left_occurences_right += 1;
            }
        }
        sim_score += a1_sorted[n] * left_occurences_right;
    }
    println!("Section B similarity score: {}", sim_score);
}