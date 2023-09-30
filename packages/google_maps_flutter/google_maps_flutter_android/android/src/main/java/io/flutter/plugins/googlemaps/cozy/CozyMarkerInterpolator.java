package io.flutter.plugins.googlemaps.cozy;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.RectF;

class CozyMarkerInterpolator {
    private final Typeface font;

    CozyMarkerInterpolator(Typeface font){
        this.font = font;
    }

    public CozyMarkerElements getInterpolatedMarkerObjects(CozyMarkerElements startCozyMarkerElements, CozyMarkerElements endCozyMarkerElements, float step) {
        // interpolate geometry
        CozyMarkerElement interpolatedCanvas = interpolateCozyMarkerElement(startCozyMarkerElements.canvas, endCozyMarkerElements.canvas, step);
        CozyMarkerElement interpolatedBubble = interpolateCozyMarkerElement(startCozyMarkerElements.bubble, endCozyMarkerElements.bubble, step);
        CozyMarkerElement interpolatedIcon = interpolateCozyMarkerElement(startCozyMarkerElements.icon, endCozyMarkerElements.icon, step);
        CozyMarkerElement interpolatedIconCircle = interpolateCozyMarkerElement(startCozyMarkerElements.iconCircle, endCozyMarkerElements.iconCircle, step);
        CozyMarkerElement interpolatedPointer = interpolateCozyMarkerElement(startCozyMarkerElements.pointer, endCozyMarkerElements.pointer, step);

        CozyMarkerElement interpolatedLabel = interpolateCozyMarkerElement(startCozyMarkerElements.labels[0], endCozyMarkerElements.labels[0], step);

        CozyMarkerElement[] interpolatedLabels = interpolateLabel(interpolatedLabel, (String) startCozyMarkerElements.labels[0].data, (String) endCozyMarkerElements.labels[0].data, step);
        interpolatedIcon.data = interpolateIcons((Bitmap) startCozyMarkerElements.icon.data, (Bitmap) endCozyMarkerElements.icon.data, step);

        return new CozyMarkerElements(interpolatedCanvas, interpolatedBubble, interpolatedLabels, interpolatedIcon, interpolatedIconCircle, interpolatedPointer);
    }

    private CozyMarkerElement[] interpolateLabel(CozyMarkerElement interpolatedLabel, String startText, String endText, float step){
        if(startText.equals(endText)){
            return new CozyMarkerElement[]{
                new CozyMarkerElement(
                    interpolatedLabel.bounds,
                    interpolatedLabel.fillColor,
                    interpolatedLabel.strokeColor,
                    interpolatedLabel.alpha,
                    startText
                )
            };
        }

        // Calculate the size of string
        Paint textPaint = new Paint();
        textPaint.setTypeface(font);
        textPaint.setTextSize(interpolatedLabel.bounds.height());
        textPaint.setAntiAlias(true);
        textPaint.setTextAlign(Paint.Align.LEFT);

        int startTextCharactersNumber = textPaint.breakText(startText, true, interpolatedLabel.bounds.width() * 1.2f, null);
        int endTextCharactersNumber = textPaint.breakText(endText, true, interpolatedLabel.bounds.width() * 1.2f, null);

        String clippedStartText = startText.substring(0, startTextCharactersNumber);
        String clippedEndText = endText.substring(0, endTextCharactersNumber);

        float startTextAlpha = 1.0f - step;
        float endTextAlpha = step;

        return new CozyMarkerElement[]{
            new CozyMarkerElement(
                interpolatedLabel.bounds,
                interpolatedLabel.fillColor,
                interpolatedLabel.strokeColor,
                startTextAlpha,
                clippedStartText
            ),
            new CozyMarkerElement(
                interpolatedLabel.bounds,
                interpolatedLabel.fillColor,
                interpolatedLabel.strokeColor,
                endTextAlpha,
                clippedEndText
            )
        };
    }
    
    private Bitmap interpolateIcons(Bitmap startIcon, Bitmap endIcon, float step){
        if(startIcon != null){
            return startIcon;
        }
        if(endIcon != null){
            return endIcon;
        }
        return null;
    }

    private CozyMarkerElement interpolateCozyMarkerElement(CozyMarkerElement startCozyMarkerElement, CozyMarkerElement endCozyMarkerElement, float step) {
        RectF interpolatedBounds = new RectF();
        interpolatedBounds.left = interpolate(startCozyMarkerElement.bounds.left, endCozyMarkerElement.bounds.left, step);
        interpolatedBounds.top = interpolate(startCozyMarkerElement.bounds.top, endCozyMarkerElement.bounds.top, step);
        interpolatedBounds.right = interpolate(startCozyMarkerElement.bounds.right, endCozyMarkerElement.bounds.right, step);
        interpolatedBounds.bottom = interpolate(startCozyMarkerElement.bounds.bottom, endCozyMarkerElement.bounds.bottom, step);

        int interpolatedFillColor = interpolateColor(startCozyMarkerElement.fillColor, endCozyMarkerElement.fillColor, step);
        int interpolatedStrokeColor = interpolateColor(startCozyMarkerElement.strokeColor, endCozyMarkerElement.strokeColor, step);
        float interpolatedAlpha = interpolate(startCozyMarkerElement.alpha, endCozyMarkerElement.alpha, step);

        return new CozyMarkerElement(interpolatedBounds, interpolatedFillColor, interpolatedStrokeColor, interpolatedAlpha);
    }

    private float interpolate(float start, float end, float step) {
        return start + (end - start) * step;
    }

    private int interpolateColor(int start, int end, float step) {
        int startRed = (start >> 16) & 0xff;
        int startGreen = (start >> 8) & 0xff;
        int startBlue = start & 0xff;

        int endRed = (end >> 16) & 0xff;
        int endGreen = (end >> 8) & 0xff;
        int endBlue = end & 0xff;

        int interpolatedRed = (int) interpolate(startRed, endRed, step);
        int interpolatedGreen = (int) interpolate(startGreen, endGreen, step);
        int interpolatedBlue = (int) interpolate(startBlue, endBlue, step);

        return Color.rgb(interpolatedRed, interpolatedGreen, interpolatedBlue);
    }
}