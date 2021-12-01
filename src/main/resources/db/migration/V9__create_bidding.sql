create table bidding (
                         bidding_id bigint auto_increment,
                         member_id bigint not null,
                         product_id bigint not null,
                         price integer not null,
                         reg_date datetime default NOW(),
                         mod_date datetime default now(),
                         foreign key (product_id) references product (product_id),
                         foreign key (member_id) references member (member_id),
                         primary key(bidding_id)
);
