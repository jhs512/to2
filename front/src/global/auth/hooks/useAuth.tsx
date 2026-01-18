"use client";

import { createContext, use, useEffect, useState } from "react";

const NEXT_PUBLIC_API_BASE_URL =
  process.env.NEXT_PUBLIC_API_BASE_URL || "http://localhost:8080";

interface LoginMember {
  id: number;
  createDate: string;
  modifyDate: string;
  username: string;
  name: string;
  profileImgUrl: string;
}

export default function useAuth() {
  const [loginMember, setLoginMember] = useState<LoginMember>(
    null as unknown as LoginMember
  );
  const isLogin = loginMember !== null;

  useEffect(() => {
    fetch(`${NEXT_PUBLIC_API_BASE_URL}/api/v1/members/me`, {
      credentials: "include",
    })
      .then((res) => {
        if (!res.ok) throw new Error();
        return res.json();
      })
      .then((data: LoginMember) => {
        setLoginMember(data);
      })
      .catch(() => {
        // 로그인 안됨
      });
  }, []);

  const clearLoginMember = () => {
    setLoginMember(null as unknown as LoginMember);
  };

  const logout = (onSuccess: () => void) => {
    fetch(`${NEXT_PUBLIC_API_BASE_URL}/api/v1/members/auth`, {
      method: "DELETE",
      credentials: "include",
    })
      .then((res) => res.json())
      .then(() => {
        clearLoginMember();
        onSuccess();
      });
  };

  return {
    isLogin,
    loginMember,
    logout,
    setLoginMember,
    clearLoginMember,
  };
}

export const AuthContext = createContext<ReturnType<typeof useAuth>>(
  null as unknown as ReturnType<typeof useAuth>
);

export function AuthProvider({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  const authState = useAuth();

  return <AuthContext value={authState}>{children}</AuthContext>;
}

export function useAuthContext() {
  const authState = use(AuthContext);

  if (authState === null) throw new Error("AuthContext is not found");

  return authState;
}

export function getLoginUrl(providerTypeCode: string, redirectUrl?: string): string {
  const baseUrl = `${NEXT_PUBLIC_API_BASE_URL}/oauth2/authorization/${providerTypeCode}`;
  if (redirectUrl) {
    return `${baseUrl}?redirectUrl=${encodeURIComponent(redirectUrl)}`;
  }
  return baseUrl;
}
