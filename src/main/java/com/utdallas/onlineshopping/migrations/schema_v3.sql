ALTER TABLE `address` ADD `type` VARCHAR(10) NOT NULL;
ALTER TABLE `tax_details` MODIFY COLUMN `tax` FLOAT(6,3) NOT NULL DEFAULT 0.0;
ALTER TABLE `address` ADD `name` VARCHAR(90) NOT NULL;