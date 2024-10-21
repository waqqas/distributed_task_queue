FROM mcr.microsoft.com/devcontainers/java:1-21-bullseye

COPY --from=golang:1.23-bullseye /usr/local/go/ /usr/local/go/
 
ENV PATH="/usr/local/go/bin:${PATH}"

ARG DEBIAN_FRONTEND=noninteractive
ARG USER=vscode

USER $USER
ARG HOME="/home/$USER"

RUN go install github.com/bazelbuild/buildtools/buildifier@latest
RUN go install github.com/bazelbuild/buildtools/buildozer@latest
RUN go install github.com/bazelbuild/buildtools/unused_deps@latest

ENV PATH="$HOME/go/bin:${PATH}"

RUN curl https://raw.githubusercontent.com/git/git/master/contrib/completion/git-completion.bash -o ~/.git-completion.bash
