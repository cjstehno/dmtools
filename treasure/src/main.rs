extern crate csv;
extern crate rand;
extern crate serde;
#[macro_use]
extern crate serde_derive;

use std::env;
use std::process;
use std::vec::Vec;

// FIXME: better error handling

#[derive(Debug)]
struct Treasure {
    cp: u32,
    sp: u32,
    ep: u32,
    gp: u32,
    pp: u32,//,
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

// TODO: --individual|--hoard(-i|-h) --cr=# --count=#[1]
fn main() {
    let cr = 1_u8;
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
            Treasure {
                cp: roll_dice(&row.cp),
                sp: roll_dice(&row.sp),
                ep: roll_dice(&row.ep),
                gp: roll_dice(&row.gp),
                pp: roll_dice(&row.pp)
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

fn roll_dice(dice: &str) -> u32 {
    if dice == "-" {
        return 0
    } else {
        return 1 // FIXME: parse and gen the random roll
    }
}