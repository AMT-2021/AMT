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

Playbooks may be run (from within the `playbooks` directory) using the command
command below. Please make sure to **read the README file under the 'playbooks/'
directory** first.:

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

## Production deployment

### Prerequisites

Before you begin, make sure the target machine on AWS is accessible as `amt-app`
without user interaction.

That is, the command `ssh amt-app` should succeed without password or any other
prompt.

This can be achieved in GNU/Linux by adding the following sections to your ssh
configuration file (`~/.ssh/config`).

```
Host amt-dmz
  Hostname ${IP_ADDRESS_OF_BASTION}
  User OCHAP
  IdentityFile ~/.ssh/amt/AMT-DMZ-OCHAP.pem

Host amt-app
  ProxyJump amt-dmz
  Hostname ${IP_ADDRESS_OF_VM_WITHIN_PRIVATE_NETWORK}
  User admin
  IdentityFile ~/.ssh/amt/AMT-OCHAP.pem
```

### Differences in the production deployment

The deployment procedure in production is very similar to the one on vagrant
with two minor differences:

- The inventory to use is `aws-ochap`.
- The `aws-ochap` inventory is encrypted with [ansible vault][] for vault with
  id `aws-amt` using a vault key.
  The vault key is not present in this repository.

[ansible vault]: https://docs.ansible.com/ansible/latest/user_guide/vault.html

> The vault key provides unrestricted access to the secrets.
> Make sure to store it in a suitable location, preferably in removable storage.

**Before** performing a deployment in production it is strongly recommended that
you validate the viability of the playbook and inventory.
This may be done using the `--check` flag.
See the following section for details.

If you find yourself in need to visualizing a variable, you can use the
following command to display all of the inventory variables:

```console
$ ansible -i ../inventories/aws-ochap all -m debug -a var='vars' \
  --vault-id aws-amt@/path/to/aws-ansible-vault.key
```

> To add a new variable, first you need to encrypt its value.
> Then you may add encrypted result to the inventory file.
>
> For example:
>
> ```console
> $ date | ansible-vault encrypt_string --vault-id aws-amt@/path/to/aws-ansible-vault.key
> Reading plaintext input from stdin. (ctrl-d to end input, twice if your content does not already have a newline)
> !vault |
>           $ANSIBLE_VAULT;1.2;AES256;aws-amt
>           35383263366134636463386537663764383437343932356666303435316264356431343035393138
>           3365623661623834333031623930623039363265386435620a343932346565343133636239323232
>           31363864346237316534616133373932326435376165643661653762646265393731383630386135
>           6363363361633965340a306466313838643763653936643539363831383463316239376461653237
>           65636331633161356432333133653231346339383239666330646236386438666338653135373230
>           3765666436353938383430326566643365303236653066633730
> Encryption successful
> ```

### Deployment procedure

After the deployment has been validated on the vagrant-provisioned inventory,
a deployment in production may proceed.

**Check** the viability of the playbook and inventory.
Note that the vault key MUST be provided for this command to succeed.

```console
$ ansible-playbook -i ../inventories/aws-ochap ochap.yaml \
  --vault-id aws-amt@/path/to/aws-ansible-vault.key --check
```

If everything goes well, ansible is able to reach the destination inventory and
should have all the required information.
You may continue with the deployment:

```console
$ ansible-playbook -i ../inventories/aws-ochap ochap.yaml \
  --vault-id aws-amt@/path/to/aws-ansible-vault.key
```
