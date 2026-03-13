import React, { createContext, useContext, useState, useCallback, useEffect } from 'react';
import { apiService } from '../services/apiService';

interface AuthUser {
  id: string;
  name: string;
  email: string;
}

interface AuthContextType {
  isAuthenticated: boolean;
  isLoading: boolean;
  user: AuthUser | null;
  loginWithEmail: () => void;
  loginWithGoogle: () => void;
  logout: () => void;
  setTokenAndSync: (token: string) => Promise<void>;
}

const AuthContext = createContext<AuthContextType | null>(null);

export const AuthProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const [user, setUser]           = useState<AuthUser | null>(null);
  const [isLoading, setIsLoading] = useState(true);

  // On app load, restore session from stored token
  useEffect(() => {
    const token = localStorage.getItem('auth_token');
    if (token) {
      apiService.syncUser(token)
        .then(dbUser => setUser({ id: dbUser.id, name: dbUser.username, email: dbUser.email }))
        .catch(() => localStorage.removeItem('auth_token'))
        .finally(() => setIsLoading(false));
    } else {
      setIsLoading(false);
    }
  }, []);

  const setTokenAndSync = useCallback(async (token: string) => {
    setIsLoading(true);
    try {
      const dbUser = await apiService.syncUser(token);
      setUser({ id: dbUser.id, name: dbUser.username, email: dbUser.email });
    } finally {
      setIsLoading(false);
    }
  }, []);

  const loginWithGoogle = useCallback(() => {
    // Directly hit Spring Boot's OAuth2 endpoint
    window.location.href = 'http://localhost:8083/oauth2/authorization/google';
  }, []);

  const loginWithEmail = useCallback(() => {
    window.location.href = '/login/email'; // or open your email form
  }, []);

  const logout = useCallback(() => {
    localStorage.removeItem('auth_token');
    setUser(null);
  }, []);

  return (
    <AuthContext.Provider value={{
      isAuthenticated: !!user,
      isLoading,
      user,
      loginWithEmail,
      loginWithGoogle,
      logout,
      setTokenAndSync,
    }}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => {
  const ctx = useContext(AuthContext);
  if (!ctx) throw new Error('useAuth must be used within AuthProvider');
  return ctx;
};