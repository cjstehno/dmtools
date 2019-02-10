use std::env;

use crate::dice::DieRoll;
use crate::gems::Gem;
use crate::treasure::Treasure;

#[derive(Debug, Deserialize)]
pub struct TreasureDefinition {
    pub roll: String,
    pub cp: String,
    pub sp: String,
    pub ep: String,
    pub gp: String,
    pub pp: String,

    #[serde(default)]
    pub gems: String,

    #[serde(default)]
    pub gem_value: String,

    #[serde(default)]
    pub art: String,

    #[serde(default)]
    pub art_value: String,

    #[serde(default)]
    pub magic: String,

    #[serde(default)]
    pub magic_table: String,

    #[serde(default)]
    pub magic_2: String,

    #[serde(default)]
    pub magic_table_2: String,
}

impl TreasureDefinition {
    pub fn generate(&self) -> Treasure {
        Treasure {
            cp: DieRoll::new(&self.cp).roll(),
            sp: DieRoll::new(&self.sp).roll(),
            ep: DieRoll::new(&self.ep).roll(),
            gp: DieRoll::new(&self.gp).roll(),
            pp: DieRoll::new(&self.pp).roll(),
            gems: Gem::roll_gems(
                DieRoll::new(&self.gems).roll(),
                TreasureDefinition::string_to_number(&self.gem_value),
            ),
            art: DieRoll::new(&self.art).roll(),
            art_value: TreasureDefinition::string_to_number(&self.art_value),
            magic: "".to_string(),// FIXME: support *&self.magic,
        }
    }

    fn string_to_number(string: &str) -> u16 {
        match string.is_empty() || string == "-" {
            true => 0,
            false => string.parse().unwrap_or(0)
        }
    }

    pub fn roll_treasure(table_type: &str, cr: u8) -> Treasure {
        let table_path = match cr {
            0...4 => format!("tables/{}-0-4.csv", table_type),
            5...10 => format!("tables/{}-5-10.csv", table_type),
            11...16 => format!("tables/{}-11-16.csv", table_type),
            _ => format!("tables/{}-17-up.csv", table_type)
        };

        let d_100 = DieRoll::new("d100").roll();

        match TreasureDefinition::select(table_path.as_str(), d_100) {
            Some(treasure) => treasure.generate(),
            None => Treasure::empty()
        }
    }

    fn select(table_path: &str, d_100: u16) -> Option<TreasureDefinition> {
        let full_path = format!("{}/{}", env::current_dir().expect("path").display(), table_path);
        debug!("File: {}", full_path);

        let mut reader = csv::Reader::from_path(full_path).expect("path reader");

        for result in reader.deserialize() {
            let treasure_record: TreasureDefinition = result.unwrap();
            if treasure_record.contains_roll(d_100) {
                return Some(treasure_record);
            }
        }

        return None;
    }

    fn contains_roll(&self, d_100: u16) -> bool {
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

        d_100 >= low_high.0 && d_100 <= low_high.1
    }
}