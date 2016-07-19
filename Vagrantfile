# -*- mode: ruby -*-
# vi: set ft=ruby :

Vagrant.configure(2) do |config|

  config.vm.box = "ubuntu/trusty64"
  config.vm.network "forwarded_port", guest: 80, host: 8080
  config.vm.provision 'shell', path: 'scripts/install_ansible.sh'
  config.vm.provision 'shell', path: 'scripts/run_ansible.sh'

end
