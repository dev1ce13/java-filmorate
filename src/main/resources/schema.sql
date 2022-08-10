/*drop table IF EXISTS FILM_GENRE;

drop table IF EXISTS FILM_LIKE;

drop table IF EXISTS FILMS;

drop table IF EXISTS FRIENDS;

drop table IF EXISTS GENRES;

drop table IF EXISTS RATINGS;

drop table IF EXISTS USERS;*/

create table IF NOT EXISTS GENRES
(
    GENRE_ID INTEGER auto_increment,
    NAME     CHARACTER VARYING(50) not null,
    constraint GENRE_ID
        primary key (GENRE_ID)
);

create table IF NOT EXISTS RATINGS
(
    RATING_ID   INTEGER auto_increment,
    NAME        CHARACTER VARYING(20) not null,
    DESCRIPTION CHARACTER VARYING(100),
    constraint RATING_ID
        primary key (RATING_ID)
);

create table IF NOT EXISTS FILMS
(
    FILM_ID      INTEGER auto_increment,
    NAME         CHARACTER VARYING(100) not null,
    DESCRIPTION  CHARACTER VARYING(200),
    RELEASE_DATE DATE,
    DURATION     INTEGER,
    RATING_ID    INTEGER                not null,
    constraint FILM_ID
        primary key (FILM_ID),
    constraint FOREIGN_KEY_NAME
        foreign key (RATING_ID) references RATINGS
);

create table IF NOT EXISTS FILM_GENRE
(
    FILM_ID  INTEGER not null,
    GENRE_ID INTEGER not null,
    constraint FILM_GENRE_FILM_ID
        foreign key (FILM_ID) references FILMS,
    constraint FILM_GENRE_GENRE_ID
        foreign key (GENRE_ID) references GENRES,
    primary key (FILM_ID, GENRE_ID)
);

create table IF NOT EXISTS USERS
(
    USER_ID  INTEGER auto_increment,
    EMAIL    CHARACTER VARYING(100) not null,
    LOGIN    CHARACTER VARYING(100) not null,
    NAME     CHARACTER VARYING(100) not null,
    BIRTHDAY DATE,
    constraint USER_ID
        primary key (USER_ID)
);

create table IF NOT EXISTS FILM_LIKE
(
    FILM_ID INTEGER not null,
    USER_ID INTEGER not null,
    constraint FILM_LIKE_FILM_ID
        foreign key (FILM_ID) references FILMS,
    constraint FILM_LIKE_USER_ID
        foreign key (USER_ID) references USERS,
    primary key (FILM_ID, USER_ID)
);

create table IF NOT EXISTS FRIENDS
(
    USER_ID   INTEGER not null,
    FRIEND_ID INTEGER not null,
    constraint FRIENDS_FRIEND_ID
        foreign key (FRIEND_ID) references USERS,
    constraint FRIENDS_USER_ID
        foreign key (USER_ID) references USERS,
    primary key (USER_ID, FRIEND_ID)
);

create unique index IF NOT EXISTS EMAIL_UNQ
    on USERS (EMAIL);

create unique index IF NOT EXISTS LOGIN_UNQ
    on USERS (LOGIN);

