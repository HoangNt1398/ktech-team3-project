import js from '@eslint/js';
import globals from 'globals';
import reactHooks from 'eslint-plugin-react-hooks';
import reactRefresh from 'eslint-plugin-react-refresh';
import ts from '@typescript-eslint/eslint-plugin';
import tsParser from '@typescript-eslint/parser';

export default [
  {
    ignores: ['dist'], // Bỏ qua thư mục build
  },
  {
    files: ['**/*.{ts,tsx}'],
    languageOptions: {
      ecmaVersion: 2020, // Sử dụng ECMAScript 2020
      globals: globals.browser, // Đặt môi trường globals cho trình duyệt
      parser: tsParser, // Dùng parser cho TypeScript
    },
    plugins: {
      'react-hooks': reactHooks, // Đăng ký plugin react-hooks
      'react-refresh': reactRefresh, // Đăng ký plugin react-refresh
    },
    rules: {
      ...reactHooks.configs.recommended.rules, // Sử dụng rule mặc định của react-hooks
      'react-refresh/only-export-components': [
        'warn',
        { allowConstantExport: true },
      ], // Rule cho React Refresh
      '@typescript-eslint/no-empty-object-type': 'off', // Tắt cảnh báo no-empty-object-type
    },
    extends: [
      js.configs.recommended, // Khuyến nghị từ @eslint/js
      'plugin:react/recommended', // Khuyến nghị từ React
      'plugin:@typescript-eslint/recommended', // Khuyến nghị từ TypeScript ESLint
    ],
  },
];
