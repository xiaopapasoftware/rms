
ALTER TABLE T_HOUSE
  ADD COLUMN ele_account_num VARCHAR(64) COLLATE utf8_bin  NULL;

ALTER TABLE T_HOUSE
  ADD COLUMN water_account_num VARCHAR(64) COLLATE utf8_bin  NULL;

ALTER TABLE T_HOUSE
  ADD COLUMN gas_account_num VARCHAR(64) COLLATE utf8_bin  NULL;

