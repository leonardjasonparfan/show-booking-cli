package com.ljparfan.showbooking.repository;

import com.ljparfan.showbooking.entity.Show;
import org.springframework.data.repository.ListCrudRepository;

import java.util.Optional;

public interface ShowRepository extends ListCrudRepository<Show, Long> {
    Optional<Show> findByShowNumber(String showNumber);

    boolean existsByShowNumber(String showNumber);
}

