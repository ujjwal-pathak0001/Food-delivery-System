import React, { createContext, useContext, useEffect, useMemo, useState } from 'react';
import { client, getStoredToken, persistToken, setAuthToken } from '../api/client';

const AuthContext = createContext(null);

export function AuthProvider({ children }) {
  const [user, setUser] = useState(null);
  const [token, setToken] = useState(() => getStoredToken());
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    if (token) {
      setAuthToken(token);
      const raw = localStorage.getItem('user');
      if (raw) {
        try {
          setUser(JSON.parse(raw));
        } catch {
          setUser(null);
        }
      }
    } else {
      setAuthToken(null);
      setUser(null);
    }
    setLoading(false);
  }, [token]);

  const login = async (email, password) => {
    const { data } = await client.post('/auth/login', { email, password });
    persistToken(data.token);
    setAuthToken(data.token);
    setToken(data.token);
    const u = {
      userId: data.userId,
      email: data.email,
      fullName: data.fullName,
      role: data.role,
    };
    localStorage.setItem('user', JSON.stringify(u));
    setUser(u);
    return data;
  };

  const register = async (payload) => {
    const { data } = await client.post('/auth/register', payload);
    persistToken(data.token);
    setAuthToken(data.token);
    setToken(data.token);
    const u = {
      userId: data.userId,
      email: data.email,
      fullName: data.fullName,
      role: data.role,
    };
    localStorage.setItem('user', JSON.stringify(u));
    setUser(u);
    return data;
  };

  const logout = () => {
    persistToken(null);
    setAuthToken(null);
    setToken(null);
    localStorage.removeItem('user');
    setUser(null);
  };

  const value = useMemo(
    () => ({
      user,
      token,
      loading,
      isAuthenticated: Boolean(token && user),
      login,
      register,
      logout,
    }),
    [user, token, loading]
  );

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

export function useAuth() {
  const ctx = useContext(AuthContext);
  if (!ctx) throw new Error('useAuth must be used within AuthProvider');
  return ctx;
}
