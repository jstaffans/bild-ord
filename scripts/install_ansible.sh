#!/bin/bash

if [ ! -f /usr/bin/ansible ]; then

  /usr/bin/apt-get update

  # Download working ansible deb package
  wget https://launchpad.net/~ansible/+archive/ubuntu/ansible-1.9/+files/ansible_1.9.4-1ppa~trusty_all.deb -O /tmp/ansible.deb

  # Manually install deps
  apt-get install -y python-support \
    python-pycurl \
    python-jinja2 \
    python-yaml \
    python-paramiko \
    python-httplib2 \
    python-crypto sshpass

  # Install Package
  dpkg -i /tmp/ansible.deb

  # Remove downloaded package
  rm -f /tmp/ansible.deb

fi
