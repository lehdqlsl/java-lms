package nextstep.qna.domain;

import nextstep.qna.CannotDeleteException;
import nextstep.users.domain.NsUserTest;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class QuestionTest {
    public static final Question Q1 = new Question(NsUserTest.JAVAJIGI, "title1", "contents1");
    public static final Question Q2 = new Question(NsUserTest.SANJIGI, "title2", "contents2");

    @Test
    void delete_성공() throws CannotDeleteException {
        Question q1 = new Question(NsUserTest.JAVAJIGI, "title1", "contents1");

        q1.delete(NsUserTest.JAVAJIGI);

        assertThat(q1.isDeleted()).isTrue();
    }

    @Test
    void delete_다른_사람이_쓴_글() {
        Question q1 = new Question(NsUserTest.JAVAJIGI, "title1", "contents1");
        assertThatThrownBy(() -> q1.delete(NsUserTest.SANJIGI))
                .isInstanceOf(CannotDeleteException.class)
                .hasMessage("질문을 삭제할 권한이 없습니다.");
    }

    @Test
    void delete_성공_질문자_답변자_같음() throws CannotDeleteException {
        Question q1 = new Question(NsUserTest.JAVAJIGI, "title1", "contents1");
        Answer answer = new Answer(NsUserTest.JAVAJIGI, q1, "답변");
        q1.addAnswer(answer);

        q1.delete(NsUserTest.JAVAJIGI);

        assertThat(q1.isDeleted()).isTrue();
        assertThat(answer.isDeleted()).isTrue();
    }

    @Test
    void delete_답변_중_다른_사람이_쓴_글() {
        Question q1 = new Question(NsUserTest.JAVAJIGI, "title1", "contents1");
        q1.addAnswer(new Answer(
                NsUserTest.SANJIGI,
                q1,
                "답변"
        ));
        assertThatThrownBy(() -> q1.delete(NsUserTest.JAVAJIGI))
                .isInstanceOf(CannotDeleteException.class)
                .hasMessage("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
    }

    @Test
    void delete_history() throws CannotDeleteException {
        Question q1 = new Question(NsUserTest.JAVAJIGI, "title1", "contents1");

        List<DeleteHistory> deleteHistories = q1.delete(NsUserTest.JAVAJIGI);

        assertThat(deleteHistories).hasSize(1);
    }


}
