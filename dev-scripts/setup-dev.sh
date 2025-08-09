#!/bin/bash
set -e

pip install pre-commit
pre-commit install
pre-commit install --hook-type commit-msg
npm install
