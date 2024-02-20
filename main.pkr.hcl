packer {
  required_plugins {
    googlecompute = {
      source  = "github.com/hashicorp/googlecompute"
      version = ">= 1.0.0"
    }
  }
}

variable "project_id" {
  type    = string
  default = "csye-6225-414222"
}

variable "gcp_ssh_username" {
  type    = string
  default = "packer"
}

variable "source_image_family" {
  type    = string
  default = "centos-stream-8"
}

variable "zone" {
  type    = string
  default = "us-east1-c"
}

variable "network" {
  type    = string
  default = "default"
}

source "googlecompute" "centos8" {
  project_id          = var.project_id
  source_image_family = var.source_image_family
  zone                = var.zone
  ssh_username        = var.gcp_ssh_username
  image_name          = "packer-custom-image"
  image_family        = "packer-custom-image-family"
  network             = var.network
  image_labels        = { created-by = "packer" }
}

build {
  sources = ["source.googlecompute.centos8"]

  provisioner "shell" {
    script = "java.sh"
  }

  provisioner "shell" {
    script = "mysql.sh"
  }

  provisioner "shell" {
    inline = [
      "sudo adduser csye6225 --shell /usr/sbin/nologin",
      "sudo usermod -aG csye6225 csye6225"
    ]
  }

  provisioner "file" {
    source = "target/Webapp-0.0.1-SNAPSHOT.jar"
    destination = "/tmp/"
  }

  provisioner "file" {
    source      = "csye6225.service"
    destination = "/tmp/"
  }
  
  provisioner "shell" {
    inline = [
      "sudo chown csye6225: /tmp/webapp-0.0.1-SNAPSHOT.jar",
      "sudo chown csye6225: /tmp/csye6225.service",
      "sudo mv /tmp/csye6225.service /etc/systemd/system"
    ]
  }
}
