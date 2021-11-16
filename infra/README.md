# Infrastructure management

We use Ansible and Vagrant to manage and test our infrastructure before
deployment.
Please make sure [Ansible][] and [Vagrant][] are installed in your system.

[Ansible][] as a configuration manager.
It is used to manage our configuration in a declarative an authoritative way.

[Ansible]: https://www.ansible.com/

[Vagrant][] is an open-source software for building and maintaining portable
environments.
We provide a [Vagrantfile][] that may be used to provide an inventory
replicating the production server using local virtual machines.

[Vagrant]: https://www.vagrantup.com/
[Vagrantfile]: https://www.vagrantup.com/docs/vagrantfile/

## Ansible basics

> Mind that with respect to most documentation out there, we store our playbooks
> in the `playbooks/` directory instead of the project root.

We use `ansible-playbook` to run a _playbooks_ over an _inventory_.
A _playbook_ is a set of _roles_ to be applied over an _inventory_.
An _inventory_ is a set of _hosts_ that may have some variables attached.
A _role_ is a 'feature' that is to be provided, such as 'web server' or
'database'; roles are made out of _tasks_, which are units of configuration,
such as 'install nginx', or 'copy the configuration file'.

Playbooks may be run (from within the `playbooks` directory) using the following
command:

```
ansible-playbook -i ../inventories/vagrant ochap.yaml
```

> If ansible is unable to find your inventory with the command above, your
> operating may not have support for symlinks. Yry using
> `../.vagrant/provisioners/ansible/inventory/vagrant_ansible_inventory`
> as inventory.

At the moment, we provide one inventory, which uses machines provisioned by
vagrant.
Vagrant needs to be provisioned **before** you run this playbook.
See [the corresponding section](#vagrant-basics).

## Vagrant basics

If you are new to vagrant, we strongly advice you to read the official
[introduction to vagrant][vagrant-intro].
It is also recommended that you read the official [getting started][vagrant-gs]
guide.

> Mind that we use the 'ansible' provisioning to have vagrant create the
> inventory configuration.
> The playbook ran by vagrant is empty and simply verifies the inventory works
> as expected.
> After the machines have been provisioned, you should use `ansible-playbook`
> to configure the VMs.

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
`vagrant ssh vg-ochap-appserver`.
