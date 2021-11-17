# Playbooks

## blank.yaml

This playbook is empty.
It exists to that the vagrant-ansible plugin can test the inventories.

## ochap.yaml

This playbook deploys the web applications.
Please look at each role individual variable requirements.

In particular, mind that the WAR files to deploy should be placed under the
`$INFRA_ROOT/artifacts`  directory.
The `INFRA_ROOT` directory is the directory that contains the 'playbooks'
directory.
