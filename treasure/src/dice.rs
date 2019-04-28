extern crate regex;

use std::fmt;

use rand::distributions::{Distribution, Uniform};
use regex::Regex;

#[derive(Debug)]
pub struct DieRoll {
    pub count: u16,
    pub d: u16,
    pub modifier: u16,
    pub multiplier: u16,
}

impl fmt::Display for DieRoll {
    fn fmt(&self, f: &mut fmt::Formatter) -> fmt::Result {
        write!(f, "{}d{}+{}x{}", self.count, self.d, self.modifier, self.multiplier)
    }
}

impl DieRoll {
    pub fn new(dice: &str) -> DieRoll {
        let roller = if dice.is_empty() || dice == "-" {
            DieRoll { count: 0, d: 1, modifier: 0, multiplier: 0 }
        } else if !dice.contains("d") {
            // consider it a constant
            DieRoll { count: DieRoll::str_to_num(dice, 1), d: 1, modifier: 0, multiplier: 1 }
        } else {
            let rx = Regex::new("([0-9]*)d([0-9]*)[+]?([0-9]*)[x]?([0-9]*)").unwrap();

            if rx.is_match(dice) {
                let groups = rx.captures(dice).unwrap();

                DieRoll {
                    count: DieRoll::str_to_num(groups.get(1).unwrap().as_str(), 1),
                    d: DieRoll::str_to_num(groups.get(2).unwrap().as_str(), 0),
                    modifier: DieRoll::str_to_num(groups.get(3).unwrap().as_str(), 0),
                    multiplier: DieRoll::str_to_num(groups.get(4).unwrap().as_str(), 1),
                }
            } else {
                // This should only happen with a misconfiguration of the tables
                panic!("Unable to create DieRoll for ({}): check table config!", dice);
            }
        };

        trace!("DieRoll: {}", roller);

        return roller;
    }

    fn str_to_num(value: &str, default_value: u16) -> u16 {
        value.parse().unwrap_or(default_value)
    }

    pub fn roll(&self) -> u16 {
        let mut rolls = 0;

        let dice = &self;

        trace!("Rolling: {}", dice);

        let mut rng = rand::thread_rng();
        let die = Uniform::from(1..dice.d + 1);

        // TODO: use fold iter here
        for _r in 0..dice.count {
            let rolled = die.sample(&mut rng);
            rolls += rolled;
        }

        let result = (rolls + dice.modifier) * dice.multiplier;

        debug!("Rolled ({}): {}", dice, result);

        return result;
    }
}

//
// TESTS
//

#[test]
fn test_simple_die_roll() {
    let d_6 = DieRoll::new("d6");

    for _ in 0..100 {
        let rolled = d_6.roll();
        assert_eq!(true, rolled >= 1 && rolled <= 6)
    }
}

#[test]
fn test_modified_die_roll() {
    let roller = DieRoll::new("1d4+1");

    for _ in 0..100 {
        let rolled = roller.roll();
        assert_eq!(true, rolled >= 2 && rolled <= 5)
    }
}

#[test]
fn test_multiplied_die_roll() {
    let roller = DieRoll::new("1d4x100");

    for _ in 0..100 {
        let rolled = roller.roll();
        assert_eq!(true, rolled == 100 || rolled == 200 || rolled == 300 || rolled == 400)
    }
}

#[test]
fn test_constant_die_roll() {
    let constant_value = DieRoll::new("2");

    for _ in 0..100 {
        let rolled = constant_value.roll();
        assert_eq!(2, rolled)
    }
}

#[test]
fn test_constant_zero_die_roll() {
    let constant_value = DieRoll::new("0");

    for _ in 0..100 {
        let rolled = constant_value.roll();
        assert_eq!(0, rolled)
    }
}

#[test]
fn test_rolling_zero_dice() {
    let zero = DieRoll::new("0d1+0x0");

    for _ in 0..100 {
        let rolled = zero.roll();
        assert_eq!(0, rolled);
    }
}