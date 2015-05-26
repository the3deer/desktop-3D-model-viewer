package org.andresoviedo.app.modelviewer3D.entities;

public class Transformation {
	public float[] translation2 = new float[]{0F, 0F, 0F};
	public float[] translation = new float[]{0F, 0F, 0F};
	public float[] rotation = new float[]{0F, 0F, 0F};
	public float[] scale = new float[]{1F, 1F, 1F};
	public Transformation() {

	}
	public Transformation(float[] translation, float[] rotation, float[] scale) {
		this.translation = translation;
		this.rotation = rotation;
		this.scale = scale;
	}
}
