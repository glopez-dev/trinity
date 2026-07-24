-- Initial schema for the Trinity backend.
-- Transcribed from the Hibernate-generated DDL so `ddl-auto: validate` matches.
-- NOTE (tech debt): customer/employee status and employee role are persisted as
-- ordinals (smallint) because AbstractUser.status / Employee.role lack
-- @Enumerated(EnumType.STRING). Cart status, by contrast, is stored as text.
-- Reproduced here verbatim to keep `validate` green; converting to STRING would
-- be a separate, data-affecting change.

create table employee (
    id                uuid          not null,
    email             varchar(255)  not null unique,
    hashed_password   varchar(255)  not null,
    first_name        varchar(100),
    last_name         varchar(100),
    type              varchar(255)  check (type in ('CUSTOMER', 'EMPLOYEE')),
    role              smallint      not null check (role between 0 and 2),
    status            smallint      not null check (status between 0 and 4),
    hire_date         timestamp(6) with time zone,
    termination_date  timestamp(6) with time zone,
    last_login_at     timestamp(6) with time zone,
    created_at        timestamp(6) with time zone not null,
    updated_at        timestamp(6) with time zone not null,
    version           bigint,
    primary key (id)
);

create table customer (
    id                   uuid          not null,
    email                varchar(255)  not null unique,
    hashed_password      varchar(255)  not null,
    first_name           varchar(100),
    last_name            varchar(100),
    type                 varchar(255)  check (type in ('CUSTOMER', 'EMPLOYEE')),
    status               smallint      not null check (status between 0 and 4),
    stripe_user_id       varchar(255)  unique,
    stripe_access_token  varchar(255),
    stripe_refresh_token varchar(255),
    token_expires_at     timestamp(6) with time zone,
    last_login_at        timestamp(6) with time zone,
    created_at           timestamp(6) with time zone not null,
    updated_at           timestamp(6) with time zone not null,
    version              bigint,
    primary key (id)
);

create table products (
    id               uuid          not null,
    barcode          varchar(13)   not null unique,
    name             varchar(255)  not null,
    brand            varchar(100),
    category         varchar(100),
    ingredients      varchar(255),
    price            numeric(10, 2) not null,
    nutriscore_grade varchar(255),
    -- nutrient levels (embedded)
    fat              varchar(255),
    saturated_fat    varchar(255),
    sugars           varchar(255),
    salt             varchar(255),
    -- nutriments (embedded)
    energy_kcal100g    float(53),
    proteins100g       float(53),
    carbohydrates100g  float(53),
    fat100g            float(53),
    fiber100g          float(53),
    salt100g           float(53),
    sugars100g         float(53),
    -- selected images (embedded)
    display_en       varchar(255),
    display_fr       varchar(255),
    small_en         varchar(255),
    small_fr         varchar(255),
    thumb_en         varchar(255),
    thumb_fr         varchar(255),
    -- stock (embedded)
    quantity         integer,
    min_threshold    integer,
    max_threshold    integer,
    created_at       timestamp(6),
    updated_at       timestamp(6),
    last_update      timestamp(6),
    primary key (id)
);

create table carts (
    id            uuid          not null,
    customer_id   uuid          not null unique,
    status        varchar(255)  not null check (status in ('CREATED', 'EDITED', 'CANCELLED', 'VALIDATED')),
    total_amount  numeric(19, 2) not null,
    currency      varchar(3)    not null,
    version       bigint,
    primary key (id)
);

create table cart_items (
    id            uuid          not null,
    cart_id       uuid          not null,
    product_id    uuid,
    product_name  varchar(255),
    quantity      integer       not null,
    unit_price    numeric(19, 2),
    total_price   numeric(19, 2),
    primary key (id)
);

alter table if exists cart_items
    add constraint fk_cart_items_cart foreign key (cart_id) references carts;
