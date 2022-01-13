create table orders
(
    orders_id bigint  not null auto_increment,
    member_id  bigint  not null,
    product_id bigint  not null,
    price      integer not null,
    reg_date   datetime default now(),
    primary key (orders_id),
    foreign key (product_id) references product (product_id) on delete cascade,
    foreign key (member_id) references member (member_id) on delete cascade

);