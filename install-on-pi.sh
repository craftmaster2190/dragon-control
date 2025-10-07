#!/usr/bin/env bash

set -e

retryCount=0
maxRetries=3
while [ $retryCount -lt 3 ]; do
    which java && java -version && break || {
        echo "Java not found. Installing Java..."
        sudo apt-get update
        sudo apt-get install -y openjdk-21-jre-headless
    }
    retryCount=$((retryCount + 1))
    echo "Retrying apt-get update (${retryCount}/${maxRetries})..."
    sleep 2
done

sudo mv -v ~/dragon.service /lib/systemd/system/dragon.service
sudo systemctl daemon-reload
sudo systemctl enable dragon.service
sudo systemctl restart dragon.service
