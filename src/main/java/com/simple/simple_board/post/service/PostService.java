package com.simple.simple_board.post.service;

import com.simple.simple_board.board.db.BoardRepository;
import com.simple.simple_board.common.Api;
import com.simple.simple_board.common.Pagination;
import com.simple.simple_board.post.db.PostEntity;
import com.simple.simple_board.post.db.PostRepository;
import com.simple.simple_board.post.model.PostRequest;
import com.simple.simple_board.post.model.PostViewRequest;
import com.simple.simple_board.reply.service.ReplyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final ReplyService replyService;
    private final BoardRepository  boardRepository;

    public PostEntity create(
            PostRequest postRequest
    ){
        var boardEntity = boardRepository.findById(postRequest.getBoardId()).get();
       var entity = PostEntity.builder()
                .board(boardEntity)
                .userName(postRequest.getUserName())
                .password(postRequest.getPassword())
                .email(postRequest.getEmail())
                .status("REGISTERED")
                .title(postRequest.getTitle())
                .content(postRequest.getContent())
                .postedAt(LocalDateTime.now())
                .build();

       return postRepository.save(entity);
    }

    public PostEntity view(PostViewRequest postViewRequest) {
        return postRepository.findFirstByIdAndStatusOrderByIdDesc(postViewRequest.getPostId(),"REGISTERED")
                .map(it ->{
                    if(!it.getPassword().equals(postViewRequest.getPassword())) {
                        var format = "패스워드가 맞지 않습니다. %s vs %s";
                        throw new RuntimeException(String.format(format, it.getPassword(), postViewRequest.getPassword()));
                    }
                  //답변글도 같이
//                    var replyList = replyService.findAllByPostId(it.getId());
//                    it.setReplies(replyList);

                    return it;
                }).orElseThrow(
                        () ->{
                            return new RuntimeException("해당 글이 존재 하지 않습니다. : " + postViewRequest.getPostId());
                        }
                );
    }

    public Api<List<PostEntity>> all(Pageable pageable) {

        var list = postRepository.findAll(pageable);

        var pagination = Pagination.builder()
                .page(list.getNumber())
                .size(list.getSize())
                .currentElements(list.getNumberOfElements())
                .totalElements(list.getTotalElements())
                .totalPage(list.getTotalPages())
                .build();

        return Api.<List<PostEntity>>builder()
                .body(list.toList())
                .pagination(pagination)
                .build();
    }

    public void delete(PostViewRequest postViewRequest) {
        postRepository.findById(postViewRequest.getPostId())
                .map(it ->{
                    if(!it.getPassword().equals(postViewRequest.getPassword())) {
                        var format = "패스워드가 맞지 않습니다. %s vs %s";
                        throw new RuntimeException(String.format(format, it.getPassword(), postViewRequest.getPassword()));
                    }
                    it.setStatus("UNREGISTERED");
                    postRepository.save(it);
                    return it;
                }).orElseThrow(
                        () ->{
                            return new RuntimeException("해당 글이 존재 하지 않습니다. : " + postViewRequest.getPostId());
                        }
                );
    }
}
