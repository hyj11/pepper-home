package com.sim.landlord.landLordJavaFx;

import com.sun.javafx.scene.control.skin.VirtualFlow;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.IndexedCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Path;
import javafx.scene.shape.PathElement;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Huang Yujiao
 * @Date: 2021/10/15 17:24
 * @Desc:
 */
public  class DragSelectionTextArea extends TextArea {
    /**
     * Start position of the highlight.
     */
    private int highlightStartPos = -1;

    /**
     * End position of the highlight.
     */
    private int highlightEndPos = -1;

    /**
     * Path node to act as highlight.
     */
    private final Path highlightPath = new Path();

    /**
     * Node to keep reference of the selectionGroup node of the TextArea.
     */
    private Group selectionGroup;

    /**
     * Node to keep reference of the all contents of the TextArea.
     */
    private StackPane contentPane;

    /**
     * Node to keep reference of the content node.
     */
    private Region textContent;

    /**
     * Specifies whether the selection of text is done for purpose of highlighting.
     */
    private boolean highlightInProgress = false;

    public DragSelectionTextArea() {
        highlightPath.setStyle("-fx-fill:red");
        highlightPath.setMouseTransparent(true);
        highlightPath.setManaged(false);
        highlightPath.setStroke(null);
        textProperty().addListener((obs, oldVal, newVal) -> removeHighlight());
        widthProperty().addListener((obs, oldVal, newVal) -> {
            if (highlightStartPos > -1 && highlightEndPos > -1 && selectionGroup != null) {
                highlightInProgress = true;
                selectRange(highlightStartPos, highlightEndPos);
            }
        });
    }

    /**
     * Highlights the range of characters in the text area.
     *
     * @param startPos Start position of the character in the text
     * @param endPos   End position of the character in the text
     */
    public void highlight(final int startPos, final int endPos) {
        highlightStartPos = startPos;
        highlightEndPos = endPos;
        highlightInProgress = true;
        selectRange(highlightStartPos, highlightEndPos);
    }

    public Path getHighlightPath() {
        return highlightPath;
    }

    @Override
    protected void layoutChildren() {
        super.layoutChildren();
        if (selectionGroup == null || contentPane == null) {
            final Region content1 = (Region) lookup(".content");
            if (content1 != null) {
                // Looking for the Group node that is responsible for selection
                content1
                        .getChildrenUnmodifiable()
                        .stream()
                        .filter(node -> node instanceof Group)
                        .map(node -> (Group) node)
                        .filter(grp -> {
                            final boolean notSelectionGroup =
                                    grp.getChildren().stream().anyMatch(node -> !(node instanceof Path));
                            return !notSelectionGroup;
                        })
                        .findFirst()
                        .ifPresent(n -> {
                            n.boundsInLocalProperty().addListener((obs, old, bil) -> {
                                if (highlightInProgress) {
                                    updateHightlightBounds();
                                }
                            });
                            selectionGroup = n;
                        });
                contentPane = (StackPane) content1.getParent();
                textContent = content1;
            }
        }
    }

    /**
     * Updates the highlight with the provided bounds.
     */
    private void updateHightlightBounds() {
        if (!selectionGroup.getChildren().isEmpty()) {
            final Path p = (Path) selectionGroup.getChildren().get(0);
            final List<PathElement> elements = new ArrayList<>(p.getElements());

            highlightPath.getElements().clear();
            highlightPath.getElements().addAll(elements);
            final Node textNode = textContent.lookup(".text");
            highlightPath.setLayoutX(textNode.getLayoutX());
            highlightPath.setLayoutY(textNode.getLayoutY());

            if (contentPane != null && !contentPane.getChildren().contains(highlightPath)) {
                contentPane.getChildren().add(highlightPath);
            }
            highlightInProgress = false;
            Platform.runLater(this::deselect);
        }
    }

    /**
     * Removes the highlight in the text area.
     */
    public void removeHighlight() {
        if (contentPane != null) {
            contentPane.getChildren().remove(highlightPath);
        }
        highlightStartPos = -1;
        highlightEndPos = -1;
    }
}