-- :name insert-user! :! :n
insert into user (username, password)
values (:username, :password)


-- :name get-user :? :1
select * from user where username = :username


-- :name delete-users! :!
delete from user


-- :name upsert-progress! :!
insert or replace into progress (user_id, game_id, state)
values (
  (select id from user where username = :username),
  :game-id,
  "done"
)


-- :name get-progress :?
select state from progress, user
where username = :username
and progress.game_id = :game-id
and progress.user_id = user.id


-- :name delete-progress! :!
delete from progress
