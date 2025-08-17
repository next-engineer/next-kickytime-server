#!/bin/bash
set -e

echo "[1/5] Git 저장소 여부 확인"
git rev-parse --is-inside-work-tree >/dev/null 2>&1 || {
  echo "Git 저장소 내부에서 실행해 주세요."; exit 1;
}

echo "[2/5] Git hooks 경로(.husky) 설정 확인"
current_hooks_path="$(git config --get core.hooksPath || true)"
if [ "$current_hooks_path" != ".husky" ]; then
  git config core.hooksPath .husky
  echo "core.hooksPath를 .husky로 설정했습니다."
else
  echo "core.hooksPath가 이미 .husky로 설정되어 있습니다."
fi

echo "[3/5] Node 모듈 설치 (npm ci가 가능하면 우선 사용)"
if [ -f package-lock.json ]; then
  npm ci || npm install
else
  npm install
fi

echo "[4/5] Husky 설치 실행 (package.json의 prepare 스크립트)"
npm run -s prepare || true

echo "[5/5] 기존 훅 파일 권한 보정"
if [ -f .husky/pre-commit ]; then
  chmod +x .husky/pre-commit || true
fi
if [ -f .husky/commit-msg ]; then
  chmod +x .husky/commit-msg || true
fi

echo "완료: Husky 전용 설정이 적용되었습니다. 기존 훅 파일은 그대로 유지되었습니다."
