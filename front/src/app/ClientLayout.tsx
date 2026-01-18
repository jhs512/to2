"use client";

import { AuthContext, useAuthContext, getLoginUrl } from "@/global/auth/hooks/useAuth";
import Link from "next/link";
import { useRouter } from "next/navigation";

export default function ClientLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  const authState = useAuthContext();
  const router = useRouter();

  const { loginMember, isLogin, logout: _logout } = authState;

  const frontendBaseUrl =
    typeof window !== "undefined" ? window.location.origin : "";
  const redirectUrl = `${frontendBaseUrl}/members/me`;

  const logout = () => {
    _logout(() => router.replace("/"));
  };

  return (
    <AuthContext value={authState}>
      <header className="border-b">
        <nav className="flex items-center gap-1 p-2 max-w-4xl mx-auto">
          <Link href="/" className="px-3 py-2 rounded hover:bg-gray-100">
            홈
          </Link>
          <div className="flex-1" />
          {!isLogin && (
            <>
              <Link
                href="/members/login"
                className="px-3 py-2 rounded hover:bg-gray-100"
              >
                로그인
              </Link>
              <a
                href={getLoginUrl("google", redirectUrl)}
                className="px-3 py-2 rounded hover:bg-gray-100 text-blue-600"
              >
                구글 로그인
              </a>
            </>
          )}
          {isLogin && (
            <>
              <Link
                href="/members/me"
                className="px-3 py-2 rounded hover:bg-gray-100 flex items-center gap-2"
              >
                {loginMember.profileImgUrl && (
                  <img
                    src={loginMember.profileImgUrl}
                    width="24"
                    height="24"
                    alt=""
                    className="rounded-full object-cover"
                  />
                )}
                <span>{loginMember.name}</span>
              </Link>
              <button
                onClick={logout}
                className="px-3 py-2 rounded hover:bg-gray-100 text-red-600"
              >
                로그아웃
              </button>
            </>
          )}
        </nav>
      </header>
      <main className="flex-1 flex flex-col max-w-4xl mx-auto w-full p-4">
        {children}
      </main>
      <footer className="text-center p-4 border-t text-gray-500 text-sm">
        &copy; 2024 to2.at
      </footer>
    </AuthContext>
  );
}
