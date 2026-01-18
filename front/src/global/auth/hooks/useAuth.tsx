"use client";

import type { components } from "@/global/backend/apiV1/schema";
import client from "@/global/backend/client";
import { createContext, use, useEffect, useState } from "react";

type MemberWithUsernameDto = components["schemas"]["MemberWithUsernameDto"];

const NEXT_PUBLIC_API_BASE_URL = process.env.NEXT_PUBLIC_API_BASE_URL;

export default function useAuth() {
  const [loginMember, setLoginMember] = useState<MemberWithUsernameDto>(
    null as unknown as MemberWithUsernameDto,
  );
  const isLogin = loginMember !== null;

  useEffect(() => {
    client.GET("/api/v1/members/me").then((res) => {
      if (res.error) return;

      setLoginMember(res.data);
    });
  }, []);

  const clearLoginMember = () => {
    setLoginMember(null as unknown as MemberWithUsernameDto);
  };

  const logout = (onSuccess: () => void) => {
    client.DELETE("/api/v1/members/auth").then((res) => {
      if (res.error) {
        alert(res.error.msg);
        return;
      }

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
  null as unknown as ReturnType<typeof useAuth>,
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

export function getLoginUrl(
  providerTypeCode: string,
  redirectUrl?: string,
): string {
  const baseUrl = `${NEXT_PUBLIC_API_BASE_URL}/oauth2/authorization/${providerTypeCode}`;
  if (redirectUrl) {
    return `${baseUrl}?redirectUrl=${encodeURIComponent(redirectUrl)}`;
  }
  return baseUrl;
}
