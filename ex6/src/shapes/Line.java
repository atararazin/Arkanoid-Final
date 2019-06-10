package shapes;

import java.util.List;

/**
*This class takes care of everything that has to do with a Line. A Line is a
*linear line with two points, each one with individual x and y values.
*This class calculates its length, its middle, if another line is equal to
*it and if it intersects with another line and at what point.
*
* @author Benjy Berkowicz & Atara Razin
*/
public class Line {
        private Point start, end;

        /**
         * this is one out of the two constructors.
         *
         * @param start
         *         the first point of the line
         * @param end
         *         the last point of the line
         */
        public Line(Point start, Point end) {
            this.start = start;
            this.end = end;
        }
        /**
         * this is the second constructor.
         *
         * @param x1
         *         the x value in the first point of the line
         * @param y1
         *         the y value in the first point of the line
         * @param x2
         *         the x value in the last point of the line
         * @param y2
         *         the y value in the last point of the line
         */
        public Line(double x1, double y1, double x2, double y2) {
            this.start = new Point(x1, y1);
            this.end = new Point(x2, y2);
        }

        /**
         * Calculates the length of the line.
         *
         * @return the distance between the two lines
         */
        public double length() {
            return start.distance(end);
        }

        /**
         * Calculates the middle point of the line.
         *
         * @return the middle
         */
        public Point middle() {
            double averageX = (this.start.getX() + this.end.getX()) / 2;
            double averageY = (this.start.getY() + this.end.getY()) / 2;
            Point middle = new Point(averageX, averageY);
            return middle;
        }

        /**
         *Returns the start point of the line.
         *
         * @return the start point
         */
        public Point start() {
            return this.start;
        }

        /**
         *Returns the end point of the line.
         *
         * @return the end point
         */
        public Point end() {
            return this.end;
        }

        /**
         * Calculates the slope of the line. If the line doesn't have a slope, it returns 0 as the slope.
         * If the slope is undefined, throw an error.
         *
         * @return the value of the slope
         * @throws ArithmeticException if the gradient is undefined
         */
        public double calculateSlope() throws ArithmeticException {
            if (this.end.getX() == this.start.getX()) {
               throw new ArithmeticException("Vertical line detected");
            } else {
                double deltaY = this.end.getY() - this.start.getY();
                double deltaX = this.end.getX() - this.start.getX();
                double slope = deltaY / deltaX;
                return slope;
            }
        }

        /**
         * Calculates the linear equation. a linear equation is in the form of y=ax+b, where a is the slope
         * and b in the intersection with the y axis. This method calculates those two and returns them as a
         * point, meaning an x and y coordinate, where the x coordinate is the a and the y coordinate is the b.
         *
         * @return the coordinates that are the equation.
         * @throws ArithmeticException as a result of the previous exception. This exception is dealing with
         * the case that the line is parallel to the y axis.
         */
        public Point calculateEquation() throws ArithmeticException {
            try {
                double a = this.calculateSlope();
                double b = this.start.getY() - (this.calculateSlope() * this.start.getX());
                Point equation = new Point(a, b);
                return equation;
            } catch (ArithmeticException e) {
                throw e;
            }
        }

        /**
         * numOfVertical lines returns whether or not the first or second line are vertical.
         * A return result of:
         * 1 - Neither this nor other are vertical
         * 2 - This is vertical and other isn't
         * 3 - This isn't vertical and other is
         * 4 - Both lines are vertical
         * @param other the other line
         * @return the result condition
         */
        @SuppressWarnings("unused")
        public int numOfVerticalLines(Line other) {
            int caseNum = 1;
            double thisSlope, otherSlope;
            try  {
                thisSlope = this.calculateSlope();
            } catch (ArithmeticException b) {
                caseNum += 1;
            }
            try {
                otherSlope = other.calculateSlope();
            } catch (ArithmeticException d) {
                caseNum += 2;
            }
            return caseNum;
        }

        /**
         * Calculates the x coordinate of the intersecting point. It also
         * deals with the case where the line is parallel to the y axis. If
         * "this" is parallel to the y axis, it will go into the outer catch statement.
         * If "other" is parallel to the y axis, it will go into the inner
         * catch statement.
         *
         * @param other
         *         a different line
         * @return a double
         * @throws ArithmeticException as a result of the previous exception.
         * It deals with the case where the line is parallel to the y-axis.
         */
        public double xIntersection(Line other) throws ArithmeticException {
            int verticalLineCase = this.numOfVerticalLines(other);
            double xIntersect;

            if (verticalLineCase == 1) {
                double coefficient = this.calculateSlope() - other.calculateSlope();
                // The 'Y' coordinate returned by calculateEquation is the 'b' from y = mx + b
                double b1 = this.calculateEquation().getY();
                double b2 = other.calculateEquation().getY();
                xIntersect = (b2 - b1) / coefficient;
            } else if (verticalLineCase == 2) {
               // If 'our' line is vertical, then the x intersection must be the x coordinate of the start
                xIntersect = this.start().getX();
            } else if (verticalLineCase == 3) {
                // If the other line is vertical, then the x intersection must be the x coordinate of the start
                xIntersect = other.start().getX();
            } else {
                throw new ArithmeticException("No x intersect between the lines");
            }
            return xIntersect;
        }

        /**
         * Calculates the y coordinate of the intersecting point. Again, there is a nested
         * try catch statement to deal with the case where one or both of the lines are
         * parallel to the y axis.
         *
         * @param other
         *         a different line
         * @return a double
         * @throws ArithmeticException as a result of the previous exception
         */
        public double yIntersection(Line other) throws ArithmeticException {

            int verticalLineCase = this.numOfVerticalLines(other);
            double slope, xIntersect, constant, yIntersect = 0;

            /*
             * The following four cases handle corner cases for instances when the different
             * lines are vertical or not.  In these specific cases slightly different variables need to be
             * substituted in to complete the equation.
             */
            // 1 - Neither this nor other are vertical
            if (verticalLineCase == 1) {
                slope = this.calculateSlope();
                xIntersect = this.xIntersection(other);
                constant = this.calculateEquation().getY();
                yIntersect = (slope * xIntersect) + constant;
            // 2 - This is vertical and other isn't
            } else if (verticalLineCase == 2) {
                slope = other.calculateSlope();
                xIntersect = this.start.getX();
                constant = other.calculateEquation().getY();
                yIntersect = (slope * xIntersect) + constant;
            // 3 - This isn't vertical and other is
            } else if (verticalLineCase == 3) {
                slope = this.calculateSlope();
                xIntersect = other.start.getX();
                constant = this.calculateEquation().getY();
                yIntersect = (slope * xIntersect) + constant;
            // 4 - both are vertical
            } else if (verticalLineCase == 4) {
                throw new ArithmeticException("No Y intersect of the two lines (Parallel");
            }

            return yIntersect;
        }

        /**
         * Attempts to find the x and y intersection of two lines, if they return correctly
         * without throwing an exception then the two lines intersect, if not or they return
         * false then they don't.
         *
         * @param other
         *         a different line
         * @return a boolean value.
         */
        public boolean isIntersecting(Line other) {
            Point intersection;
            try {
                // We attempt to find the intersection
                intersection = new Point(this.xIntersection(other), this.yIntersection(other));
            } catch (ArithmeticException e) {
                return false;
            }
            // We check if the discovered intersection point is on both lines
            boolean isInBounds = this.isOnLine(intersection) && other.isOnLine(intersection);
            return isInBounds;
        }


        /**
         * Returns the intersection point if the lines intersect, and null otherwise.
         *
         * @param other
         *         a different line
         * @return the intersecting point, if it exists.
         */

        public Point intersectionWith(Line other) {
            if (this.isIntersecting(other)) {
                return new Point(this.xIntersection(other), this.yIntersection(other));
            } else {
                return null;
            }
        }

        /**
         * isOnLine checks if the distance between the start of a line, a point, and the end of the line
         * added together is equal to the 'length' of a line.  If those two sums are equal then the given point is
         * on the line.
         * @param checkPoint the point to be checked.
         * @return if the substituted equation and the bounds return true, then the point is on the line
         */
        public boolean isOnLine(Point checkPoint) {
            Point equationPoints;
            double slopeTimesX, startToPoint, pointToEnd;
            boolean substitutedEqn, isWithinBounds;

            // We try to get the equation of the line, if we succeed we attempt to sub in the variables of our point
            try {
               equationPoints = this.calculateEquation();
               slopeTimesX = equationPoints.getX() * checkPoint.getX();
               substitutedEqn = Math.round(checkPoint.getY()) == Math.round(slopeTimesX + equationPoints.getY());
            } catch (ArithmeticException e) {
               // If one of the lines is vertical then we see if the x coordinates are equal
               substitutedEqn = Math.round(this.start().getX()) == Math.round(checkPoint.getX());
            }

            startToPoint = checkPoint.distance(this.start());
            pointToEnd = checkPoint.distance(this.end());
            // Checks if the distance between the point and the start/end equals the 'length' of the line.
            isWithinBounds = Math.round(this.length()) == Math.round(startToPoint + pointToEnd);

            return (substitutedEqn && isWithinBounds);
        }

        /**
         * Returns (if an intersection exists) between the line and any of the component lines of a
         * given rectangle.
         * @param rect the rectangle to be checked
         * @return the closest collision point to the rectangle or null
         */
        public Point closestIntersectionToStartOfLine(Rectangle rect) {
            // We get a list of all intersection points between the line and the rectangle
            List<Point> intersectionPoints = rect.intersectionPoints(this);
            Point shortestPointDistance;
            // What follows is a 'min' function, we initialize a value at get(0) and check to see if there is a shorter
            // distance
            if (!intersectionPoints.isEmpty()) {
                shortestPointDistance = intersectionPoints.get(0);
                // We loop through every point to see if there is a closer one
                for (Point interPoint : intersectionPoints) {
                    if (interPoint.distance(this.start) < shortestPointDistance.distance(this.start)) {
                        shortestPointDistance = interPoint;
                    }
                }
            // If there are no intersection points at all, we return null
            } else {
                shortestPointDistance = null;
            }
            return shortestPointDistance;
        }

        /**
         * checks if two lines are equal.
         *
         * @param other
         *         a different line
         * @return a boolean value depending on if the lines are equal or not.
         *         the last point of the line
         */
        public boolean equals(Line other) {
            return (this.start.equals(other.start) == this.end.equals(other.end));
        }
}