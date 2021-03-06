package com.mrs.project.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.tags.EvalTag;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.core.JsonParser;
import com.mrs.project.dto.BoardDTO;
import com.mrs.project.service.AdminService;

@Controller
public class AdminController {
	@Autowired AdminService service;
	@Value("#{config['manager.id']}") String managerid;
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
// 관리자 페이지 접속 -> 무조건 게시글 관리부터 가게 되어있음
	@RequestMapping(value = "/admin", method = RequestMethod.GET)
	public ModelAndView index(Model model,HttpSession session,@RequestParam String type, RedirectAttributes rAttr) {
		//logger.info("관리자모드 진입");
		//logger.info("type :"+type);
		ModelAndView mav = new ModelAndView();	
			//System.out.println(managerid+"어드민값");
			//System.out.println("Session :"+session.getAttribute("loginid"));
			String loginId=(String) session.getAttribute("loginid");
			String msg = "접근할 수 없습니다.";
			String page = "index";
			if(managerid.equals(loginId)) {
				msg = "접근 성공";
				mav.setViewName("admin/admin_board");
				mav.addObject("type", type);
			}else {
				if(!managerid.equals(loginId)) {
					
				}
				mav.setViewName(page);
			}		
			rAttr.addFlashAttribute("msg", msg);
		return mav;
	}
	
	//관리자 게시글 삭제
	@RequestMapping(value = "/admindel", method = RequestMethod.GET)
	public @ResponseBody HashMap<String, Object> admindel(@RequestParam String board_idx) {
		//logger.info(board_idx+"삭제 idx값");
		HashMap<String, Object> map = new HashMap<String, Object>();
		String msg = "실패";
		int del_cnt = service.admindel(board_idx);
		if(del_cnt>0) {
			msg = "성공";
		}
				// 제이슨 형태로 결과값을 보내줘야함.
		
		map.put("msg", msg);
		map.put("del_cnt", del_cnt);
		return map;
	}

	/*
	 * @RequestMapping(value = "/admin_faqboard", method = RequestMethod.GET) public
	 * ModelAndView adminfaqboard(Model model,@RequestParam Map<String, String>
	 * params,HttpSession session, RedirectAttributes rAttr) {
	 * logger.info("여기오나요?"); String msg = null; ModelAndView mav = new
	 * ModelAndView(); if(managerid!=null) { mav = service.adminfaqlist(params);
	 * mav.setViewName("admin/admin_faqboard2");
	 * logger.info("회원리스트를 잘 불러오는가요?"+mav); } else { if(!managerid.equals("admin"))
	 * { msg = "접근할 수 없습니다."; } } rAttr.addFlashAttribute("msg", msg); return mav; }
	 */
		
	@RequestMapping(value = "/admin_member", method = RequestMethod.GET)
	public ModelAndView admin_member(Model model) {
		//logger.info("여기오나요?");
		ModelAndView mav = new ModelAndView();	
		if(managerid!=null) {
			mav.setViewName("admin/admin_member");
		}			
		return mav;
	}
	
	//아작스 사용해서 페이징한 리스트
		@RequestMapping(value = "/membercall", method = RequestMethod.GET)
		public @ResponseBody HashMap<String, Object> listcall(@RequestParam HashMap<String, String>params) {
			String page = params.get("page");
			String pagePerCnt = params.get("ppn");	
			return service.membercall(Integer.parseInt(page), Integer.parseInt(pagePerCnt));
		}
	
	@RequestMapping(value = "/adminmemberdel", method = RequestMethod.GET)
	public @ResponseBody HashMap<String, Object> adminmemberdel(@RequestParam String id) {
		//logger.info(id+"삭제 idx값");
		HashMap<String, Object> map = new HashMap<String, Object>();
		String msg = "실패";
		int del_cnt = service.adminmemberdel(id);
		if(del_cnt>0) {
			msg = "성공";
		}		
		map.put("msg", msg);
		map.put("del_cnt", del_cnt);
		return map;
	}
	
	
}