package com.capstone.controller;

import java.io.PrintWriter;
import java.util.List;

import javax.inject.Inject;
import javax.management.RuntimeErrorException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.capstone.domain.GoodsVO;
import com.capstone.domain.MemberVO;
import com.capstone.domain.TradeVO;
import com.capstone.service.MemberService;

@Controller
@RequestMapping("/member/*")
public class MemberController {

	private static final Logger logger = LoggerFactory.getLogger(MemberController.class);

	@Inject
	MemberService service;

	// 회원 가입 get
	@RequestMapping(value = "/signup", method = RequestMethod.GET)
	public void getSignup() throws Exception {
		logger.info("get signup");
	}

	// 회원 가입 post
	@RequestMapping(value = "/signup", method = RequestMethod.POST)
	public String postSignup(MemberVO vo) throws Exception {
		logger.info("post signup");
		service.signup(vo);

		return "redirect:/";
	}

	// 로그인 get
	@RequestMapping(value = "/signin", method = RequestMethod.GET)
	public void getSignin() throws Exception {
		logger.info("get signin");
	}

	// 로그인 post
	@RequestMapping(value = "/signin", method = RequestMethod.POST)
	public String postSignin(MemberVO vo, Model model, HttpServletRequest req, RedirectAttributes rttr, HttpServletResponse response) throws Exception {
			logger.info("post signin");
			String Id = req.getParameter("Id");
			String Pw = req.getParameter("Pw");
			
			//줄떈 id,pw를 주고 db들렸다 나와서야 resultType="vo"타입으로 받는 것
			//그 이후에 MemberVO 가 채워진다.
			MemberVO login = service.signin(Id,Pw);
			
			
			//MemberVO login = service.signin(vo);  // MemverVO형 변수 login에 로그인 정보를 저장
			HttpSession session = req.getSession();  // 현재 세션 정보를 가져옴
			
			//value가 9번이 아니면 user
			if(service.getV(Id)!=9) {
				if(login==null){
					model.addAttribute("msg","ID나PW가 틀립니다.");
					return "member/signin";
				}else{
					session.setAttribute("member", login);
					
					List<TradeVO> list = service.tradeView(login.getId());
					response.setContentType("text/html; charset=UTF-8");
					 
					PrintWriter out = response.getWriter();
				/*
				if(list.size()==0) {
					out.println("<script language='javascript'>");
					out.println("alert('충대 장터에 오신것을 환영합니다.');");
					out.println("</script>");
					 
					out.flush();
				}
				else {					 
					out.println("<script language='javascript'>");
					out.println("alert('완료된 거래 중 작성하지 않은 후기가 있습니다.');");
					out.println("</script>");
					 
					out.flush();
				}
			}
			*/
					return "/move/index";
				}else{
					if(login==null){
						model.addAttribute("msg","ID나PW가 틀립니다.");
						return "member/signin";
					}else{
						session.setAttribute("member", login);
						//관리자니 list도 필요없을거고
						//List<TradeVO> list = service.tradeView(login.getId());
						response.setContentType("text/html; charset=UTF-8");
						
				//value가 9번이면 manager
				
				return "/move/managerpage";
	}

	// 로그아웃
	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logout(HttpSession session) throws Exception {
		logger.info("get logout");

		session.invalidate();

		return "redirect:/";
	}

	// 아이디 중복 체크
	@ResponseBody
	@RequestMapping(value = "/idChk", method = RequestMethod.POST)
	public int postIdChk(HttpServletRequest req, MemberVO vo) throws Exception {
		String user = req.getParameter("Id");
		MemberVO result = service.idChk(user);
		if (result == null) {
			return 2;
		} else
			return 1;
	}

}
