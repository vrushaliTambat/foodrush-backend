package com.vrush.repository;

import com.vrush.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category,Long> {

    //ManyToOne relationship with restaurant therefore List<Category> is returned
    public List<Category> findByRestaurantId(Long id);

}
