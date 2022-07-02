# Experiments from the Cryptography Cafe

## Running the benchmarks

```
cargo build --release
./gradlew clean
LD_LIBRARY_PATH="$(pwd)/target/release" ./gradlew jmh --no-daemon
```
