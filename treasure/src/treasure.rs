use std::fmt;

use crate::gems::Art;
use crate::gems::Gem;
use crate::gems::MagicItem;

#[derive(Debug)]
pub struct Treasure {
    pub cp: u16,
    pub sp: u16,
    pub ep: u16,
    pub gp: u16,
    pub pp: u16,
    pub gems: Vec<Gem>,
    pub art: Vec<Art>,
    pub magic: Vec<MagicItem>
}

impl fmt::Display for Treasure {
    fn fmt(&self, f: &mut fmt::Formatter) -> fmt::Result {
        let mut output = String::from(" -- Coins --\n");

        output.push_str(format!("  - {}cp, {}sp, {}ep, {}gp, {}pp\n", self.cp, self.sp, self.ep, self.gp, self.pp).as_str());

        if !self.gems.is_empty(){
            output.push_str(" -- Gems --\n");
        }

        for gem in self.gems.iter() {
            output.push_str(format!("  - {} ({}gp)\n", gem.description, gem.value).as_str());
        }

        if !self.art.is_empty(){
            output.push_str(" -- Artwork --\n");
        }

        for art in self.art.iter() {
            output.push_str(format!("  - {} ({}gp)\n", art.description, art.value).as_str());
        }

        if !self.magic.is_empty() {
            output.push_str(" -- Magic Items --\n");
        }

        for item in self.magic.iter() {
            output.push_str(format!("  - {}\n", item.description).as_str());
        }

        writeln!(f, "{}", output)
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
            gems: vec![],
            art: vec![],
            magic: vec![],
        }
    }
}

