package org.andresoviedo.app.modelviewer3D.cameras;

/*
 Class Name:

 CCamera.

 Created by:

 Allen Sherrod (Programming Ace of www.UltimateGameProgramming.com).

 Description:

 This class represents a camera in a 3D scene.
 */

public class Camera {

	public static final float UP = 0.5f; // Forward speed.
	public static final float DOWN = -0.5f; // Backward speed.
	public static final float LEFT = 0.5f; // Left speed.
	public static final float RIGHT = -0.5f; // Right speed.
	public static final float STRAFE_LEFT = -0.5f; // Left straft speed.
	public static final float STRAFE_RIGHT = 0.5f; // Right straft speed.

	public static final int AIM = 10;

	public float xPos, yPos; // Camera position.
	public float zPos;
	public float xView, yView, zView; // Look at position.
	public float xUp, yUp, zUp; // Up direction.
	float xStrafe, yStrafe, zStrafe; // Strafe direction.
	float currentRotationAngle; // Keeps us from going too far up or down.

	public Camera() {
		// Initialize variables...
		xPos = yPos = zPos = 0;
		xView = yView = zView = 0;
		xUp = yUp = zUp = 0;
		xStrafe = yStrafe = zStrafe = 0;

	}

	public void SetCamera(float x, float y, float z, float xv, float yv,
			float zv, float xu, float yu, float zu) {
		// Here we set the camera to the values sent in to us. This is mostly
		// used to set up a
		// default position.
		xPos = x;
		yPos = y;
		zPos = z;
		xView = xv;
		yView = yv;
		zView = zv;
		xUp = xu;
		yUp = yu;
		zUp = zu;
	}

	public void MoveCamera(float direction) {
		// Moving the camera requires a little more then adding 1 to the z or
		// subracting 1.
		// First we need to get the direction at which we are looking.
		float xLookDirection = 0, yLookDirection = 0, zLookDirection = 0;

		// The look direction is the view minus the position (where we are).
		xLookDirection = xView - xPos;
		yLookDirection = yView - yPos;
		zLookDirection = zView - zPos;

		// Normalize the direction.
		float dp = 1 / (float) Math.sqrt(xLookDirection * xLookDirection
				+ yLookDirection * yLookDirection + zLookDirection
				* zLookDirection);
		xLookDirection *= dp;
		yLookDirection *= dp;
		zLookDirection *= dp;

		// Call UpdateCamera to move our camera in the direction we want.
		UpdateCamera(xLookDirection, yLookDirection, zLookDirection, direction);
	}

	void UpdateCamera(float xDir, float yDir, float zDir, float dir) {
		// Move the camera on the X and Z axis. Notice I commented out the yPos
		// and yView
		// updates. This is because without them we can keep the camera on the
		// ground in
		// the simple camera tutorials without having to alter them afterwards.
		xPos += xDir * dir;
		// yPos += yDir * dir;
		zPos += zDir * dir;

		// Move the view along with the position
		xView += xDir * dir;
		// yView += yDir * dir;
		zView += zDir * dir;
	}

	public void StrafeCam(float direction) {
		// Calculate the strafe then update the camera position.
		CalculateStrafe();
		UpdateCamera(xStrafe, yStrafe, zStrafe, direction);
	}

	void CalculateStrafe() {
		float xDir = 0, yDir = 0, zDir = 0;
		float xCross = 0, yCross = 0, zCross = 0;

		// Strafing is just like moving the camera forward and backward.
		// First we will get the direction we are looking.
		xDir = xView - xPos;
		yDir = yView - yPos;
		zDir = zView - zPos;

		// Normalize the direction.
		float dp = 1 / (float) Math.sqrt(xDir * xDir + yDir * yDir + zDir
				* zDir);
		xDir *= dp;
		yDir *= dp;
		zDir *= dp;

		// Now if we were to call UpdateCamera() we will be moving the camera
		// foward or backwards.
		// We don't want that here. We want to strafe. To do so we have to get
		// the cross product
		// of our direction and Up direction view. The up was set in SetCamera
		// to be 1 positive
		// y. That is because anything positive on the y is considered up. After
		// we get the
		// cross product we can save it to the strafe variables so that can be
		// added to the
		// camera using UpdateCamera().

		// Get the cross product of the direction we are looking and the up
		// direction.
		xCross = (yDir * zUp) - (zDir * yUp);
		yCross = (zDir * xUp) - (xDir * zUp);
		zCross = (xDir * yUp) - (yDir * xUp);

		// Save our strafe (cross product) values in xStrafe, yStrafe, and
		// zStrafe.
		xStrafe = xCross;
		yStrafe = yCross;
		zStrafe = zCross;
	}

	public void RotateCamera(float AngleDir, float xSpeed, float ySpeed,
			float zSpeed) {
		float xNewLookDirection = 0, yNewLookDirection = 0, zNewLookDirection = 0;
		float xLookDirection = 0, yLookDirection = 0, zLookDirection = 0;
		float CosineAngle = 0, SineAngle = 0;

		// System.out.println("AngleDir[" + AngleDir + "]");

		// First we will need to calculate the cos and sine of our angle. I
		// creaetd two macros to
		// do this in the CCamera.h header file called GET_COS and GET_SINE. To
		// use the macros
		// we just send in the variable we ant to store the results and the
		// angle we need to
		// calculate.
		CosineAngle = (float) Math.cos(AngleDir);
		SineAngle = (float) Math.sin(AngleDir);

		// Next get the look direction (where we are looking) just like in the
		// move camera function.
		xLookDirection = xView - xPos;
		yLookDirection = yView - yPos;
		zLookDirection = zView - zPos;

		// Normalize the direction.
		float dp = 1 / (float) Math.sqrt(xLookDirection * xLookDirection
				+ yLookDirection * yLookDirection + zLookDirection
				* zLookDirection);
		xLookDirection *= dp;
		yLookDirection *= dp;
		zLookDirection *= dp;

		// Calculate the new X position.
		xNewLookDirection = (CosineAngle + (1 - CosineAngle) * xSpeed)
				* xLookDirection;
		xNewLookDirection += ((1 - CosineAngle) * xSpeed * ySpeed - zSpeed
				* SineAngle)
				* yLookDirection;
		xNewLookDirection += ((1 - CosineAngle) * xSpeed * zSpeed + ySpeed
				* SineAngle)
				* zLookDirection;

		// Calculate the new Y position.
		yNewLookDirection = ((1 - CosineAngle) * xSpeed * ySpeed + zSpeed
				* SineAngle)
				* xLookDirection;
		yNewLookDirection += (CosineAngle + (1 - CosineAngle) * ySpeed)
				* yLookDirection;
		yNewLookDirection += ((1 - CosineAngle) * ySpeed * zSpeed - xSpeed
				* SineAngle)
				* zLookDirection;

		// Calculate the new Z position.
		zNewLookDirection = ((1 - CosineAngle) * xSpeed * zSpeed - ySpeed
				* SineAngle)
				* xLookDirection;
		zNewLookDirection += ((1 - CosineAngle) * ySpeed * zSpeed + xSpeed
				* SineAngle)
				* yLookDirection;
		zNewLookDirection += (CosineAngle + (1 - CosineAngle) * zSpeed)
				* zLookDirection;

		// Last we add the new rotations to the old view to correctly rotate the
		// camera.
		xView = xPos + xNewLookDirection;
		yView = yPos + yNewLookDirection;
		zView = zPos + zNewLookDirection;
	}

	public void Rotate(float incX, float incY) {
		RotateByMouse(AIM + incX, AIM + incY, AIM, AIM);
	}

	void RotateByMouse(float mousePosX, float mousePosY, float midX, float midY) {
		float yDirection = 0.0f; // Direction angle.
		float yRotation = 0.0f; // Rotation angle.

		// If the mouseX and mouseY are at the middle of the screen then we
		// can't rotate the view.
		if ((mousePosX == midX) && (mousePosY == midY))
			return;

		// Next we get the direction of each axis. We divide by 1000 to get a
		// smaller value back.
		yDirection = (float) ((midX - mousePosX)) / 1.0f;
		yRotation = (float) ((midY - mousePosY)) / 1.0f;

		// We use curentRotX to help use keep the camera from rotating too far
		// in either direction.
		currentRotationAngle -= yRotation;

		// Stop the camera from going to high...
		if (currentRotationAngle > 1.5f) {
			currentRotationAngle = 1.5f;
			return;
		}

		// Stop the camera from going to low...
		if (currentRotationAngle < -1.5f) {
			currentRotationAngle = -1.5f;
			return;
		}

		// Next we get the axis which is a perpendicular vector of the view
		// direction and up values.
		// We use the cross product of that to get the axis then we normalize
		// it.
		float xAxis = 0, yAxis = 0, zAxis = 0;
		float xDir = 0, yDir = 0, zDir = 0;

		// Get the Direction of the view.
		xDir = xView - xPos;
		yDir = yView - yPos;
		zDir = zView - zPos;

		// Get the cross product of the direction and the up.
		xAxis = (yDir * zUp) - (zDir * yUp);
		yAxis = (zDir * xUp) - (xDir * zUp);
		zAxis = (xDir * yUp) - (yDir * xUp);

		// Normalize it.
		float len = 1 / (float) Math.sqrt(xAxis * xAxis + yAxis * yAxis + zAxis
				* zAxis);
		xAxis *= len;
		yAxis *= len;
		zAxis *= len;

		// Rotate the camera.
		RotateCamera(yRotation, xAxis, yAxis, zAxis);
		RotateCamera(yDirection, 0, 1, 0);
	}

	public String locationToString() {
		return xPos + "," + yPos + "," + zPos;
	}

	public String intLocationToString() {
		return (float) (xPos) + "," + (float) yPos + "," + (float) zPos;
	}
}
