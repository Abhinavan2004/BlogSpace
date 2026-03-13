import { StrictMode } from 'react';
import { createRoot } from 'react-dom/client';
import App from './App.tsx';
import './index.css';

console.log('Domain:', import.meta.env.VITE_AUTH0_DOMAIN);

createRoot(document.getElementById('root')!).render(
  <StrictMode>
      <App />
  </StrictMode>
);