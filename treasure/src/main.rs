#[macro_use]
extern crate clap;
extern crate csv;
extern crate fern;
#[macro_use]
extern crate log;
extern crate rand;
extern crate serde;
#[macro_use]
extern crate serde_derive;

use clap::App;

use crate::treasure_definition::TreasureDefinition;

mod treasure;
mod treasure_definition;
mod dice;
mod gems; // TODO: rename to jewelry?

fn main() {
    let yaml = load_yaml!("cli.yml");
    let matches = App::from_yaml(yaml)
        .version(crate_version!())
        .author(crate_authors!("\n"))
        .about(crate_description!())
        .get_matches();

    let cr: u8 = matches.value_of("cr").unwrap().parse().unwrap();
    let generate_hoard: bool = matches.occurrences_of("hoard") > 0;

    configure_logging(matches.occurrences_of("verbose"));

    let treasure = match generate_hoard {
        true => TreasureDefinition::roll_treasure("hoard", cr),
        false => TreasureDefinition::roll_treasure("individual", cr)
    };

    println!("CR-{} {} Treasure:\n{}", cr, if generate_hoard { "Hoard" } else { "Individual" }, treasure);
}

fn configure_logging(verbosity: u64) {
    fern::Dispatch::new()
        .format(|out, message, record| {
            out.finish(format_args!("[{}] {}", record.level(), message))
        })
        .level(match verbosity {
            0 => log::LevelFilter::Warn,
            1 => log::LevelFilter::Info,
            2 => log::LevelFilter::Debug,
            _ => log::LevelFilter::Trace
        })
        .chain(std::io::stdout())
        .apply()
        .unwrap();
}