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

// Helper — decode JWT payload without a library
const decodeJwt = (token: string): { sub?: string } | null => {
  try {
    const parts = token.split('.');
    if (parts.length !== 3) return null;
    return JSON.parse(atob(parts[1]));
  } catch {
    return null;
  }
};

export const AuthProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const [user, setUser] = useState<AuthUser | null>(null);
  const [isLoading, setIsLoading] = useState(true);

  // On app load, restore session from stored token
  useEffect(() => {
    const token = localStorage.getItem('auth_token');
    if (token) {
      apiService.syncUser(token)
        .then(dbUser => {
          setUser({ id: dbUser.id, name: dbUser.username, email: dbUser.email });
        })
        .catch(() => {
          // Sync failed — fall back to JWT payload so user stays logged in
          console.warn('Sync failed on load, using JWT fallback');
          const payload = decodeJwt(token);
          if (payload?.sub) {
            setUser({
              id: payload.sub,
              name: payload.sub,
              email: payload.sub,
            });
          } else {
            // Token is invalid — clear it
            localStorage.removeItem('auth_token');
          }
        })
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
    } catch (err) {
      console.warn('setTokenAndSync: sync failed, using JWT fallback', err);
      // Fall back to JWT payload — keeps user logged in even if sync endpoint is down
      const payload = decodeJwt(token);
      if (payload?.sub) {
        setUser({
          id: payload.sub,
          name: payload.sub,
          email: payload.sub,
        });
      } else {
        localStorage.removeItem('auth_token');
        throw err; // Re-throw only if token itself is invalid
      }
    } finally {
      setIsLoading(false);
    }
  }, []);

  const loginWithGoogle = useCallback(() => {
    window.location.href = 'https://blogspace-production-c0a6.up.railway.app/oauth2/authorization/google';
  }, []);

  const loginWithEmail = useCallback(() => {
    window.location.href = '/login/email';
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