package fr.florent.composant.event;

import fr.florent.model.selection.Area;
import javafx.scene.Node;
import javafx.scene.input.MouseButton;

public class EventSelection {

    private Area area;

    private IActionAreaEvent onDragged;
    private IActionAreaEvent onRelease;

    private boolean inner;

    public EventSelection(Node node) {
        this(node, MouseButton.PRIMARY);
    }

    public EventSelection(Node node, MouseButton mouseButton) {

        node.setOnMousePressed(e -> {
            if (e.getButton() == mouseButton) {
                area = new Area(e.getX(), e.getY());
                inner = true;
            }
        });

        node.setOnMouseDragged(e -> {
            if (e.getButton() == mouseButton) {

                if (inner) {
                    area.setWidth(e.getX() - area.getBegin().getX());
                    area.setHeight(e.getY() - area.getBegin().getY());
                }
                if (onDragged != null) {
                    onDragged.action(area);
                }
            }
        });

        node.setOnMouseReleased(e -> {
            if (e.getButton() == mouseButton) {

                if (inner) {
                    area.setWidth(e.getX() - area.getBegin().getX());
                    area.setHeight(e.getY() - area.getBegin().getY());
                }
                if (onRelease != null) {
                    onRelease.action(area);
                }
            }
        });

        node.setOnMouseExited(e -> {
            inner = false;
        });
        node.setOnMouseEntered(e -> {
            inner = true;
        });

    }


    public void setOnDragged(IActionAreaEvent onDragged) {
        this.onDragged = onDragged;
    }


    public void setOnRelease(IActionAreaEvent onRelease) {
        this.onRelease = onRelease;
    }


}
