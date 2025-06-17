package com.example.surveysystem.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.surveysystem.models.Man;

public interface ManRepository extends JpaRepository<Man, Long> {
    // Здесь можно добавить дополнительные методы, если необходимо
}
