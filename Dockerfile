FROM debian:bookworm-slim

RUN apt-get update && apt-get install -y --no-install-recommends \
    wget unzip ca-certificates \
    && rm -rf /var/lib/apt/lists/*

WORKDIR /repo
ENTRYPOINT ["scripts/build.sh"]
