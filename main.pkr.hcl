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

source "googlecompute" "centos" {
  project_id          = var.project_id
  source_image_family = var.source_image_family
  zone                = var.zone
  ssh_username        = var.gcp_ssh_username
  image_name          = "packer-custom-centos8-image"
  image_family        = "packer-custom-centos8-image-family"
  network             = var.network
  image_labels        = { created-by = "packer" }
}

build {
  sources = ["source.googlecompute.centos"]

  provisioner "shell" {
    script = "java.sh"
  }

  provisioner "shell" {
    script = "mysql.sh"
  }

  provisioner "file" {
    source = "target/Webapp-0.0.1-SNAPSHOT.jar"
    destination = "/tmp/"
  }

  provisioner "file" {
    source      = "csye6225.service"
    destination = "/tmp/"
  }

}
