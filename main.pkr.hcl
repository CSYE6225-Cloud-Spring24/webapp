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
  default = "devv-415008"
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
  image_family        = "packer-custom8-image-family"
  network             = var.network
  image_labels        = { created-by = "packer" }
}

build {
  sources = ["source.googlecompute.centos"]

  provisioner "shell" {
    script = "java.sh"
  }

  provisioner "shell" {
    inline = [
      "sudo adduser csye6225 --shell /usr/sbin/nologin",
      "sudo usermod -aG csye6225 csye6225"
    ]
  }

  provisioner "file" {
    source      = "target/Webapp-0.0.1-SNAPSHOT.jar"
    destination = "/tmp/"
  }

  provisioner "file" {
    source      = "csye6225.service"
    destination = "/tmp/"
  }

  provisioner "shell" {
    inline = [
      "sudo chown csye6225: /tmp/Webapp-0.0.1-SNAPSHOT.jar",
      "sudo chown csye6225: /tmp/csye6225.service",
      "sudo mv /tmp/csye6225.service /etc/systemd/system",
      "sudo systemctl daemon-reload",
      "sudo systemctl enable csye6225.service",
      "sudo systemctl start csye6225.service",
      "sudo curl -sSO https://dl.google.com/cloudagents/add-google-cloud-ops-agent-repo.sh",
      "sudo bash add-google-cloud-ops-agent-repo.sh --also-install",
      "sudo systemctl enable google-cloud-ops-agent",
      "sudo systemctl start google-cloud-ops-agent"
    ]
  }

  provisioner "shell" {
    script = "OpsAgent.sh"
  }
}

