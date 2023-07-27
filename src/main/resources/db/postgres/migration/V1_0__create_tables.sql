create table public.request_data
(
    id        serial,
    body      varchar(1000) not null,
    response  varchar(1000),
    timestamp timestamp
);

