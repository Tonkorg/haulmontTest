package ru.tyaga.haulmonttest.services;

import org.springframework.stereotype.Service;
import ru.tyaga.haulmonttest.entity.Group;
import ru.tyaga.haulmonttest.repositories.GroupRepository;

import java.util.List;

@Service
public class GroupService {
    private final GroupRepository groupRepository;

    public GroupService(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    public List<Group> findAllGroups() {
        return groupRepository.findAll();
    }

    public void addGroup(Group group) {
        groupRepository.save(group);
    }

    public void deleteGroup(Long id) {
        groupRepository.deleteById(id);
    }


    public List<Group> findByName(String name) {
        try {
            String [] split = name.split(" ");
            if(split.length < 2) {
                return groupRepository.findByNumber(name);
            }
            else
            {
                return groupRepository.findByNumberAndFaculty(split[0], split[1]);
            }

        } catch (Exception e) {
            System.out.println("Произошла ошибка при поиске группы по имени: " + e.getMessage());
            return null;
        }
    }

    public void updateGroup(Group group) {
        groupRepository.save(group);
    }
}
