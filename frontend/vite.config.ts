import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';

export default defineConfig({
  plugins: [react()],
  server: {
    port: 5173,
    proxy: {
      '/adduser': 'http://localhost:8080',
      '/addBio': 'http://localhost:8080',
      '/deleteBio': 'http://localhost:8080',
      '/changeBio': 'http://localhost:8080',
      '/addStatus': 'http://localhost:8080',
      '/deleteStatus': 'http://localhost:8080',
      '/changeUserName': 'http://localhost:8080',
      '/changeName': 'http://localhost:8080',
      '/follow': 'http://localhost:8080',
      '/allusers': 'http://localhost:8080',
      '/unfollow': 'http://localhost:8080',
      '/getAllFollowers': 'http://localhost:8080',
      '/getAllFollowing': 'http://localhost:8080',
      '/blockuser': 'http://localhost:8080',
      '/unBlockuser': 'http://localhost:8080',
      '/uploadPost': 'http://localhost:8080',
      '/delete': 'http://localhost:8080',
      '/allpost': 'http://localhost:8080'
    }
  }
});


