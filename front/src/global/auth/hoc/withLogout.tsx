"use client";

import { useAuthContext } from "@/global/auth/hooks/useAuth";
import { useRouter } from "next/navigation";
import { useEffect, ComponentType } from "react";

export default function withLogout<P extends object>(
  Component: ComponentType<P>
) {
  return function GuestOnlyComponent(props: P) {
    const { isLogin, loginMember } = useAuthContext();
    const router = useRouter();

    useEffect(() => {
      if (isLogin && loginMember !== null) {
        router.replace("/");
      }
    }, [isLogin, loginMember, router]);

    if (isLogin) {
      return null;
    }

    return <Component {...props} />;
  };
}
