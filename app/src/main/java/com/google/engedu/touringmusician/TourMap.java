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

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

public class TourMap extends View {

    private Bitmap mapImage;
    private CircularLinkedList list1 = new CircularLinkedList();
    private CircularLinkedList list2 = new CircularLinkedList();
    private CircularLinkedList list3 = new CircularLinkedList();

    private String insertMode = "Smallest";

    public TourMap(Context context) {
        super(context);
        mapImage = BitmapFactory.decodeResource(
                getResources(),
                R.drawable.map);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(mapImage, 0, 0, null);
        Paint pointPaint = new Paint();
        pointPaint.setColor(Color.RED);
        /**
         **
         **  YOUR CODE GOES HERE
         **
         **/
        Paint lineBeginningPaint = new Paint();
        Paint lineNearestPaint = new Paint();
        Paint lineSmallestPaint = new Paint();

        lineBeginningPaint.setColor(Color.YELLOW);
        lineBeginningPaint.setStrokeWidth(8);
        lineNearestPaint.setColor(Color.BLUE);
        lineNearestPaint.setStrokeWidth(8);
        lineSmallestPaint.setColor(Color.GREEN);
        lineSmallestPaint.setStrokeWidth(8);
        Point prev = null;
        for (Point p : list1) {
            canvas.drawCircle(p.x, p.y, 20, pointPaint);
            if (prev != null) {
                canvas.drawLine(prev.x, prev.y, p.x, p.y, lineBeginningPaint);
            }
            prev = p;
        }
        prev = null;
        for (Point p : list2) {
            canvas.drawCircle(p.x, p.y, 20, pointPaint);
            if (prev != null) {
                canvas.drawLine(prev.x, prev.y, p.x, p.y, lineNearestPaint);
            }
            prev = p;
        }
        prev = null;
        for (Point p : list3) {
            canvas.drawCircle(p.x, p.y, 20, pointPaint);
            if (prev != null) {
                canvas.drawLine(prev.x, prev.y, p.x, p.y, lineSmallestPaint);
            }
            prev = p;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Point p = new Point((int) event.getX(), (int)event.getY());
//                if (insertMode.equals("Closest")) {
//                    list.insertNearest(p);
//                } else if (insertMode.equals("Smallest")) {
//                    list.insertSmallest(p);
//                } else {
//                    list.insertBeginning(p);
//                }
                list1.insertBeginning(p);
                list2.insertNearest(p);
                list3.insertSmallest(p);
                TextView message = (TextView) ((Activity) getContext()).findViewById(R.id.game_status);
//                if (message != null) {
//                    message.setText(String.format("Tour length is now %.2f", list.totalDistance()));
//                }
                if (message != null) {
                    message.setText(String.format("Beginnng %.2f\nNearest %.2f\nSmallest %.2f", list1.totalDistance(), list2.totalDistance(), list3.totalDistance()));
                }
                invalidate();
                return true;
        }
        return super.onTouchEvent(event);
    }

    public void reset() {
        list1.reset();
        list2.reset();
        list3.reset();

        invalidate();
    }

    public void setInsertMode(String mode) {
        insertMode = mode;
    }
}
