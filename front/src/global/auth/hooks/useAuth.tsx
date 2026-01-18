"use client";

import {
  createContext,
  useContext,
  useState,
  useEffect,
  useCallback,
  ReactNode,
} from "react";

const NEXT_PUBLIC_API_BASE_URL =
  process.env.NEXT_PUBLIC_API_BASE_URL || "http://localhost:8080";

interface LoginMember {
  id: number;
  createDate: string;
  modifyDate: string;
  username: string;
  nickname: string;
  profileImgUrl: string;
}

interface ApiResponse<T> {
  resultCode: string;
  msg: string;
  data: T;
}

interface AuthContextType {
  loginMember: LoginMember | null;
  isLoggedIn: boolean;
  isLoading: boolean;
  logout: () => Promise<void>;
  refresh: () => Promise<void>;
}

const AuthContext = createContext<AuthContextType | null>(null);

export function AuthProvider({ children }: { children: ReactNode }) {
  const [loginMember, setLoginMember] = useState<LoginMember | null>(null);
  const [isLoading, setIsLoading] = useState(true);

  const fetchMe = useCallback(async () => {
    try {
      const response = await fetch(`${NEXT_PUBLIC_API_BASE_URL}/api/v1/members/me`, {
        credentials: "include",
      });
      if (response.ok) {
        const data: ApiResponse<LoginMember> = await response.json();
        if (data?.data) {
          setLoginMember(data.data);
        } else {
          setLoginMember(null);
        }
      } else {
        setLoginMember(null);
      }
    } catch {
      setLoginMember(null);
    } finally {
      setIsLoading(false);
    }
  }, []);

  useEffect(() => {
    fetchMe();
  }, [fetchMe]);

  const logout = async () => {
    try {
      await fetch(`${NEXT_PUBLIC_API_BASE_URL}/api/v1/members/logout`, {
        method: "DELETE",
        credentials: "include",
      });
    } finally {
      setLoginMember(null);
    }
  };

  const refresh = async () => {
    setIsLoading(true);
    await fetchMe();
  };

  return (
    <AuthContext.Provider
      value={{
        loginMember,
        isLoggedIn: !!loginMember,
        isLoading,
        logout,
        refresh,
      }}
    >
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth() {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error("useAuth must be used within an AuthProvider");
  }
  return context;
}

export function getGoogleLoginUrl(redirectUrl?: string): string {
  const baseUrl = `${NEXT_PUBLIC_API_BASE_URL}/oauth2/authorization/google`;
  if (redirectUrl) {
    return `${baseUrl}?redirectUrl=${encodeURIComponent(redirectUrl)}`;
  }
  return baseUrl;
}
