package de.moritzjunge.financer.repositories;

import de.moritzjunge.financer.model.Household;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HouseholdRepository extends JpaRepository<Household, Long> {

    List<Household> findAllByOwnerId(Long id);

}
