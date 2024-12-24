package ru.tyaga.haulmonttest.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import ru.tyaga.haulmonttest.entity.Group;
import ru.tyaga.haulmonttest.entity.Student;
import ru.tyaga.haulmonttest.services.StudentService;
import ru.tyaga.haulmonttest.services.GroupService;

import java.util.List;

@Route("")
public class StudentListView extends VerticalLayout {

    private final StudentService studentService;
    private final GroupService groupService;

    private Grid<Student> grid = new Grid<>(Student.class);
    private TextField lastNameFilter = new TextField("Фамилия");
    private TextField groupFilter = new TextField("Номер группы");
    private ComboBox<Group> groupComboBox = new ComboBox<>("Список всех групп");

    private Button addButton = new Button("Добавить студента");
    private Button updateButton = new Button("Обновить студента");
    private Button deleteButton = new Button("Удалить студента");
    private Button createGroupButton = new Button("Создать новую группу/Обновить существующую");
    private Button deleteGroupButton = new Button("Удалить группу");

    @Autowired
    public StudentListView(StudentService studentService, GroupService groupService) {
        this.studentService = studentService;
        this.groupService = groupService;


        HorizontalLayout fieldLayout = new HorizontalLayout(lastNameFilter, groupFilter, groupComboBox);

        HorizontalLayout buttonLayout = new HorizontalLayout(addButton, updateButton, deleteButton,
                createGroupButton, deleteGroupButton);


        add(fieldLayout, buttonLayout, grid);

        updateGrid();


        addButton.addClickListener(event -> openStudentEditor(null));
        updateButton.addClickListener(event -> updateSelectedStudent());
        deleteButton.addClickListener(event -> deleteSelectedStudent());
        createGroupButton.addClickListener(event -> openGroupEditor());
        deleteGroupButton.addClickListener(event -> deleteGroupDialog());

        lastNameFilter.addValueChangeListener(event -> updateGrid());
        groupFilter.addValueChangeListener(event -> updateGrid());
        grid.setSelectionMode(Grid.SelectionMode.SINGLE);
    }

    private void updateGrid() {
        groupComboBox.setItems(groupService.findAllGroups());
        groupComboBox.setItemLabelGenerator(Group::toString);

        String lastNameFilterValue = lastNameFilter.getValue().trim();
        String groupNumberFilterValue = groupFilter.getValue().trim();

        if (lastNameFilterValue.isEmpty() && groupNumberFilterValue.isEmpty()) {
            grid.setItems(studentService.findAllStudents());
        } else {
            List<Student> students;

            if (!groupNumberFilterValue.isEmpty()) {
                List<Group> groups = groupService.findByName(groupNumberFilterValue);

                if (groups == null || groups.isEmpty()) {
                    students = List.of();
                } else {
                    students = groups.stream()
                            .flatMap(group -> studentService.getStudentsByGroupId(group.getId()).stream())
                            .distinct()
                            .toList();
                }
            } else {
                students = studentService.filterByLastName(lastNameFilterValue);
            }

            if (!lastNameFilterValue.isEmpty()) {
                students = students.stream()
                        .filter(student -> student.getLastName().equalsIgnoreCase(lastNameFilterValue))
                        .toList();
            }

            grid.setItems(students);
        }
    }

    private void openStudentEditor(Student student) {
        StudentEditorDialog dialog = new StudentEditorDialog(student, this::refreshGrid, studentService, groupService);
        dialog.open();
    }

    private void updateSelectedStudent() {
        Student selectedStudent = grid.asSingleSelect().getValue();
        if (selectedStudent != null) {
            openStudentEditor(selectedStudent);
        } else {
            Notification.show("Пожалуйста, выберите студента для обновления.");
        }
    }

    private void deleteSelectedStudent() {
        Student selectedStudent = grid.asSingleSelect().getValue();
        if (selectedStudent != null) {
            Dialog dialog = new Dialog();
            dialog.add("Вы уверены, что хотите удалить студента " + selectedStudent.getName() + " " + selectedStudent.getLastName() + "?");

            // Добавляем кнопки
            Button deleteButton = new Button("Удалить", event -> {
                studentService.deleteStudent(selectedStudent.getId());
                updateGrid();
                dialog.close();
            });

            Button cancelButton = new Button("Отмена", event -> dialog.close());
            dialog.add(deleteButton, cancelButton);
            dialog.open();
        } else {
            Notification.show("Пожалуйста, выберите студента для удаления.");
        }
    }

    private void openGroupEditor() {
        GroupEditorDialog dialog = new GroupEditorDialog(groupService, this::refreshGrid);
        dialog.open();
        updateGrid();
    }

    private void refreshGrid() {
        updateGrid();
    }

    private void deleteGroupDialog() {
        GroupDeleteDialog dialog = new GroupDeleteDialog(groupService, this::refreshGrid);
        dialog.open();
    }
}
