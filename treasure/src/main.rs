extern crate clap;
extern crate csv;
extern crate rand;
extern crate serde;
#[macro_use]
extern crate serde_derive;

use clap::{App, Arg};

use crate::dice::DieRoll;
use crate::treasure::Treasure;
use crate::treasure_definition::TreasureDefinition;

mod treasure;
mod treasure_definition;
mod dice;

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
        roll_treasure("hoard", cr)
    } else {
        roll_treasure("individual", cr)
    };

    println!("Treasure (CR-{} {}): {:?}", cr, if hoard { "Hoard" } else { "Individual" }, treasure);
}

fn roll_treasure(table_type: &str, cr: u8) -> Treasure {
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