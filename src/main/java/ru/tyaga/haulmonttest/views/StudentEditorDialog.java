package ru.tyaga.haulmonttest.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import ru.tyaga.haulmonttest.entity.Group;
import ru.tyaga.haulmonttest.entity.Student;
import ru.tyaga.haulmonttest.services.GroupService;
import ru.tyaga.haulmonttest.services.StudentService;

import java.util.List;

public class StudentEditorDialog extends Dialog {


    private TextField nameField = new TextField("Имя");

    private TextField lastNameField = new TextField("Фамилия");
    private TextField patronymicField = new TextField("Отчество");
    private DatePicker birthDateField = new DatePicker("Дата рождения");
    private ComboBox<Group> groupComboBox = new ComboBox<>("Группа");
    private Button saveButton = new Button("Сохранить");
    private Button cancelButton = new Button("Отменить");

    private Long studentId;

    public StudentEditorDialog(Student student, Runnable onSave, StudentService studentService, GroupService groupService) {

        nameField.setRequired(true);
        lastNameField.setRequired(true);
        birthDateField.setRequired(true);
        setWidth("400px");

        List<Group> groups = groupService.findAllGroups();
        if (groups.isEmpty()) {
            groupComboBox.setPlaceholder("Нет существующих групп");
            groupComboBox.setEnabled(false);
        } else {
            groupComboBox.setItems(groups);
            groupComboBox.setItemLabelGenerator(Group::toString);
        }

        VerticalLayout layout = new VerticalLayout(nameField, lastNameField, patronymicField, birthDateField, groupComboBox, saveButton, cancelButton);
        add(layout);

        if (student != null) {
            this.studentId = student.getId();
            nameField.setValue(student.getName());
            lastNameField.setValue(student.getLastName());
            patronymicField.setValue(student.getPatronymic());
            birthDateField.setValue(student.getBirthDate());
            groupComboBox.setValue(student.getGroup());
        }

        saveButton.addClickListener(event -> {
            Group selectedGroup = groupComboBox.getValue();
            if (nameField.getValue().trim().isEmpty() || lastNameField.getValue().trim().isEmpty() || birthDateField.getValue().toString().isEmpty() || nameField.getValue() == null || lastNameField.getValue() == null || birthDateField.getValue() == null) {
                Notification.show("Не все поля заполнены");
            } else {
                if (studentId != null) {
                    Student updatedStudent = new Student();
                    updatedStudent.setId(studentId);
                    updatedStudent.setName(nameField.getValue());
                    updatedStudent.setLastName(lastNameField.getValue());
                    updatedStudent.setPatronymic(patronymicField.getValue());
                    updatedStudent.setBirthDate(birthDateField.getValue());
                    updatedStudent.setGroup(selectedGroup);
                    studentService.updateStudent(studentId, updatedStudent);
                } else {
                    Student newStudent = new Student();
                    newStudent.setName(nameField.getValue());
                    newStudent.setLastName(lastNameField.getValue());
                    newStudent.setPatronymic(patronymicField.getValue());
                    newStudent.setBirthDate(birthDateField.getValue());
                    if (groupComboBox != null) {
                        newStudent.setGroup(selectedGroup);
                    } else {
                        newStudent.setGroup(null);
                    }
                    studentService.addStudent(newStudent);
                }

                onSave.run();
                close();
            }
        });

        cancelButton.addClickListener(event -> close());
    }
}
