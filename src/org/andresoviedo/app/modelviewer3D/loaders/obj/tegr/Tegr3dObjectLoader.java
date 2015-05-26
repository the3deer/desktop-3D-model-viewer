package org.andresoviedo.app.modelviewer3D.loaders.obj.tegr;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StreamTokenizer;
import java.util.List;
import java.util.Vector;

import javax.media.opengl.GL;
import javax.media.opengl.GLException;

import org.andresoviedo.app.modelviewer3D.objects.Object3D;


import com.sun.opengl.util.texture.Texture;
import com.sun.opengl.util.texture.TextureIO;

public class Tegr3dObjectLoader extends Object3D {

	File file;
	public File textureFile;

	// List<float[]> vertices = new Vector<float[]>();
	List<float[]> vNormals = new Vector<float[]>();
	List<float[]> textCoord = new Vector<float[]>();
	List<int[]> faces = new Vector<int[]>();
	List<float[]> fNormals = new Vector<float[]>();

	private String fileFormat;

	// Possible formats
	// V, VC, VNTCN?, VNC, VTC, VNTC, VNCN?, VTCN
	// V, VNT, VN, VT
	// C, CN

	boolean loaded = false;

	public int offset = -1;

	public static void main(String[] args) {
		new Tegr3dObjectLoader("c:\\TEGR\\jogltest\\models\\tegr.3d");
	}

	public Tegr3dObjectLoader(String filePath) {
		this(new File(filePath));
	}

	public Tegr3dObjectLoader(File file) {
		this.file = file;
		this.enableLighting = true;
		this.enableTextures = true;
		this.drawSolid = true;
	}

	public void load() throws Exception {
		BufferedReader br = null;
		StreamTokenizer tok = null;
		try {
			br = new BufferedReader(new FileReader(file));
			tok = new StreamTokenizer(br);
			tok.resetSyntax();
			tok.wordChars('a', 'z');
			tok.wordChars('A', 'Z');
			tok.wordChars('0', '9');
			tok.wordChars('.', '.');
			tok.wordChars('-', '-');
			tok.whitespaceChars(0, ' ');
			tok.commentChar('#');
			tok.slashSlashComments(true);
			tok.slashStarComments(true);
			if (tok.nextToken() == StreamTokenizer.TT_WORD) {
				fileFormat = tok.sval.toUpperCase();

				// Read vertex, normals and texture coords
				int numVertex = readNextInt(tok);
				if (fileFormat.startsWith("VNT")) {
					for (int i = 0; i < numVertex; i++) {
						readNextTuple(tok, vertices, 3);
						readNextTuple(tok, vNormals, 3);
						readNextTuple(tok, textCoord, 2);
					}
				} else if (fileFormat.startsWith("VT")) {
					for (int i = 0; i < numVertex; i++) {
						readNextTuple(tok, vertices, 3);
						readNextTuple(tok, textCoord, 2);
					}
				} else if (fileFormat.startsWith("VN3")) {
					List<float[]> dump = new Vector<float[]>();
					for (int i = 0; i < numVertex; i++) {
						readNextTuple(tok, vertices, 3);
						readNextTuple(tok, vNormals, 3);
						readNextTuple(tok, dump, 2);
					}
					fileFormat = "VN" + fileFormat.substring(3);
				} else if (fileFormat.startsWith("VN")) {
					for (int i = 0; i < numVertex; i++) {
						readNextTuple(tok, vertices, 3);
						readNextTuple(tok, vNormals, 3);
					}
				} else if (fileFormat.startsWith("V")) {
					for (int i = 0; i < numVertex; i++) {
						readNextTuple(tok, vertices, 3);
					}
				} else {
					throw new Exception("Bad file format");
				}

				// Read faces and it's normals
				if (fileFormat.endsWith("CN")) {
					int numFaces = readNextInt(tok);
					for (int i = 0; i < numFaces; i++) {
						int facesSize = readNextInt(tok);
						readNextIntTuple(tok, faces, facesSize);
						readNextTuple(tok, fNormals, 3);
					}
				} else if (fileFormat.endsWith("C3")) {
					int numFaces = readNextInt(tok);
					for (int i = 0; i < numFaces; i++) {
						readNextIntTuple(tok, faces, 3);

						// Calculate normal to face
						// fNormals.add(calculateNormal(faces.get(i)));
					}
					fileFormat = fileFormat.substring(0,
							fileFormat.length() - 1);
				} else if (fileFormat.endsWith("C")) {
					int numFaces = readNextInt(tok);
					for (int i = 0; i < numFaces; i++) {
						int facesSize = readNextInt(tok);
						readNextIntTuple(tok, faces, facesSize);

						// Calculate normal to face
						// fNormals.add(calculateNormal(faces.get(i)));
					}
				}

				// Check whether first vertex index is 0 or 1
				int minIndex = Integer.MAX_VALUE;
				for (int[] face : faces) {
					for (int fv : face) {
						if (fv < minIndex) {
							minIndex = fv;
						}
					}
				}
				offset = -minIndex;

				// Calculate normals
				if (!faces.isEmpty() && fNormals.isEmpty()) {
					for (int[] face : faces) {
						fNormals.add(calculateNormal(face));
					}
				}

				// Calculate normals to vertex if there is any face defined
				if (vNormals.isEmpty() && !fNormals.isEmpty()) {
					// for (int idVertex = 0; idVertex < vertices.size();
					// idVertex++) {
					// float[] vNormal = new float[]{0, 0, 0};
					// for (int face[] : faces) {
					// for (int fv : face) {
					// if ((fv + offset) == idVertex) {
					// float[] fn = fNormals.get(fv + offset);
					// for (int i = 0; i < 3; i++) {
					// vNormal[i] += fn[i];
					// }
					// }
					// }
					// }
					// }
					for (int idVertex = 0; idVertex < vertices.size(); idVertex++) {
						vNormals.add(new float[]{0, 0, 0});
					}
					for (int i = 0; i < faces.size(); i++) {
						int[] face = faces.get(i);
						float[] fn = fNormals.get(i);
						for (int fv : face) {
							float[] vn = vNormals.get(fv + offset);
							for (int comp = 0; comp < 3; comp++) {
								vn[comp] += fn[comp];
							}
							vNormals.set(fv + offset, vn);
						}
					}
					for (int idVertex = 0; idVertex < vNormals.size(); idVertex++) {
						float[] vn = vNormals.get(idVertex);
						float modul = (float) Math.sqrt(Math.pow(vn[0], 2)
								+ Math.pow(vn[1], 2) + Math.pow(vn[2], 2));
						vn = new float[]{vn[0] / modul, vn[1] / modul,
								vn[2] / modul};
						vNormals.set(idVertex, vn);
					}
				}

				// Model 1: Divide texture to 4 parts
				// (0-90,90-180,180-270,270-360)

				if (textCoord.isEmpty() && !vertices.isEmpty()) {
					calculateBoundingBox();
					if (boundingBox != null) {
						for (float[] vertex : vertices) {
							float[] vertexToCenter = new float[]{
									vertex[0] - boundingBox.center[0],
									vertex[1] - boundingBox.center[1],
									vertex[2] - boundingBox.center[2]};

							float radius = (float) Math.sqrt(Math.pow(
									vertexToCenter[0], 2)
									+ Math.pow(vertexToCenter[1], 2)
									+ Math.pow(vertexToCenter[2], 2));

							float phi = (float) Math.toDegrees(Math
									.asin(vertexToCenter[1] / radius)) + 90;
							float lambda = 0F;
							if (vertexToCenter[0] >= 0
									&& vertexToCenter[2] >= 0) {
								// zone = 0;
								if (vertexToCenter[2] == 0) {
									lambda = 90;
								} else {
									lambda = (float) Math.toDegrees(Math
											.atan(vertexToCenter[0]
													/ vertexToCenter[2]));
								}
							} else if (vertexToCenter[0] >= 0
									&& vertexToCenter[2] < 0) {
								// zone = 1;
								if (vertexToCenter[0] == 0) {
									lambda = 90;
								} else {
									lambda = 90 - (float) Math.toDegrees(Math
											.atan(vertexToCenter[2]
													/ vertexToCenter[0]));
								}
							} else if (vertexToCenter[0] < 0
									&& vertexToCenter[2] <= 0) {
								// zone = 2;
								lambda = 180;
								if (vertexToCenter[2] == 0) {
									lambda += 90;
								} else {
									lambda += (float) Math.toDegrees(Math
											.atan(vertexToCenter[0]
													/ vertexToCenter[2]));
								}
							} else {
								// zone = 3;
								lambda = 270 - (float) Math.toDegrees(Math
										.atan(vertexToCenter[2]
												/ vertexToCenter[0]));
							}
							// System.out.print("vertex[" + vertexToCenter[0]
							// + "," + vertexToCenter[1] + ","
							// + vertexToCenter[2] + "] - ");
							// System.out.println("x[" + (float) lambda / 360
							// + "] y[" + (float) phi / 180 + "]");
							textCoord.add(new float[]{(float) lambda / 360,
									1 - (float) phi / 180});
							// float u = vertex[0] / radius;
							// float v = vertex[1] / radius;
							// textCoord.add(new float[]{u, v});
						}
					}
				}
			} else {
				throw new Exception("Bad file format");
			}

			System.out.println("Tegr3dObjectLoader-> File[" + file
					+ "], fileFormat[" + fileFormat + "], FirstVertex at ["
					+ (-offset) + "] Vertices [" + vertices.size()
					+ "], vNormals[" + vNormals.size() + "], textCoord["
					+ textCoord.size() + "], Faces [" + faces.size()
					+ "], fNormals[" + fNormals.size() + "]");
		} catch (Exception ex) {
			if (tok != null) {
				System.out.println("Exception at line: " + tok.lineno()
						+ " -> " + ex.getMessage());
			}
			ex.printStackTrace();
			throw ex;
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException ex2) {
					ex2.printStackTrace();
				}
			}
		}
		loaded = true;
	}
	private int readNextInt(StreamTokenizer tok) throws Exception {
		if (tok.nextToken() == StreamTokenizer.TT_WORD) {
			return Integer.parseInt(tok.sval);
		}
		throw new Exception("Value expected");
	}

	private void readNextTuple(StreamTokenizer tok, List<float[]> list, int size)
			throws IOException {
		float tuple[] = new float[size];
		for (int i = 0; i < size; i++) {
			tok.nextToken();
			tuple[i] = Float.parseFloat(tok.sval);
		}
		list.add(tuple);
	}

	private void readNextIntTuple(StreamTokenizer tok, List<int[]> list,
			int size) throws IOException {
		int tuple[] = new int[size];
		for (int i = 0; i < size; i++) {
			tok.nextToken();
			tuple[i] = Integer.parseInt(tok.sval);
		}
		list.add(tuple);
	}

	public int createCallList(GL gl, boolean drawSolidEnabled,
			boolean textureEnabled, boolean lightingEnabled,
			boolean drawNormals, boolean shadeSmoothEnabled,
			boolean showBoundingBox) throws Exception {
		if (!loaded) {
			load();
		}

		if (lightingEnabled && enableLighting) {
			gl.glEnable(GL.GL_LIGHTING);
		} else {
			gl.glDisable(GL.GL_LIGHTING);
		}

		int ret = gl.glGenLists(1);
		gl.glNewList(ret, GL.GL_COMPILE);
		// V, VC, VNTCN, VNC, VTC, VNTC, VNCN, VTCN

		// System.out.println("Pushing [" + id + "]");
		gl.glPushName(id);

		// Set color
		gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL.GL_AMBIENT, mat_ambient, 0);
		gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL.GL_DIFFUSE, mat_diffuse, 0);
		gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL.GL_SPECULAR, mat_specular, 0);
		gl.glMaterialfv(GL.GL_FRONT, GL.GL_SHININESS, mat_shininess, 0);
		gl.glMaterialfv(GL.GL_FRONT, GL.GL_EMISSION, mat_emission, 0);
		gl.glColor3f(color[0], color[1], color[2]);

		// Enable texture
		Texture texture = null;
		if (textureEnabled && enableTextures && textureFile != null
				&& !textCoord.isEmpty()) {
			try {
				texture = TextureIO.newTexture(textureFile, true);
				texture.enable();
				texture.bind();
				gl.glEnable(GL.GL_TEXTURE_2D);
				gl.glTexParameterf(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_S,
						GL.GL_REPEAT);
				gl.glTexParameterf(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_T,
						GL.GL_REPEAT);
				gl.glTexEnvi(GL.GL_TEXTURE_ENV, GL.GL_TEXTURE_ENV_MODE,
						GL.GL_REPLACE);
			} catch (GLException ex) {
				ex.printStackTrace();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

		if (fileFormat.equals("V")) {
			gl.glPointSize(5.0F);
			gl.glBegin(GL.GL_POINTS);
			for (int i = 0; i < vertices.size(); i++) {
				gl.glVertex3fv(vertices.get(i), 0);
			}
			gl.glEnd();
			gl.glPointSize(1.0F);
		} else if (fileFormat.equals("VC")) {
			for (int i = 0; i < faces.size(); i++) {
				int[] face = faces.get(i);
				gl.glBegin(drawSolidEnabled && drawSolid
						? GL.GL_POLYGON
						: GL.GL_LINE_LOOP);
				if (!shadeSmoothEnabled) {
					if (!fNormals.isEmpty()) {
						gl.glNormal3fv(fNormals.get(i), 0);
					}
				}
				for (int j = 0; j < face.length; j++) {
					if (shadeSmoothEnabled) {
						if (!vNormals.isEmpty()) {
							gl.glNormal3fv(vNormals.get(face[j] + offset), 0);
						}
					}
					if (!textCoord.isEmpty()) {
						gl.glTexCoord2fv(textCoord.get(face[j] + offset), 0);
					}
					gl.glVertex3fv(vertices.get(face[j] + offset), 0);
				}
				gl.glEnd();
			}
		} else if (fileFormat.equals("VTC")) {
			for (int i = 0; i < faces.size(); i++) {
				int[] face = faces.get(i);
				gl.glBegin(drawSolidEnabled && drawSolid
						? GL.GL_POLYGON
						: GL.GL_LINE_LOOP);
				if (!shadeSmoothEnabled) {
					if (!fNormals.isEmpty()) {
						gl.glNormal3fv(fNormals.get(i), 0);
					}
				}
				for (int j = 0; j < face.length; j++) {
					gl.glTexCoord2fv(textCoord.get(face[j] + offset), 0);
					if (shadeSmoothEnabled) {
						if (!vNormals.isEmpty()) {
							gl.glNormal3fv(vNormals.get(face[j] + offset), 0);
						}
					}
					gl.glVertex3fv(vertices.get(face[j] + offset), 0);
				}
				gl.glEnd();
			}
		} else if (fileFormat.equals("VNC")) {
			for (int i = 0; i < faces.size(); i++) {
				int[] face = faces.get(i);
				gl.glBegin(drawSolidEnabled && drawSolid
						? GL.GL_POLYGON
						: GL.GL_LINE_LOOP);
				if (!shadeSmoothEnabled) {
					if (!fNormals.isEmpty()) {
						gl.glNormal3fv(fNormals.get(i), 0);
					}
				}
				for (int j = 0; j < face.length; j++) {
					// gl.glNormal3fv(vNormals.get(face[j] + offset), 0);
					if (shadeSmoothEnabled) {
						if (!vNormals.isEmpty()) {
							gl.glNormal3fv(vNormals.get(face[j] + offset), 0);
						}
					}
					if (!textCoord.isEmpty()) {
						gl.glTexCoord2fv(textCoord.get(face[j] + offset), 0);
					}
					gl.glVertex3fv(vertices.get(face[j] + offset), 0);
				}
				gl.glEnd();
			}
		} else if (fileFormat.equals("VCN")) {
			for (int i = 0; i < faces.size(); i++) {
				int[] face = faces.get(i);
				gl.glBegin(drawSolidEnabled && super.drawSolid
						? GL.GL_POLYGON
						: GL.GL_LINE_LOOP);
				// gl.glNormal3fv(fNormals.get(i), 0);
				if (!shadeSmoothEnabled) {
					if (!fNormals.isEmpty()) {
						gl.glNormal3fv(fNormals.get(i), 0);
					}
				}
				for (int j = 0; j < face.length; j++) {
					if (!textCoord.isEmpty()) {
						gl.glTexCoord2fv(textCoord.get(face[j] + offset), 0);
					}
					if (shadeSmoothEnabled) {
						if (!vNormals.isEmpty()) {
							gl.glNormal3fv(vNormals.get(face[j] + offset), 0);
						}
					}
					gl.glVertex3fv(vertices.get(face[j] + offset), 0);
				}
				gl.glEnd();
			}
		} else if (fileFormat.equals("VNCN")) {
			for (int i = 0; i < faces.size(); i++) {
				int[] face = faces.get(i);
				gl.glBegin(drawSolidEnabled && drawSolid
						? GL.GL_POLYGON
						: GL.GL_LINE_LOOP);
				// gl.glNormal3fv(fNormals.get(i), 0);
				if (!shadeSmoothEnabled) {
					if (!fNormals.isEmpty()) {
						gl.glNormal3fv(fNormals.get(i), 0);
					}
				}
				for (int j = 0; j < face.length; j++) {
					gl.glNormal3fv(vNormals.get(face[j] + offset), 0);
					if (!textCoord.isEmpty()) {
						gl.glTexCoord2fv(textCoord.get(face[j] + offset), 0);
					}
					if (shadeSmoothEnabled) {
						if (!vNormals.isEmpty()) {
							gl.glNormal3fv(vNormals.get(face[j] + offset), 0);
						}
					}
					gl.glVertex3fv(vertices.get(face[j] + offset), 0);
				}
				gl.glEnd();
			}
		} else if (fileFormat.equals("VTCN")) {
			for (int i = 0; i < faces.size(); i++) {
				int[] face = faces.get(i);
				gl.glBegin(drawSolidEnabled && drawSolid
						? GL.GL_POLYGON
						: GL.GL_LINE_LOOP);
				// gl.glNormal3fv(fNormals.get(i), 0);
				if (!shadeSmoothEnabled) {
					if (!fNormals.isEmpty()) {
						gl.glNormal3fv(fNormals.get(i), 0);
					}
				}
				for (int j = 0; j < face.length; j++) {
					gl.glTexCoord2fv(textCoord.get(face[j] + offset), 0);
					if (shadeSmoothEnabled) {
						if (!vNormals.isEmpty()) {
							gl.glNormal3fv(vNormals.get(face[j] + offset), 0);
						}
					}
					gl.glVertex3fv(vertices.get(face[j] + offset), 0);
				}
				gl.glEnd();
			}
		} else if (fileFormat.equals("VNTC")) {
			for (int i = 0; i < faces.size(); i++) {
				int[] face = faces.get(i);
				gl.glBegin(drawSolidEnabled && drawSolid
						? GL.GL_POLYGON
						: GL.GL_LINE_LOOP);
				for (int j = 0; j < face.length; j++) {
					gl.glNormal3fv(vNormals.get(face[j] + offset), 0);
					gl.glTexCoord2fv(textCoord.get(face[j] + offset), 0);
					gl.glVertex3fv(vertices.get(face[j] + offset), 0);
				}
				gl.glEnd();
			}
		} else if (fileFormat.equals("VNTCN")) {
			for (int i = 0; i < faces.size(); i++) {
				int[] face = faces.get(i);
				gl.glBegin(drawSolidEnabled && drawSolid
						? GL.GL_POLYGON
						: GL.GL_LINE_LOOP);
				gl.glNormal3fv(fNormals.get(i), 0);
				for (int j = 0; j < face.length; j++) {
					gl.glTexCoord2fv(textCoord.get(face[j] + offset), 0);
					gl.glVertex3fv(vertices.get(face[j] + offset), 0);
				}
				gl.glEnd();
			}
		}
		if (texture != null) {
			texture.disable();
		}
		if (showBoundingBox) {
			drawBoundingBox(gl, new float[]{0F, 0F, 0F}, 1F);
		}
		if (drawNormals) {
			gl.glColor3f(0.0F, 0.0F, 0.0F);
			if (!fNormals.isEmpty()) {
				for (int i = 0; i < faces.size(); i++) {
					int[] face = faces.get(i);
					drawNormal(gl, i, face);
				}
			}
			if (!vNormals.isEmpty()) {
				for (int idVertex = 0; idVertex < vNormals.size(); idVertex++) {
					drawVertexNormal(gl, idVertex);
				}
			}
		}
		gl.glPopName();
		gl.glEndList();
		return ret;
	}
	private float[] calculateNormal(int[] face) {
		float[] v0 = vertices.get(face[invertedNormals ? 2 : 0] + offset);
		float[] v1 = vertices.get(face[1] + offset);
		float[] v2 = vertices.get(face[invertedNormals ? 0 : 2] + offset);
		float[] va = new float[]{v0[0] - v1[0], v0[1] - v1[1], v0[2] - v1[2]};
		float[] vb = new float[]{v2[0] - v1[0], v2[1] - v1[1], v2[2] - v1[2]};
		float[] n = new float[]{va[1] * vb[2] - va[2] * vb[1],
				va[2] * vb[0] - va[0] * vb[2], va[0] * vb[1] - va[1] * vb[0]};
		float modul = (float) Math.sqrt(Math.pow(n[0], 2) + Math.pow(n[1], 2)
				+ Math.pow(n[2], 2));
		float[] vn = new float[]{n[0] / modul, n[1] / modul, n[2] / modul};
		return vn;
	}

	private void drawNormal(GL gl, int faceNo, int[] face) {
		// get already calculated normal
		float[] vn = fNormals.get(faceNo);

		// get first vertex of face
		float[] v0 = vertices.get(face[0] + offset);

		// calculate center of polygon
		float[] max = new float[]{v0[0], v0[1], v0[2]};
		float[] min = new float[]{v0[0], v0[1], v0[2]};

		for (int i = 0; i < face.length; i++) {
			float[] vertex = vertices.get(face[i] + offset);
			for (int j = 0; j < 3; j++) {
				if (vertex[j] > max[j]) {
					max[j] = vertex[j];
				} else if (vertex[j] < min[j]) {
					min[j] = vertex[j];
				}
			}
		}
		float[] center = new float[]{(max[0] + min[0]) / 2,
				(max[1] + min[1]) / 2, (max[2] + min[2]) / 2};
		float[] vn2 = new float[]{(center[0] + vn[0] / getScale()[0]),
				(center[1] + vn[1] / getScale()[1]),
				(center[2] + vn[2] / getScale()[2])};

		// Draw normal as a line
		gl.glBegin(GL.GL_LINES);
		gl.glVertex3fv(center, 0);
		gl.glVertex3fv(vn2, 0);
		gl.glEnd();
	}

	private void drawVertexNormal(GL gl, int idVertex) {
		float[] vertex = vertices.get(idVertex);
		float[] vNormal = vNormals.get(idVertex);

		// Draw normal as a line
		gl.glBegin(GL.GL_LINES);
		gl.glVertex3fv(vertex, 0);
		gl.glVertex3f(vertex[0] + vNormal[0] / getScale()[0], vertex[1]
				+ vNormal[1] / getScale()[1], vertex[2] + vNormal[2]
				/ getScale()[2]);
		gl.glEnd();
	}

	public String toString() {
		return file.getName();
	}

}
