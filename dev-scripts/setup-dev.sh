#!/bin/bash
set -e

pip install pre-commit

# bash 환경 재로딩 (윈도우 Git Bash / 맥 bash 공통)
if [ -f ~/.bashrc ]; then
    source ~/.bashrc
fi

# 맥 zsh 환경 대응
if [ -f ~/.zshrc ]; then
    source ~/.zshrc
fi

pre-commit install
pre-commit install --hook-type commit-msg
npm install
