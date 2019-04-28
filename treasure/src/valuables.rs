use std::env;

use crate::dice::DieRoll;

#[derive(Debug, Deserialize)]
pub struct ValuableObject {
    pub roll: String,
    pub description: String,
}

impl ValuableObject {
    fn select(table_path: &str, die_roll: u16) -> Option<ValuableObject> {
        let full_path = format!("{}/{}", env::current_dir().expect("path").display(), table_path);
        debug!("File: {}", full_path);

        let mut reader = csv::Reader::from_path(full_path).expect("path reader");

        for result in reader.deserialize() {
            let object_record: ValuableObject = result.unwrap();
            if object_record.contains_roll(die_roll) {
                return Some(object_record);
            }
        }

        return None;
    }

    fn contains_roll(&self, rolled: u16) -> bool {
        if self.roll.contains("-") {
            let bounds: Vec<&str> = (&self.roll).split("-").collect();

            let low_high = match bounds.len() {
                1 => {
                    let single: u16 = bounds[0].parse::<u16>().unwrap();
                    (single, single)
                }
                _ => {
                    (
                        bounds[0].parse::<u16>().unwrap(),
                        bounds[1].parse::<u16>().unwrap()
                    )
                }
            };

            rolled >= low_high.0 && rolled <= low_high.1
        } else {
            (&self.roll).parse::<u16>().unwrap() == rolled
        }
    }
}

#[derive(Debug)]
pub struct Gem {
    pub value: u16,
    pub description: String,
}

impl Gem {
    pub fn roll_gems(count: u16, value: u16) -> Vec<Gem> {
        if count > 0 {
            let table_path = format!("tables/gems-{}gp.csv", value);
            let selection_die = DieRoll::new(match value {
                10 => "1d12",
                50 => "1d12",
                100 => "1d10",
                500 => "1d6",
                1000 => "1d8",
                5000 => "1d4",
                _ => "0"
            });

            debug!("Selecting {} {}gp gems", count, value);

            let mut gems: Vec<Gem> = vec![];

            // FIXME: verify bounds!
            for _n in 0..count {
                let die_value = selection_die.roll();
                match ValuableObject::select(table_path.as_str(), die_value) {
                    Some(val_obj) => {
                        let gem = Gem { value, description: val_obj.description };
                        debug!("Selected gem: {:?}", gem);
                        gems.push(gem)
                    }
                    None => ()
                }
            }

            return gems;
        } else {
            vec![]
        }
    }
}

#[derive(Debug)]
pub struct Art {
    pub value: u16,
    pub description: String,
}

impl Art {
    pub fn roll_art(count: u16, value: u16) -> Vec<Art> {
        if count > 0 {
            let table_path = format!("tables/art-{}gp.csv", value);
            let selection_die = DieRoll::new(match value {
                25 => "1d10",
                250 => "1d10",
                750 => "1d10",
                2500 => "1d10",
                7500 => "1d8",
                _ => "0"
            });

            debug!("Selecting {} {}gp artwork", count, value);

            let mut arts: Vec<Art> = vec![];

            // FIXME: verify bounds!
            for _n in 0..count {
                let die_value = selection_die.roll();
                match ValuableObject::select(table_path.as_str(), die_value) {
                    Some(val_obj) => {
                        let art = Art { value, description: val_obj.description };
                        debug!("Selected artwork: {:?}", art);
                        arts.push(art)
                    }
                    None => ()
                }
            }

            return arts;
        } else {
            vec![]
        }
    }
}

#[derive(Debug)]
pub struct MagicItem {
    pub description: String
}

impl MagicItem {
    pub fn roll_magic(count: u16, table: &str, count_2: u16, table_2: &str) -> Vec<MagicItem> {
        let mut first_table = MagicItem::roll_magic_table(count, table);
        let mut second_table = MagicItem::roll_magic_table(count_2, table_2);

        first_table.append(&mut second_table);

        return first_table;
    }

    fn roll_magic_table(count: u16, table: &str) -> Vec<MagicItem> {
        if count > 0 {
            let table_path = format!("tables/magic-{}.csv", table);
            let selection_die = DieRoll::new("d100");

            debug!("Selecting {} magic items (Table {})", count, table);

            let mut magic_items: Vec<MagicItem> = vec![];

            // FIXME: verify bounds!
            for _n in 0..count {
                let die_value = selection_die.roll();
                match ValuableObject::select(table_path.as_str(), die_value) {
                    Some(val_obj) => {
                        let item = MagicItem { description: val_obj.description };
                        debug!("Selected magic item: {:?}", item);
                        magic_items.push(item)
                    }
                    None => ()
                }
            }

            return magic_items;
        } else {
            vec![]
        }
    }
}

//
// TESTS
//

#[test]
fn test_loading_magic_a() {
    let valuable_1 = ValuableObject::select("tables/magic-A.csv", 1).unwrap();
    assert_eq!(valuable_1.roll, "1-50");
    assert_eq!(valuable_1.description, "Potion of healing");

    let valuable_100 = ValuableObject::select("tables/magic-A.csv", 100).unwrap();
    assert_eq!(valuable_100.roll, "100");
    assert_eq!(valuable_100.description, "Driftglobe");
}