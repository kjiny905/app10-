package com.kh.app.domain.member.dao;

import com.kh.app.domain.entity.Member;

import java.util.List;

public interface MemberDAO {
  //가입
  Member save(Member member);

  //수정
  void update(Long member_id, Member member);

  //조회 by mail
  Member findByEmail(String email);

  //조회 by member_id
  Member findById(String memberId);

  //전체조회(관리자)
  List<Member> findAll();

  //탈퇴
  void delete(String email);

  //회원유무
  boolean isExist(String email);

  //로그인
  Member login(String email, String passwd);

  //아이디찾기
  String findEmailByNickname(String nickname);

  //비밀번호찾기
  String findPasswdByIdAndNickname(String memberId, String passwd);

}
