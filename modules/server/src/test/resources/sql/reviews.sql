create table if not exists reviews (
    id bigserial primary key ,
    company_id bigint not null,
    user_id bigint not null,
    management int not null,
    culture int not null,
    salary int not null,
    benefits int not null,
    would_recommend int not null,
    review text not null,
    ctime timestamp not null default now(),
    mtime timestamp not null default now()
);