module.exports = {
  extends: ['@commitlint/config-conventional'],
  plugins: [
    {
      rules: {
        'emoji-required': (parsed, when = 'always') => {
          const hasEmoji = /^(?:\p{Extended_Pictographic}|\p{Emoji_Presentation}|\p{Emoji}(?:\uFE0F)?|:\w+:)\s+/u.test(parsed.header || '');
          const negates = when === 'never';
          const pass = negates ? !hasEmoji : hasEmoji;
          return [pass, '커밋 메시지 맨 앞에 이모지 + 공백이 필요합니다.'];
        },
      },
    },
  ],
  rules: {
    'emoji-required': [2, 'always'],
    'type-enum': [2, 'always', ['feat','fix','typo','refactor','docs','style','test','chore','revert','config','setup','ui','arch','design','upgrade','downgrade','remove']],
    'header-max-length': [2, 'always', 100],
  },
  parserPreset: {
    parserOpts: {
      headerPattern: /^((?:\p{Extended_Pictographic}|\p{Emoji_Presentation}|\p{Emoji}(?:\uFE0F)?|:\w+:)\s+)(\w+)(?:\(([^)]*)\))?:\s(.+)$/u,
      headerCorrespondence: ['emoji','type','scope','subject'],
    },
  },
};
