extern crate clap;
extern crate csv;
extern crate rand;
extern crate serde;
#[macro_use]
extern crate serde_derive;

use std::vec::Vec;

use clap::{App, Arg};

use crate::dice::DieRoll;
use crate::hoard_treasure::HoardTreasure;
use crate::individual_treasure::IndividualTreasure;
use crate::treasure::Treasure;

mod treasure;
mod individual_treasure;
mod hoard_treasure;
mod dice;

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
    let table = match cr {
        0...4 => HoardTreasure::load("tables/hoard-0-4.csv"),
        _ => vec![]
    };

    let d_100 = DieRoll::new("d100").roll();
    match select_hoard_record(&table, d_100) {
        Some(tres) => tres.generate(),
        None => Treasure::empty()
    }
}

fn individual_treasure(cr: u8) -> Treasure {
    let table = match cr {
        0...4 => IndividualTreasure::load("tables/individual-0-4.csv"),
        5...10 => IndividualTreasure::load("tables/individual-5-10.csv"),
        11...16 => IndividualTreasure::load("tables/individual-11-16.csv"),
        _ => IndividualTreasure::load("tables/individual-17-up.csv")
    };

    let d_100 = DieRoll::new("d100").roll();
    match select_record(&table, d_100) {
        Some(tres) => tres.generate(),
        None => Treasure::empty()
    }
}

// FIXME: would be good to make this generic
fn select_record(table: &Vec<IndividualTreasure>, d_100: u16) -> Option<&IndividualTreasure> {
    table.iter().find(|row| is_in_range(d_100, row.roll.as_str()))
}

fn select_hoard_record(table: &Vec<HoardTreasure>, d_100: u16) -> Option<&HoardTreasure> {
    table.iter().find(|row| is_in_range(d_100, row.roll.as_str()))
}

fn is_in_range(d_100: u16, range: &str) -> bool {
    let bounds: Vec<&str> = range.split("-").collect();
    let low: u16 = bounds[0].parse::<u16>().unwrap();
    let high: u16 = bounds[1].parse::<u16>().unwrap();

    d_100 >= low && d_100 <= high
}