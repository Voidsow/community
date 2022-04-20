drop table if exists user;
create table user
(
    id              int(16) auto_increment primary key,
    username        char(32),
    password        varchar(32),
    salt            char(32),
    type            int          default 0 comment '0 普通用户；1 管理员',
    email           varchar(64)  default null,
    status          int          default 0 comment '0 已验证；1 未验证',
    activation_mode varchar(128) default null,
    header_url      varchar(256) default null,
    gmt_create      datetime       default null,
    gmt_modified    datetime       default null
);

create index username_index on user (username);
create index email_index on user (email);

drop table if exists post;
create table post
(
    id           int auto_increment primary key,
    uid          int(16),
    title        varchar(128),
    content      text,
    type         int    default 0 comment '0 普通；1 置顶',
    status       int    default 0 comment '0 正常；1 精华',
    gmt_create   datetime default null,
    gmt_modified datetime default null,
    comment_num  int    default 0,
    score        double default null
);
create index post_uid_index on post (uid);