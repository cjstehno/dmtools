extern crate regex;

use regex::Regex;

#[derive(Debug)]
pub struct DieRoll {
    pub count: u16,
    pub d: u16,
    pub modifier: u16,
    pub multiplier: u16,
}

// FIXME: test description of "1"

impl DieRoll {
    pub fn new(dice: &str) -> DieRoll {
        if dice.is_empty() || dice == "-" {
            DieRoll { count: 0, d: 0, modifier: 0, multiplier: 0 }
        } else {
            let rx = Regex::new("([0-9]*)d([0-9]*)[+]?([0-9]*)[x]?([0-9]*)").unwrap();
            println!("Matches: {}", rx.is_match(dice));

            let groups = rx.captures(dice).unwrap();

            DieRoll {
                count: DieRoll::str_to_num(groups.get(1).unwrap().as_str(), 1),
                d: DieRoll::str_to_num(groups.get(2).unwrap().as_str(), 0),
                modifier: DieRoll::str_to_num(groups.get(3).unwrap().as_str(), 0),
                multiplier: DieRoll::str_to_num(groups.get(4).unwrap().as_str(), 1),
            }
        }
    }

    fn str_to_num(value: &str, default_value: u16) -> u16 {
        value.parse().unwrap_or(default_value)
    }

    pub fn roll(&self) -> u16 {
        let mut rolls = 0;

        let dice = &self;

        // TODO: use fold iter here
        for _r in 0..dice.count {
            let rolled = (rand::random::<u16>() % dice.d) + 1;
            println!("Rolled ({}): {}", dice.d, rolled);
            rolls += rolled;
        }

        (rolls + dice.modifier) * dice.multiplier
    }
}