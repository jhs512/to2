"use client";

import { useAuth } from "@/global/auth/hooks/useAuth";
import { useRouter } from "next/navigation";
import { useEffect, ComponentType } from "react";

export default function withLogout<P extends object>(
  Component: ComponentType<P>
) {
  return function GuestOnlyComponent(props: P) {
    const { isLoggedIn, isLoading } = useAuth();
    const router = useRouter();

    useEffect(() => {
      if (!isLoading && isLoggedIn) {
        router.replace("/");
      }
    }, [isLoggedIn, isLoading, router]);

    if (isLoading) {
      return (
        <div className="flex items-center justify-center min-h-screen">
          <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-gray-900"></div>
        </div>
      );
    }

    if (isLoggedIn) {
      return null;
    }

    return <Component {...props} />;
  };
}
