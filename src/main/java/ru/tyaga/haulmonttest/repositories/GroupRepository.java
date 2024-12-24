package ru.tyaga.haulmonttest.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import ru.tyaga.haulmonttest.entity.Group;

import java.util.List;

public interface GroupRepository extends JpaRepository<Group, Long> {

    List<Group> findByNumber(String number);

    List<Group> findByNumberAndFaculty(String number, String faculty);
}
