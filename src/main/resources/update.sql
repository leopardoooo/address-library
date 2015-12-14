/**
 * 本次更新sql脚本.
 */


ALTER TABLE `ad_tree`
DROP INDEX `tree_county_id`,
DROP INDEX `tree_level`,
DROP INDEX `tree_status`,
ADD INDEX `tree_parent_id` (`addr_parent`) USING BTREE ;

