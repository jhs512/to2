"use client";

import { useAuthContext } from "@/global/auth/hooks/useAuth";
import { useRouter } from "next/navigation";
import { useEffect, ComponentType } from "react";

export default function withLogin<P extends object>(
  Component: ComponentType<P>
) {
  return function ProtectedComponent(props: P) {
    const { isLogin, loginMember } = useAuthContext();
    const router = useRouter();

    useEffect(() => {
      if (!isLogin && loginMember === null) {
        // 아직 로딩 중이 아닌데 로그인이 안되어 있으면 리다이렉트
        const timer = setTimeout(() => {
          if (!isLogin) {
            router.replace("/members/login");
          }
        }, 100);
        return () => clearTimeout(timer);
      }
    }, [isLogin, loginMember, router]);

    if (!isLogin) {
      return (
        <div className="flex items-center justify-center min-h-[200px]">
          <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-gray-900"></div>
        </div>
      );
    }

    return <Component {...props} />;
  };
}
