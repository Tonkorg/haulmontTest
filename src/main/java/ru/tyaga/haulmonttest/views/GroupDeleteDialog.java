package ru.tyaga.haulmonttest.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import ru.tyaga.haulmonttest.entity.Group;
import ru.tyaga.haulmonttest.services.GroupService;

import java.util.List;


public class GroupDeleteDialog extends Dialog {


    private ComboBox<Group> groupComboBox = new ComboBox<>("Группа");

    private final Button deleteButton = new Button("Удалить");
    private final Button cancelButton = new Button("Отменить");


    public GroupDeleteDialog(GroupService groupService, Runnable onSave) {

        setWidth("400px");

        List<Group> groups = groupService.findAllGroups();
        groupComboBox.setItems(groups);
        groupComboBox.setItemLabelGenerator(Group::toString);

        VerticalLayout layout = new VerticalLayout(groupComboBox, deleteButton, cancelButton);
        add(layout);

        deleteButton.addClickListener(e -> {
            Group g = groupComboBox.getValue();
            if (g != null) {
                Dialog dialog = new Dialog();
                dialog.add("Вы уверены что хотите удалить группу" + g.getNumber() + " " + g.getFaculty() + "?");


                Button deleteButton = new Button("Удалить", event -> {
                    try {
                        groupService.deleteGroup(g.getId());
                        onSave.run();
                        dialog.close();
                        Notification.show("Группа успешно удалена");
                        this.close();
                    } catch (Exception ex) {
                        dialog.close();
                        Notification.show("Нельзя удалить группу, пока в ней есть студенты");
                        this.close();
                    }

                });
                Button cancelButton = new Button("Отмена", event -> dialog.close());
                dialog.add(deleteButton, cancelButton);
                dialog.open();

            } else {
                Notification.show("Не выбрана группа для удаления");
            }
        });

        cancelButton.addClickListener(e -> close());
    }
}
