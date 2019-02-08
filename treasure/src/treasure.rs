#[derive(Debug)]
pub struct Treasure {
    pub cp: u16,
    pub sp: u16,
    pub ep: u16,
    pub gp: u16,
    pub pp: u16,
    pub gems: u16,
    pub gem_value: u16,
//    art: u32,
//    artValue: u32,
//    magic: ?
}

impl Treasure {
    pub fn empty() -> Treasure {
        Treasure { cp: 0, sp: 0, ep: 0, gp: 0, pp: 0, gems: 0, gem_value: 0 }
    }
}

