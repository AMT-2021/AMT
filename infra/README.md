# Vagrant

[Vagrant][] is an open-source software for building and maintaining portable
environments.
We provide a [Vagrantfile][] that may be used to provide an inventory
replicating the production server using a local virtual machine.

[Vagrant]: https://www.vagrantup.com/
[Vagrantfile]: https://www.vagrantup.com/docs/vagrantfile/

### Vagrant basics

If you are new to vagrant, we strongly advice you to read the official
[introduction to vagrant][vagrant-intro].
It is also recommended that you read the official [getting started][vagrant-gs]
guide.

[vagrant-intro]: https://www.vagrantup.com/intro/index.html
[vagrant-gs]: https://www.vagrantup.com/intro/getting-started/index.html

### Getting started

- Provisioning VMs.
  ```
  vagrant up
  ```
  Note that preparing the VMs may take some time depending on your system.
  You can check the status of the all vagrant VMs using the following command:
  ```
  vagrant global-status
  ```

- Destroying any trace of the provisioned VMs. This command may be used if the
  `Vagrantfile` has been modified.
  ```
  vagrant destroy
  ```
  Note that you will have to manually remove the entries related to the vagrant
  hosts from your `~/.ssh/known_hosts` file when you recreate the VMs.
  See the vagrant files for getting the IP address.

- If you are under GNU/Linux and using the libvirt vagrant backend (default),
  you can manage and attach to the server console using `virt-manager`.

You can test your machines are up and running with the following command:
`vagrant ssh amtvg0`.
