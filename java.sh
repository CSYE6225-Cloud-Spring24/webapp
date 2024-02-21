#!/bin/bash

sudo dnf update -y
sudo dnf upgrade -y

# Install Java 17
echo "Installing Java"
sudo dnf install java-17-openjdk -y

# Install Maven
echo "Installing Maven"
sudo dnf install maven -y

# Update jdk version
echo "Updating JDK Version"
# export JAVA_HOME=/usr/lib/jvm/java-17-openjdk>> ~/.bashrc
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk/>> ~/.source /etc/environment
# export PATH=$JAVA_HOME/bin:$PATH>> ~/.bashrc

# Check Java
echo "Java Version"
java --version

# Install Tomcat
echo "Start Tomcat Installation"
sudo dnf install -y tomcat
 
# Start and enable Tomcat service
sudo systemctl start tomcat
sudo systemctl enable tomcat
echo "Completed Tomcat Installation"


