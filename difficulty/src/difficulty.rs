use std::fmt::{Display, Error, Result, Formatter};

use crate::difficulty::DifficultyRating::{Deadly, Easy, Hard, Medium};

#[derive(Debug, PartialEq)]
pub enum DifficultyRating {
    Easy,
    Medium,
    Hard,
    Deadly,
}

impl Display for DifficultyRating {

    fn fmt(&self, f: &mut Formatter) -> Result {
        f.write_str(match self {
            Easy => "Easy",
            Medium => "Medium",
            Hard => "Hard",
            Deadly => "Deadly"
        }).expect("Unable to display DifficultyRating.");

        Ok(())
    }
}

#[derive(Debug)]
pub struct Monster {
    pub cr: String,
    pub count: u8,
}

const GROUP_MULTIPLIERS: [f32;6] = [1.0, 1.5, 2.0, 2.5, 3.0, 4.0];

pub fn calculate_difficulty(party_level: u8, party_size: u8, monsters: &Vec<Monster>) -> DifficultyRating {
    let xp_thresholds = calculate_xp_thresholds(party_level, party_size);
    debug!("XP-Thresholds: {:?}", xp_thresholds);

    let monster_xp_total = calculate_monster_xp(party_size, monsters);
    debug!("Monsters-XP: {}", monster_xp_total);

    // find DR that matches or is closest to but lower than monster xp
    match xp_thresholds.iter().enumerate().filter(|(_,x)| { monster_xp_total <= **x }).rev().last() {
        Some((idx,xp)) => {
            debug!("Found threshold: ({}, {})", idx, xp);

            match idx {
                0 => Easy,
                1 => Medium,
                2 => Hard,
                _ => Deadly
            }
        },
        None => Deadly
    }
}

fn calculate_xp_thresholds(party_level:u8, party_size:u8) -> [u32;4] {
    let thresholds = match party_level {
        1 => [25, 50,75, 100],
        2 => [50,100,150,200],
        3 => [75,150,225, 400],
        4 => [125, 250, 375,500],
        5 => [250, 500, 750, 1100],
        6 => [300,600,900,1400],
        7 => [350,750,1100,1700],
        8 => [450, 900, 1400, 2100],
        9 => [550, 1100, 1600, 2400],
        10 => [600, 1200, 1900, 2800],
        11 => [800, 1600, 2400, 3600],
        12 => [1000, 2000, 3000, 4500],
        13 => [1100, 2200, 3400, 5100],
        14 => [1250, 2500, 3800, 5700],
        15 => [1400, 2800, 4300, 6400],
        16 => [1600, 3200, 4800, 7200],
        17 => [2000, 3900, 5900, 8800],
        18 => [2100, 4200, 6300, 9500],
        19 => [2400, 4900, 7300, 10900],
        20 => [2800, 5700, 8500, 12700],
        _ => panic!(format!("Invalid party level ({}) specified!", party_level))
    };

    let party_size = party_size as u32;
    [thresholds[0] * party_size, thresholds[1] * party_size, thresholds[2] * party_size, thresholds[3] * party_size]
}

fn calculate_monster_xp(party_size: u8, monsters: &Vec<Monster>) -> u32 {
    // sum xp of all monsters
    let raw_xp_total : u32 = monsters.iter().map(|m| { resolve_xp(&m.cr) * m.count as u32 }).sum();

    // resolve multiplier and with party_size adjustment
    let multiplier = resolve_multiplier(party_size, monsters.len() as u8);

    // apply multiplier to monster xp and return
    (raw_xp_total as f32 * multiplier) as u32
}

fn resolve_xp(cr: &String) -> u32 {
    match cr.as_str() {
        "0" => 10,
        "1/8" => 25,
        "1/4" => 50,
        "1/2" => 100,
        "1" => 200,
        "2" => 450,
        "3" => 700,
        "4" => 1100,
        "5" => 1800,
        "6" => 2300,
        "7" => 2900,
        "8" => 3900,
        "9" => 5000,
        "10" => 5900,
        "11" => 7200,
        "12" => 8400,
        "13" => 10000,
        "14" => 11500,
        "15" => 13000,
        "16" => 15000,
        "17" => 18000,
        "18" => 20000,
        "19" => 22000,
        "20" => 25000,
        "21" => 33000,
        "22" => 41000,
        "23" => 50000,
        "24" => 62000,
        "25" => 75000,
        "26" => 90000,
        "27" => 105000,
        "28" => 120000,
        "29" => 135000,
        "30" => 155000,
        _ => panic!(format!("Invalid CR specified: {}", cr))
    }
}

fn resolve_multiplier(party_size: u8, monster_count: u8) -> f32 {
    let mut multiplier_index = match monster_count {
        1 => 0,
        2 => 1,
        3...6 => 2,
        7...10 => 3,
        11...14 => 4,
        _ => 5
    };

    if party_size < 3 && multiplier_index < 5 {
        multiplier_index = multiplier_index +  1;
    } else if party_size > 6 && multiplier_index > 0 {
        multiplier_index = multiplier_index - 1;
    }

    GROUP_MULTIPLIERS[multiplier_index]
}

#[cfg(test)]
mod tests {
    use crate::difficulty::{calculate_difficulty, Monster};
    use crate::difficulty::DifficultyRating::{Deadly, Medium};

    #[test] fn calculate_single_challenger() {
        let challengers = vec![Monster { cr: "1".to_string(), count: 1 }];

        let diff = calculate_difficulty(1, 1, &challengers);

        assert_eq!(Deadly, diff);
    }

    #[test] fn calculate_4_party_single_monster() {
        let challengers = vec![Monster { cr: "1".to_string(), count: 1 }];

        let diff = calculate_difficulty(1, 4, &challengers);

        assert_eq!(Medium, diff);
    }

    // FIXME: more tests
}

