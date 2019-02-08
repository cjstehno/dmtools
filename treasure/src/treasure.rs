#[derive(Debug)]
pub struct Treasure {
    pub cp: u16,
    pub sp: u16,
    pub ep: u16,
    pub gp: u16,
    pub pp: u16,
    pub gems: u16,
    pub gem_value: u16,
    pub art: u16,
    pub art_value: u16,
    pub magic: String,
}

impl Treasure {
    pub fn empty() -> Treasure {
        Treasure { cp: 0, sp: 0, ep: 0, gp: 0, pp: 0, gems: 0, gem_value: 0, art: 0, art_value: 0, magic:String::from("") }
    }
}

