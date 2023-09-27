package com.example.demo.pinPoint.photo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, Long> {

    Photo findPhotoById(Long id);

}


