package ee.mustamae.checkpoint.service;

import ee.mustamae.checkpoint.dto.ChatRoomCreateDto;
import ee.mustamae.checkpoint.dto.ChatRoomDto;
import ee.mustamae.checkpoint.mapper.ChatRoomMapper;
import ee.mustamae.checkpoint.model.ChatRoom;
import ee.mustamae.checkpoint.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomMapper chatRoomMapper;

    public ChatRoomDto create(ChatRoomCreateDto chatRoomCreateDto) {
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setLink(UUID.randomUUID().toString());
        chatRoom.setPassword(chatRoomCreateDto.getPassword());
        return chatRoomMapper.fromEntityToDto(chatRoomRepository.save(chatRoom));
    }
}
