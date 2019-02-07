mod treasure;
mod individual_treasure;
mod dice;

extern crate clap;
extern crate csv;
extern crate rand;

extern crate serde;
#[macro_use]
extern crate serde_derive;

use std::vec::Vec;

use crate::treasure::Treasure;
use crate::individual_treasure::IndividualTreasure;
use crate::dice::DieRoll;

use clap::{App, Arg};

// FIXME: better error handling

fn main() {
    let matches = App::new("Treasure Calculator")
        .version("0.0.1")
        .author("Christopher J. Stehno <chris@stehno.com>")
        .about("Calculates random treasure for D&D 5e.")
        .arg(Arg::with_name("hoard").long("hoard").help("Generates hoard treasure."))
        .arg(Arg::with_name("cr").long("cr").short("c").value_name("CHALLENGE-RATING").help("Specifies the Challenge Rating.").required(true).takes_value(true))
        .arg(Arg::with_name("rolls").long("rolls").short("r").value_name("COUNT").help("Number of treasure rolls to generate.").takes_value(true))
        .get_matches();

    let cr: u8 = matches.value_of("cr").unwrap().parse().unwrap();
    let rolls: u8 = matches.value_of("rolls").unwrap_or("1").parse().unwrap();
    let hoard: bool = matches.occurrences_of("hoard") > 0;

    println!("Rolling {} {} CR-{} treasure(s).", rolls, if hoard { "hoard" } else { "individual" }, cr);

    let treasure = if hoard {
        hoard_treasure(cr)
    } else {
        individual_treasure(cr)
    };

    println!("Treasure (CR-{} {}): {:?}", cr, if hoard { "Hoard" } else { "Individual" }, treasure);
}

fn hoard_treasure(cr: u8) -> Treasure {
    // FIXME: temp
    Treasure { cp: 0, sp: 0, ep: 0, gp: 0, pp: 0 }
}

fn individual_treasure(cr: u8) -> Treasure {
    let table = match cr {
        0...4 => IndividualTreasure::load("tables/individual-0-4.csv"),
        _ => vec![]
    };

    let d_100 = DieRoll::new("d100").roll();
    match table.iter().find(|row| is_in_range(d_100, row.roll.as_str())) {
        Some(row) => row.generate(),
        None => Treasure { cp: 0, sp: 0, ep: 0, gp: 0, pp: 0 }
    }
}

fn is_in_range(d_100: u16, range: &str) -> bool {
    let bounds: Vec<&str> = range.split("-").collect();
    let low: u16 = bounds[0].parse::<u16>().unwrap();
    let high: u16 = bounds[1].parse::<u16>().unwrap();

    d_100 >= low && d_100 <= high
}
