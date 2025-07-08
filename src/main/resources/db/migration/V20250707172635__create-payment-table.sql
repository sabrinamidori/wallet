create sequence if not exists payment_seq AS BIGINT NO MAXVALUE START WITH 1;

create table if not exists  payment
(
    id                       bigint    default nextval('payment_seq'::regclass) not null
        primary key,
    user_id                  bigint                                                  not null,
    amount                   numeric(19, 2) default 0                                not null,
    fee_amount               numeric(19, 2) default 0                                not null,
    status                   varchar(50)                                             not null,
    account_id               bigint                                                  not null,
    account_holder_name      varchar(250)                                            not null,
    routing_number           varchar(100)                                            not null,
    account_number           varchar(100)                                            not null,
    account_currency         varchar(3) default 'USD'                                not null,
    withdraw_id              bigint,
    refund_id                bigint,
    external_id              varchar(50),
    fail_reason              varchar(250),
    created_at               timestamp default now()                                 not null,
    updated_at               timestamp,
    deleted_at               timestamp,
    deleted                  boolean   default false
);