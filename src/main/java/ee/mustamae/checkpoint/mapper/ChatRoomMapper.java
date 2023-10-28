package ee.mustamae.checkpoint.mapper;

import ee.mustamae.checkpoint.dto.ChatRoomDto;
import ee.mustamae.checkpoint.model.ChatRoom;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", disableSubMappingMethodsGeneration = true, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class ChatRoomMapper implements DtoAndEntityMapper<ChatRoomDto, ChatRoom> {
}
