package com.spinner.www.board.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.spinner.www.board.entity.Board;
import com.spinner.www.board.entity.QBoard;
import com.spinner.www.board.io.BoardListResponse;
import com.spinner.www.board.io.BoardResponse;
import com.spinner.www.member.entity.QMember;
import com.spinner.www.reply.entity.QReply;
import com.spinner.www.reply.io.ReplyResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.types.Projections.list;
import static com.spinner.www.board.entity.QBoard.board;
import static com.spinner.www.reply.entity.QReply.reply;
import static com.spinner.www.member.entity.QMember.member;

@Repository
@RequiredArgsConstructor
public class BoardQueryRepo {
    private final JPAQueryFactory jpaQueryFactory;
    private final int NOT_REMOVED = 0;
    private final int NOT_REPORTED = 0;

    QReply childReply = new QReply("childReply");
    QMember memberReply = new QMember("memberReply");
    QMember memberChildReply = new QMember("memberChildReply");

    public List<BoardListResponse> getSliceOfBoard(
            @Nullable
            Long idx,
            int size,
            @Nullable
            String keyword
    ) {
        return jpaQueryFactory.select(
                        Projections.constructor(
                                BoardListResponse.class,
                                board.boardIdx,
                                board.boardTitle,
                                board.boardContent,
                                board.member.memberNickname,
                                JPAExpressions
                                        .select(reply.count())
                                        .from(reply)
                                        .where(reply.boardIdx.eq(board.boardIdx)
                                                .and(reply.replyIsRemoved.eq(NOT_REMOVED))),
                                board.createdAt,
                                board.createdDate,
                                board.modifiedAt,
                                board.modifiedDate
                        )
                )
                .from(board)
                .where(getNotRemoved(), getNotReported(), ltBoardIdx(idx), search(keyword))
                .orderBy(board.boardIdx.desc())
                .limit(size)
                .fetch();
    }

// 댓글과 대댓글을 1개씩만 가져오는 문제가 있어 사용하지 않음
    public BoardResponse getBoard(
            Long idx
    ) {
        Map<Long, BoardResponse> result = jpaQueryFactory
                .from(board)
//                .leftJoin(reply).on(
//                        reply.boardIdx.eq(board.boardIdx)
//                )
//                .leftJoin(reply.childReplies, childReply)
//                .on(
//                        childReply.boardIdx.eq(board.boardIdx)
//                )
//                .leftJoin(member).on(board.member.memberIdx.eq(member.memberIdx))
//                .leftJoin(memberReply).on(reply.member.memberIdx.eq(memberReply.memberIdx))
//                .leftJoin(memberChildReply).on(childReply.member.memberIdx.eq(memberChildReply.memberIdx))
                .where(getBoardIdx(idx), getNotRemoved(), getNotReported())
                .transform(groupBy(board.boardIdx).as(
                        Projections.constructor(
                                BoardResponse.class,
                                board.boardIdx,
                                board.member.memberNickname,
                                board.boardTitle,
                                board.boardContent,
                                list(
                                        Projections.constructor(ReplyResponse.class,
//                                        board.boardIdx,
                                                reply.replyIdx,
                                                reply.replyContent
//                                        reply.member.memberNickname,
//                                        list(
//                                                Projections.constructor(ReplyResponse.class,
////                                                        board.boardIdx,
//                                                        childReply.replyIdx,
//                                                        childReply.replyContent,
//                                                        childReply.member.memberNickname
//                                                )
//                                        )
                                        )
                                ),
                                board.createdAt,
                                board.createdDate,
                                board.modifiedAt,
                                board.modifiedDate
                        )
                ));
        return result.get(idx);
    }

    private BooleanBuilder getNotRemoved() {
        return new BooleanBuilder(board.boardIsRemoved.eq(NOT_REMOVED));
    }

    private BooleanBuilder getNotRemovedReply() {
        return new BooleanBuilder(board.replies.any().replyIsRemoved.eq(NOT_REMOVED));
    }

    private BooleanBuilder getNotReported() {
        return new BooleanBuilder(board.boardIsReported.eq(NOT_REPORTED));
    }

    private BooleanBuilder search(String keyword) {
        return containsTitle(keyword).or(containsNickname(keyword));
    }

    private BooleanBuilder containsTitle(@Nullable String title) {
        return StringUtils.hasText(title) ? new BooleanBuilder(board.boardTitle.contains(title)) : new BooleanBuilder();
    }

    private BooleanBuilder containsNickname(@Nullable String nickname) {
        return StringUtils.hasText(nickname) ? new BooleanBuilder(board.member.memberNickname.contains(nickname)) : new BooleanBuilder();
    }

    private BooleanBuilder ltBoardIdx(@Nullable Long idx) {
        return idx == null ? new BooleanBuilder() : new BooleanBuilder(board.boardIdx.lt(idx));
    }

    private BooleanBuilder getBoardIdx(@Nullable Long idx) {
        return new BooleanBuilder(board.boardIdx.eq(idx));
    }

}
