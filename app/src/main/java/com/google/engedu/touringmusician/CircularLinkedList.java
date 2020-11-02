/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.touringmusician;


import android.graphics.Point;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Pair;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

public class CircularLinkedList implements Iterable<Point> {

    private class Node {
        Point point;
        Node prev, next;

        public Node(Point p) {
            this.point = p;
            this.prev = null;
            this.next = null;
        }
    }
    Node head;


    public void insertBeginning(Point p) {
        Node newNode = new Node(p);
        if (head == null) {
            head = newNode;
            newNode.next = newNode;
            newNode.prev = newNode;
        }
        else {
            newNode.next=head;
            newNode.prev=head.prev;

            head.prev=newNode;
            newNode.prev.next=newNode;
            head=newNode;
        }
    }

    private float distanceBetween(Point from, Point to) {
        return (float) Math.sqrt(Math.pow(from.y-to.y, 2) + Math.pow(from.x-to.x, 2));
    }

    public float totalDistance() {
        float total = 0;
        Point prev = null;
        for (Point p: this) {
            if (prev != null) {
                total += distanceBetween(prev, p);
            }
            prev = p;
        }
        return total;
    }

    public void insertNearest(Point p) {
        Node newNode = new Node(p);
        if (head == null) {
            head = newNode;
            head.next = head;
            head.prev = head;
        }
        else {
            Node toInsert = head;
            float nearest = Integer.MAX_VALUE;
            Node current = head;
            while (current.next != head) {
                Point currentP = current.point;
                float distance = distanceBetween(p, currentP);
                if (distance < nearest) {
                    nearest = distance;
                    toInsert = current;
                }
                current = current.next;
            }
            Node after = toInsert.next;
            newNode.next = after;
            newNode.prev = toInsert;
            after.prev = newNode;
            toInsert.next = newNode;
        }
    }

    public void insertSmallest(Point p) {
        if (head == null || head.next == head) {
            insertBeginning(p);
        }
        else {
            Node newNode = new Node(p);
            float minIncrease = Integer.MAX_VALUE;
            Node current = head;
            Node toInsert = head;
            while (current.next != head) {
                float distanceBeforeAdd = distanceBetween(current.point, current.next.point);
                float distanceAfterAdd = distanceBetween(current.point, p) + distanceBetween(current.next.point, p);
                if (distanceAfterAdd - distanceBeforeAdd < minIncrease) {
                    minIncrease = distanceAfterAdd - distanceBeforeAdd;
                    toInsert = current;
                }
                current = current.next;
            }
            Node after = toInsert.next;
            newNode.next = after;
            newNode.prev = toInsert;
            after.prev = newNode;
            toInsert.next = newNode;
        }
    }

    public void reset() {
        head = null;
    }

    private class CircularLinkedListIterator implements Iterator<Point> {

        Node current;

        public CircularLinkedListIterator() {
            current = head;
        }

        @Override
        public boolean hasNext() {
            return (current != null);
        }

        @Override
        public Point next() {
            Point toReturn = current.point;
            current = current.next;
            if (current == head) {
                current = null;
            }
            return toReturn;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    @Override
    public Iterator<Point> iterator() {
        return new CircularLinkedListIterator();
    }


}
