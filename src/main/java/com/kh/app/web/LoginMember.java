package com.kh.app.web;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 회원 세션 정보(로그인에 성공하면 아래 정보를 기본으로 가져가기 위함)
 */
@Data
@AllArgsConstructor
public class LoginMember {
  private Long memberId;
  private String email;
  private String nickname;
  private String gubun;
}
