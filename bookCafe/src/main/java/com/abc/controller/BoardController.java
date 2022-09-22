package com.abc.controller;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.abc.domain.Board;
import com.abc.domain.Recommend;
import com.abc.domain.Reply;
import com.abc.service.BoardService;
import com.abc.service.ReplyService;
import com.abc.util.FileService;
import com.abc.util.PageNavigator;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("board")
@Controller
public class BoardController {

	@Autowired
	private BoardService bService;

	@Autowired
	private ReplyService rService;

	@Value("${spring.servlet.multipart.location}")
	private String uploadPath;

	@Value("${user.board.page}")
	private int countPerPage;

	@Value("${user.board.group}")
	private int pagePerGroup;

	@GetMapping("/list")
	public String recommendList(String type, String searchWord, Model model,
			@RequestParam(name = "page", defaultValue = "1") int page) {

		List<Board> recommendList = null; // 결과 담을 리스트
		PageNavigator navi = bService.getPageNavigator(pagePerGroup, countPerPage, page, type, searchWord);

		log.debug("type : {}, searchWord : {}", type, searchWord);

		if (searchWord == null || type == null) {
			// 검색어 또는 카테고리 없으면 전체검색
			recommendList = bService.selectAllBoard(navi, null, null);
		} else {
			// 검색어 또는 카테고리 있으면 조건검색
			recommendList = bService.selectAllBoard(navi, type, searchWord);
			// 검색어와 타입을 model에 추가
			model.addAttribute("type", type);
			model.addAttribute("searchWord", searchWord);
		}
		model.addAttribute("recommendList", recommendList);
		model.addAttribute("navi", navi);

		log.debug("recommendList size : {}", recommendList.size());

		return "boardView/recommendList";
	}

	// 글 쓰기 화면 호출
	@GetMapping("/write")
	public String write() {
		log.debug("write() 실행");

		return "boardView/write";
	}

	// 글 쓰기 DB 저장
	@PostMapping("/write")
	public String write(Board board, @AuthenticationPrincipal UserDetails user, @RequestParam MultipartFile file) {
		board.setMemberId(user.getUsername());
		log.debug("write board : {}", board);
		log.debug("write user : {}", user);
		// 첨부된 파일의 파일명 출력
		log.debug("write file : {}", file.getOriginalFilename());

		// 인증정보에서 username(id값)을 가져와서 board 객체에 지정
		// 파일이 첨부되어 있으면 파일 정보를 board 객체에 저장
		if (!file.isEmpty()) { // file.isEmpty() == false?
			// 저장할 파일명 생성
			String savedFile = FileService.saveFile(file, uploadPath);
			log.debug("savedFile : {}", savedFile);
			board.setSavedFile(savedFile);
			board.setOriginalFile(file.getOriginalFilename());
		}

		bService.insertBoard(board);
		
		return "redirect:./list";
	}

	// 글 수정하기(화면 호출)
	@GetMapping("/update")
	public String update(int boardNum, Model model) {
		log.debug("update() num : {}", boardNum);

		Board resultBoard = bService.selectOneBoard(boardNum);

		log.debug("update() board : {}", resultBoard);

		model.addAttribute("board", resultBoard);

		return "boardView/update";
	}

	// 글 수정하기(DB 저장)
	@PostMapping("/update")
	public String update(Board board, @RequestParam MultipartFile file) {

		log.debug("update() board : {}", board);

		// 파일이 첨부되어 있으면 파일 정보를 board 객체에 저장
		if (!file.isEmpty()) { // file.isEmpty() == false?
			// 저장할 파일명 생성
			String savedFile = FileService.saveFile(file, uploadPath);
			log.debug("savedFile : {}", savedFile);
			board.setSavedFile(savedFile);
			board.setOriginalFile(file.getOriginalFilename());
		}
		bService.updateBoard(board);

		return "redirect:./list";
	}

	// 글 삭제하기
	@GetMapping("/delete")
	public String delete(int boardNum) {
		log.debug("delete() num : {}", boardNum);

		bService.deleteBoard(boardNum);

		return "redirect:./list";
	}

	// 글 읽기
	// 받아오고자 하는 값이 기본자료형일때 아무것도 넘겨주지 않으면
	// null이 들어오려고 해서 예외 발생
	@GetMapping("/read")
	public String read(int boardNum, @RequestParam(name = "page", defaultValue = "1") int page, @AuthenticationPrincipal UserDetails user, Model model) {
		log.debug("read() boardNum : {}", boardNum);

		PageNavigator navi = rService.getPageNavigator(boardNum, pagePerGroup, countPerPage, page);
		
		// service 호출
		bService.updateViewCount(boardNum);
		Board resultBoard = bService.selectOneBoard(boardNum);

		// 해당 글 번호의 댓글 정보 가져오기
		List<Reply> replyList = rService.selectReplyByBoardNum(boardNum, navi);

		log.debug("replyList size : {}", replyList.size());

		if (user != null) {
			String memberId = user.getUsername();
			Recommend recommend = new Recommend();
			recommend.setMemberId(memberId);
			recommend.setBoardNum(boardNum);

			// 유저 아이디와 추천글 번호에 따른 추천 가져옴
			Recommend memberRecommend = bService.selectRecommend(recommend);
			log.debug("memberRecommend : {}", memberRecommend);
			model.addAttribute("memberRecommend", memberRecommend);
		}

		model.addAttribute("board", resultBoard);
		model.addAttribute("replyList", replyList);
		model.addAttribute("navi", navi);

		return "boardView/read";
	}

	@GetMapping("/download")
	public String downloadFile(int boardNum, Model model, HttpServletResponse response) {

		log.debug("boardNum : {}", boardNum);

		// 이 번호에 해당하는 Board 객체를 가져온다
		Board board = bService.selectOneBoard(boardNum);

		// 원래 파일명
		String originalFile = board.getOriginalFile();

		try {
			// 여기 첨부파일 있음 이름은 originalFile
			response.setHeader("Content-Disposition",
					"attachment;filename=" + URLEncoder.encode(originalFile, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		// 저장된 파일 경로
		String fullPath = uploadPath + "/" + board.getSavedFile();

		// 서버의 파일을 읽을 입력 스트림 & 클라이언트에 출력할 출력스트림
		FileInputStream fileIn = null;
		ServletOutputStream fileOut = null;

		try {
			// fullPath에 해당하는 주소의 파일을 읽어옴
			fileIn = new FileInputStream(fullPath);
			fileOut = response.getOutputStream();

			// Spring의 파일 관련 유틸 이용해서 출력
			FileCopyUtils.copy(fileIn, fileOut);

			fileIn.close();
			fileOut.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

		return "redirect:/";
	}

	// 그림이 있으면 표시해줄 기능
	@GetMapping("/display")
	public ResponseEntity<Resource> display(int boardNum) {

		log.debug("boardNum : {}", boardNum);

		Board board = bService.selectOneBoard(boardNum);

		Resource resource = new FileSystemResource(uploadPath + "/" + board.getSavedFile());

		HttpHeaders header = new HttpHeaders();

		Path filePath = null;

		try {
			filePath = Paths.get(uploadPath + "/" + board.getSavedFile());

			// response의 header에
			// 제가 첨부한 내용의 타입은 파일임
			header.add("Content-type", Files.probeContentType(filePath));
		} catch (IOException e) {
			e.printStackTrace();
		}

		return new ResponseEntity<Resource>(resource, header, HttpStatus.OK);
	}

	@PostMapping("/writeReply")
	public String writeReply(Reply reply, @AuthenticationPrincipal UserDetails user) {
		log.debug("reply : {}", reply);

		String memberId = user.getUsername();

		reply.setMemberId(memberId);

		rService.insertReply(reply);

		return "redirect:./read?boardNum=" + reply.getBoardNum() + "#reList";
	}

	@GetMapping("/deleteReply")
	public String deleteReply(int boardNum) {
		log.debug("boardNum : {}", boardNum);

		Reply reply = rService.selectOneReply(boardNum);

		rService.deleteReply(boardNum);

		return "redirect:./read?boardNum=" + reply.getBoardNum();
	}

	// 추천 버튼 눌렀을때 추천하는 기능
	@PostMapping("/addRecommend")
	public String addRecommend(int boardNum, @AuthenticationPrincipal UserDetails user) {

		String memberId = user.getUsername();
		Recommend recommend = new Recommend();

		recommend.setMemberId(memberId);
		recommend.setBoardNum(boardNum);

		// 유저 아이디와 추천글 번호에 따른 추천 가져옴
		Recommend memberRecommend = bService.selectRecommend(recommend);

		if (memberRecommend == null) { // 추천 이력이 없을때 추천 추가
			bService.insertRecommend(recommend); // 추천 항목 DB 삽입
			bService.updateRecommendCount(recommend.getBoardNum()); // 게시물 추천수 늘리기
		}
		return "redirect:./read?boardNum=" + boardNum;
	}
}
