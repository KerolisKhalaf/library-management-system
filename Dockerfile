
FROM maven:3.9.9-eclipse-temurin-17

RUN apt-get update \
    && apt-get install -y --no-install-recommends \
        fluxbox \
        fonts-dejavu-core \
        libasound2t64 \
        libgl1 \
        libgtk-3-0t64 \
        libxi6 \
        libxrender1 \
        libxtst6 \
        novnc \
        websockify \
        x11vnc \
        xvfb \
    && rm -rf /var/lib/apt/lists/*

WORKDIR /opt/app

COPY pom.xml .
RUN mvn -B dependency:go-offline

COPY src ./src
RUN mvn -B -DskipTests package

COPY docker/entrypoint.sh /usr/local/bin/library-entrypoint

RUN sed -i 's/\r$//' /usr/local/bin/library-entrypoint \
    && chmod +x /usr/local/bin/library-entrypoint \
    && mkdir -p /data

ENV LIBRARY_DATA_DIR=/data

EXPOSE 6080

ENTRYPOINT ["/usr/local/bin/library-entrypoint"]

