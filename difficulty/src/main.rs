#[macro_use]
extern crate clap;
extern crate fern;
#[macro_use]
extern crate log;

use clap::App;

mod difficulty;

fn main() {
    let yaml = load_yaml!("cli.yml");
    let matches = App::from_yaml(yaml)
        .version(crate_version!())
        .author(crate_authors!("\n"))
        .about(crate_description!())
        .get_matches();

    configure_logging(matches.occurrences_of("verbose"));
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