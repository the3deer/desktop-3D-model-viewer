package org.andresoviedo.app.modelviewer3D.objects.room;

import javax.media.opengl.GL;

import org.andresoviedo.app.modelviewer3D.objects.Object3D;


public abstract class EmbededObject3D extends Object3D {
	public abstract int createCallList(GL gl, boolean drawSolid,
			boolean lightOn, boolean textureOn);

	public String toString() {
		return this.getClass().getName().substring(
				this.getClass().getPackage().getName().length() + 1);
	}
}
