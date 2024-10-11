FROM mcr.microsoft.com/devcontainers/java:1-21-bullseye

ARG DEBIAN_FRONTEND=noninteractive
ARG USER=vscode

USER $USER
ARG HOME="/home/$USER"

RUN curl https://raw.githubusercontent.com/git/git/master/contrib/completion/git-completion.bash -o ~/.git-completion.bash
