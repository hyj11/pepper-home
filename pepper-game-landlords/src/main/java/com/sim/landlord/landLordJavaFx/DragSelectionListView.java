package com.sim.landlord.landLordJavaFx;

import com.sun.javafx.scene.control.skin.VirtualFlow;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.IndexedCell;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 该类增强了ListView本身的行中选中功能.
 * <br/>1. 鼠标拖动选中
 * <br/>2. 连续Ctrl+Shift区间选中
 * <p>
 * 其中使用VirtualFlow vf = ((VirtualFlow) ((ListViewSkin)
 * getChildrenUnmodifiable().get(0)).getChildrenUnmodifiable().get(0));来判断当前显示可见的行号,
 * 使用setOnMousePressed/setOnMouseDragged/setOnMouseReleased来处理增强的拖动和Ctrl+Shift选中事件.
 * <p>
 * 遗留问题: 当Ctrl+Shift操作后一次鼠标点击在一个已经选中的行时, 最后的结果会取消选中该行.
 * 如果还需要添加其他鼠标事件而需要使用到选中状态时可能会有冲突, 还未测试.
 *
 * @Author: Huang Yujiao
 * @Date: 2021/10/15 17:24
 * @Desc:
 */
public class DragSelectionListView<T extends Object> extends ListView<T> {

    /**
     * 鼠标拖动之前ListView的选中状态. 在鼠标拖动的过程中需要根据拖动事件的起始行号和当前行号来计算新选中的行,
     * 同事和原始选中状态结合作为新的选中状态.
     */
    private ObservableList<Integer> oldSelectedIndices;
    /**
     * 鼠标拖动事件是否已经开始. 会在MouseDragged中设置为true, 在MouseReleased中重置为false
     */
    private boolean isDragStarted = false;
    /**
     * 最后一次鼠标点击选中的行号. 每次鼠标点击时都会进行记录
     */
    private int lastPressedRow;
    /**
     * 鼠标拖动事件的起始行. 会在MousePressed中设置为当前点击行, 在MouseReleased中重置为-1
     */
    private int dragStartedRow = -1;
    /**
     * 上一次拖动经过的行号. 鼠标拖动事件过程中, 会不断的触发MouseDragged事件, 每次事件结束时记录鼠标所在行号,
     * 在MouseReleased中重置为-1
     */
    private int prevDragRow = -1;

    public DragSelectionListView() {
        getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        addDragSelectionEventHandlers();
    }

    public DragSelectionListView(ObservableList<T> ol) {
        super(ol);
        getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        addDragSelectionEventHandlers();
    }

    /**
     * 根据相对于ListView的坐标，获取鼠标所在行
     *
     * @param x
     * @param y
     * @return
     */
    public int getRowAtPoint(double x, double y) {
        int row = -1;
        VirtualFlow vf = (VirtualFlow) (getChildrenUnmodifiable().get(0));
        int firstIndex = vf.getFirstVisibleCell().getIndex();
        int lastIndex = vf.getLastVisibleCell().getIndex();
        for (int i = firstIndex; i <= lastIndex; i++) {
            IndexedCell visibleCell = vf.getVisibleCell(i);
            if (visibleCell.getBoundsInParent().contains(x, y)) {
                row = i;
                break;
            }
        }
        return row;
    }

    /**
     * 获取当前显示出来的第一行行号
     *
     * @return
     */
    public int getFirstVisibleRow() {
        VirtualFlow vf = (VirtualFlow) (getChildrenUnmodifiable().get(0));
        return vf.getFirstVisibleCell().getIndex();
    }

    /**
     * 获取当前显示出来的最后一行行号
     *
     * @return
     */
    public int getLastVisibleRow() {
        VirtualFlow vf = (VirtualFlow) (getChildrenUnmodifiable().get(0));
        return vf.getLastVisibleCell().getIndex();
    }

    /**
     * 添加用于处理拖动选中和连续Ctrl+Shift选中的事件: MousePressed/MouseDraggedMouseReleased
     */
    private void addDragSelectionEventHandlers() {
        setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                final int rowAtPoint = getRowAtPoint(t.getX(), t.getY());

                //<editor-fold defaultstate="collapsed" desc="当Shift和Ctrl键同时按下时，会增加选中鼠标两次点击之间的行(不分左右键)">
                if (t.isControlDown() && t.isShiftDown()) {
                    final int min = Math.min(rowAtPoint, lastPressedRow);
                    final int max = Math.max(rowAtPoint, lastPressedRow);
                    DragSelectionListView.this.getSelectionModel().selectRange(min, max + 1);
                }

                Node node = t.getPickResult().getIntersectedNode();

                // go up from the target node until a list cell is found or it's clear
                // it was not a cell that was clicked
                while (node != null && !(node instanceof ListCell)) {
                    node = node.getParent();
                }

                // if is part of a cell or the cell,
                // handle event instead of using standard handling
                if (node instanceof ListCell) {
                    // prevent further handling
                    t.consume();

                    ListCell cell = (ListCell) node;
                    ListView lv = cell.getListView();

                    // focus the listview
                    lv.requestFocus();

                    if (!cell.isEmpty()) {
                        // handle selection for non-empty cells
                        int index = cell.getIndex();
                        if (cell.isSelected()) {
                            lv.getSelectionModel().clearSelection(index);
                        } else {
                            lv.getSelectionModel().select(index);
                        }
                    }
                }

                    dragStartedRow = rowAtPoint;
                    lastPressedRow = rowAtPoint;
                }

        });
        setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                int rowAtPoint = getRowAtPoint(t.getX(), t.getY());
                if (prevDragRow == rowAtPoint) {
                    return;
                }

                ObservableList<Integer> selectedIndices = DragSelectionListView.this.getSelectionModel().getSelectedIndices();
                if (!isDragStarted) {
                    oldSelectedIndices = FXCollections.observableArrayList(selectedIndices);
                    isDragStarted = true;
                } else {
                    DragSelectionListView.this.getSelectionModel().clearSelection();
                    for (Integer integer : oldSelectedIndices) {
                        DragSelectionListView.this.getSelectionModel().selectIndices(integer);
                    }

                    if (dragStartedRow != -1) {
                        DragSelectionListView.this.getSelectionModel().selectRange(Math.min(rowAtPoint, dragStartedRow), Math.max(rowAtPoint, dragStartedRow) + 1);
                    }
                }

                prevDragRow = rowAtPoint;
            }
        });
        setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                System.out.println(dragStartedRow + " " + prevDragRow);

                //下面主要是重置Drag完毕后的一些状态
                dragStartedRow = -1;
                prevDragRow = -1;
                isDragStarted = false;


            }
        });


//        addEventHandler(MouseEvent.MOUSE_CLICKED, evt -> {
//            Node node = evt.getPickResult().getIntersectedNode();
//
//            // go up from the target node until a list cell is found or it's clear
//            // it was not a cell that was clicked
//            while (node != null  && !(node instanceof ListCell)) {
//                node = node.getParent();
//            }
//
//            // if is part of a cell or the cell,
//            // handle event instead of using standard handling
//            if (node instanceof ListCell) {
//                // prevent further handling
//                evt.consume();
//
//                ListCell cell = (ListCell) node;
//                ListView lv = cell.getListView();
//
//                // focus the listview
//                lv.requestFocus();
//
//                if (!cell.isEmpty()) {
//                    // handle selection for non-empty cells
//                    int index = cell.getIndex();
//                    if (cell.isSelected()) {
//                        lv.getSelectionModel().clearSelection(index);
//                    } else {
//                        lv.getSelectionModel().select(index);
//                    }
//                }
//            }
//        });
    }

    public ObservableList listAdd(List oldList, ObservableList newlist) {
        oldList.stream().forEach(n -> {
            if (!newlist.contains(n)) {
                newlist.add(n);
            } else {
                newlist.remove(n);
            }
        });
        return newlist;
    }

    public List hasContains(ObservableList oldList, ObservableList newlist) {
        List li = (List) oldList.stream()
                .map(t -> newlist.stream().filter(s -> Objects.nonNull(t) && Objects.nonNull(s) && Objects.equals(t, s)).findAny().orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        return li;
    }

    public ObservableList listDis(ObservableList oldList) {
        List li = (List) oldList.stream().distinct().collect(Collectors.toList());
        oldList.clear();
        li.forEach(o -> {
            oldList.add(o);
        });
        return oldList;
    }

    public List<Integer> getNumList(int num1, int num2) {
        List<Integer> list = new ArrayList();
        if (num1 < num2) {
            while (num1 < num2) {
                list.add(num1);
                num1++;
            }
        }
        return list;
    }

}