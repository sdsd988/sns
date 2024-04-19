package com.fast.sns.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum AlarmType {

    /**
     * 알람 텍스트를 어디서 관리하는 것이 좋을까?
     *  알람 텍스트는 변화될 가능성이 많다.
     *  1.  DB
     *  2. 백엔드
     *  3. 프런트
     *
     *  알람 텍스트가 생각보다 간단하지 않다.
     *  알람 텍스트에 "A씨가 당신의 게시글에 좋아요를 눌렀습니다." 나갈 경우
     *  A씨에 대한 정보, 게시글에 대한 정보가 텍스트에 포함되어야 한다.
     *  또한, A를 눌렀을 때 A씨의 피드로 이동해야 한다.
     *
     */
    NEW_COMMENT_ON_POST("new comment"),
    NEW_LIKE_ON_POST("new like"),
    ;

    private final String alarmText;

}
