package com.simple.simple_board.board.controller;

import com.simple.simple_board.board.model.BoardDto;
import com.simple.simple_board.board.model.BoardRequest;
import com.simple.simple_board.board.service.BoardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/board")
@RequiredArgsConstructor
public class BoardApiController {

    private final BoardService boardService;

    @PostMapping("")
    public BoardDto create(
            @Valid
            @RequestBody BoardRequest boardRequest
    ){
        return boardService.create((boardRequest));
    }

    @GetMapping("/id/{id}")
    public BoardDto view(
            @PathVariable Long id
    ){
        var entity = boardService.view(id);
        log.info(entity.toString());
        return entity;
    }
}
