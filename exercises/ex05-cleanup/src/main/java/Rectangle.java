/** A rectangle with mutable dimensions. */
public class Rectangle {
  private double width;
  private double height;

  /**
   * Creates a rectangle.
   *
   * @param width the width
   * @param height the height
   */
  public Rectangle(double width, double height) {
    this.width = width;
    this.height = height;
  }

  /**
   * Returns the area.
   *
   * @return width multiplied by height
   */
  public double area() {
    return width * height;
  }

  /**
   * Scales both dimensions.
   *
   * @param factor the multiplier for each dimension
   */
  public void scale(double factor) {
    width *= factor;
    height *= factor;
  }

  /**
   * Compares areas.
   *
   * @param other the rectangle to compare
   * @return true if this rectangle has greater area
   */
  public boolean isLargerThan(Rectangle other) {
    return area() > other.area();
  }
}
