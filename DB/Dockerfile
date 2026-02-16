FROM postgres:17

ENV POSTGRES_DB=game_track_db
ENV POSTGRES_USER=postgres
ENV POSTGRES_PASSWORD=postgres

COPY init/ /docker-entrypoint-initdb.d/
