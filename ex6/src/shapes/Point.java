package shapes;

/**
*This class takes care of everything that has to do with a point. A point is an
*ordered pair, with an x value and a y value.
*This class calculates a)the distance between a point and another point,b) if two
*points are equal and can c)return the values of the x and y.
*
* @author Benjy Berkowicz & Atara Razin
*/
public class Point {
        //members
        private double x;
        private double y;

        /**
         * this is the constructor of the class point.
         *
         * @param x
         *         the left side of the pair
         * @param y
         *         the right side of the pair
         */
        public Point(double x, double y) {
            this.x = x;
            this.y = y;
        }

        /**
         * distance returns the distance of this point to another point.
         *
         * @param other
         *         another point
         * @return Math.sqrt((dx * dx) + (dy * dy))
         */
        public double distance(Point other) {
            double dx = this.x - other.getX();
            double dy = this.y - other.getY();
            return Math.sqrt((dx * dx) + (dy * dy));
        }

        /**
         * equals -- return true if the points are equal, false otherwise.
         *
         * @param other
         *         another point.
         * @return this.x == other.getX() && this.y == other.getY();
         */
        public boolean equals(Point other) {
            return (this.x == other.getX()) && (this.y == other.getY());
        }

        /**
         * this method returns the value of x.
         *
         * @return this.x (the value of x)
         */
        public double getX() {
            return this.x;
        }

        /**
         * this method returns the value of y.
         *
         * @return this.y (the value of y)
         */

        public double getY() {
           return this.y;
        }

        /**
         * setX changes the x value of the point to a new provided one.
         * @param xNew new x value
         */
        public void setX(double xNew) {
            this.x = xNew;
        }

        /**
         * setY changes the y value of the point to a new provided one.
         * @param yNew new y value
         */
        public void setY(double yNew) {
            this.y = yNew;
        }
}
