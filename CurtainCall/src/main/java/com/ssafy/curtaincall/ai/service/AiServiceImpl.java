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

		prompt.append("아래 태그 목록을 기반으로 이 사용자의 뮤지컬 관람 성향을 해석해 하나의 수식어를 만들어줘.\n");
		prompt.append("반드시 지켜야 할 규칙 7가지를 말해줄게.\\n");
		prompt.append("1. 결과는 반드시 '____한 뮤덕' 형식의 한 문장만 출력해.\n");
		prompt.append("2. 태그 단어(로맨스, 드라마, 코미디 등)를 그대로 사용하거나 단순 결합하지 마.\n");
		prompt.append("3. 태그는 '의미 -> 성향 -> 추상적 표현'으로 변환해서 표현해.\n");
		prompt.append("4. 감정, 취향, 관람 태도를 드러내는 형용사를 사용해.\n");
		prompt.append("5. 너무 일반적인 표현(로맨스 드라마 뮤덕 등)은 사용하지 마.\n");
		prompt.append("6. 높은 빈도의 태그 1~2개만 반영해.\n");
		prompt.append("7. 욕설, 비하, 부정적인 표현은 사용하지 마.\n");
		
		prompt.append("예시:\n");
		prompt.append("- 태그: 로맨스, 드라마 -> 감정선에 깊이 몰입하는 뮤덕\n");
		prompt.append("- 태그: 판타지, 대서사 -> 서사에 빠져드는 상상력 풍부한 뮤덕\n");
		prompt.append("- 태그: 코미디, 가족 -> 웃음과 온기를 즐기는 뮤덕\n");

		prompt.append("아래는 유저가 관람한 뮤지컬의 태그 목록과 그 빈도수야.\n");
		for (HashtagDTO tag : list) {
			prompt.append("태그 : " + tag.getHashtag() + ", 빈도수 : " + tag.getCount() + "\n");
		}

		return client
				.prompt() // 프롬프트 생성기 시작
				.user(prompt.toString())
				.call() // ai에게 요청 후 응답 받기
				.content(); // 받은 응답 내용
	}

}
