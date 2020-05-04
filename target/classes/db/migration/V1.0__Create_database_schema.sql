create table communify_user
(
    id      bigserial       primary key,
    name    varchar(100)    not null    unique,
    bio     varchar(5000)   not null
);


create table playlist
(
    id              bigserial       primary key,
    name            varchar(100)    not null,
    description     varchar(5000)   not null,
    creation_date   date            not null,
    owner_id        bigint          not null    references communify_user
);


create table track
(
    id              bigserial       primary key,
    name            varchar(100)    not null,
    author          varchar(100)    not null,
    description     varchar(5000)   not null,
    duration        bigint          not null
);


create table genre
(
    id      bigserial   primary key,
    name    varchar(100)    unique
);


create table track_playlist
(
    track_id        bigint  references track,
    playlist_id     bigint  references playlist,
    primary key(track_id, playlist_id)
);


create table playlist_genre
(
    playlist_id     bigint  references playlist,
    genre_id        bigint  references genre,
    primary key(playlist_id, genre_id)
);


create table playlist_user
(
    playlist_id     bigint  references playlist,
    user_id         bigint  references communify_user,
    primary key(playlist_id, user_id)
);