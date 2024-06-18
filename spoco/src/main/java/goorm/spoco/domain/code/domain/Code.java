package goorm.spoco.domain.code.domain;

import goorm.spoco.domain.algorithm.domain.Algorithm;
import goorm.spoco.domain.member.domain.Member;
import goorm.spoco.domain.review.domain.Review;
import goorm.spoco.global.common.response.Status;
import goorm.spoco.infra.compiler.dto.ResultStatus;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.*;

@Getter
@Entity
public class Code {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CODE_ID")
    private Long codeId;

    private String detail;

    private String solveTime;

    private String language;

    @Enumerated(EnumType.STRING)
    private ResultStatus answerType;

    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "ALGORITHM_ID")
    private Algorithm algorithm;

    @OneToMany(mappedBy = "code", cascade = CascadeType.ALL)
    private List<Review> reviews = new ArrayList<>();

    public Code() {
    }

    //== 연관관계 메서드 ==//
    public void addMember(Member member) {
        this.member = member;
        member.getCodes().add(this);
    }

    public void addAlgorithm(Algorithm algorithm) {
        this.algorithm = algorithm;
        algorithm.getCodes().add(this);
    }

    //== 생성 메서드 ==//
    public static Code create(Member member, Algorithm algorithm, String detail, ResultStatus resultStatus) {
        Code code = new Code();
        code.addMember(member);
        code.addAlgorithm(algorithm);
        code.detail = detail;
        code.answerType = resultStatus;

        // 제출 수 증가
        code.getAlgorithm().increaseSubmit();
        if (code.answerType.equals(ResultStatus.PASS)) {
            code.getAlgorithm().increaseAnswer();
        }

        code.status = Status.ACTIVE;
        return code;
    }

    //== 비즈니스 로직 ==//
    public void delete() {
        this.status = Status.DELETE;
    }

    public void update(String detail, ResultStatus resultStatus) {
        this.detail = detail;
        this.answerType = resultStatus;

        this.getAlgorithm().increaseSubmit();
        if (this.answerType.equals(ResultStatus.PASS)) {
            this.getAlgorithm().increaseAnswer();
        }
    }


}