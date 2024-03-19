#!/bin/bash

sudo chown -R csye6225:csye6225 /etc/google-cloud-ops-agent/

cat <<EOF | sudo tee /etc/google-cloud-ops-agent/config.yaml
logging:
  receivers:
    my-app-receiver:
      type: files
      include_paths:
        - /var/logs/webapp/application.log
      record_log_file_path: true
  processors:
    my-app-processor:
      type: parse_json
      time_key: time
      time_format: "%Y-%m-%dT%H:%M:%S.%L%Z"
  service:
    pipelines:
      default_pipeline:
        receivers: [my-app-receiver]
        processors: [my-app-processor]
EOF

sudo systemctl restart google-cloud-ops-agent
