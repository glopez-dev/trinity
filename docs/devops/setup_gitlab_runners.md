# GitLab Runner Setup Guide

This guide explains how to set up a Docker-in-Docker (DinD) GitLab runner on an AWS EC2 instance using Ansible.

## Prerequisites

### Local Machine Requirements
- Ansible installed
- SSH key pair for EC2 access

### AWS EC2 Instance Requirements
- Ubuntu Server (recommended: Ubuntu 22.04 LTS)
- Minimum specifications:
  - 2 vCPUs
  - 4 GB RAM
  - 20 GB storage
- Security group rules:
  - Inbound: SSH (port 22)
  - Outbound: All traffic

## File Structure

```
devops/ansible/
├── gitlab_runner_dind_setup.yml   # Main Ansible playbook
├── inventory.example.yml          # Example inventory configuration
└── variables.example.yml          # Example variables configuration
```

## Configuration Steps

### 1. Prepare Inventory File

Create your inventory file from the example:
```bash
cp devops/ansible/inventory.example.yml devops/ansible/inventory.yml
```

Edit `inventory.yml` with your EC2 instance details:
```yaml
all:
  hosts:
    ec2_gitlab_runner:
      ansible_host: <your_aws_ec2_public_dns_name>  # e.g., ec2-13-51-150-9.eu-north-1.compute.amazonaws.com
      ansible_user: <your_aws_ec2_hostname>         # Should be 'ubuntu'
      ansible_ssh_private_key_file: <path_to_your_private_key>  # e.g., ~/.ssh/aws_gitlab_runner.pem
      ansible_ssh_common_args: '-o StrictHostKeyChecking=no'
```

### 2. Set Up Variables

Create your variables file from the example:
```bash
cp devops/ansible/variables.example.yml devops/ansible/variables.yml
```

Edit `variables.yml` with your GitLab configuration:
```yaml
REGISTRATION_TOKEN: <your_gitlab_runner_token>  # e.g., GR13489412H_5ZcrZRxD7fDbL_Vxm
YOUR_NAME: <your_identifier>                    # e.g., Gabriel
```

### 3. Deploy the Runner

Execute the Ansible playbook:
```bash
ansible-playbook -i devops/ansible/inventory.yml devops/ansible/gitlab_runner_dind_setup.yml
```

## What the Playbook Does

The Ansible playbook performs the following tasks:

1. System Preparation:
   - Updates package cache
   - Installs required dependencies
   - Configures system settings for Docker

2. Docker Installation:
   - Installs Docker from official repository
   - Configures Docker daemon for DinD support
   - Starts and enables Docker service

3. GitLab Runner Setup:
   - Installs GitLab runner package
   - Registers runner with GitLab instance
   - Configures runner for DinD operation

## Verification

After deployment, verify the runner setup:

1. Check runner status on EC2:
```bash
ssh -i <your-key.pem> ubuntu@<ec2-instance> sudo gitlab-runner list
```

2. Verify runner registration in GitLab:
   - Go to your GitLab project
   - Navigate to Settings > CI/CD > Runners
   - Look for your newly registered runner

## Troubleshooting

### Common Issues

1. **Connection Failures**
   ```bash
   # Check SSH connectivity
   ssh -i <your-key.pem> ubuntu@<ec2-instance>
   
   # Verify Ansible can reach the host
   ansible ec2_gitlab_runner -i devops/ansible/inventory.yml -m ping
   ```

2. **Docker Issues**
   ```bash
   # Check Docker service status
   sudo systemctl status docker
   
   # Verify Docker can run containers
   sudo docker run hello-world
   ```

3. **Runner Registration Problems**
   ```bash
   # Check runner service status
   sudo systemctl status gitlab-runner
   
   # View runner logs
   sudo gitlab-runner verify
   ```

### Runner Maintenance

1. **Restart Runner**
   ```bash
   sudo gitlab-runner restart
   ```

2. **Re-register Runner**
   ```bash
   sudo gitlab-runner unregister --all
   sudo gitlab-runner register
   ```

3. **Clean Docker System**
   ```bash
   sudo docker system prune -af
   ```

## Security Considerations

1. **SSH Access**
   - Use SSH key-based authentication only
   - Disable password authentication
   - Regularly rotate SSH keys

2. **Runner Token**
   - Store registration token securely
   - Use environment-specific runners
   - Regularly rotate runner tokens

3. **Docker Security**
   - Keep Docker updated
   - Use official images
   - Implement resource limits

## Maintenance and Updates

### Regular Maintenance Tasks
1. Update system packages:
   ```bash
   sudo apt update && sudo apt upgrade -y
   ```

2. Update Docker:
   ```bash
   sudo apt update
   sudo apt upgrade docker-ce docker-ce-cli containerd.io
   ```

3. Update GitLab Runner:
   ```bash
   sudo apt update
   sudo apt upgrade gitlab-runner
   ```

## Additional Resources

- [GitLab Runner Documentation](https://docs.gitlab.com/runner/)
- [Docker-in-Docker Guide](https://docs.gitlab.com/ee/ci/docker/using_docker_build.html)
- [AWS EC2 Documentation](https://docs.aws.amazon.com/ec2/)