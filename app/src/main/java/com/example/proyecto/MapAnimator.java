package com.example.proyecto;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.List;

/**
 * Created by amal.chandran on 22/12/16.
 */

public class MapAnimator {

  private static MapAnimator mapAnimator;

  private Polyline backgroundPolyline;

  private Polyline foregroundPolyline;

  private PolylineOptions optionsForeground;

  private AnimatorSet firstRunAnimSet;

  private AnimatorSet secondLoopRunAnimSet;

  static final int ANIMCOLOR = Color.DKGRAY; //Second color

  static final int  BGCOLOR = Color.LTGRAY; //

  static final long ANIMDURATION = 3000; //foregroundRouteAnimator.setDuration //First Drawing Time

  static final long SLSDELAY = 500; //secondLoopRunAnimSet.setStartDelay //Second color time hold

  static final long CADURATION = 1000; //colorAnimation.setDuration //Hide Time

  static final long PCDURATION = 5000; //percentageCompletion.setDuration //Animation Duration

  private MapAnimator() {

  }

  public static MapAnimator getInstance() {
    if (mapAnimator == null) {
      mapAnimator = new MapAnimator();
    }
    return mapAnimator;
  }

  public void animateRoute(GoogleMap googleMap, List<LatLng> bangaloreRoute) {
    if (firstRunAnimSet == null) {
      firstRunAnimSet = new AnimatorSet();
    } else {
      firstRunAnimSet.removeAllListeners();
      firstRunAnimSet.end();
      firstRunAnimSet.cancel();

      firstRunAnimSet = new AnimatorSet();
    }
    if (secondLoopRunAnimSet == null) {
      secondLoopRunAnimSet = new AnimatorSet();
    } else {
      secondLoopRunAnimSet.removeAllListeners();
      secondLoopRunAnimSet.end();
      secondLoopRunAnimSet.cancel();

      secondLoopRunAnimSet = new AnimatorSet();
    }
    //Reset the polylines
    if (foregroundPolyline != null) {
      foregroundPolyline.remove();
    }
    if (backgroundPolyline != null) {
      backgroundPolyline.remove();
    }

    PolylineOptions optionsBackground = new PolylineOptions().add(bangaloreRoute.get(0)).color(ANIMCOLOR).width(10);
    backgroundPolyline = googleMap.addPolyline(optionsBackground);

    optionsForeground = new PolylineOptions().add(bangaloreRoute.get(0)).color(BGCOLOR).width(12);
    foregroundPolyline = googleMap.addPolyline(optionsForeground);

    final ValueAnimator percentageCompletion = ValueAnimator.ofInt(0, 100);
    percentageCompletion.setDuration(PCDURATION);
    percentageCompletion.setInterpolator(new DecelerateInterpolator());
    percentageCompletion.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
      @Override
      public void onAnimationUpdate(ValueAnimator animation) {
        List<LatLng> foregroundPoints = backgroundPolyline.getPoints();

        int percentageValue = (int) animation.getAnimatedValue();
        int pointcount = foregroundPoints.size();
        int countTobeRemoved = (int) (pointcount * (percentageValue / 100.0f));
        List<LatLng> subListTobeRemoved = foregroundPoints.subList(0, countTobeRemoved);
        subListTobeRemoved.clear();

        foregroundPolyline.setPoints(foregroundPoints);
      }
    });
    percentageCompletion.addListener(new Animator.AnimatorListener() {
      @Override
      public void onAnimationStart(Animator animation) {
      }

      @Override
      public void onAnimationEnd(Animator animation) {
        foregroundPolyline.setColor(ANIMCOLOR);
        foregroundPolyline.setPoints(backgroundPolyline.getPoints());
      }

      @Override
      public void onAnimationCancel(Animator animation) {

      }

      @Override
      public void onAnimationRepeat(Animator animation) {

      }
    });

    ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), ANIMCOLOR, BGCOLOR);
    colorAnimation.setInterpolator(new AccelerateInterpolator());
    colorAnimation.setDuration(CADURATION); // milliseconds

    colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
      @Override
      public void onAnimationUpdate(ValueAnimator animator) {
        foregroundPolyline.setColor((int) animator.getAnimatedValue());
      }

    });

    ObjectAnimator foregroundRouteAnimator = ObjectAnimator
        .ofObject(this, "routeIncreaseForward", new RouteEvaluator(), bangaloreRoute.toArray());
    foregroundRouteAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
    foregroundRouteAnimator.addListener(new Animator.AnimatorListener() {
      @Override
      public void onAnimationStart(Animator animation) {

      }

      @Override
      public void onAnimationEnd(Animator animation) {
        backgroundPolyline.setPoints(foregroundPolyline.getPoints());
      }

      @Override
      public void onAnimationCancel(Animator animation) {

      }

      @Override
      public void onAnimationRepeat(Animator animation) {

      }
    });
    foregroundRouteAnimator.setDuration(ANIMDURATION);
//        foregroundRouteAnimator.start();

    firstRunAnimSet.playSequentially(foregroundRouteAnimator,
        percentageCompletion);
    firstRunAnimSet.addListener(new Animator.AnimatorListener() {
      @Override
      public void onAnimationStart(Animator animation) {

      }

      @Override
      public void onAnimationEnd(Animator animation) {
        secondLoopRunAnimSet.start();
      }

      @Override
      public void onAnimationCancel(Animator animation) {

      }

      @Override
      public void onAnimationRepeat(Animator animation) {

      }
    });

    secondLoopRunAnimSet.playSequentially(colorAnimation,
        percentageCompletion);
    secondLoopRunAnimSet.setStartDelay(SLSDELAY);

    secondLoopRunAnimSet.addListener(new Animator.AnimatorListener() {
      @Override
      public void onAnimationStart(Animator animation) {

      }

      @Override
      public void onAnimationEnd(Animator animation) {
        secondLoopRunAnimSet.start();
      }

      @Override
      public void onAnimationCancel(Animator animation) {

      }

      @Override
      public void onAnimationRepeat(Animator animation) {

      }
    });

    firstRunAnimSet.start();
  }

  /**
   * This will be invoked by the ObjectAnimator multiple times. Mostly every 16ms.
   **/
  public void setRouteIncreaseForward(LatLng endLatLng) {
    List<LatLng> foregroundPoints = foregroundPolyline.getPoints();
    foregroundPoints.add(endLatLng);
    foregroundPolyline.setPoints(foregroundPoints);
  }

}

