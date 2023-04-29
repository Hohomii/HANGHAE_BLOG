package com.sparta.board.entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import javax.persistence.*;

@Entity @Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BoardLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", nullable = false)
    //기본키 삭제시 외래키 같이 삭제.단방향에서도 적용 가능.but 주의 숙지!
    //user가 삭제되면 사용자-게시글 연관관계인 BoardLike가 지워지는 것
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BOARD_ID", nullable = false)
    private Board board;

    @Column(nullable = false)
    private boolean status;     //true는 좋아요, false는 좋아요 취소

    public BoardLike(Board board, User user) {
        this.board = board;
        this.user = user;
        this.status = true;
    }

    public void unLikeBoard(Board board) {
        this.status = false;
        board.setLikeCount(board.getLikeCount() - 1);
    }
}
