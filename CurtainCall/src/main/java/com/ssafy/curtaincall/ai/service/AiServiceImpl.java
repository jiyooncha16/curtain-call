package com.ssafy.curtaincall.ai.service;

import java.util.List;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssafy.curtaincall.ai.dto.HashtagDTO;
import com.ssafy.curtaincall.ai.mapper.AiMapper;

@Service
public class AiServiceImpl implements AiService{

	//빈 생성
	private final AiMapper mapper;
	private final ChatClient client;
	public AiServiceImpl(AiMapper mapper, ChatClient client) {
		this.mapper = mapper;
		this.client = client;
	}

	//sql문 결과 받아오기
	@Override
	public List<HashtagDTO> hashtagCount(int id) {

		return mapper.selectCountGroupByHashtag(id);
	}

	@Override
	public String generateUserTaste(int id) {
		
		//리스트 불러오기
		List<HashtagDTO> list = hashtagCount(id);
		
		//프롬프트 만들기
		StringBuilder prompt = new StringBuilder();
		prompt.append("이 유저의 뮤지컬 관람 성향을 분석해 수식어를 만들어줘.\n");
		prompt.append("형식 : '____한 뮤덕' 이외에는 아무 말도 하지 말아줘.\n");
		prompt.append("아래에 태그를 적어줄 건데, 태그 이름만 나열하는 건 하지 마.\n");
		prompt.append("예를 들면, 태그가 '로맨스', '드라마' 일 때 '로맨티시즘 뮤덕', '로맨틱한 드라마 뮤덕'와 같이 변형하고 창의적이게 응답해.,\n");
		prompt.append("높은 순위 태그가 여러개이면 맨 위 한두개만 참고해.\n");
		prompt.append("욕설이나 비방하는 단어는 적지 마.\n");
		prompt.append("아래는 유저가 관람한 뮤지컬의 태그 목록과 그 빈도수야.\n");
		for (HashtagDTO tag : list) {
			prompt.append("태그 : " + tag.getHashtag() + ", 빈도수 : " + tag.getCount() + "\n");
		}

		System.out.println("여기서부터 에러가 나는 것 같아요");
		return client
				.prompt() // 프롬프트 생성기 시작
				.user(prompt.toString())
				.call() // ai에게 요청 후 응답 받기
				.content(); // 받은 응답 내용
	}

}
