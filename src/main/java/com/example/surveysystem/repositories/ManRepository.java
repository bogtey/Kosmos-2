package com.example.surveysystem.repositories;

<<<<<<< HEAD
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.surveysystem.models.Man;

public interface ManRepository extends JpaRepository<Man, Long> {
    // Здесь можно добавить дополнительные методы, если необходимо
}
=======
import com.example.surveysystem.models.Man;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ManRepository extends JpaRepository<Man, Long> {
    // Здесь можно добавить дополнительные методы, если это необходимо
}
>>>>>>> b3bc88fb42afef154ec27e6e71abd4cda449e8a9
