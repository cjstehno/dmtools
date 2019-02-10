extern crate clap;
extern crate csv;
extern crate fern;
#[macro_use]
extern crate log;
extern crate rand;
extern crate serde;
#[macro_use]
extern crate serde_derive;

use clap::{App, Arg};

use crate::treasure_definition::TreasureDefinition;

mod treasure;
mod treasure_definition;
mod dice;
mod gems; // TODO: rename to jewelry?

fn main() {
    let matches = App::new("Treasure Calculator")
        .version("0.0.1")
        .author("Christopher J. Stehno <chris@stehno.com>")
        .about("Calculates random treasure for D&D 5e.")
        .arg(Arg::with_name("individual").long("individual").help("Generated individual treasure (default)."))
        .arg(Arg::with_name("verbose").long("verbose").short("v").multiple(true).help("Turns on verbose operation logging information."))
        .arg(Arg::with_name("hoard").long("hoard").help("Generates hoard treasure."))
        .arg(Arg::with_name("cr").long("cr").short("c").value_name("CHALLENGE-RATING").help("Specifies the Challenge Rating.").required(true).takes_value(true))
        .arg(Arg::with_name("rolls").long("rolls").short("r").value_name("# ROLLS").help("Number of treasure rolls to generate.").takes_value(true))
        .get_matches();

    let cr: u8 = matches.value_of("cr").unwrap().parse().unwrap();
    let rolls: u8 = matches.value_of("rolls").unwrap_or("1").parse().unwrap();
    let verbose_count = matches.occurrences_of("verbose");
    let generate_hoard: bool = matches.occurrences_of("hoard") > 0;

    if verbose_count > 0 {
        enable_verbose(verbose_count).unwrap();
    }

    let treasure_category = if generate_hoard { "Hoard" } else { "Individual" };

    debug!("Rolling {} {} CR-{} treasure(s).", rolls, treasure_category, cr);

    let treasure = match generate_hoard {
        true => TreasureDefinition::roll_treasure("hoard", cr),
        false => TreasureDefinition::roll_treasure("individual", cr)
    };

    println!("CR-{} {} Treasure:\n{}", cr, treasure_category, treasure);
}

fn enable_verbose(verbose_count: u64) -> Result<(), fern::InitError> {
    fern::Dispatch::new()
        .format(|out, message, record| {
            out.finish(format_args!("[{}] {}", record.level(), message))
        })
        .level(if verbose_count == 1 { log::LevelFilter::Debug } else  { log::LevelFilter::Trace })
        .chain(std::io::stdout())
        .apply()?;

    Ok(())
}