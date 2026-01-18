"use client";

import withLogin from "@/global/auth/hoc/withLogin";
import { useAuthContext } from "@/global/auth/hooks/useAuth";

export default withLogin(function Page() {
  const { loginMember } = useAuthContext();

  return (
    <div className="space-y-6">
      <h1 className="text-2xl font-bold">내 정보</h1>

      <div className="bg-white rounded-lg shadow p-6 space-y-4">
        {loginMember.profileImgUrl && (
          <div className="flex justify-center">
            <img
              src={loginMember.profileImgUrl}
              alt="프로필 이미지"
              className="w-24 h-24 rounded-full object-cover"
            />
          </div>
        )}

        <div className="space-y-2">
          <div className="flex border-b pb-2">
            <span className="w-24 text-gray-500">ID</span>
            <span>{loginMember.id}</span>
          </div>
          <div className="flex border-b pb-2">
            <span className="w-24 text-gray-500">이름</span>
            <span>{loginMember.name}</span>
          </div>
          <div className="flex border-b pb-2">
            <span className="w-24 text-gray-500">아이디</span>
            <span className="text-sm text-gray-600">{loginMember.username}</span>
          </div>
          <div className="flex border-b pb-2">
            <span className="w-24 text-gray-500">가입일</span>
            <span className="text-sm">{loginMember.createDate}</span>
          </div>
          <div className="flex">
            <span className="w-24 text-gray-500">수정일</span>
            <span className="text-sm">{loginMember.modifyDate}</span>
          </div>
        </div>
      </div>
    </div>
  );
});
