package com.capstone.persistence;

import java.util.List;

import com.capstone.domain.GoodsVO;
import com.capstone.domain.MemberVO;
import com.capstone.domain.TradeVO;

public interface MemberDAO {
		// 회원 가입
		public void signup(MemberVO vo) throws Exception;
		
		// 로그인
		public MemberVO signin(String Id,String Pw) throws Exception;
		
		//아이디 중복체크
		public MemberVO idChk(String Id) throws Exception;
		
		//verify
		public int getV(String Id) throws Exception;
		
		//거래 조회
		public List<TradeVO> tradeView(String Id) throws Exception;
}