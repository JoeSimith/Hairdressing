==========================================================
--如果表结构或者新建表时请记录对应的sql语句并提交到svn
==========================================================
--举例如下：
-- Create table DICT_YJFL 应急分类 start 
create table DICT_YJFL
(
  YJFL_ID VARCHAR2(32) not null,
  YJFLMC  VARCHAR2(128),
  CJUSER  VARCHAR2(32),
  CJSJ    VARCHAR2(32),
  XGUSER  VARCHAR2(32),
  XGSJ    VARCHAR2(32)
);
-- Add comments to the table 
comment on table DICT_YJFL
  is '应急分类';
-- Add comments to the columns 
comment on column DICT_YJFL.YJFLMC
  is '应急分类名称';

--默认插入数据
insert into dict_yjfl (YJFL_ID, YJFLMC, CJUSER, CJSJ, XGUSER, XGSJ)
values ('2b53e809434b47f6bfb41d97c85ce9cf', '防汛', '系统管理员', '2017-08-24 16:41:27', '', '');

insert into dict_yjfl (YJFL_ID, YJFLMC, CJUSER, CJSJ, XGUSER, XGSJ)
values ('88a680d6ac914413869de431a94ea527', '土方开挖', '系统管理员', '2017-08-24 16:41:38', '', '');

-- Create table DICT_YJFL 应急分类 end 2017/09/24


---------------------------------------------------------------------------------------------------------------------------------------------------
---------------------------------------------------------------------------------------------------------------------------------------------------
---------------------请不要删除上面的内容，请在下面追加对应sql语句及说明、及添加时间。sql语句结束注意加上";" 分号----------------------------------
------------------------------------------------------------*表的注释和字段的注释一定要加上*-------------------------------------------------------
---------------------------------------------------------------------------------------------------------------------------------------------------
---------------------------------------------------------------------------------------------------------------------------------------------------

---增加触发器 点赞自动增加删除点赞个数20181028  start ---

--增加
CREATE TRIGGER hairstyleevaluateAdd AFTER INSERT
ON bus_hairstyleevaluate FOR EACH ROW
BEGIN
    UPDATE bus_hairstyle t set t.evaluatecount = t.evaluatecount + 1 where t.ID = NEW.hairstyleid;
END

--删除
CREATE TRIGGER hairstyleevaluateDel AFTER DELETE
ON bus_hairstyleevaluate FOR EACH ROW
BEGIN
    UPDATE bus_hairstyle t set t.evaluatecount = t.evaluatecount - 1 where t.ID = OLD.hairstyleid;
END
---增加触发器 点赞自动增加删除点赞个数20181028  end ---