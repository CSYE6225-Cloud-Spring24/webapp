#!/bin/bash

sudo dnf upgrade -y
sudo dnf install java-17-openjdk -y

# Install MySQL
echo "Installing MySQL"
sudo dnf install mysql-server -y
sudo systemctl start mysqld.service
mysql -u root  -e "CREATE DATABASE webappDB;"
mysql -u root  -e "CREATE USER 'web-app'@'localhost' IDENTIFIED BY 'web-app';"
mysql -u root  -e "GRANT ALL ON *.* TO 'web-app'@'localhost';"
mysql -u root  -e "FLUSH PRIVILEGES;"

# Start and enable MySQL service
sudo systemctl start mysqld
sudo systemctl enable mysqld




