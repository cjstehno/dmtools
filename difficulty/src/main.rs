#[macro_use]
extern crate clap;
extern crate fern;
#[macro_use]
extern crate log;

use clap::{App, ArgMatches};

use crate::difficulty::{calculate_difficulty, Monster};

mod difficulty;

fn main() {
    let yaml = load_yaml!("cli.yml");
    let matches = App::from_yaml(yaml)
        .version(crate_version!())
        .author(crate_authors!("\n"))
        .about(crate_description!())
        .get_matches();

    configure_logging(matches.occurrences_of("verbose"));

    let party_size = matches.value_of("party-size").unwrap_or("4");
    let party_size = party_size.parse::<u8>().expect("Unable to parse party size.");

    let monsters = gather_monsters(&matches);

    match matches.value_of("party-level"){
        Some(level) => {
            // generate for single level
            let party_level = level.parse::<u8>().expect("Unable to parse party level");
            let rating = calculate_difficulty(party_level, party_size, &monsters);
            println!("[{}] {:?}", level, rating);
        },
        None => {
            // generate for all levels
            for level in 1..21 {
                let rating = calculate_difficulty(level, party_size, &monsters);
                println!("[{}] {:?}", level , rating);
            }
        }
    };
}

fn append_cr(monsters: &mut Vec<Monster>, matches: &ArgMatches, cr_n: &str){
    if let Some(count) = matches.value_of(format!("cr-{}", cr_n)){
        monsters.push(Monster {
            cr: cr_n.to_string(),
            count: count.parse::<u8>().expect("Unable to parse count.")
        })
    }
}

fn gather_monsters(matches: &ArgMatches) -> Vec<Monster> {
    let mut monsters = vec![];

    append_cr(&mut monsters, &matches, "0");
    append_cr(&mut monsters, &matches, "1/8");
    append_cr(&mut monsters, &matches, "1/4");
    append_cr(&mut monsters, &matches, "1/2");
    append_cr(&mut monsters, &matches, "1");
    append_cr(&mut monsters, &matches, "2");
    append_cr(&mut monsters, &matches, "3");
    append_cr(&mut monsters, &matches, "4");
    append_cr(&mut monsters, &matches, "5");
    append_cr(&mut monsters, &matches, "6");
    append_cr(&mut monsters, &matches, "7");
    append_cr(&mut monsters, &matches, "8");
    append_cr(&mut monsters, &matches, "9");
    append_cr(&mut monsters, &matches, "10");
    append_cr(&mut monsters, &matches, "11");
    append_cr(&mut monsters, &matches, "12");
    append_cr(&mut monsters, &matches, "13");
    append_cr(&mut monsters, &matches, "14");
    append_cr(&mut monsters, &matches, "15");
    append_cr(&mut monsters, &matches, "16");
    append_cr(&mut monsters, &matches, "17");
    append_cr(&mut monsters, &matches, "18");
    append_cr(&mut monsters, &matches, "19");
    append_cr(&mut monsters, &matches, "20");
    append_cr(&mut monsters, &matches, "21");
    append_cr(&mut monsters, &matches, "22");
    append_cr(&mut monsters, &matches, "23");
    append_cr(&mut monsters, &matches, "24");
    append_cr(&mut monsters, &matches, "25");
    append_cr(&mut monsters, &matches, "26");
    append_cr(&mut monsters, &matches, "27");
    append_cr(&mut monsters, &matches, "28");
    append_cr(&mut monsters, &matches, "29");
    append_cr(&mut monsters, &matches, "30");

    monsters
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