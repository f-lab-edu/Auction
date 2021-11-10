create table product (
                         product_id bigint not null auto_increment,
                         mod_date datetime,
                         reg_date datetime,
                         deadline datetime,
                         description text,
                         fix_price integer,
                         name varchar(60) not null,
                         now_price integer,
                         sale_type varchar(20) not null,
                         start_price integer,
                         status varchar(10) not null,
                         category_id bigint not null,
                         member_id bigint not null,
                         primary key (product_id),
                         foreign key (category_id) references category (category_id),
                         foreign key (member_id) references member (member_id)

) engine=InnoDB;