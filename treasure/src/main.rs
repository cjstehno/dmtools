extern crate clap;
extern crate csv;
extern crate rand;
extern crate regex;
extern crate serde;
#[macro_use]
extern crate serde_derive;

use std::env;
use std::process;
use std::vec::Vec;

use clap::{App, Arg};
use regex::Regex;

// FIXME: better error handling

#[derive(Debug)]
struct Treasure {
    cp: u16,
    sp: u16,
    ep: u16,
    gp: u16,
    pp: u16,//,
//    gems: u32,
//    gemValue: u32,
//    art: u32,
//    artValue: u32,
//    magic: ?
}

#[derive(Debug, Deserialize)]
struct IndividualTreasure {
    roll: String,
    cp: String,
    sp: String,
    ep: String,
    gp: String,
    pp: String,
}

#[derive(Debug)]
struct DieRoll {
    count: u16,
    d: u16,
    modifier: u16,
    multiplier: u16,
}

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

    let treasure = individual_treasure(cr);

    println!("Treasure (CR-{}): {:?}", cr, treasure);
}

fn individual_treasure(cr: u8) -> Treasure {
    // TODO: use CR to find individual file

    // load the table data
    let table = load_individual_table("tables/individual-0-4.csv");

    // 1-100 random
    let d_100 = rand::random::<u16>() % 100;
    println!("d100: {}", d_100);

    match table.iter().find(|row| is_in_range(d_100, row.roll.as_str())) {
        Some(row) => {
            println!("Row: {:?}", row);
            // TODO: add Treasure::generate() method to do this
            Treasure {
                cp: roll_dice(&row.cp),
                sp: roll_dice(&row.sp),
                ep: roll_dice(&row.ep),
                gp: roll_dice(&row.gp),
                pp: roll_dice(&row.pp),
            }
        }
        None => Treasure { cp: 0, sp: 0, ep: 0, gp: 0, pp: 0 }
    }
}

fn is_in_range(d_100: u16, range: &str) -> bool {
    let bounds: Vec<&str> = range.split("-").collect();
    let low: u16 = bounds[0].parse::<u16>().unwrap();
    let high: u16 = bounds[1].parse::<u16>().unwrap();

    if d_100 >= low && d_100 <= high {
        true
    } else {
        false
    }
}

fn load_individual_table(path: &str) -> Vec<IndividualTreasure> {
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

// TODO: the DirRoll struct should provide this
fn roll_dice(dice: &str) -> u16 {
    if dice == "-" {
        return 0;
    } else {
        let rx = Regex::new("([0-9]*)d([0-9]*)[+]?([0-9]*)[x]?([0-9]*)").unwrap();
        println!("Matches: {}", rx.is_match(dice));

        let groups = rx.captures(dice).unwrap();
        let die_roll = DieRoll {
            count: str_to_num(groups.get(1).unwrap().as_str(), 1),
            d: str_to_num(groups.get(2).unwrap().as_str(), 0),
            modifier: str_to_num(groups.get(3).unwrap().as_str(), 0),
            multiplier: str_to_num(groups.get(4).unwrap().as_str(), 1),
        };
        println!("Die: {:?}", die_roll);

        return roll(die_roll);
    }
}

// FIXME: refactor some of this into DieRoll impl
fn str_to_num(value: &str, default_value: u16) -> u16 {
    value.parse().unwrap_or(default_value)
}

// TODO: the DieRoll struct should do this
fn roll(dice: DieRoll) -> u16 {
    let mut rolls = 0;

    // TODO: use fold iter here
    for _r in 0..dice.count {
        let rolled = (rand::random::<u16>() % dice.d) + 1;
        println!("Rolled ({}): {}", dice.d, rolled);
        rolls += rolled;
    }

    (rolls + dice.modifier) * dice.multiplier
}
