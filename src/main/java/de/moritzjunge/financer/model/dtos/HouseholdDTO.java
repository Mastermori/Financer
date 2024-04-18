package de.moritzjunge.financer.model.dtos;

import de.moritzjunge.financer.model.FUser;
import de.moritzjunge.financer.model.Household;
import groovyjarjarantlr4.v4.runtime.misc.NotNull;
import jakarta.validation.constraints.NotEmpty;

import java.util.ArrayList;
import java.util.List;

public class HouseholdDTO {

    @NotNull
    private Long id;
    @NotNull
    @NotEmpty
    private String name;
    @NotNull
    private Long ownerId;
    @NotNull
    private List<Long> participantIds;

    public static HouseholdDTO fromEntities(@NotNull Household household) {
        HouseholdDTO householdDTO = new HouseholdDTO();
        householdDTO.setId(household.getId());
        householdDTO.setName(household.getName());
        householdDTO.setOwnerId(household.getOwner().getId());
        householdDTO.setParticipantIds(new ArrayList<>(household.getParticipants().stream().map(FUser::getId).toList()));
        return householdDTO;
    }

    public Long getId() {
        return id;
    }

    public HouseholdDTO setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public HouseholdDTO setName(String name) {
        this.name = name;
        return this;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public HouseholdDTO setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
        return this;
    }

    public List<Long> getParticipantIds() {
        return participantIds;
    }

    public HouseholdDTO setParticipantIds(List<Long> participantIds) {
        this.participantIds = participantIds;
        return this;
    }
}
