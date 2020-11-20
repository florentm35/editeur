package fr.florent.composant.event;

import fr.florent.model.selection.Area;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

public class EventSelectionAndDragged {

    public interface IActionAreaEvent {
        void action(Area area);
    }

    public interface IActionAreaWithEvent {
        void action(Area area, MouseEvent event);
    }

    private Area area;
    private int width;
    private int height;

    private IActionAreaEvent onDragged;
    private IActionAreaEvent onRelease;


    private IActionAreaWithEvent onMouve;

    private boolean inner;
    private boolean isDragged;

    public EventSelectionAndDragged(Node node, int width, int height) {
        this(node, MouseButton.PRIMARY, width, height);
    }


    public EventSelectionAndDragged(Node node, MouseButton mouseButton, int width, int height) {
        this.width = width;
        this.height = height;

        isDragged = false;


        node.setOnMouseMoved(e -> {
            if (!isDragged && onMouve != null) {
                onMouve.action(area, e);
            }
        });

        node.setOnMousePressed(e -> {
            if (e.getButton() == mouseButton) {
                isDragged = true;
                area = new Area(e.getX(), e.getY());
                inner = true;
            }
        });

        node.setOnMouseDragged(e -> {
            if (e.getButton() == mouseButton) {

                if (inner) {
                    setAreaWidthHeight(e);
                }
                if (onDragged != null) {
                    onDragged.action(area);
                }
            }
        });

        node.setOnMouseReleased(e -> {
            if (e.getButton() == mouseButton) {
                isDragged = false;
                if (inner) {
                    setAreaWidthHeight(e);
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

    /**
     * Set to area width and height
     *
     * @param e
     */
    private void setAreaWidthHeight(MouseEvent e) {
        double x = area.getBegin().getX();
        double y = area.getBegin().getY();
        double widthSelection = e.getX() - x;
        double heightSelection = e.getY() - y;

        if (x + widthSelection >= this.width) {
            widthSelection = this.width - x - 1;
        }
        if (y + heightSelection >= this.height) {
            heightSelection = this.height - y - 1;
        }

        area.setWidth(widthSelection);
        area.setHeight(heightSelection);

        area.setEnd(new Point2D(e.getX(), e.getY()));

    }


    public void setOnDragged(IActionAreaEvent onDragged) {
        this.onDragged = onDragged;
    }


    public void setOnRelease(IActionAreaEvent onRelease) {
        this.onRelease = onRelease;
    }

    public void setOnMouve(IActionAreaWithEvent onMouve) {
        this.onMouve = onMouve;
    }

}
