package dsa.p1.p3gui;

import javafx.scene.paint.Color;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;

/**
 *
 * @author kn https://gist.github.com/kn0412/2086581e98a32c8dfa1f69772f14bca4
 * @author Ansel Obergfell
 */
public class Arrow extends Path {
	private static int defaultArrowHeadSize = 10;
	private static int defaultElipseHeight = 50;
	private static int defaultOffset = 10, xOffset = 125, yOffset = 55;

	public Arrow(double startX, double startY, double endX, double endY) {
		this(startX, startY, endX, endY, Color.BLACK);
	}

	public Arrow(double startX, double startY, double endX, double endY, Color c) {
		startX += xOffset;
		startY += yOffset;
		endX += xOffset;
		endY += yOffset;

		strokeProperty().bind(fillProperty());
		setStrokeWidth(1.5);
		setFill(c);

		// ArrowHead
		double angle = Math.atan2((endY - startY), (endX - startX));
		double sign = Math.signum(Math.signum(angle) + .1);
		endY -= ((defaultElipseHeight + defaultOffset) * sign);
		angle -= Math.PI / 2;
		double sin = Math.sin(angle);
		double cos = Math.cos(angle);

		// Line
		getElements().add(new MoveTo(startX, startY));
		getElements().add(new LineTo(endX, endY));

		// point1
		double srt = Math.sqrt(3) / 2;
		double x1 = (-.5 * cos + srt * sin) * defaultArrowHeadSize + endX;
		double y1 = (-.5 * sin - srt * cos) * defaultArrowHeadSize + endY;

		// point2
		double x2 = (.5 * cos + srt * sin) * defaultArrowHeadSize + endX;
		double y2 = (.5 * sin - srt * cos) * defaultArrowHeadSize + endY;

		getElements().add(new LineTo(x1, y1));
		getElements().add(new LineTo(x2, y2));
		getElements().add(new LineTo(endX, endY));
	}
}