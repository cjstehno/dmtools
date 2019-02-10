use crate::gems::Art;
use crate::gems::Gem;

#[derive(Debug)]
pub struct Treasure {
    pub cp: u16,
    pub sp: u16,
    pub ep: u16,
    pub gp: u16,
    pub pp: u16,
    pub gems: Vec<Gem>,
    pub art: Vec<Art>,
    pub magic: String, // FIXME: need to account for magic_2
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
            magic: String::from(""),
        }
    }
}

