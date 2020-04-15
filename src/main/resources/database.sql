-- user用户表 --
create table user
(
  user_id INT(11) not null auto_increment comment '用户id',
  user_name VARCHAR(50) not null comment '用户名',
  user_passwd VARCHAR(255) not null comment '用户密码（加密的）',
  user_email varchar(100) default null comment '绑定的邮箱',
  user_gender TINYINT default -1 comment '性别，默认为无',
  user_age TINYINT default -1 comment '年龄，默认为无',
  user_birthday DATE default NULL comment '生日，默认无',
  user_create_time DATETIME default NULL comment '注册时间',
  user_head_photo_url VARCHAR(255) DEFAULT NULL comment '用户头像，不指定则为null',
  user_status TINYINT not null comment '用户的状态，是否被封禁',
  user_is_admin TINYINT default 0 comment '是否为管理员标识',
  primary key(user_id),
  unique key `user_name_unique` (`user_name`) using btree
) engine = InnoDB auto_increment = 1001 default charset = utf8;

alter table user add column user_is_admin TINYINT default 0 comment '是否为管理员标识';
  -- blog博客表 --
  create table blog
  (
    blog_id int(11) not null auto_increment comment '博客id',
    blog_mark varchar(255) not null comment '在url上的标识md5',
    blog_table_of_content varchar(100) default null comment '博客目录，以 ，分割每个目录标题',
    blog_title varchar(255) not null comment '博客标题',
    blog_content longtext not null comment '博客内容',
    blog_create_time datetime not null comment '博客创建时间',
    blog_user_id int(11) not null comment '博客所属的用户id',
    blog_category_id int(11) not null comment '博客分类id',
    blog_love_number int(4) default 0 comment '博客点赞数量',
    primary key(blog_id)
) engine = InnoDB auto_increment = 2001 default charset = utf8;

create table blog_picture(
    id int primary key auto_increment,
    blog_id int not null comment '博客的id',
    blog_picture_mask varchar(255) not null comment '博客展示的图片标识'
)engine = InnoDB auto_increment = 1 default charset = utf8;

 -- tag标签表 --
create table tag
(
    tag_id int(11) not null auto_increment comment '标签id',
    tag_name varchar(20) not null comment '标签名字',
    tag_category_id int(4) not null comment '标签分类id',
    primary key(tag_id)
) engine = InnoDB auto_increment = 3001 default charset = utf8;

-- 标签分类表 --
create table tag_category(
    id int(4) not null auto_increment comment 'id',
    tag_category_name varchar(50) not null comment '标签分类的名字',
    primary key(id)
)engine = InnoDB auto_increment = 11 default charset = utf8

-- 博客<-->标签多对多表
create table tags_blogs_map(
    id int(4) not null auto_increment comment 'id',
    blog_id int(11) not null comment '博客对应标签的id',
    tag_id int(11) not null comment '标签对应博客的id',
    primary key(id)
)engine = InnoDB auto_increment = 0 default charset = utf8

-- category 博客分类表 --
create table category
(
    category_id int(11) not null auto_increment comment '分类id',
    category_name varchar(50) not null comment '分类名字',
    primary key(category_id),
    unique key `category_name_unique` (`category_name`) using btree
)engine = InnoDB auto_increment = 4001 default charset = utf8;

-- comments评论表 --
create table comments
(
    comments_id int(11) not null
    auto_increment comment '评论表id',
    comments_content text not null comment '评论内容',
    comments_user_id int(11) not null comment '评论的用户id',
    comments_blog_id int(11) not null comment '评论的博客id',
    comments_reply_id int(11) default null comment '评论回复id',
    primary key(comments_id)
)engine = InnoDB auto_increment = 5001 default charset = utf8;

-- love 点赞表 --
create table love
(
    love_id int(11) not null auto_increment comment '点赞id',
    love_user_id int(11) not null comment '点赞用户id',
    love_blog_id int(11) not null comment '点赞的博客id',
    primary key(love_id)
)engine = InnoDB auto_increment = 6001 default charset = utf8;

create table blog_read(
    read_id int(11) not null auto_increment comment '主键',
    read_blog_id int(11) not null comment '博客的id',
    read_number int(6) not null comment '已读次数',
    primary key(read_id)
)engine = InnoDB auto_increment = 9001 default charset = utf8;

 -- follow 关注表 --
create table follow
(
    follow_id int(11) not null auto_increment comment '关注表id',
    follow_user_id int(11) not null comment '关注的用户id',
    followed_user_id int(11) not null comment '被关注的用户id',
    follow_status TINYINT not null comment '关注的状态，是否取消关注'
    PRIMARY KEY(follow_id)
) engine = InnoDB auto_increment = 7001 default charset = utf8;

-- collection 收藏表 --
create table collection
(
    collection_id int(11) not null auot_increment comment '收藏id',
    collection_user_id int(11) not null comment '收藏的用户id',
    collection_blog_id int(11) not null comment '收藏的博客id',
    collection_status TINYINT not null comment '收藏的状态，是否取消收藏',
    primary key(collection_id)
) engine = InnoDB auto_increment = 8001 default charset = utf8;

delimiter //  # 定义//为一句sql的结束标志，取消;的所代表的意义
drop procedure if exists test;  # 如果存在名字为test的procedure则删除
create procedure getBlogTags(blogId int(11))  # 创建（创建函数使用的关键字为function 函数名()）
begin

    declare tagId int; # 声明变量
    declare flag int default 0;
    # 这是重点，定义一个游标来记录sql查询的结果(此处的知识点还有SQL的模糊查询，见补充)
    declare s_list cursor for select tag_id from tags_blogs_map where  blog_id = blogId;
    # 为下面while循环建立一个退出标志，当游标遍历完后将flag的值设置为1
    declare continue handler for not found set flag=1;
    open s_list;  # 打开游标
    # 将游标中的值赋给定义好的变量，实现for循环的要点
        fetch s_list into tagId;
        while flag <> 1 do
            # sql提供了字符串的切分，有left、right、substring、substring_index
            # 在T-SQL中，局部变量必须以@作为前缀，声明方式set，select还有点差别
            #set @temp_s = substring_index(old_pro, "省", 1);
            # 根据id的唯一性，利用当前查询到的记录中的字段值来实现更新
            SELECT tag_name from tag where tag_id = tagId;
            # 游标往后移（此处的游标是不是让你想起了C里面的指针）
            fetch s_list into tagId;
        end while;
        #select * from temp_table;
    close s_list;  # 关闭游标
end
