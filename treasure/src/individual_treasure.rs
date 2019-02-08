use std::env;
use std::process;

use crate::dice::DieRoll;
use crate::treasure::Treasure;

#[derive(Debug, Deserialize)]
pub struct IndividualTreasure {
    pub roll: String,
    pub cp: String,
    pub sp: String,
    pub ep: String,
    pub gp: String,
    pub pp: String,
}

impl IndividualTreasure {
    pub fn generate(&self) -> Treasure {
        Treasure {
            cp: DieRoll::new(&self.cp).roll(),
            sp: DieRoll::new(&self.sp).roll(),
            ep: DieRoll::new(&self.ep).roll(),
            gp: DieRoll::new(&self.gp).roll(),
            pp: DieRoll::new(&self.pp).roll(),
            gems: 0,
            gem_value: 0
        }
    }

    pub fn load(path: &str) -> Vec<IndividualTreasure> {
        let mut table_items = vec![];

        let full_path = format!("{}/{}", env::current_dir().expect("path").display(), path);
        println!("File: {}", full_path);

        let mut reader = csv::Reader::from_path(full_path).expect("path reader");

        for result in reader.deserialize() {
            match result {
                Ok(record) => {
                    let treasure_record: IndividualTreasure = record;
                    table_items.push(treasure_record);
                }
                Err(err) => {
                    println!("Error reading CSV from file ({}): {}", path, err);
                    process::exit(1);
                }
            }
        }

        table_items
    }
}