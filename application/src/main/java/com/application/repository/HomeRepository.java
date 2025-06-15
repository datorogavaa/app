package com.application.repository;

import com.application.model.Home;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import org.springframework.stereotype.Repository;
import java.util.List;
@Repository
public interface HomeRepository extends JpaRepository<Home, Long> {
    List<Home> findByRentedUntilBeforeAndUserIsNotNull(LocalDateTime dateTime);
}
