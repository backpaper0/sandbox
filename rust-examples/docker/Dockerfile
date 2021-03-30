FROM rust:alpine
WORKDIR /workspace
COPY src ./src
COPY Cargo.toml .
COPY Cargo.lock .
RUN cargo build --release

FROM alpine
WORKDIR /runtime
COPY --from=0 /workspace/target/release/demo .
CMD ["./demo"]
