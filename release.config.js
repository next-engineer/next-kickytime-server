module.exports = {
  branches: ['main'],
  plugins: [
    ['@semantic-release/commit-analyzer', {
      preset: 'conventionalcommits',
      parserOpts: {
        headerPattern: /^((?:\p{Extended_Pictographic}|\p{Emoji_Presentation}|\p{Emoji}(?:\uFE0F)?|:\w+:)\s*)?(feat|fix|perf|refactor|docs|style|test|chore|revert)(?:\(([^)]+)\))?!?:\s(.+)$/u,
        headerCorrespondence: ['emoji','type','scope','subject'],
        noteKeywords: ['BREAKING CHANGE','BREAKING-CHANGE'],
      },
      releaseRules: [
        { breaking: true, release: 'major' },
        { type: 'feat', release: 'minor' },
        { type: 'fix', release: 'patch' },
      ],
    }],
    ['@semantic-release/release-notes-generator', {
      preset: 'conventionalcommits',
      parserOpts: {
        headerPattern: /^((?:\p{Extended_Pictographic}|\p{Emoji_Presentation}|\p{Emoji}(?:\uFE0F)?|:\w+:)\s*)?(feat|fix|perf|refactor|docs|style|test|chore|revert)(?:\(([^)]+)\))?!?:\s(.+)$/u,
        headerCorrespondence: ['emoji','type','scope','subject'],
        noteKeywords: ['BREAKING CHANGE','BREAKING-CHANGE'],
      },
    }],
    ['@semantic-release/changelog', { changelogFile: 'CHANGELOG.md' }],
    ['@semantic-release/git', {
      assets: ['CHANGELOG.md'],
      message: '📦 chore(release): ${nextRelease.version} [skip ci]\n\n${nextRelease.notes}',
    }],
    '@semantic-release/github',
  ],
};
