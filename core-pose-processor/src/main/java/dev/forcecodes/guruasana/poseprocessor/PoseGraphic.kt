package dev.forcecodes.guruasana.poseprocessor

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.Log
import com.google.mlkit.vision.pose.Pose
import com.google.mlkit.vision.pose.PoseLandmark
import dev.forcecodes.guruasana.poseprocessor.camera.GraphicOverlay
import dev.forcecodes.guruasana.poseprocessor.camera.GraphicOverlay.Graphic

/** Draw the detected pose in preview.  */
class PoseGraphic internal constructor(
    overlay: GraphicOverlay?,
    private val pose: Pose,
    private val showInFrameLikelihood: Boolean,
    private val visualizeZ: Boolean,
    private val rescaleZForVisualization: Boolean
) : Graphic(overlay) {
    private var zMin = Float.MAX_VALUE
    private var zMax = Float.MIN_VALUE
    private val leftPaint: Paint
    private val rightPaint: Paint
    private val whitePaint: Paint = Paint()

    init {
        whitePaint.strokeWidth = STROKE_WIDTH
        whitePaint.color = Color.WHITE
        whitePaint.textSize = IN_FRAME_LIKELIHOOD_TEXT_SIZE
        leftPaint = Paint()
        leftPaint.strokeWidth = STROKE_WIDTH
        leftPaint.color = Color.GREEN
        rightPaint = Paint()
        rightPaint.strokeWidth = STROKE_WIDTH
        rightPaint.color = Color.YELLOW
    }

    override fun draw(canvas: Canvas) {
        val landmarks = pose.allPoseLandmarks
        if (landmarks.isEmpty()) {
            return
        }

        // Draw all the points
        for (landmark in landmarks) {
            Log.d(
                "Landmark",
                "Position: " + landmark.position + " Likelihood: " + landmark.inFrameLikelihood + " Type: " + landmark.landmarkType
            )
            drawPoint(canvas, landmark, whitePaint)
            if (visualizeZ && rescaleZForVisualization) {
                zMin = zMin.coerceAtMost(landmark.position3D.z)
                zMax = zMax.coerceAtLeast(landmark.position3D.z)
            }
        }
        val nose = pose.getPoseLandmark(PoseLandmark.NOSE)
        val lefyEyeInner = pose.getPoseLandmark(PoseLandmark.LEFT_EYE_INNER)
        val lefyEye = pose.getPoseLandmark(PoseLandmark.LEFT_EYE)
        val leftEyeOuter = pose.getPoseLandmark(PoseLandmark.LEFT_EYE_OUTER)
        val rightEyeInner = pose.getPoseLandmark(PoseLandmark.RIGHT_EYE_INNER)
        val rightEye = pose.getPoseLandmark(PoseLandmark.RIGHT_EYE)
        val rightEyeOuter = pose.getPoseLandmark(PoseLandmark.RIGHT_EYE_OUTER)
        val leftEar = pose.getPoseLandmark(PoseLandmark.LEFT_EAR)
        val rightEar = pose.getPoseLandmark(PoseLandmark.RIGHT_EAR)
        val leftMouth = pose.getPoseLandmark(PoseLandmark.LEFT_MOUTH)
        val rightMouth = pose.getPoseLandmark(PoseLandmark.RIGHT_MOUTH)
        val leftShoulder = pose.getPoseLandmark(PoseLandmark.LEFT_SHOULDER)
        val rightShoulder = pose.getPoseLandmark(PoseLandmark.RIGHT_SHOULDER)
        val leftElbow = pose.getPoseLandmark(PoseLandmark.LEFT_ELBOW)
        val rightElbow = pose.getPoseLandmark(PoseLandmark.RIGHT_ELBOW)
        val leftWrist = pose.getPoseLandmark(PoseLandmark.LEFT_WRIST)
        val rightWrist = pose.getPoseLandmark(PoseLandmark.RIGHT_WRIST)
        val leftHip = pose.getPoseLandmark(PoseLandmark.LEFT_HIP)
        val rightHip = pose.getPoseLandmark(PoseLandmark.RIGHT_HIP)
        val leftKnee = pose.getPoseLandmark(PoseLandmark.LEFT_KNEE)
        val rightKnee = pose.getPoseLandmark(PoseLandmark.RIGHT_KNEE)
        val leftAnkle = pose.getPoseLandmark(PoseLandmark.LEFT_ANKLE)
        val rightAnkle = pose.getPoseLandmark(PoseLandmark.RIGHT_ANKLE)
        val leftPinky = pose.getPoseLandmark(PoseLandmark.LEFT_PINKY)
        val rightPinky = pose.getPoseLandmark(PoseLandmark.RIGHT_PINKY)
        val leftIndex = pose.getPoseLandmark(PoseLandmark.LEFT_INDEX)
        val rightIndex = pose.getPoseLandmark(PoseLandmark.RIGHT_INDEX)
        val leftThumb = pose.getPoseLandmark(PoseLandmark.LEFT_THUMB)
        val rightThumb = pose.getPoseLandmark(PoseLandmark.RIGHT_THUMB)
        val leftHeel = pose.getPoseLandmark(PoseLandmark.LEFT_HEEL)
        val rightHeel = pose.getPoseLandmark(PoseLandmark.RIGHT_HEEL)
        val leftFootIndex = pose.getPoseLandmark(PoseLandmark.LEFT_FOOT_INDEX)
        val rightFootIndex = pose.getPoseLandmark(PoseLandmark.RIGHT_FOOT_INDEX)

        // Face
        drawLine(canvas, nose, lefyEyeInner, whitePaint)
        drawLine(canvas, lefyEyeInner, lefyEye, whitePaint)
        drawLine(canvas, lefyEye, leftEyeOuter, whitePaint)
        drawLine(canvas, leftEyeOuter, leftEar, whitePaint)
        drawLine(canvas, nose, rightEyeInner, whitePaint)
        drawLine(canvas, rightEyeInner, rightEye, whitePaint)
        drawLine(canvas, rightEye, rightEyeOuter, whitePaint)
        drawLine(canvas, rightEyeOuter, rightEar, whitePaint)
        drawLine(canvas, leftMouth, rightMouth, whitePaint)
        drawLine(canvas, leftShoulder, rightShoulder, whitePaint)
        drawLine(canvas, leftHip, rightHip, whitePaint)

        // Left body
        drawLine(canvas, leftShoulder, leftElbow, leftPaint)
        drawLine(canvas, leftElbow, leftWrist, leftPaint)
        drawLine(canvas, leftShoulder, leftHip, leftPaint)
        drawLine(canvas, leftHip, leftKnee, leftPaint)
        drawLine(canvas, leftKnee, leftAnkle, leftPaint)
        drawLine(canvas, leftWrist, leftThumb, leftPaint)
        drawLine(canvas, leftWrist, leftPinky, leftPaint)
        drawLine(canvas, leftWrist, leftIndex, leftPaint)
        drawLine(canvas, leftIndex, leftPinky, leftPaint)
        drawLine(canvas, leftAnkle, leftHeel, leftPaint)
        drawLine(canvas, leftHeel, leftFootIndex, leftPaint)

        // Right body
        drawLine(canvas, rightShoulder, rightElbow, rightPaint)
        drawLine(canvas, rightElbow, rightWrist, rightPaint)
        drawLine(canvas, rightShoulder, rightHip, rightPaint)
        drawLine(canvas, rightHip, rightKnee, rightPaint)
        drawLine(canvas, rightKnee, rightAnkle, rightPaint)
        drawLine(canvas, rightWrist, rightThumb, rightPaint)
        drawLine(canvas, rightWrist, rightPinky, rightPaint)
        drawLine(canvas, rightWrist, rightIndex, rightPaint)
        drawLine(canvas, rightIndex, rightPinky, rightPaint)
        drawLine(canvas, rightAnkle, rightHeel, rightPaint)
        drawLine(canvas, rightHeel, rightFootIndex, rightPaint)
    }

    fun drawPoint(canvas: Canvas, landmark: PoseLandmark, paint: Paint?) {
        val point = landmark.position3D
        updatePaintColorByZValue(
            paint, canvas, visualizeZ, rescaleZForVisualization, point.z, zMin, zMax
        )
        canvas.drawCircle(translateX(point.x), translateY(point.y), DOT_RADIUS, paint!!)
    }

    fun drawLine(
        canvas: Canvas,
        startLandmark: PoseLandmark?,
        endLandmark: PoseLandmark?,
        paint: Paint?
    ) {
        val start = startLandmark!!.position3D
        val end = endLandmark!!.position3D

        // Gets average z for the current body line
        val avgZInImagePixel = (start.z + end.z) / 2
        updatePaintColorByZValue(
            paint, canvas, visualizeZ, rescaleZForVisualization, avgZInImagePixel, zMin, zMax
        )
        canvas.drawLine(
            translateX(start.x),
            translateY(start.y),
            translateX(end.x),
            translateY(end.y),
            paint!!
        )
    }

    companion object {
        private const val DOT_RADIUS = 8.0f
        private const val IN_FRAME_LIKELIHOOD_TEXT_SIZE = 30.0f
        private const val STROKE_WIDTH = 10.0f
    }
}