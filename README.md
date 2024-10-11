# Overview

The distributed task queue system will have:
1. A Task Producer that generates tasks and publishes them to a Kafka topic.
2. Worker services that consume tasks, process them, and report results back to the Result Aggregator using gRPC.
3. A Result Aggregator service that receives task results and stores or logs them.

# Development Environment Setup

- [Install/update latest docker](https://docs.docker.com/engine/install/) (Docker desktop on Mac and Windows)
- Install docker-compose version 1.29.2 or greater. [Instruction here](https://docs.docker.com/compose/install/#install-compose-on-linux-systems)
- Install [VSCode](https://code.visualstudio.com/download)
- Install [Remote Development Extension Pack](https://marketplace.visualstudio.com/items?itemName=ms-vscode-remote.vscode-remote-extensionpack) VSCode extension
- [Clone the repo](#cloning-the-repository) and open in VSCode
- Copy `dev.env` to `.env` and [fill the values that are empty](#setting-values-in-env-file)
- Reopen the project in Dev-container


## Cloning the repository

- Go through the [Git Setup](#git-setup)
- Clone the repo using command `git clone git@github.com:waqqas/distributed_task_queue.git`

## Git Setup

- [Install Git](https://git-scm.com/book/en/v2/Getting-Started-Installing-Git) on your development machine
- Create an SSH key.
  - Generate SSH key `ssh-keygen -t ed25519 -C "<your-email-address>"`
  - Name the key `~/.ssh/id_ed25519`
  - Set passphrase
- Add SSH key to SSH client
  - `eval "$(ssh-agent -s)" ssh-add ~/.ssh/id_ed25519`
- Copy the SSH public key on clipboard.
  - For MacOS `tr -d '\n' < ~/.ssh/id_ed25519.pub | pbcopy`
  - For Linux `xclip -sel clip < ~/.ssh/id_ed25519.pub`
  - For Windows, on Git Bash `cat ~/.ssh/id_ed25519.pub | clip`
- Add SSH key on GitHub.
  - Login to your GitHub account. (Create one if you don't have any)
  - Go to [SSH and GPG Keys](https://github.com/settings/keys) menu in settings and click on "New SSH Key" button
  - Paste the SSH key, set Key-type to "Authentication Key", give a Title and click on "Add SSH key"
- Mark the project directory as git safe directory
  - `git config --global --add safe.directory /workspaces/distributed_task_queue`
