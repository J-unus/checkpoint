package ee.mustamae.checkpoint.mapper;

import java.util.List;

public interface DtoAndEntityMapper<DTO, ENTITY> {

    ENTITY fromDtoToEntity(DTO dto);

    DTO fromEntityToDto(ENTITY entity);

    List<ENTITY> fromDtoToEntity(List<DTO> dtoList);

    List<DTO> fromEntityToDto(List<ENTITY> entityList);
}
