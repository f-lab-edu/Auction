create table category (
                          category_id bigint not null auto_increment,
                          name varchar(30),
                          mod_date datetime,
                          reg_date datetime default NOW(),
                          primary key (category_id)
) engine=InnoDB DEFAULT CHARSET=utf8;
