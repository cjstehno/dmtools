use std::collections::HashMap;
use crate::treasure_definition::Tables;

use crate::dice::DieRoll;

#[derive(Debug, Deserialize)]
pub struct ValuableObject {
    pub roll: String,
    pub description: String,
}

impl ValuableObject {
    fn select(table_path: &str, die_roll: u16) -> Option<ValuableObject> {
        trace!("Selecting valuable table: {}", table_path);

        let table_contents = Tables::get(table_path).expect("table");

        let mut reader = csv::Reader::from_reader(table_contents.as_ref());

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

#[derive(Debug, PartialEq, Hash)]
pub struct Gem {
    pub value: u16,
    pub description: String,
}

impl Eq for Gem {}

impl Gem {
    pub fn roll_gems(count: u16, value: u16) -> HashMap<Gem, u8> {
        if count > 0 {
            let table_path = format!("gems-{}gp.csv", value);
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

            let mut gems: HashMap<Gem, u8> = HashMap::new();

            for _n in 0..(count + 1) {
                let die_value = selection_die.roll();
                match ValuableObject::select(table_path.as_str(), die_value) {
                    Some(val_obj) => {
                        let gem = Gem { value, description: val_obj.description };
                        debug!("Selected gem: {:?}", gem);

                        let gem_count = match gems.get(&gem) {
                            Some(c) => *c,
                            None => 0_u8
                        } + 1;

                        gems.insert(gem, gem_count);
                    }
                    None => ()
                }
            }

            return gems;
        } else {
            HashMap::new()
        }
    }
}

#[derive(Debug, PartialEq, Hash)]
pub struct Art {
    pub value: u16,
    pub description: String,
}

impl Eq for Art {}

impl Art {
    pub fn roll_art(count: u16, value: u16) -> HashMap<Art, u8> {
        if count > 0 {
            let table_path = format!("art-{}gp.csv", value);
            let selection_die = DieRoll::new(match value {
                25 => "1d10",
                250 => "1d10",
                750 => "1d10",
                2500 => "1d10",
                7500 => "1d8",
                _ => "0"
            });

            debug!("Selecting {} {}gp artwork", count, value);

            let mut arts: HashMap<Art, u8> = HashMap::new();

            for _n in 0..(count + 1) {
                let die_value = selection_die.roll();
                match ValuableObject::select(table_path.as_str(), die_value) {
                    Some(val_obj) => {
                        let art = Art { value, description: val_obj.description };
                        debug!("Selected artwork: {:?}", art);

                        let art_count = match arts.get(&art) {
                            Some(c) => *c,
                            None => 0
                        } + 1;

                        arts.insert(art, art_count);
                    }
                    None => ()
                }
            }

            return arts;
        } else {
            HashMap::new()
        }
    }
}

#[derive(Debug, PartialEq, Hash)]
pub struct MagicItem {
    pub description: String
}

impl Eq for MagicItem {}

impl MagicItem {
    pub fn roll_magic(count: u16, table: &str, count_2: u16, table_2: &str) -> HashMap<MagicItem, u8> {
        let mut first_table = MagicItem::roll_magic_table(count, table);
        let second_table = MagicItem::roll_magic_table(count_2, table_2);

        for (item, count) in second_table {
            first_table.insert(item, count);
        }

        return first_table;
    }

    fn roll_magic_table(count: u16, table: &str) -> HashMap<MagicItem, u8> {
        if count > 0 {
            let table_path = format!("magic-{}.csv", table);
            let selection_die = DieRoll::new("d100");

            debug!("Selecting {} magic items (Table {})", count, table);

            let mut magic_items: HashMap<MagicItem, u8> = HashMap::new();

            for _n in 0..(count + 1) {
                let die_value = selection_die.roll();
                match ValuableObject::select(table_path.as_str(), die_value) {
                    Some(val_obj) => {
                        let item = MagicItem { description: val_obj.description };
                        debug!("Selected magic item: {:?}", item);

                        let item_count = match magic_items.get(&item) {
                            Some(c) => *c,
                            None => 0
                        } + 1;

                        magic_items.insert(item, item_count);
                    }
                    None => ()
                }
            }

            return magic_items;
        } else {
            HashMap::new()
        }
    }
}

//
// TESTS
//

#[test]
fn test_loading_magic_a() {
    let valuable_1 = ValuableObject::select("magic-A.csv", 1).unwrap();
    assert_eq!(valuable_1.roll, "1-50");
    assert_eq!(valuable_1.description, "Potion of healing");

    let valuable_100 = ValuableObject::select("magic-A.csv", 100).unwrap();
    assert_eq!(valuable_100.roll, "100");
    assert_eq!(valuable_100.description, "Driftglobe");
}