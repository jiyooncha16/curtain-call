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
		prompt.append("반드시 지켜야 할 규칙 10가지를 말해줄게.\\n");
		prompt.append("1. 결과는 반드시 '____한 뮤덕' 형식의 한 문장만 출력해.\n");
		prompt.append("2. 태그 단어(로맨스, 드라마, 코미디 등)를 그대로 사용하거나 단순 결합하지 마.\n");
		prompt.append("3. 태그는 '의미 -> 성향 -> 추상적 표현'으로 변환해서 표현해.\n");
		prompt.append("4. 감정, 취향, 관람 태도를 드러내는 형용사를 사용해.\n");
		prompt.append("5. 너무 일반적인 표현(로맨스 드라마 뮤덕 등)은 사용하지 마.\n");
		prompt.append("6. 높은 빈도의 태그 1~2개만 반영해.\n");
		prompt.append("7. 욕설, 비하, 부정적인 표현은 사용하지 마.\n");
		prompt.append("8. 무조건 15자 이내로만 출력해. 15자 이상일 경우 다시 생성해.\n");
		prompt.append("9. 만약 태그 목록이 null이거나 tag 리스트에 정보가 없다면 다음의 문장을 출력해. '아직 취향을 찾는 중인 뮤덕🔍' \n");
		prompt.append("10. 뮤덕 뒤에 수식어와 어울리는 이모지를 달아. \n");
		
		prompt.append("예시:\n");
		prompt.append("- 태그: 로맨스, 드라마 -> 감정선에 깊이 몰입하는 뮤덕💖\n");
		prompt.append("- 태그: 판타지, 대서사 -> 서사에 빠져드는 상상력 풍부한 뮤덕🌠\n");
		prompt.append("- 태그: 코미디, 가족 -> 웃음과 온기를 즐기는 뮤덕😄\n");
		prompt.append("- 태그 목록이 비어있음 -> 아직 취향을 찾는 중인 뮤덕🔍\n");

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
	
	@Override
	public String generateReview(String title, String keyword) {
		
		//프롬프트 만들기
		StringBuilder prompt = new StringBuilder();

		prompt.append("아래 키워드를 기반으로 뮤지컬 리뷰 초안을 생성해줘.\n");
		prompt.append("키워드 : ");
		prompt.append(keyword);
		prompt.append("\n");
		prompt.append("뮤지컬 제목 : ");
		prompt.append(title);
		prompt.append("\n");
		prompt.append("\n");

		prompt.append("답변을 생성할 때 반드시 지켜야 할 규칙 9가지를 말해줄게.\n");
		prompt.append("1. 리뷰는 실제 관객이 작성한 것처럼 자연스러운 1인칭 시점으로 작성해줘.\n");
		prompt.append("2. 전체 분량은 공백 포함 90자의 짧은 리뷰 초안으로 작성해줘.\n");
		prompt.append("3. 키워드는 리뷰 내용에 자연스럽게 녹여서 사용하고, 키워드를 나열하지는 마.\n");
		prompt.append("4. 줄거리 요약보다는 관람 후 느낀 감정과 인상을 중심으로 작성해줘.\n");
		prompt.append("5. 과도한 극찬이나 광고 문구처럼 보이는 표현은 피하고, 솔직한 톤을 유지해줘.\n");
		prompt.append("6. 배우 실명이나 스포일러가 될 수 있는 내용은 언급하지 마.\n");
		prompt.append("7. 이 리뷰는 초안이므로, 너무 완성된 느낌보다는 사용자가 다듬기 쉬운 문장으로 작성해줘.\n");
		prompt.append("8. 실제 뮤지컬 제목을 검색해보고, 그 뮤지컬의 장르를 반영해서 작성해.\n");
		prompt.append("9. 생성한 리뷰 이외에는 다른 말 전혀 하지 마.\n");
		prompt.append("10. 내가 주는 예시에서 '- 제목'부터 '답변 : '까지의 내용은 주어진 정보이니, 절대로 답변에 넣지 마. \n");
		prompt.append("11. 한국어만 사용해.\n");
		prompt.append("12. 제목을 언급하지 마.\n");
		
		prompt.append("예시:\n");
		prompt.append("- 제목 : 레미제라블 / 키워드 : 여운, 감동, 넘버일 경우의 답변 : 무겁지만 몰입감 있는, 감동이 잔잔하게 남는 작품이었다.\r\n"
									+ "넘버와 분위기가 잘 어우러져서 여운 있게 즐길 수 있었다.\n");
		prompt.append("- 제목 : 위키드 / 키워드 : 몰입, 무대, 감정선일 경우의 답변 :  처음부터 끝까지 몰입해서 보게 된 공연이었다.\r\n"
	            + "화려한 무대와 감정선이 잘 어우러져 기억에 오래 남는다.✨\n");
		prompt.append("- 제목 : 오페라의 유령 / 키워드 : 분위기, 음악, 여운일 경우의 답변 :  공연 내내 분위기에 자연스럽게 빠져들었다.\r\n"
	            + "음악이 인상 깊어서 관람 후에도 여운이 길게 남았다.🎭\n");
		prompt.append("- 제목 : 시카고 / 키워드 : 에너지, 넘버, 재미일 경우의 답변 :  경쾌한 에너지가 가득해서 보는 내내 즐거웠다.\r\n"
	            + "넘버가 인상적이라 부담 없이 재미있게 감상할 수 있었다.🔥\n");


		return client
				.prompt() // 프롬프트 생성기 시작
				.user(prompt.toString())
				.call() // ai에게 요청 후 응답 받기
				.content(); // 받은 응답 내용
	}

}
