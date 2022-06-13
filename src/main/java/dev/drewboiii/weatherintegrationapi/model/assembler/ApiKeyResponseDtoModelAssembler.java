package dev.drewboiii.weatherintegrationapi.model.assembler;

import dev.drewboiii.weatherintegrationapi.controller.ApiKeyController;
import dev.drewboiii.weatherintegrationapi.dto.response.ApiKeyResponseDto;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ApiKeyResponseDtoModelAssembler implements RepresentationModelAssembler<ApiKeyResponseDto, EntityModel<ApiKeyResponseDto>> {

    @Override
    public EntityModel<ApiKeyResponseDto> toModel(ApiKeyResponseDto entity) {
        return EntityModel.of(
                entity,
                linkTo(methodOn(ApiKeyController.class).details(entity.getApiKey())).withSelfRel()
        );
    }

}
