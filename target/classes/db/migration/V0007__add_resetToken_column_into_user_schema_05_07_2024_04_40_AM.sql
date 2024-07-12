-- Add resetToken column to tbl_user
ALTER TABLE tbl_user
ADD COLUMN reset_token VARCHAR(255);