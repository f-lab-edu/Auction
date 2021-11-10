create table product_img (
                             product_img_id bigint not null auto_increment,
                             reg_date datetime,
                             url varchar(255),
                             product_id bigint,
                             primary key (product_img_id),
                             foreign key (product_id) references product (product_id)
) engine=InnoDB;
