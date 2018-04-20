package com.valentinfilatov.voptimizer;

import android.location.Location; //TODO: bad dependency

import com.valentinfilatov.voptimizer.POJO.Coordinate;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by valik on 4/20/18.
 */

public class CoordsBus {
    private static CoordsBus instance;

    private PublishSubject<Location> subject = PublishSubject.create();

    public static CoordsBus instanceOf() {
        if (instance == null) {
            instance = new CoordsBus();
        }
        return instance;
    }

    public void pushCoordinate(Location loc) {
        subject.onNext(loc);
    }

    /**
     * Subscribe to this Observable. On event, do something
     * e.g. replace a fragment
     */
    public Observable<Location> getEvents() {
        return subject;
    }
}
