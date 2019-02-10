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
        (&self.roll).parse::<u16>().unwrap() == rolled
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
pub struct Artwork {
    pub count: u16,
    pub value: u16,
    pub description: String,
}

impl Artwork {

}

// FIXME: implement artwork