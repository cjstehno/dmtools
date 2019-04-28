use std::collections::HashMap;
use std::fmt;

use crate::valuables::Art;
use crate::valuables::Gem;
use crate::valuables::MagicItem;

#[derive(Debug)]
pub struct Treasure {
    pub cp: u16,
    pub sp: u16,
    pub ep: u16,
    pub gp: u16,
    pub pp: u16,
    pub gems: HashMap<Gem, u8>,
    pub art: Vec<Art>,
    pub magic: Vec<MagicItem>,
}

impl fmt::Display for Treasure {
    fn fmt(&self, f: &mut fmt::Formatter) -> fmt::Result {
        let mut output = String::from(" -- Coins --\n");

        output.push_str(format!("  - {}cp, {}sp, {}ep, {}gp, {}pp\n", self.cp, self.sp, self.ep, self.gp, self.pp).as_str());

        if !self.gems.is_empty() {
            output.push_str("\n -- Gems --\n");
        }

        for (gem,count) in &(self.gems) {
            output.push_str(format!("  - {} x {} ({}gp)\n", count, gem.description, gem.value).as_str());
        }

        if !self.art.is_empty() {
            output.push_str("\n -- Artwork --\n");
        }

        for art in self.art.iter() {
            output.push_str(format!("  - {} ({}gp)\n", art.description, art.value).as_str());
        }

        if !self.magic.is_empty() {
            output.push_str("\n -- Magic Items --\n");
        }

        for item in self.magic.iter() {
            output.push_str(format!("  - {}\n", item.description).as_str());
        }

        write!(f, "{}", output)
    }
}

impl Treasure {
    pub fn empty() -> Treasure {
        Treasure {
            cp: 0,
            sp: 0,
            ep: 0,
            gp: 0,
            pp: 0,
            gems: HashMap::new(),
            art: vec![],
            magic: vec![],
        }
    }
}

