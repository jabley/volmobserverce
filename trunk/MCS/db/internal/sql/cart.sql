drop table cart;
create table cart 
(
	userid	number(20),
	cat_id	number,
	tran_id	number,
	quantity number(5),
	order_date	date,
	status	varchar2(20)
);

drop index idx_cat;
create index idx_cart on cart(tran_id) tablespace INDX;
