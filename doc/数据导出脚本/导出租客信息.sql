SELECT
  t.`TENANT_NAME` AS '姓名',
  CASE
    t.`GENDER`
    WHEN '1'
    THEN '男'
    ELSE '女'
  END AS '性别',
  CASE
    t.ID_TYPE
    WHEN '0'
    THEN '身份证'
    ELSE '其他'
  END AS '证件类型',
  t.id_no AS 证件号码,
  t.BIRTHDAY AS 出生日期,
  CASE
    t.DEGREES
    WHEN '0'
    THEN '中专'
    WHEN '1'
    THEN '大专'
    WHEN '2'
    THEN '本科'
    WHEN '3'
    THEN '硕士研究生'
    WHEN '4'
    THEN '博士'
    ELSE '-' END AS 学历,
    t.CELL_PHONE AS 手机号,
    t.EMAIL AS 电子邮箱,
    t.HOUSE_REGISTER AS 户籍,
    t.POSITION AS 职位,
    t.remarks AS 备注,
    CASE
      t.`TENANT_TYPE`
      WHEN '1'
      THEN '企业租客'
      ELSE '个人租客'
    END AS '租客类型',
    u.`name` AS '跟进销售',
    tc.`COMPANY_NAME` AS '所属公司'
FROM
  t_tenant t
  LEFT JOIN sys_user u
    ON t.`USER_ID` = u.`id`
  LEFT JOIN t_company tc
    ON t.`COMPANY_ID` = tc.`ID`
WHERE t.`DEL_FLAG` = '0';