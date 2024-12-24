package ru.tyaga.haulmonttest.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.combobox.ComboBox;
import ru.tyaga.haulmonttest.entity.Group;
import ru.tyaga.haulmonttest.services.GroupService;

import java.util.List;


public class GroupEditorDialog extends Dialog {

    private final GroupService groupService;
    private ComboBox<Group> groupComboBox = new ComboBox<>("Группа");
    private TextField newGroupNameField = new TextField("Номер группы");
    private TextField newGroupFacultyField = new TextField("Название факультета");

    private Button saveButton = new Button("Создать");
    private Button cancelButton = new Button("Отменить");

    public GroupEditorDialog(GroupService groupService, Runnable onSave) {
        this.groupService = groupService;

        setWidth("400px");

        List<Group> groups = groupService.findAllGroups();
        groups.add(0, new Group());
        groupComboBox.setItems(groups);
        groupComboBox.setItemLabelGenerator(Group::toString);

        VerticalLayout layout = new VerticalLayout(groupComboBox, newGroupNameField, newGroupFacultyField, saveButton, cancelButton);
        add(layout);

        groupComboBox.addValueChangeListener(event ->
        {
            if(event.getValue().getFaculty() != null) {
                saveButton.setText("Редактировать");
            }else saveButton.setText("Создать");

        });

        saveButton.addClickListener(event -> {
            Group selectedGroup = groupComboBox.getValue();


            if (selectedGroup != null && !newGroupNameField.getValue().trim().isEmpty()) {

                selectedGroup.setNumber(newGroupNameField.getValue().trim());
                selectedGroup.setFaculty(newGroupFacultyField.getValue().trim());
                groupService.updateGroup(selectedGroup);
                onSave.run();
                close();
            } else {
                Group newGroup = new Group();
                newGroup.setNumber(newGroupNameField.getValue().trim());
                newGroup.setFaculty(newGroupFacultyField.getValue().trim());
                groupService.addGroup(newGroup);
                onSave.run();
                close();
            }
        });

        cancelButton.addClickListener(event -> close());
    }
}
