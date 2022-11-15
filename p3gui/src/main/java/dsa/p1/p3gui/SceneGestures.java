package dsa.p1.p3gui;

import javafx.event.EventHandler;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;

public class SceneGestures {

	private EventHandler<MouseEvent> onMouseClickedEventHandler = new EventHandler<>() {

		@Override
		public void handle(MouseEvent event) {
			if (event.getButton().equals(MouseButton.MIDDLE)) {
				if (event.getClickCount() == 2) {
					panAndZoomPane.resetZoom();
				}
			}
			if (event.getButton().equals(MouseButton.SECONDARY)) {
				if (event.getClickCount() == 2) {
					panAndZoomPane.fitWidth();
				}
			}
		}
	};

	private EventHandler<MouseEvent> onMouseDraggedEventHandler = new EventHandler<>() {
		@Override
		public void handle(MouseEvent event) {
			if (event.getButton().equals(MouseButton.MIDDLE)) {
				panAndZoomPane.setTranslateX(
						sceneDragContext.translateAnchorX + event.getX() - sceneDragContext.mouseAnchorX);
				panAndZoomPane.setTranslateY(
						sceneDragContext.translateAnchorY + event.getY() - sceneDragContext.mouseAnchorY);

				event.consume();
			}
		}
	};

	private EventHandler<MouseEvent> onMousePressedEventHandler = new EventHandler<>() {

		@Override
		public void handle(MouseEvent event) {
			if (event.getButton().equals(MouseButton.MIDDLE)) {
				sceneDragContext.mouseAnchorX = event.getX();
				sceneDragContext.mouseAnchorY = event.getY();

				sceneDragContext.translateAnchorX = panAndZoomPane.getTranslateX();
				sceneDragContext.translateAnchorY = panAndZoomPane.getTranslateY();
			}
		}

	};

	private EventHandler<ScrollEvent> onScrollEventHandler = new EventHandler<>() {

		@Override
		public void handle(ScrollEvent event) {

			double delta = PanAndZoomPane.DEFAULT_DELTA;

			double scale = panAndZoomPane.getScale();
			double oldScale = scale;

			panAndZoomPane.setDeltaY(event.getDeltaY());
			if (panAndZoomPane.deltaY.get() < 0) {
				scale /= delta;
			} else {
				scale *= delta;
			}

			double f = (scale / oldScale) - 1;
			double dx = (event.getX() - (panAndZoomPane.getBoundsInParent().getWidth() / 2
					+ panAndZoomPane.getBoundsInParent().getMinX()));
			double dy = (event.getY() - (panAndZoomPane.getBoundsInParent().getHeight() / 2
					+ panAndZoomPane.getBoundsInParent().getMinY()));

			panAndZoomPane.setPivot(f * dx, f * dy, scale);

			event.consume();

		}
	};

	private DragContext sceneDragContext = new DragContext();

	private PanAndZoomPane panAndZoomPane;

	public SceneGestures(PanAndZoomPane canvas) {
		this.panAndZoomPane = canvas;
	}

	public EventHandler<MouseEvent> getOnMouseClickedEventHandler() {
		return onMouseClickedEventHandler;
	}

	public EventHandler<MouseEvent> getOnMouseDraggedEventHandler() {
		return onMouseDraggedEventHandler;
	}

	public EventHandler<MouseEvent> getOnMousePressedEventHandler() {
		return onMousePressedEventHandler;
	}

	public EventHandler<ScrollEvent> getOnScrollEventHandler() {
		return onScrollEventHandler;
	}
}