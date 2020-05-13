drop table if exists playlist_genre;

drop table if exists genre;

alter table playlist
add column if not exists genre varchar(100);