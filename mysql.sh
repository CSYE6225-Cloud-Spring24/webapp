#!/bin/bash

sudo dnf update -y
sudo dnf upgrade -y

# Install MySQL
echo "Installing MySQL"
sudo dnf install mysql-server -y
sudo systemctl start mysqld.service
mysql -u root  -e "CREATE DATABASE db;"
mysql -u root  -e "CREATE USER 'web-app'@'localhost' IDENTIFIED BY 'web-app';"
mysql -u root  -e "GRANT ALL ON *.* TO 'web-app'@'localhost';"
mysql -u root  -e "FLUSH PRIVILEGES;"

# Start and enable MySQL service
sudo systemctl start mysqld
sudo systemctl enable mysqld
 
# # Configure the SQL for the first time
# SECURE_INSTALLATION=$(expect -c "
# spawn sudo mysql_secure_installation
# expect \"Enter current password for root (enter for none):\"
# send \"\r\"
# expect \"Set root password?\"
# send \"y\r\"
# expect \"New password:\"
# send \"web-app\r\"
# expect \"Re-enter new password:\"
# send \"web-app\r\"
# expect \"Remove anonymous users?\"
# send \"y\r\"
# expect \"Disallow root login remotely?\"
# send \"y\r\"
# expect \"Remove test database and access to it?\"
# send \"y\r\"
# expect \"Reload privilege tables now?\"
# send \"y\r\"
# expect eof
# ")
 
echo "$SECURE_INSTALLATION"



