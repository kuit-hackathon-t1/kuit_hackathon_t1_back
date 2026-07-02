package com.example.kuit_hackathon_back.domain.mission.dto;

import com.example.kuit_hackathon_back.domain.mission.entity.MissionCategory;
import java.security.SecureRandom;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * 임시 미션 추천 로직.
 * 실제 AI 추천 서비스가 붙기 전까지, 하드코딩된 미션 풀에서 랜덤으로 하나를 골라 반환한다.
 * 추후 AI 연동 시 이 클래스(또는 이 클래스가 구현하는 인터페이스)만 교체하면 된다.
 */
@Component
public class MissionTemplateProvider {

    private static final List<MissionTemplate> TEMPLATES = List.of(
            new MissionTemplate(
                    "목적 없이 10분 걷기",
                    "10분 동안 목적지 없이 걷다가 멈춘 곳을 찍어보아요.",
                    MissionCategory.OBSERVATION, true,
                    List.of("어디에서 발견했나요?", "왜 이 장소에서 멈췄나요?", "이 순간이 주는 느낌은 어떤가요?")),
            new MissionTemplate(
                    "이 동네에서 가장 이상한 색 찾기",
                    "평소 눈에 띄지 않던 색을 찾아 사진으로 남겨보세요.",
                    MissionCategory.OBSERVATION, true,
                    List.of("어떤 색이었나요?", "왜 눈에 띄었나요?", "이 색에서 어떤 기분이 드나요?")),
            new MissionTemplate(
                    "가장 오래돼 보이는 글자를 사진으로 남기세요",
                    "동네를 걷다가 가장 오래된 간판이나 글자를 찾아보세요.",
                    MissionCategory.LOCAL, true,
                    List.of("어디서 발견했나요?", "얼마나 오래된 것 같나요?", "어떤 이야기가 담겨있을 것 같나요?")),
            new MissionTemplate(
                    "낯선 사람에게 말 걸어보기",
                    "여행지에서 처음 보는 사람에게 짧은 대화를 시도해보세요.",
                    MissionCategory.ACTION, false,
                    List.of("누구에게 말을 걸었나요?", "무슨 대화를 나눴나요?", "대화 후 기분이 어땠나요?")),
            new MissionTemplate(
                    "랜덤 방향으로 5번째 골목 들어가기",
                    "지도를 보지 않고 감으로 골목을 선택해서 걸어보세요.",
                    MissionCategory.RANDOM, true,
                    List.of("어떤 골목이었나요?", "예상과 달랐던 점이 있나요?", "그곳에서 무엇을 발견했나요?"))
    );

    private final SecureRandom random = new SecureRandom();

    public MissionTemplate getRandomTemplate() {
        return TEMPLATES.get(random.nextInt(TEMPLATES.size()));
    }

    public record MissionTemplate(String title, String description, MissionCategory category, boolean isLocal,
            List<String> guides) {
    }
}
