-- Local source of truth for payments: until now the aggregate only lived in
-- memory around the gateway call, making reconciliation impossible.
create table payments (
    id             uuid           not null,
    amount         numeric(19, 2) not null,
    currency       varchar(3)     not null,
    provider       varchar(255)   not null check (provider in ('STRIPE', 'PAYPAL')),
    status         varchar(255)   not null check (status in ('PENDING', 'AUTHORIZED', 'SUCCEEDED', 'FAILED', 'CANCELLED')),
    external_ref   varchar(255),
    failure_reason varchar(255),
    created_at     timestamp(6) with time zone not null,
    primary key (id)
);
