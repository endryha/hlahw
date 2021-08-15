-- calculated fields
select month(birthdate) month, count(month(birthdate)) count_per_month from user group by month order by month;

select count(*) from user where birthdate = STR_TO_DATE('16-10-1988','%d-%m-%Y');