package com.example.kanbanbackend.Service;

import com.example.kanbanbackend.Auth.JwtTokenUtil;
import com.example.kanbanbackend.Config.Permission;
import com.example.kanbanbackend.Config.VisibilityConfig;
import com.example.kanbanbackend.DTO.BoardDTO;
import com.example.kanbanbackend.DTO.VisibilityDTO;
import com.example.kanbanbackend.Entitites.Primary.*;
import com.example.kanbanbackend.Exception.BadRequestException;
import com.example.kanbanbackend.Exception.ForBiddenException;
import com.example.kanbanbackend.Exception.ItemNotFoundException;
import com.example.kanbanbackend.Repository.Primary.*;
import io.jsonwebtoken.Claims;
import io.viascom.nanoid.NanoId;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class BoardService {
    @Autowired
    ModelMapper mapper;
    @Autowired
    ListMapper listMapper;
    @Autowired
    ConfigRepository configRepository;
    @Autowired
    PrimaryUserRepository primaryUserRepository;
    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private BoardUserRepository boardUserRepository;
    @Autowired
    private DefaultStatusRepository defaultStatusRepository;
    @Autowired
    private StatusRepository statusRepository;

    @Autowired
    private JwtTokenUtil tokenUtil;
    @Autowired
    private Permission permission;
    @Autowired
    private VisibilityConfig visibilityConfig;

    @Transactional
    public BoardDTO createBoardUser(BoardDTO boardDTO, HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7).trim();;

        Claims claims = tokenUtil.getAllClaimsFromToken(token);

        Config config = configRepository.saveAndFlush(new Config());

        String oid = claims.get("oid",String.class);
        String email = claims.get("email",String.class);
        String role = "OWNER";
        User user = primaryUserRepository.saveAndFlush(new User(oid,claims.getSubject(),email));


        Board newBoard = boardRepository.saveAndFlush(new Board(NanoId.generate(10),boardDTO.getBoardName(),config, Visibility.PRIVATE));

        BoardUser boardUser = boardUserRepository.saveAndFlush(new BoardUser(user,newBoard,role));

        List<DefaultStatus> defaultStatusList = defaultStatusRepository.findByIdBetween(1,4);
        for (DefaultStatus defaultStatus : defaultStatusList) {
            Status customStatus = new Status();
            customStatus.setStatusName(defaultStatus.getStatusName());
            customStatus.setStatusDescription(defaultStatus.getStatusDescription());
            customStatus.setStatusColor(defaultStatus.getStatusColor());
            customStatus.setCreatedOn(Timestamp.valueOf(defaultStatus.getCreatedOn().toLocalDateTime()));
            customStatus.setUpdatedOn(Timestamp.valueOf(defaultStatus.getUpdatedOn().toLocalDateTime()));
            customStatus.setBoard(newBoard);// Associate CustomStatus with the new Board
            statusRepository.save(customStatus);
        }


        return mapper.map(boardUser, BoardDTO.class);
    }

    public List<BoardDTO> getBoardUserByBoardId(String boardId,HttpServletRequest request){
        List<BoardUser> boardUserList = boardUserRepository.findBoardUserByBoard_Id(boardId);
        if(boardUserList.isEmpty()) {
            throw new ItemNotFoundException("Board id '" + boardId + "' not found");
        }
//        Claims claims = tokenUtil.decodedToken(request);
            return listMapper.mapList(boardUserList, BoardDTO.class);


    }

    public VisibilityDTO setVisibility(String boardId, VisibilityDTO visibilityDTO, HttpServletRequest request){
        Board board = boardRepository.findBoardById(boardId);
        if(board == null) {
            throw new ItemNotFoundException("Board id '" + boardId + "' not found");
        }
        if(board.getVisibility().toString().equalsIgnoreCase(visibilityDTO.getVisibility())) {
            throw new BadRequestException("Board id '" + boardId + "' is still " + " " +board.getVisibility().toString());
        }


            try {
                // แปลง String เป็น Enum ถ้าค่าไม่ถูกต้องจะเกิด IllegalArgumentException
                Visibility visibility = Visibility.valueOf(visibilityDTO.getVisibility().toUpperCase());
                board.setVisibility(visibility);
                boardRepository.saveAndFlush(board);
                VisibilityDTO visibilityDTO2 = new VisibilityDTO();
                visibilityDTO2.setVisibility(visibility.toString().toUpperCase());
                return visibilityDTO2;
            } catch (IllegalArgumentException e) {
                // Handle ค่า visibility ที่ไม่ถูกต้อง และโยน BadRequestException (400)
                throw new BadRequestException("Invalid visibility value: " + visibilityDTO.getVisibility());
            }

    }
}
