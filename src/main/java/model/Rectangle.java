import com.example.fabrichmetod.AbstrakClass;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import java.util.HashMap;
import java.util.Map;

public class Rectangle extends AbstrakClass {
    private double width;
    private double height;

    public Rectangle(double x, double y, double width, double height,
                     Color color, boolean filled) {
        super(x, y, color, filled);
        this.width = width;
        this.height = height;
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.setStroke(color);
        gc.setFill(filled ? color : Color.TRANSPARENT);

        // Рисуем прямоугольник от точки (startX, startY)
        if (filled) {
            gc.fillRect(startX, startY, width, height);
        }
        gc.strokeRect(startX, startY, width, height);
    }

    @Override
    public void drawPreview(GraphicsContext gc, double currentX, double currentY) {
        double previewWidth = currentX - startX;
        double previewHeight = currentY - startY;
        Color previewColor = color.deriveColor(0, 1, 1, 0.5);

        gc.setStroke(previewColor);
        gc.setFill(filled ? previewColor : Color.TRANSPARENT);
        gc.setLineDashes(5);

        if (filled) {
            gc.fillRect(startX, startY, previewWidth, previewHeight);
        }
        gc.strokeRect(startX, startY, previewWidth, previewHeight);
        gc.setLineDashes();
    }

    @Override
    public boolean contains(double x, double y) {
        // Проверяем, находится ли точка внутри прямоугольника
        boolean withinX = x >= Math.min(startX, startX + width) &&
                x <= Math.max(startX, startX + width);
        boolean withinY = y >= Math.min(startY, startY + height) &&
                y <= Math.max(startY, startY + height);
        return withinX && withinY;
    }

    @Override
    public String getType() {
        return "RECTANGLE";
    }

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("type", getType());
        map.put("x", startX);
        map.put("y", startY);
        map.put("width", width);
        map.put("height", height);
        map.put("color", color.toString());
        map.put("filled", filled);
        return map;
    }

    // Геттеры и сеттеры
    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    // Для удобства - получение координат всех углов
    public double getTopLeftX() {
        return Math.min(startX, startX + width);
    }

    public double getTopLeftY() {
        return Math.min(startY, startY + height);
    }

    public double getBottomRightX() {
        return Math.max(startX, startX + width);
    }

    public double getBottomRightY() {
        return Math.max(startY, startY + height);
    }

    @Override
    public String toString() {
        return String.format("Rectangle(x=%.1f, y=%.1f, width=%.1f, height=%.1f, color=%s, filled=%s)",
                startX, startY, width, height, color, filled);
    }
}