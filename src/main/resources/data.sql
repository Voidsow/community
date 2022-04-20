delete
from user;

insert into user(username, password, salt,gmt_create,gmt_modified,header_url)
values ('kira', 'freedom', '12345678912345678912345678912345','2019-09-08 09:23:24','2019-09-08 09:23:24','https://avatars.githubusercontent.com/u/71585632?v=4');

insert into user(username, password, salt,gmt_create,gmt_modified,header_url)
values ('athran', 'justice', '12345678912345678912345678912345','2019-09-08 09:23:25','2019-09-08 09:23:24','https://avatars.githubusercontent.com/u/71585632?v=4');

insert into user(username, password, salt,gmt_create,gmt_modified,header_url)
values ('asuna', 'kirito', '12345678912345678912345678912345','2019-09-08 09:23:26','2019-09-08 09:23:24','https://avatars.githubusercontent.com/u/71585632?v=4');

insert into post(uid, title, content,gmt_create,gmt_modified)
values (1, 'test0', 'this is a test post.','2019-09-08 09:25:24','2019-09-08 09:25:24');

insert into post(uid, title, content,gmt_create,gmt_modified)
values (2, 'test1', 'this is a test post.','2019-09-08 09:25:24','2019-09-08 09:25:24');

insert into post(uid, title, content,gmt_create,gmt_modified)
values (3, 'test2', 'this is a test post.','2019-09-08 09:25:24','2019-09-08 09:25:24');
