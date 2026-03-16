import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

export default defineConfig({
  plugins: [react()],
  server: {
    proxy: {
      '/api': {
        target: 'http://localhost:8083',
        changeOrigin: true,
        secure: false,
        configure: (proxy) => {
          proxy.on('proxyReq', (proxyReq, req) => {
            // Only remove Authorization on register endpoint
            // to prevent browser native auth popup
            if (req.url?.includes('/auth/register')) {
              proxyReq.removeHeader('Authorization');
            }
          });
        },
      }
    }
  }
})