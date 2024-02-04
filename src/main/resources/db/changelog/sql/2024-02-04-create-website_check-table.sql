create table caws.website_check(
 id bigserial not null unique,
 url varchar(255) not null,
 content varchar not null,
 last_checked_date timestamptz,
 found bool not null,
 found_date timestamptz,
 notification_email varchar(255) not null,
 creation_date timestamptz not null,
 update_date timestamptz not null
);