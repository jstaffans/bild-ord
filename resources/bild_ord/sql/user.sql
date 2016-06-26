-- :name insert-user! :! :n
insert into user (username, password)
values (:username, :password)


-- :name get-user :? :1
select * from user where username = :username
