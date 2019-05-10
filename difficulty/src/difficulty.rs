#[derive(Debug)]
pub enum DifficultyRating {
    Easy,
    Medium,
    Hard,
    Deadly,
}

pub struct Challenger {
    pub cr: String,
    pub count: u8,
}

pub fn calculate_difficulty(level: u8, challengers: &Vec<Challenger>) -> DifficultyRating {
    let effective_xp = calculate_effective_xp(challengers);
    resolve_difficulty(level, effective_xp)
}

fn calculate_effective_xp(challengers: &Vec<Challenger>) -> u16 {
    let mut total_xp: f32 = 0.0;
    let mut challenger_count = 0;

    for chall in challengers.iter() {
        total_xp = total_xp + fetch_xp_for_cr(chall.cr.as_str()) * (chall.count as f32);
        challenger_count += chall.count;
    }

    let multiplier = resolve_multiplier(challenger_count);

    (total_xp * multiplier) as u16
}

fn fetch_xp_for_cr(cr: &str) -> f32 {
    match cr {
        "0" => 10_f32,
        "1/8" => 25_f32,
        "1/4" => 50_f32,
        "1/2" => 100_f32,
        "1" => 200_f32,
        "2" => 450_f32,
        "3" => 700_f32,
        "4" => 1100_f32,
        "5" => 1800_f32,
        "6" => 2300_f32,
        "7" => 2900_f32,
        "8" => 3900_f32,
        "9" => 5000_f32,
        "10" => 5900_f32,
        "11" => 7200_f32,
        "12" => 8400_f32,
        "13" => 10000_f32,
        "14" => 11500_f32,
        "15" => 11500_f32,
        "16" => 15000_f32,
        "17" => 18000_f32,
        "18" => 20000_f32,
        "19" => 22000_f32,
        "20" => 25000_f32,
        "21" => 33000_f32,
        "22" => 41000_f32,
        "23" => 50000_f32,
        "24" => 62000_f32,
        "25" => 75000_f32,
        "26" => 90000_f32,
        "27" => 105000_f32,
        "28" => 120000_f32,
        "29" => 135000_f32,
        "30" => 155000_f32,
        _ => panic!(format!("Invalid CR value: {}", cr))
    }
}

fn resolve_multiplier(challenger_count: u8) -> f32 {
    match challenger_count {
        0 => 0.0,
        1 => 1.0,
        2 => 1.5,
        3 => 2.0,
        4 => 2.0,
        5 => 2.0,
        6 => 2.0,
        7 => 2.5,
        8 => 2.5,
        9 => 2.5,
        10 => 2.5,
        11 => 3.0,
        12 => 3.0,
        13 => 3.0,
        14 => 3.0,
        _ => 4.0
    }
}

fn resolve_difficulty(level: u8, challenger_xp: u16) -> DifficultyRating {
    let level_thresholds = resolve_difficulty_values(level);

    for (thresh, idx) in level_thresholds.iter().rev().enumerate() {
        if thresh < (challenger_xp as usize) {
            return match idx {
                0 => DifficultyRating::Deadly,
                1 => DifficultyRating::Hard,
                2 => DifficultyRating::Medium,
                _ => DifficultyRating::Easy
            };
        }
    }

    panic!("Unable to calculate difficulty!");
}

fn resolve_difficulty_values(level: u8) -> [u16; 4] {
    match level {
        1 => [100, 200, 300, 400],
        2 => [200, 400, 636, 800],
        3 => [300, 600, 900, 1600],
        4 => [500, 1000, 1500, 2000],
        5 => [1000, 2000, 3000, 4400],
        6 => [1200, 2400, 3600, 5600],
        7 => [1400, 3000, 4400, 6800],
        8 => [1800, 3600, 5600, 8400],
        9 => [2200, 4400, 6400, 9600],
        10 => [2400, 4800, 7600, 11200],
        11 => [3200, 6400, 9600, 14400],
        12 => [4000, 8000, 12000, 18000],
        13 => [4400, 8800, 13600, 20400],
        14 => [5000, 10000, 15200, 22800],
        15 => [5600, 11200, 17200, 25600],
        16 => [6400, 12800, 19200, 28800],
        17 => [8000, 15600, 23600, 35200],
        18 => [8400, 16800, 25200, 38000],
        19 => [9600, 19600, 29200, 43600],
        20 => [11200, 22800, 34400, 50800],
        21 => [12000, 24000, 36000, 54000],
        _ => panic!(format!("Invalid level value: {}", level))
    }
}

#[cfg(test)]
mod tests {
    use crate::difficulty::{calculate_difficulty, Challenger};

    #[test]
    fn calculate_single_challenger() {
        let challengers = vec![Challenger { cr: String::from("10"), count: 4 }];

        let diff = calculate_difficulty(1, &challengers);
        something is wrong... need some values from the other app to compare

        println!("{:?}", diff);
    }
}

