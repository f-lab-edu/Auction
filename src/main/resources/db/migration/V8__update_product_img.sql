-- url을 전부 저장하면 중복되는 텍스트가 생겨서 filename만 저장하도록 변경
alter table product_img change url file_name varchar(255);

alter table product_img add (mod_date datetime default now())