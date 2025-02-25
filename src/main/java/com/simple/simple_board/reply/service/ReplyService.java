package com.simple.simple_board.reply.service;

import com.simple.simple_board.post.db.PostRepository;
import com.simple.simple_board.reply.db.ReplyEntity;
import com.simple.simple_board.reply.db.ReplyRepository;
import com.simple.simple_board.reply.model.ReplyRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReplyService{

    private final ReplyRepository replyRepository;

    private final PostRepository postRepository;
    public ReplyEntity create(
            ReplyRequest replyRequest
    ){
        var optionalPostEntity = postRepository.findById(replyRequest.getPostId());

        if(optionalPostEntity.isEmpty()){
            throw new RuntimeException("게시물이 존재 하지 않습니다. + " + replyRequest.getPostId());
        }
        var entity = ReplyEntity.builder()
                .post(optionalPostEntity.get())
                .userName(replyRequest.getUserName())
                .status("REGISTERED")
                .title(replyRequest.getTitle())
                .password(replyRequest.getPassword())
                .content(replyRequest.getContent())
                .repliedAt(LocalDateTime.now())
                .build();

        return replyRepository.save(entity);
    }

    public List<ReplyEntity> findAllByPostId(Long postId){
        return replyRepository.findAllByPostIdAndStatusOrderByIdDesc(postId,"REGISTERED");
    }
}
