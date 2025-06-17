package com.example.surveysystem.repositories;

import com.example.surveysystem.models.Man;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ManRepository extends JpaRepository<Man, Long> {
    // Здесь можно добавить дополнительные методы, если это необходимо
}
