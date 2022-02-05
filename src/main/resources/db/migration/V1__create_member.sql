create table member (
                        member_id bigint auto_increment,
                        name varchar(30),
                        password varchar(255),
                        nickname varchar(30),
                        phone varchar(11),
                        address varchar(255),
                        primary key (member_id)
) DEFAULT CHARSET=utf8;
