#!/bin/bash

sudo dnf update -y
sudo dnf upgrade -y

# Install MySQL
echo "Installing MySQL"
sudo dnf install mysql-server -y

# Start and enable MySQL service
sudo systemctl start mysqld
sudo systemctl enable mysqld
 
# Configure the SQL for the first time
SECURE_INSTALLATION=$(expect -c "
spawn sudo mysql_secure_installation
expect \"Enter current password for root (enter for none):\"
send \"\r\"
expect \"Set root password?\"
send \"y\r\"
expect \"New password:\"
send \"root\r\"
expect \"Re-enter new password:\"
send \"root\r\"
expect \"Remove anonymous users?\"
send \"y\r\"
expect \"Disallow root login remotely?\"
send \"y\r\"
expect \"Remove test database and access to it?\"
send \"y\r\"
expect \"Reload privilege tables now?\"
send \"y\r\"
expect eof
")
 
echo "$SECURE_INSTALLATION"



