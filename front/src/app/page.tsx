"use client";

import Link from "next/link";
import { useAuthContext } from "@/global/auth/hooks/useAuth";

export default function Home() {
  const { isLogin, loginMember } = useAuthContext();

  return (
    <div className="space-y-8">
      <section className="text-center py-12">
        <h1 className="text-4xl font-bold text-gray-900 mb-4">
          to2.at에 오신 것을 환영합니다
        </h1>
        <p className="text-lg text-gray-600">
          {isLogin
            ? `${loginMember.name}님, 반갑습니다!`
            : "로그인하여 서비스를 이용해보세요."}
        </p>
      </section>

      <section className="grid gap-4 md:grid-cols-2">
        {isLogin ? (
          <>
            <Link
              href="/members/me"
              className="p-6 bg-white rounded-lg shadow hover:shadow-md transition-shadow"
            >
              <h2 className="text-xl font-semibold mb-2">내 정보</h2>
              <p className="text-gray-600">내 계정 정보를 확인하세요.</p>
            </Link>
          </>
        ) : (
          <>
            <Link
              href="/members/login"
              className="p-6 bg-white rounded-lg shadow hover:shadow-md transition-shadow"
            >
              <h2 className="text-xl font-semibold mb-2">로그인</h2>
              <p className="text-gray-600">소셜 계정으로 간편하게 로그인하세요.</p>
            </Link>
          </>
        )}
      </section>
    </div>
  );
}
